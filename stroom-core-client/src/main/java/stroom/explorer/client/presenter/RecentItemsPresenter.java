/*
 * Copyright 2024 Crown Copyright
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
 */

package stroom.explorer.client.presenter;

import stroom.data.grid.client.PagerView;
import stroom.dispatch.client.RestFactory;
import stroom.explorer.client.event.ShowRecentItemsEvent;
import stroom.explorer.client.presenter.RecentItemsPresenter.RecentItemsProxy;
import stroom.explorer.shared.ExplorerConstants;
import stroom.widget.popup.client.event.HidePopupRequestEvent;
import stroom.widget.popup.client.event.ShowPopupEvent;
import stroom.widget.popup.client.presenter.PopupSize;
import stroom.widget.popup.client.presenter.PopupType;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;

public class RecentItemsPresenter
        extends AbstractFindPresenter<RecentItemsProxy>
        implements ShowRecentItemsEvent.Handler {

    private final RecentItems recentItems;
    private boolean showing;

    @Inject
    public RecentItemsPresenter(final EventBus eventBus,
                                final FindView view,
                                final RecentItemsProxy proxy,
                                final PagerView pagerView,
                                final RestFactory restFactory,
                                final RecentItems recentItems) {
        super(eventBus, view, proxy, pagerView, restFactory);
        this.recentItems = recentItems;
    }

    @ProxyEvent
    @Override
    public void onShowRecentItems(final ShowRecentItemsEvent event) {
        if (!showing) {
            showing = true;
            focusText = true;
            refresh();
            final PopupSize popupSize = PopupSize.resizable(800, 800);
            ShowPopupEvent.builder(this)
                    .popupType(PopupType.CLOSE_DIALOG)
                    .popupSize(popupSize)
                    .caption("Recent Items")
                    .onShow(e -> getView().focus())
                    .onHideRequest(HidePopupRequestEvent::hide)
                    .onHide(e -> showing = false)
                    .fire();
        }
    }

    @Override
    protected void updateFilter(final ExplorerTreeFilterBuilder explorerTreeFilterBuilder) {
        explorerTreeFilterBuilder.setRecentItems(recentItems.getRecentItems());
        // Don't want favourites in the recent items as they are effectively duplicates
        explorerTreeFilterBuilder.setIncludedRootTypes(ExplorerConstants.SYSTEM);
        explorerTreeFilterBuilder.setNameFilter(explorerTreeFilterBuilder.build().getNameFilter(), true);
    }


    // --------------------------------------------------------------------------------


    @ProxyCodeSplit
    public interface RecentItemsProxy extends Proxy<RecentItemsPresenter> {

    }
}
