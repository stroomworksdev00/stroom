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

package stroom.search.solr.client.presenter;

import stroom.docref.DocRef;
import stroom.entity.client.presenter.ContentCallback;
import stroom.entity.client.presenter.DocumentEditTabPresenter;
import stroom.entity.client.presenter.LinkTabPanelView;
import stroom.entity.client.presenter.MarkdownEditPresenter;
import stroom.search.solr.shared.SolrIndexDoc;
import stroom.widget.tab.client.presenter.TabData;
import stroom.widget.tab.client.presenter.TabDataImpl;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

public class SolrIndexPresenter extends DocumentEditTabPresenter<LinkTabPanelView, SolrIndexDoc> {

    private static final TabData SETTINGS = new TabDataImpl("Settings");
    private static final TabData FIELDS = new TabDataImpl("Fields");
    private static final TabData DOCUMENTATION = new TabDataImpl("Documentation");

    private final SolrIndexSettingsPresenter indexSettingsPresenter;
    private final SolrIndexFieldListPresenter indexFieldListPresenter;
    private final MarkdownEditPresenter markdownEditPresenter;

    @Inject
    public SolrIndexPresenter(final EventBus eventBus,
                              final LinkTabPanelView view,
                              final SolrIndexSettingsPresenter indexSettingsPresenter,
                              final SolrIndexFieldListPresenter indexFieldListPresenter,
                              final MarkdownEditPresenter markdownEditPresenter) {
        super(eventBus, view);
        this.indexSettingsPresenter = indexSettingsPresenter;
        this.indexFieldListPresenter = indexFieldListPresenter;
        this.markdownEditPresenter = markdownEditPresenter;

        addTab(FIELDS);
        addTab(SETTINGS);
        addTab(DOCUMENTATION);
        selectTab(FIELDS);
    }

    @Override
    protected void onBind() {
        super.onBind();
        registerHandler(indexSettingsPresenter.addDirtyHandler(event -> {
            if (event.isDirty()) {
                setDirty(true);
            }
        }));
        registerHandler(indexFieldListPresenter.addDirtyHandler(event -> {
            if (event.isDirty()) {
                setDirty(true);
            }
        }));
        registerHandler(markdownEditPresenter.addDirtyHandler(event -> {
            if (event.isDirty()) {
                setDirty(true);
            }
        }));
    }

    @Override
    protected void getContent(final TabData tab, final ContentCallback callback) {
        if (SETTINGS.equals(tab)) {
            callback.onReady(indexSettingsPresenter);
        } else if (FIELDS.equals(tab)) {
            callback.onReady(indexFieldListPresenter);
        } else if (DOCUMENTATION.equals(tab)) {
            callback.onReady(markdownEditPresenter);
        } else {
            callback.onReady(null);
        }
    }

    @Override
    public void onRead(final DocRef docRef, final SolrIndexDoc doc, final boolean readOnly) {
        super.onRead(docRef, doc, readOnly);
        indexSettingsPresenter.read(docRef, doc, readOnly);
        indexFieldListPresenter.read(docRef, doc, readOnly);
        markdownEditPresenter.setText(doc.getDescription());
        markdownEditPresenter.setReadOnly(readOnly);
    }

    @Override
    protected SolrIndexDoc onWrite(SolrIndexDoc doc) {
        doc = indexSettingsPresenter.write(doc);
        doc = indexFieldListPresenter.write(doc);
        doc.setDescription(markdownEditPresenter.getText());
        return doc;
    }

    @Override
    public String getType() {
        return SolrIndexDoc.DOCUMENT_TYPE;
    }
}
