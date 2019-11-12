/*
 * Copyright 2018 Crown Copyright
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

package stroom.annotation.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.MyPresenterWidget;
import com.gwtplatform.mvp.client.View;
import stroom.alert.client.event.AlertEvent;
import stroom.annotation.client.AnnotationEditPresenter.AnnotationEditView;
import stroom.annotation.shared.Annotation;
import stroom.annotation.shared.AnnotationDetail;
import stroom.annotation.shared.AnnotationEntry;
import stroom.annotation.shared.AnnotationResource;
import stroom.annotation.shared.CreateEntryRequest;
import stroom.dispatch.client.Rest;
import stroom.dispatch.client.RestFactory;
import stroom.security.client.api.ClientSecurityContext;
import stroom.security.shared.UserResource;
import stroom.widget.customdatebox.client.ClientDateUtil;
import stroom.widget.popup.client.event.HidePopupEvent;
import stroom.widget.popup.client.event.RenamePopupEvent;
import stroom.widget.popup.client.event.ShowPopupEvent;
import stroom.widget.popup.client.presenter.PopupPosition;
import stroom.widget.popup.client.presenter.PopupSize;
import stroom.widget.popup.client.presenter.PopupUiHandlers;
import stroom.widget.popup.client.presenter.PopupView.PopupType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class AnnotationEditPresenter extends MyPresenterWidget<AnnotationEditView> implements AnnotationEditUiHandlers {
    private static final long ONE_SECOND = 1000;
    private static final long ONE_MINUTE = ONE_SECOND * 60;
    private static final long ONE_HOUR = ONE_MINUTE * 60;

    private final RestFactory restFactory;
    private final ChooserPresenter statusPresenter;
    private final ChooserPresenter assignedToPresenter;
    private final ClientSecurityContext clientSecurityContext;

    private AnnotationDetail annotationDetail;
    private Annotation sourceAnnotation;
    private String currentTitle;
    private String currentSubject;
    private String currentStatus;
    private String currentAssignedTo;

    @Inject
    public AnnotationEditPresenter(final EventBus eventBus,
                                   final AnnotationEditView view,
                                   final RestFactory restFactory,
                                   final ChooserPresenter statusPresenter,
                                   final ChooserPresenter assignedToPresenter,
                                   final ClientSecurityContext clientSecurityContext) {
        super(eventBus, view);
        this.restFactory = restFactory;
        this.statusPresenter = statusPresenter;
        this.assignedToPresenter = assignedToPresenter;
        this.clientSecurityContext = clientSecurityContext;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(statusPresenter.addDataSelectionHandler(e -> {
            final String selected = statusPresenter.getSelected();
            changeStatus(selected, true);
        }));
        registerHandler(assignedToPresenter.addDataSelectionHandler(e -> {
            final String selected = assignedToPresenter.getSelected();
            changeAssignedTo(selected, true);
        }));
    }

    private void changeTitle(final String selected, final boolean addEntry) {
        if (!Objects.equals(currentTitle, selected)) {
            currentTitle = selected;
            getView().setTitle(selected);

            if (addEntry && annotationDetail != null) {
                final CreateEntryRequest request = new CreateEntryRequest(annotationDetail.getAnnotation(), Annotation.TITLE, selected);
                addEntry(request);
            }
        }
    }

    private void changeSubject(final String selected, final boolean addEntry) {
        if (!Objects.equals(currentSubject, selected)) {
            currentSubject = selected;
            getView().setSubject(selected);

            if (addEntry && annotationDetail != null) {
                final CreateEntryRequest request = new CreateEntryRequest(annotationDetail.getAnnotation(), Annotation.SUBJECT, selected);
                addEntry(request);
            }
        }
    }

    private void changeStatus(final String selected, final boolean addEntry) {
        if (!Objects.equals(currentStatus, selected)) {
            currentStatus = selected;
            getView().setStatus(selected);
            HidePopupEvent.fire(this, statusPresenter, true, true);

            if (addEntry && annotationDetail != null) {
                final CreateEntryRequest request = new CreateEntryRequest(annotationDetail.getAnnotation(), Annotation.STATUS, selected);
                addEntry(request);
            }
        }
    }

    private void changeAssignedTo(final String selected, final boolean addEntry) {
        if (!Objects.equals(currentAssignedTo, selected)) {
            currentAssignedTo = selected;
            getView().setAssignedTo(selected);
            HidePopupEvent.fire(this, assignedToPresenter, true, true);

            if (addEntry && annotationDetail != null) {
                final CreateEntryRequest request = new CreateEntryRequest(annotationDetail.getAnnotation(), Annotation.ASSIGNED_TO, selected);
                addEntry(request);
            }
        }
    }

    private void addEntry(final CreateEntryRequest request) {
        final AnnotationResource annotationResource = GWT.create(AnnotationResource.class);
        final Rest<AnnotationDetail> rest = restFactory.create();
        rest.onSuccess(this::read).call(annotationResource).createEntry(request);
    }

    public void show(final Annotation annotation) {
        this.sourceAnnotation = annotation;
        readAnnotation(sourceAnnotation);
        final AnnotationResource annotationResource = GWT.create(AnnotationResource.class);
        final Rest<AnnotationDetail> rest = restFactory.create();
        rest.onSuccess(this::edit).call(annotationResource).get(annotation.getId(), annotation.getStreamId(), annotation.getEventId());
    }

    private void edit(final AnnotationDetail annotationDetail) {
        read(annotationDetail);

        final PopupUiHandlers internalPopupUiHandlers = new PopupUiHandlers() {
            @Override
            public void onHideRequest(final boolean autoClose, final boolean ok) {
                hide();
            }

            @Override
            public void onHide(final boolean autoClose, final boolean ok) {
            }
        };

        final PopupSize popupSize = new PopupSize(800, 600, true);
        ShowPopupEvent.fire(this,
                this,
                PopupType.CLOSE_DIALOG,
                null,
                popupSize, getCaption(annotationDetail),
                internalPopupUiHandlers,
                false);

        getView().focusComment();
    }

    private String getCaption(final AnnotationDetail annotationDetail) {
        if (annotationDetail == null) {
            return "Create Annotation";
        }
        return "Edit Annotation";
    }

    private void hide() {
        HidePopupEvent.fire(AnnotationEditPresenter.this, AnnotationEditPresenter.this);
    }

    private void read(final AnnotationDetail annotationDetail) {
        final Date now = new Date();

        if (annotationDetail != null) {
            if (this.annotationDetail == null) {
                // If this is an existing annotation then change the dialog caption.
                RenamePopupEvent.fire(this, this, getCaption(annotationDetail));
            }
            this.annotationDetail = annotationDetail;

            getView().setButtonText("Comment");

            readAnnotation(annotationDetail.getAnnotation());

            final List<AnnotationEntry> entries = annotationDetail.getEntries();
            if (entries != null) {
                final SafeHtmlBuilder builder = new SafeHtmlBuilder();
                builder.appendHtmlConstant("<div class=\"annotationHistoryInner\">");
                entries.forEach(entry -> {
                    builder.appendHtmlConstant("<div class=\"annotationHistoryLine\">");
                    builder.appendHtmlConstant("<div class=\"annotationHistoryLineMargin\"><div class=\"annotationHistoryLineMarker\"></div></div>");
                    if (Annotation.COMMENT.equals(entry.getEntryType())) {
                        builder.appendHtmlConstant("<div class=\"annotationHistoryCommentBorder\">");
                        builder.appendHtmlConstant("<div class=\"annotationHistoryCommentHeader\">");
                        builder.appendHtmlConstant("<b>");
                        builder.appendEscaped(entry.getCreateUser());
                        builder.appendHtmlConstant("</b>");
                        builder.appendEscaped(" commented ");
                        builder.append(getDurationLabel(entry.getCreateTime(), now));
                        builder.appendHtmlConstant("</div>");
                        builder.appendHtmlConstant("<div class=\"annotationHistoryCommentBody\">");
                        builder.appendEscaped(entry.getData());
                        builder.appendHtmlConstant("</div>");
                        builder.appendHtmlConstant("</div>");
                    } else {
                        builder.appendHtmlConstant("<div class=\"annotationHistoryItem\">");
                        builder.appendHtmlConstant("<b>");
                        builder.appendEscaped(entry.getCreateUser());
                        builder.appendHtmlConstant("</b>");
                        builder.appendEscaped(" changed ");
                        builder.appendEscaped(entry.getEntryType().toLowerCase());
                        builder.appendEscaped(" to ");
                        builder.appendHtmlConstant("<b>");
                        if (entry.getData() != null && !entry.getData().trim().isEmpty()) {
                            builder.appendEscaped(entry.getData());
                        } else {
                            builder.appendEscaped("'  '");
                        }
                        builder.appendHtmlConstant("</b> ");
                        builder.append(getDurationLabel(entry.getCreateTime(), now));
                        builder.appendHtmlConstant("</div>");
                    }
                    builder.appendHtmlConstant("</div>");
                });

                builder.appendHtmlConstant("</div>");

                final HTML panel = new HTML(builder.toSafeHtml());
                panel.setStyleName("annotationHistoryOuter");
                getView().setHistoryView(panel);
            }

        } else {
            getView().setButtonText("Create");
        }

        if (currentStatus == null) {
            final AnnotationResource annotationResource = GWT.create(AnnotationResource.class);
            final Rest<List<String>> rest = restFactory.create();
            rest.onSuccess(values -> {
                if (currentStatus == null && values != null && values.size() > 0) {
                    changeStatus(values.get(0), false);
                }
            }).call(annotationResource).getStatus(null);
        }

        if (currentTitle == null || currentTitle.trim().length() == 0) {
            changeTitle(null, false);
        }

        if (currentSubject == null || currentSubject.trim().length() == 0) {
            changeSubject(null, false);
        }
    }

    private void readAnnotation(final Annotation annotation) {
        currentTitle = annotation.getTitle();
        currentSubject = annotation.getSubject();
        currentStatus = annotation.getStatus();
        currentAssignedTo = annotation.getAssignedTo();
        getView().setTitle(currentTitle);
        getView().setSubject(currentSubject);
        getView().setStatus(currentStatus);
        getView().setAssignedTo(currentAssignedTo);
        getView().setAssignYourselfVisible(!Objects.equals(currentAssignedTo, clientSecurityContext.getUserId()));
    }

    private SafeHtml getDurationLabel(final long time, final Date now) {
        final SafeHtmlBuilder builder = new SafeHtmlBuilder();
        builder.appendHtmlConstant("<span class=\"annotationDurationLabel\" title=\"" + ClientDateUtil.toISOString(time) + "\">");
        builder.appendEscaped(getDuration(time, now));
        builder.appendHtmlConstant("</span>");
        return builder.toSafeHtml();
    }

    private String getDuration(final long time, final Date now) {
        final Date start = new Date(time);
        final int days = CalendarUtil.getDaysBetween(start, now);
        if (days == 1) {
            return "yesterday";
        } else if (days > 365) {
            final int years = days / 365;
            if (years == 1) {
                return "a year ago";
            } else {
                return years + "years ago";
            }
        } else if (days > 1) {
            return days + " days ago";
        }

        long diff = now.getTime() - time;
        if (diff > ONE_HOUR) {
            final int hours = (int) (diff / ONE_HOUR);
            if (hours == 1) {
                return "an hour ago";
            } else if (hours > 1) {
                return hours + " hours ago";
            }
        }

        if (diff > ONE_MINUTE) {
            final int minutes = (int) (diff / ONE_MINUTE);
            if (minutes == 1) {
                return "a minute ago";
            } else if (minutes > 1) {
                return minutes + " minutes ago";
            }
        }

        if (diff > ONE_SECOND) {
            final int seconds = (int) (diff / ONE_SECOND);
            if (seconds == 1) {
                return "a second ago";
            } else if (seconds > 1) {
                return seconds + " seconds ago";
            }
        }

        return "just now";
    }

    @Override
    public void onTitleChange() {
        changeTitle(getView().getTitle(), true);
    }

    @Override
    public void onSubjectChange() {
        changeSubject(getView().getSubject(), true);
    }

    @Override
    public void showStatusChooser(final Element element) {
        statusPresenter.setDataSupplier((filter, consumer) -> {
            final AnnotationResource annotationResource = GWT.create(AnnotationResource.class);
            final Rest<List<String>> rest = restFactory.create();
            rest.onSuccess(consumer).call(annotationResource).getStatus(filter);
        });
        statusPresenter.clearFilter();
        statusPresenter.setSelected(currentStatus);
        final PopupPosition popupPosition = new PopupPosition(element.getAbsoluteLeft() - 1,
                element.getAbsoluteTop() + element.getClientHeight() + 2);
        ShowPopupEvent.fire(this, statusPresenter, PopupType.POPUP, popupPosition, null, element);
    }

    @Override
    public void showAssignedToChooser(final Element element) {
        if (currentAssignedTo == null) {
            assignedToPresenter.setClearSelectionText(null);
        } else {
            assignedToPresenter.setClearSelectionText("Clear");
        }
        assignedToPresenter.setDataSupplier((filter, consumer) -> {
            final UserResource userResource = GWT.create(UserResource.class);
            final Rest<List<String>> rest = restFactory.create();
            rest.onSuccess(consumer).call(userResource).getAssociates(filter);
        });
        assignedToPresenter.clearFilter();
        assignedToPresenter.setSelected(currentAssignedTo);
        final PopupPosition popupPosition = new PopupPosition(element.getAbsoluteLeft() - 1,
                element.getAbsoluteTop() + element.getClientHeight() + 2);
        ShowPopupEvent.fire(this, assignedToPresenter, PopupType.POPUP, popupPosition, null, element);
    }

    @Override
    public void assignYourself() {
        changeAssignedTo(clientSecurityContext.getUserId(), true);
    }

    @Override
    public void create() {
        final String comment = getView().getComment();
        if (comment != null && comment.length() > 0) {
            final Annotation annotation = new Annotation();
            annotation.setStreamId(sourceAnnotation.getStreamId());
            annotation.setEventId(sourceAnnotation.getEventId());
            annotation.setTitle(currentTitle);
            annotation.setSubject(currentSubject);
            annotation.setStatus(currentStatus);
            annotation.setAssignedTo(currentAssignedTo);
            annotation.setComment(comment);
            annotation.setHistory(comment);

            final CreateEntryRequest request = new CreateEntryRequest(annotation, Annotation.COMMENT, comment);
            addEntry(request);
            getView().setComment("");
        } else {
            AlertEvent.fireWarn(this, "Please enter a comment", null);
        }
    }

    public interface AnnotationEditView extends View, HasUiHandlers<AnnotationEditUiHandlers> {
        String getTitle();

        void setTitle(String title);

        String getSubject();

        void setSubject(String subject);

        void setStatus(String status);

        void setAssignedTo(String assignedTo);

        String getComment();

        void setComment(String comment);

        void setHistoryView(Widget view);

        void setButtonText(String text);

        void setAssignYourselfVisible(boolean visible);

        void focusComment();
    }
}