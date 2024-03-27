/*
 * Copyright 2017 Crown Copyright
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

package stroom.search.elastic.client;

import stroom.core.client.ContentManager;
import stroom.dispatch.client.RestFactory;
import stroom.docref.DocRef;
import stroom.docstore.shared.DocRefUtil;
import stroom.document.client.DocumentPlugin;
import stroom.document.client.DocumentPluginEventManager;
import stroom.entity.client.presenter.DocumentEditPresenter;
import stroom.search.elastic.client.presenter.ElasticClusterPresenter;
import stroom.search.elastic.shared.ElasticClusterDoc;
import stroom.search.elastic.shared.ElasticClusterResource;
import stroom.security.client.api.ClientSecurityContext;

import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;

import java.util.function.Consumer;
import javax.inject.Singleton;

@Singleton
public class ElasticClusterPlugin extends DocumentPlugin<ElasticClusterDoc> {

    private static final ElasticClusterResource ELASTIC_CLUSTER_RESOURCE = GWT.create(ElasticClusterResource.class);

    private final Provider<ElasticClusterPresenter> editorProvider;
    private final RestFactory restFactory;

    @Inject
    public ElasticClusterPlugin(
            final EventBus eventBus,
            final Provider<ElasticClusterPresenter> editorProvider,
            final RestFactory restFactory,
            final ContentManager contentManager,
            final DocumentPluginEventManager entityPluginEventManager,
            final ClientSecurityContext securityContext) {
        super(eventBus, contentManager, entityPluginEventManager, securityContext);

        this.editorProvider = editorProvider;
        this.restFactory = restFactory;
    }

    @Override
    protected DocumentEditPresenter<?, ?> createEditor() {
        return editorProvider.get();
    }

    @Override
    public void load(final DocRef docRef,
                     final Consumer<ElasticClusterDoc> resultConsumer,
                     final Consumer<Throwable> errorConsumer) {
        restFactory
                .resource(ELASTIC_CLUSTER_RESOURCE)
                .method(res -> res.fetch(docRef.getUuid()))
                .onSuccess(resultConsumer)
                .onFailure(errorConsumer)
                .exec();
    }

    @Override
    public void save(final DocRef docRef,
                     final ElasticClusterDoc document,
                     final Consumer<ElasticClusterDoc> resultConsumer,
                     final Consumer<Throwable> errorConsumer) {
        restFactory
                .resource(ELASTIC_CLUSTER_RESOURCE)
                .method(res -> res.update(document.getUuid(), document))
                .onSuccess(resultConsumer)
                .onFailure(errorConsumer)
                .exec();
    }

    @Override
    public String getType() {
        return ElasticClusterDoc.DOCUMENT_TYPE;
    }

    @Override
    protected DocRef getDocRef(final ElasticClusterDoc document) {
        return DocRefUtil.create(document);
    }
}
