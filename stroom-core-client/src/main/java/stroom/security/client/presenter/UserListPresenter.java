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

package stroom.security.client.presenter;

import stroom.cell.info.client.SvgCell;
import stroom.data.grid.client.EndColumn;
import stroom.data.grid.client.MyDataGrid;
import stroom.data.grid.client.PagerView;
import stroom.dispatch.client.RestFactory;
import stroom.query.api.v2.ExpressionOperator;
import stroom.security.shared.FindUserCriteria;
import stroom.security.shared.QuickFilterExpressionParser;
import stroom.security.shared.User;
import stroom.security.shared.UserFields;
import stroom.security.shared.UserResource;
import stroom.svg.client.Preset;
import stroom.svg.client.SvgPresets;
import stroom.ui.config.client.UiConfigCache;
import stroom.util.shared.GwtNullSafe;
import stroom.widget.button.client.ButtonView;
import stroom.widget.dropdowntree.client.view.QuickFilterTooltipUtil;
import stroom.widget.popup.client.event.HidePopupRequestEvent;
import stroom.widget.popup.client.event.ShowPopupEvent;
import stroom.widget.popup.client.presenter.PopupSize;
import stroom.widget.popup.client.presenter.PopupType;
import stroom.widget.util.client.MouseUtil;
import stroom.widget.util.client.MultiSelectionModelImpl;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.MyPresenterWidget;

import java.util.function.Consumer;

/**
 * For selecting a username to add a user to a permissions set or selecting a user to add to a group.
 * For internal IDP this will list users from account and stroom_user combined.
 * For external IDP this will list users from stroom_user only and users on the IDP but not in
 * stroom_user will have to be manually added to the list.
 */
public class UserListPresenter
        extends MyPresenterWidget<UserListView>
        implements UserListUiHandlers {

    private static final UserResource USER_RESOURCE = GWT.create(UserResource.class);

    private final MyDataGrid<User> dataGrid;
    private final MultiSelectionModelImpl<User> selectionModel;
    private final RestFactory restFactory;
    private ButtonView newButton = null;
    private ButtonView addMultipleButton = null;
    private final CreateNewUserPresenter newPresenter;
    private final CreateMultipleUsersPresenter createMultipleUsersPresenter;
    private UserDataProvider dataProvider;
    private FindUserCriteria findUserCriteria;
    private User selected;

    @Inject
    public UserListPresenter(final EventBus eventBus,
                             final UserListView userListView,
                             final PagerView pagerView,
                             final RestFactory restFactory,
                             final CreateNewUserPresenter newPresenter,
                             final CreateMultipleUsersPresenter createMultipleUsersPresenter,
                             final UiConfigCache uiConfigCache) {
        super(eventBus, userListView);
        this.restFactory = restFactory;
        this.newPresenter = newPresenter;
        this.createMultipleUsersPresenter = createMultipleUsersPresenter;

        dataGrid = new MyDataGrid<>();
        selectionModel = dataGrid.addDefaultSelectionModel(false);
        pagerView.setDataWidget(dataGrid);

        // Not easy to determine if we are dealing in users or groups at this point so just
        // call it Quick Filter
        uiConfigCache.get(uiConfig -> {
            if (uiConfig != null) {
                userListView.registerPopupTextProvider(() -> QuickFilterTooltipUtil.createTooltip(
                        "Quick Filter",
                        UserFields.FILTER_FIELD_DEFINITIONS,
                        uiConfig.getHelpUrlQuickFilter()));
            }
        }, this);

        userListView.setDatGridView(pagerView);
        userListView.setUiHandlers(this);

        // Icon
        dataGrid.addColumn(new Column<User, Preset>(new SvgCell()) {
            @Override
            public Preset getValue(final User userRef) {
                return SvgPresets.USER;
            }
        }, "</br>", 20);

        // Display Name
        final Column<User, String> displayNameCol = new Column<User, String>(new TextCell()) {
            @Override
            public String getValue(final User userRef) {
                return GwtNullSafe.requireNonNullElse(userRef.getDisplayName(), userRef.getSubjectId());
            }
        };
        dataGrid.addResizableColumn(displayNameCol, "Display Name", 200);

        // Full name
        final Column<User, String> fullNameCol = new Column<User, String>(new TextCell()) {
            @Override
            public String getValue(final User userRef) {
                return userRef.getFullName();
            }
        };
        dataGrid.addResizableColumn(fullNameCol, "Full Name", 350);

        // Subject ID (aka the unique ID for the user)
        dataGrid.addResizableColumn(new Column<User, String>(new TextCell()) {
            @Override
            public String getValue(final User userRef) {
                return userRef.getSubjectId();
            }
        }, "Unique User Identity", 300);


        dataGrid.addEndColumn(new EndColumn<>());
        // Only want to let user create a stroom user if they are using an external IDP.
        // If internal then all users are available to be picked from the account tbl.
        uiConfigCache.get(config -> {
            if (config != null) {
                if (config.isExternalIdentityProvider()) {
                    newButton = pagerView.addButton(
                            SvgPresets.ADD.title("Add Identity Provider User"));
                    addMultipleButton = pagerView.addButton(
                            SvgPresets.ADD_MULTIPLE.title("Add Multiple Identity Provider Users"));
                }
            }
        }, this);
    }

    @Override
    protected void onBind() {
        super.onBind();
        GwtNullSafe.consume(newButton, button ->
                registerHandler(newButton.addClickHandler(event -> {
                    if (MouseUtil.isPrimary(event)) {
                        onNew();
                    }
                })));
        // addMultipleButton not always present
        GwtNullSafe.consume(addMultipleButton, button ->
                registerHandler(addMultipleButton.addClickHandler(event -> {
                    if (MouseUtil.isPrimary(event)) {
                        onAddMultiple();
                    }
                })));
        registerHandler(selectionModel.addSelectionHandler(event -> {
            selected = selectionModel.getSelected();
            if (event.getSelectionType().isDoubleSelect()) {
                if (selected != null) {
                    HidePopupRequestEvent.builder(UserListPresenter.this).fire();
                }
            }
        }));
    }

    private void onNew() {
//        newPresenter.show(e -> {
//            if (e.isOk()) {
//                selected = newPresenter.getUserName();
//                if (selected != null) {
//                    HidePopupRequestEvent.builder(this).fire();
//                }
//            }
//            e.hide();
//        });
    }

    private void onAddMultiple() {
//        createMultipleUsersPresenter.show(e -> {
//            if (e.isOk()) {
//                HidePopupRequestEvent.builder(this).fire();
//            }
//            e.hide();
//        });
    }

    @Override
    public void changeNameFilter(String name) {
        if (findUserCriteria != null) {
            String filter = name;

            if (filter != null) {
                filter = filter.trim();
                if (filter.length() == 0) {
                    filter = null;
                }
            }

            try {
                final ExpressionOperator expression = QuickFilterExpressionParser
                        .parse(filter, UserFields.DEFAULT_FIELDS, UserFields.ALL_FIELD_MAP);
                findUserCriteria.setExpression(expression);
                dataProvider.refresh();

            } catch (final RuntimeException e) {
                GWT.log(e.getMessage());
            }
        }
    }

    public void show(final Consumer<User> userConsumer) {
        final FindUserCriteria findUserCriteria = new FindUserCriteria();
        setup(findUserCriteria);

        final PopupSize popupSize = PopupSize.resizable(1_000, 400);
        ShowPopupEvent.builder(this)
                .popupType(PopupType.OK_CANCEL_DIALOG)
                .popupSize(popupSize)
                .caption("Choose User To Add")
                .onShow(e -> getView().focus())
                .onHideRequest(e -> {
                    if (e.isOk()) {
                        final User selected = getSelected();
                        if (selected != null) {
//                            restFactory
//                                    .create(USER_RESOURCE)
//                                    .method(res -> res.createUser(selected))
//                                    .onSuccess(r -> {
//                                        userConsumer.accept(r);
//                                        e.hide();
//                                    })
//                                    .onFailure(RestErrorHandler.forPopup(this, e))
//                                    .taskListener(this)
//                                    .exec();
                        } else {
                            e.hide();
                        }
                    } else {
                        e.hide();
                    }
                })
                .fire();
    }

    public void setup(final FindUserCriteria findUserCriteria) {
        this.findUserCriteria = findUserCriteria;
        dataProvider = new UserDataProvider(getEventBus(), restFactory, getDataGrid());
        dataProvider.setCriteria(findUserCriteria, this);
        refresh();
    }

    public void refresh() {
        dataProvider.refresh();
    }

    public User getSelected() {
        return selected;
    }

    public DataGrid<User> getDataGrid() {
        return dataGrid;
    }
}
