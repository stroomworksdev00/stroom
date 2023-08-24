/*
 * Copyright 2022 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stroom.analytics.rule.impl;

import stroom.analytics.shared.AnalyticProcessConfig;
import stroom.analytics.shared.AnalyticRuleDoc;
import stroom.analytics.shared.AnalyticRuleDoc.Builder;
import stroom.datasource.api.v2.DataSource;
import stroom.docref.DocContentMatch;
import stroom.docref.DocRef;
import stroom.docref.DocRefInfo;
import stroom.docstore.api.AuditFieldFilter;
import stroom.docstore.api.DependencyRemapper;
import stroom.docstore.api.Store;
import stroom.docstore.api.StoreFactory;
import stroom.docstore.api.UniqueNameUtil;
import stroom.explorer.shared.DocumentType;
import stroom.explorer.shared.DocumentTypeGroup;
import stroom.importexport.shared.ImportSettings;
import stroom.importexport.shared.ImportState;
import stroom.query.language.DataSourceResolver;
import stroom.query.language.SearchRequestBuilder;
import stroom.query.util.LambdaLogger;
import stroom.query.util.LambdaLoggerFactory;
import stroom.security.api.SecurityContext;
import stroom.util.shared.Message;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
class AnalyticRuleStoreImpl implements AnalyticRuleStore {

    private static final LambdaLogger LOGGER = LambdaLoggerFactory.getLogger(AnalyticRuleStoreImpl.class);

    private final Store<AnalyticRuleDoc> store;
    private final SecurityContext securityContext;
    private final Provider<DataSourceResolver> dataSourceResolverProvider;

    @Inject
    AnalyticRuleStoreImpl(final StoreFactory storeFactory,
                          final AnalyticRuleSerialiser serialiser,
                          final SecurityContext securityContext,
                          final Provider<DataSourceResolver> dataSourceResolverProvider) {
        this.store = storeFactory.createStore(serialiser, AnalyticRuleDoc.DOCUMENT_TYPE, AnalyticRuleDoc.class);
        this.securityContext = securityContext;
        this.dataSourceResolverProvider = dataSourceResolverProvider;
    }

    ////////////////////////////////////////////////////////////////////////
    // START OF ExplorerActionHandler
    ////////////////////////////////////////////////////////////////////////

    @Override
    public DocRef createDocument(final String name) {
        final DocRef docRef = store.createDocument(name);

        // Create an alert rule from a template.

        // Read and write as a processing user to ensure we are allowed as documents do not have permissions added to
        // them until after they are created in the store.
        securityContext.asProcessingUser(() -> {
            final AnalyticRuleDoc analyticRuleDoc = store.readDocument(docRef);
            store.writeDocument(analyticRuleDoc);
        });
        return docRef;
    }

    @Override
    public DocRef copyDocument(final DocRef docRef, final Set<String> existingNames) {
        final String newName = UniqueNameUtil.getCopyName(docRef.getName(), existingNames);
        final AnalyticRuleDoc document = store.readDocument(docRef);
        return store.createDocument(newName,
                (type, uuid, docName, version, createTime, updateTime, createUser, updateUser) -> {
                    final Builder builder = document
                            .copy()
                            .type(type)
                            .uuid(uuid)
                            .name(docName)
                            .version(version)
                            .createTimeMs(createTime)
                            .updateTimeMs(updateTime)
                            .createUser(createUser)
                            .updateUser(updateUser);

                    final AnalyticProcessConfig analyticProcessConfig = document.getAnalyticProcessConfig();
                    if (analyticProcessConfig != null) {
                        analyticProcessConfig.setEnabled(false);
                        builder.analyticProcessConfig(analyticProcessConfig);
                    }

                    return builder.build();
                });
    }

    @Override
    public DocRef moveDocument(final String uuid) {
        return store.moveDocument(uuid);
    }

    @Override
    public DocRef renameDocument(final String uuid, final String name) {
        return store.renameDocument(uuid, name);
    }

    @Override
    public void deleteDocument(final String uuid) {
        store.deleteDocument(uuid);
    }

    @Override
    public DocRefInfo info(String uuid) {
        return store.info(uuid);
    }

    @Override
    public DocumentType getDocumentType() {
        return new DocumentType(
                DocumentTypeGroup.SEARCH,
                AnalyticRuleDoc.DOCUMENT_TYPE,
                "Analytic Rule",
                AnalyticRuleDoc.ICON);
    }

    ////////////////////////////////////////////////////////////////////////
    // END OF ExplorerActionHandler
    ////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    // START OF HasDependencies
    ////////////////////////////////////////////////////////////////////////

    @Override
    public Map<DocRef, Set<DocRef>> getDependencies() {
        return store.getDependencies(createMapper());
    }

    @Override
    public Set<DocRef> getDependencies(final DocRef docRef) {
        return store.getDependencies(docRef, createMapper());
    }

    @Override
    public void remapDependencies(final DocRef docRef,
                                  final Map<DocRef, DocRef> remappings) {
        store.remapDependencies(docRef, remappings, createMapper());
    }

    private BiConsumer<AnalyticRuleDoc, DependencyRemapper> createMapper() {
        return (doc, dependencyRemapper) -> {
            try {
                if (doc.getQuery() != null) {
                    SearchRequestBuilder.extractDataSourceNameOnly(doc.getQuery(), dataSourceName -> {
                        try {
                            if (dataSourceName != null) {
                                final DataSource dataSource = dataSourceResolverProvider
                                        .get()
                                        .resolveDataSource(dataSourceName);
                                if (dataSource != null && dataSource.getDocRef() != null) {
                                    final DocRef remapped = dependencyRemapper.remap(dataSource.getDocRef());
                                    if (remapped != null &&
                                            remapped.getName() != null &&
                                            remapped.getName().length() > 0 &&
                                            !Objects.equals(dataSourceName, remapped.getName())) {
                                        doc.setQuery(doc.getQuery().replaceFirst(dataSourceName, remapped.getName()));
                                    }
                                }
                            }
                        } catch (final RuntimeException e) {
                            LOGGER.debug(e::getMessage, e);
                        }
                    });
                }
            } catch (final RuntimeException e) {
                LOGGER.debug(e::getMessage, e);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////
    // END OF HasDependencies
    ////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    // START OF DocumentActionHandler
    ////////////////////////////////////////////////////////////////////////

    @Override
    public AnalyticRuleDoc readDocument(final DocRef docRef) {
        return store.readDocument(docRef);
    }

    @Override
    public AnalyticRuleDoc writeDocument(final AnalyticRuleDoc document) {
        return store.writeDocument(document);
    }

    ////////////////////////////////////////////////////////////////////////
    // END OF DocumentActionHandler
    ////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////
    // START OF ImportExportActionHandler
    ////////////////////////////////////////////////////////////////////////

    @Override
    public Set<DocRef> listDocuments() {
        return store.listDocuments();
    }

    @Override
    public DocRef importDocument(final DocRef docRef,
                                 final Map<String, byte[]> dataMap,
                                 final ImportState importState,
                                 final ImportSettings importSettings) {
        return store.importDocument(docRef, dataMap, importState, importSettings);
    }

    @Override
    public Map<String, byte[]> exportDocument(final DocRef docRef,
                                              final boolean omitAuditFields,
                                              final List<Message> messageList) {
        if (omitAuditFields) {
            return store.exportDocument(docRef, messageList, new AuditFieldFilter<>());
        }
        return store.exportDocument(docRef, messageList, d -> d);
    }

    @Override
    public String getType() {
        return AnalyticRuleDoc.DOCUMENT_TYPE;
    }

    @Override
    public Set<DocRef> findAssociatedNonExplorerDocRefs(DocRef docRef) {
        return null;
    }

    ////////////////////////////////////////////////////////////////////////
    // END OF ImportExportActionHandler
    ////////////////////////////////////////////////////////////////////////

    @Override
    public List<DocRef> list() {
        return store.list();
    }

    @Override
    public List<DocRef> findByNames(final List<String> name, final boolean allowWildCards) {
        return store.findByNames(name, allowWildCards);
    }

    @Override
    public List<DocContentMatch> findByContent(final String pattern, final boolean regex, final boolean matchCase) {
        return store.findByContent(pattern, regex, matchCase);
    }
}
