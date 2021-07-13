/*
 * Copyright 2016 Crown Copyright
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

package stroom.widget.menu.client.presenter;

import stroom.task.client.TaskEndEvent;
import stroom.task.client.TaskStartEvent;
import stroom.widget.menu.client.presenter.MenuPresenter.MenuView;
import stroom.widget.popup.client.event.HidePopupEvent;
import stroom.widget.popup.client.event.ShowPopupEvent;
import stroom.widget.popup.client.presenter.PopupPosition;
import stroom.widget.popup.client.presenter.PopupPosition.HorizontalLocation;
import stroom.widget.popup.client.presenter.PopupUiHandlers;
import stroom.widget.popup.client.presenter.PopupView.PopupType;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.MyPresenterWidget;
import com.gwtplatform.mvp.client.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MenuPresenter
        extends MyPresenterWidget<MenuView>
        implements MenuUiHandlers {

    private final Provider<MenuPresenter> menuListPresenterProvider;
    private MenuPresenter currentMenu;
    private MenuItem currentItem;
    private MenuPresenter parent;
    private List<Element> autoHidePartners;

    @Inject
    public MenuPresenter(final EventBus eventBus,
                         final MenuView view,
                         final Provider<MenuPresenter> menuListPresenterProvider) {
        super(eventBus, view);
        this.menuListPresenterProvider = menuListPresenterProvider;
        view.setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        registerHandler(getView().bind());
    }

    @Override
    public void showSubMenu(final MenuItem menuItem, final Element element, boolean focus) {
        // Only change the popup if the item selected is changing and we have
        // some sub items.
        if (currentItem == null || !currentItem.equals(menuItem)) {
            // We are changing the highlighted item so close the current popup
            // if it is open.
            hideChildren();

            if (menuItem instanceof HasChildren) {
                // Try and get some sub items.
                final HasChildren hasChildren = (HasChildren) menuItem;

                hasChildren.getChildren().onSuccess(children -> {
                    if (children != null && children.size() > 0) {
                        // We are changing the highlighted item so close the current popup
                        // if it is open.
                        hideChildren();

                        final MenuPresenter presenter = menuListPresenterProvider.get();
                        presenter.setParent(MenuPresenter.this);
//                        presenter.setHighlightItems(getHighlightItems());
                        presenter.setData(children);

                        // Set the current presenter telling us that the
                        // popup is showing.
                        currentMenu = presenter;
                        currentItem = menuItem;

                        final PopupUiHandlers popupUiHandlers = new PopupUiHandlers() {
                            @Override
                            public void onHideRequest(final boolean autoClose, final boolean ok) {
                                presenter.hideChildren();
                                presenter.hideSelf();
                            }

                            @Override
                            public void onHide(final boolean autoClose, final boolean ok) {
                            }
                        };

                        final List<Element> autoHidePartners = new ArrayList<>();

                        // Add parent auto hide partners.
                        if (MenuPresenter.this.autoHidePartners != null
                                && MenuPresenter.this.autoHidePartners.size() > 0) {
                            autoHidePartners.addAll(MenuPresenter.this.autoHidePartners);
                        }

                        // Add this as an auto hide partner
                        autoHidePartners.add(element);
                        presenter.setAutoHidePartners(autoHidePartners);
                        final Element[] partners = autoHidePartners.toArray(new Element[0]);

                        final PopupPosition popupPosition = new PopupPosition(
                                element.getAbsoluteRight(),
                                element.getAbsoluteLeft(),
                                element.getAbsoluteTop(),
                                element.getAbsoluteTop(),
                                HorizontalLocation.RIGHT,
                                null);

                        if (focus) {
                            presenter.selectFirstItem();
                        }

                        ShowPopupEvent.fire(
                                MenuPresenter.this,
                                presenter,
                                PopupType.POPUP,
                                popupPosition,
                                popupUiHandlers,
                                partners);
                    }
                });
            }

        } else if (focus && currentMenu != null) {
            currentMenu.getView().focus();
        }
    }

    public void selectFirstItem() {
        getView().selectFirstItem();
    }

    @Override
    public void focusParent() {
        if (parent != null) {
            hideChildren();
            parent.getView().focus();
        }
    }

    @Override
    public void execute(final CommandMenuItem menuItem) {
        if (menuItem != null && menuItem.getCommand() != null) {
            hideAll();
            TaskStartEvent.fire(MenuPresenter.this);
            Scheduler.get().scheduleDeferred(() -> {
                try {
                    menuItem.getCommand().execute();
                } finally {
                    TaskEndEvent.fire(MenuPresenter.this);
                }
            });
        }
    }

    private void hideSelf() {
        HidePopupEvent.fire(this, this);
    }

    private void hideChildren() {
        // First make sure all children are hidden.
        if (currentMenu != null) {
            currentMenu.hideChildren();
            currentMenu.hideSelf();
            currentMenu = null;
            currentItem = null;
        }
    }

    private void hideParent() {
        // First make sure all children are hidden.
        if (parent != null) {
            parent.hideSelf();
            parent.hideParent();
        }
    }

    @Override
    public void escape() {
        hideAll();

        MenuPresenter p = this;
        while (p.parent != null) {
            p = p.parent;
        }
        final List<Element> autoHidePartners = p.getAutoHidePartners();
        if (autoHidePartners != null && autoHidePartners.size() > 0) {
            autoHidePartners.get(0).focus();
        }
    }

    public void hideAll() {
        hideChildren();
        hideSelf();
        hideParent();
    }

    public void setParent(final MenuPresenter parent) {
        this.parent = parent;
    }

    public List<Element> getAutoHidePartners() {
        return autoHidePartners;
    }

    public void setAutoHidePartners(final List<Element> autoHidePartners) {
        this.autoHidePartners = autoHidePartners;
    }

    public void setData(final List<Item> items) {
        getView().setData(items);
    }

    public void setHighlightItems(final Set<Item> highlightItems) {
        getView().setHighlightItems(highlightItems);
    }

    public interface MenuView extends View, HasUiHandlers<MenuUiHandlers> {

        HandlerRegistration bind();

        void setData(List<Item> items);

        void setHighlightItems(Set<Item> highlightItems);

        void selectFirstItem();

        void focus();
    }
}
