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

package stroom.explorer.client.presenter;

import stroom.alert.client.event.AlertEvent;
import stroom.data.client.event.DataSelectionEvent;
import stroom.data.client.event.DataSelectionEvent.DataSelectionHandler;
import stroom.data.client.event.HasDataSelectionHandlers;
import stroom.dispatch.client.RestFactory;
import stroom.docref.DocRef;
import stroom.explorer.shared.DocumentTypes;
import stroom.explorer.shared.ExplorerNode;
import stroom.explorer.shared.ExplorerTreeFilter;
import stroom.ui.config.client.UiConfigCache;
import stroom.widget.dropdowntree.client.view.DropDownTreeUiHandlers;
import stroom.widget.dropdowntree.client.view.DropDownTreeView;
import stroom.widget.dropdowntree.client.view.QuickFilterTooltipUtil;
import stroom.widget.popup.client.event.HidePopupEvent;
import stroom.widget.popup.client.event.ShowPopupEvent;
import stroom.widget.popup.client.presenter.DefaultPopupUiHandlers;
import stroom.widget.popup.client.presenter.PopupSize;
import stroom.widget.popup.client.presenter.PopupUiHandlers;
import stroom.widget.popup.client.presenter.PopupView.PopupType;
import stroom.widget.util.client.SelectionType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.MyPresenterWidget;

class ExplorerDropDownTreePresenter
        extends MyPresenterWidget<DropDownTreeView>
        implements DropDownTreeUiHandlers, HasDataSelectionHandlers<ExplorerNode> {

    private final PopupUiHandlers popupUiHandlers;
    private final ExtendedExplorerTree explorerTree;
    private boolean allowFolderSelection;
    private String caption = "Choose item";
    private ExplorerNode selectedExplorerNode;

    @Inject
    ExplorerDropDownTreePresenter(final EventBus eventBus,
                                  final DropDownTreeView view,
                                  final RestFactory restFactory,
                                  final UiConfigCache uiConfigCache) {
        super(eventBus, view);
        getView().setUiHandlers(this);

        popupUiHandlers = new DefaultPopupUiHandlers(this) {
            @Override
            public void onShow() {
                super.onShow();
                explorerTree.setFocus(true);
            }

            @Override
            public void onHideRequest(final boolean autoClose, final boolean ok) {
                if (ok) {
                    final ExplorerNode selected = getSelectedEntityData();
                    if (isSelectionAllowed(selected)) {
                        DataSelectionEvent.fire(ExplorerDropDownTreePresenter.this, selected, false);
                        selectedExplorerNode = selected;
                        hide(autoClose, ok);
                    } else {
                        AlertEvent.fireError(ExplorerDropDownTreePresenter.this,
                                "You must choose a valid item.", null);
                    }
                } else {
                    hide(autoClose, ok);
                }
            }
        };

        explorerTree = new ExtendedExplorerTree(this, restFactory);
        setIncludeNullSelection(true);

        // Add views.
        view.setCellTree(explorerTree);

        uiConfigCache.get()
                .onSuccess(uiConfig ->
                        view.setQuickFilterTooltipSupplier(() -> QuickFilterTooltipUtil.createTooltip(
                                "Choose Item Quick Filter",
                                ExplorerTreeFilter.FIELD_DEFINITIONS,
                                uiConfig.getHelpUrl())));
    }

    public void show() {
        getView().clearFilter();
        refresh();
        final PopupSize popupSize = PopupSize.resizable(400, 550);
        ShowPopupEvent.fire(this,
                this,
                PopupType.OK_CANCEL_DIALOG,
                popupSize,
                caption,
                popupUiHandlers);
    }

    protected void setIncludeNullSelection(final boolean includeNullSelection) {
        explorerTree.getTreeModel().setIncludeNullSelection(includeNullSelection);
    }

    protected void setSelectedTreeItem(final ExplorerNode selectedItem,
                                       final SelectionType selectionType,
                                       final boolean initial) {
        // Is the selection type valid?
        if (isSelectionAllowed(selectedItem)) {
            // Drop down presenters need to know what the initial selection was so that they can
            // update the name of their selected item properly.
            if (initial) {
                DataSelectionEvent.fire(this, selectedItem, false);
            } else if (selectionType.isDoubleSelect()) {
                DataSelectionEvent.fire(this, selectedItem, true);
                this.selectedExplorerNode = selectedItem;
                HidePopupEvent.fire(this, this, true, true);
            }
        }
    }

    private boolean isSelectionAllowed(final ExplorerNode selected) {
        if (selected == null) {
            return true;
        }
        if (allowFolderSelection) {
            return true;
        }

        return !DocumentTypes.isFolder(selected.getType());
    }

    @Override
    public void nameFilterChanged(final String text) {
        explorerTree.changeNameFilter(text);
    }

    public void refresh() {
        explorerTree.setSelectedItem(selectedExplorerNode);
        explorerTree.getTreeModel().reset();
        explorerTree.getTreeModel().setEnsureVisible(selectedExplorerNode);
        explorerTree.getTreeModel().refresh();
    }

    public void setIncludedTypes(final String... includedTypes) {
        explorerTree.getTreeModel().setIncludedTypes(includedTypes);
    }

    public void setTags(final String... tags) {
        explorerTree.getTreeModel().setTags(tags);
    }

    public void setRequiredPermissions(final String... requiredPermissions) {
        explorerTree.getTreeModel().setRequiredPermissions(requiredPermissions);
    }

    public DocRef getSelectedEntityReference() {
        final ExplorerNode explorerNode = getSelectedEntityData();
        if (explorerNode == null) {
            return null;
        }
        return explorerNode.getDocRef();
    }

    public void setSelectedEntityReference(final DocRef docRef) {
        setSelectedEntityData(ExplorerNode.create(docRef));
    }

    private ExplorerNode getSelectedEntityData() {
        return resolve(explorerTree.getSelectionModel().getSelected());
    }

    private void setSelectedEntityData(final ExplorerNode explorerNode) {
        this.selectedExplorerNode = explorerNode;
        refresh();
    }

    public void setAllowFolderSelection(final boolean allowFolderSelection) {
        this.allowFolderSelection = allowFolderSelection;
    }

    @Override
    public HandlerRegistration addDataSelectionHandler(final DataSelectionHandler<ExplorerNode> handler) {
        return addHandlerToSource(DataSelectionEvent.getType(), handler);
    }

    private static ExplorerNode resolve(final ExplorerNode selection) {
        if (selection == ExplorerTreeModel.NULL_SELECTION) {
            return null;
        }

        return selection;
    }

    public void setCaption(final String caption) {
        this.caption = caption;
    }

    private static class ExtendedExplorerTree extends ExplorerTree {

        private final ExplorerDropDownTreePresenter explorerDropDownTreePresenter;

        public ExtendedExplorerTree(final ExplorerDropDownTreePresenter explorerDropDownTreePresenter,
                                    final RestFactory restFactory) {
            super(restFactory, false);
            this.explorerDropDownTreePresenter = explorerDropDownTreePresenter;
        }

        @Override
        protected void setInitialSelectedItem(final ExplorerNode selection) {
            super.setInitialSelectedItem(selection);
            explorerDropDownTreePresenter.setSelectedTreeItem(resolve(selection),
                    new SelectionType(false, false),
                    true);
        }

        @Override
        protected void doSelect(final ExplorerNode row, final SelectionType selectionType) {
            super.doSelect(row, selectionType);
            explorerDropDownTreePresenter.setSelectedTreeItem(resolve(row), selectionType, false);
        }
    }
}
