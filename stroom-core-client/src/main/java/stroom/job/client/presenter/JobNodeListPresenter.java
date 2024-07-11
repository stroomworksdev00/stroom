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

package stroom.job.client.presenter;

import stroom.alert.client.event.AlertEvent;
import stroom.cell.info.client.InfoHelpLinkColumn;
import stroom.cell.tickbox.client.TickBoxCell;
import stroom.cell.tickbox.shared.TickBoxState;
import stroom.cell.valuespinner.client.ValueSpinnerCell;
import stroom.cell.valuespinner.shared.EditableInteger;
import stroom.data.client.presenter.ColumnSizeConstants;
import stroom.data.client.presenter.RestDataProvider;
import stroom.data.grid.client.EndColumn;
import stroom.data.grid.client.MyDataGrid;
import stroom.data.grid.client.OrderByColumn;
import stroom.data.grid.client.PagerView;
import stroom.dispatch.client.RestErrorHandler;
import stroom.dispatch.client.RestFactory;
import stroom.job.client.JobTypeCell;
import stroom.job.shared.FindJobNodeCriteria;
import stroom.job.shared.Job;
import stroom.job.shared.JobNode;
import stroom.job.shared.JobNode.JobType;
import stroom.job.shared.JobNodeInfo;
import stroom.job.shared.JobNodeResource;
import stroom.job.shared.JobNodeUtil;
import stroom.job.shared.ScheduleReferenceTime;
import stroom.preferences.client.DateTimeFormatter;
import stroom.schedule.client.SchedulePopup;
import stroom.svg.client.Preset;
import stroom.svg.client.SvgPresets;
import stroom.ui.config.client.UiConfigCache;
import stroom.util.client.DataGridUtil;
import stroom.util.client.DelayedUpdate;
import stroom.util.shared.ModelStringUtil;
import stroom.util.shared.ResultPage;
import stroom.util.shared.scheduler.Schedule;
import stroom.widget.util.client.MouseUtil;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.view.client.Range;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.MyPresenterWidget;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class JobNodeListPresenter extends MyPresenterWidget<PagerView> {

    private static final JobNodeResource JOB_NODE_RESOURCE = GWT.create(JobNodeResource.class);

    private final RestFactory restFactory;
    private final DateTimeFormatter dateTimeFormatter;
    private final SchedulePopup schedulePresenter;
    private final UiConfigCache clientPropertyCache;

    private final RestDataProvider<JobNode, ResultPage<JobNode>> dataProvider;
    private final Map<JobNode, JobNodeInfo> latestNodeInfo = new HashMap<>();

    private final MyDataGrid<JobNode> dataGrid;

    private String jobName;
    private final FindJobNodeCriteria findJobNodeCriteria = new FindJobNodeCriteria();
    private final DelayedUpdate redrawDelayedUpdate;

    @Inject
    public JobNodeListPresenter(final EventBus eventBus,
                                final PagerView view,
                                final RestFactory restFactory,
                                final DateTimeFormatter dateTimeFormatter,
                                final SchedulePopup schedulePresenter,
                                final UiConfigCache clientPropertyCache) {
        super(eventBus, view);
        this.restFactory = restFactory;
        this.dateTimeFormatter = dateTimeFormatter;
        this.schedulePresenter = schedulePresenter;
        this.clientPropertyCache = clientPropertyCache;

        dataGrid = new MyDataGrid<>();
        dataGrid.addDefaultSelectionModel(true);
        this.redrawDelayedUpdate = new DelayedUpdate(dataGrid::redraw);
        view.setDataWidget(dataGrid);

        initTable();

        dataProvider = new RestDataProvider<JobNode, ResultPage<JobNode>>(eventBus) {
            @Override
            protected void exec(final Range range,
                                final Consumer<ResultPage<JobNode>> dataConsumer,
                                final RestErrorHandler errorHandler) {
                findJobNodeCriteria.getJobName().setString(jobName);
                restFactory
                        .create(JOB_NODE_RESOURCE)
                        .method(res -> res.find(findJobNodeCriteria))
                        .onSuccess(dataConsumer)
                        .onFailure(errorHandler)
                        .taskListener(view)
                        .exec();
            }

            @Override
            protected void changeData(final ResultPage<JobNode> data) {
                // Ping each node.
                data.getValues().forEach(row -> {
                    restFactory
                            .create(JOB_NODE_RESOURCE)
                            .method(res -> res.info(row.getJob().getName(), row.getNodeName()))
                            .onSuccess(info -> {
                                latestNodeInfo.put(row, info);
                                scheduleDataGridRedraw();
                            })
                            .onFailure(throwable -> {
                                latestNodeInfo.remove(row);
                                scheduleDataGridRedraw();
                            })
                            .taskListener(getView())
                            .exec();
                });
                super.changeData(data);
            }
        };
    }

    private void scheduleDataGridRedraw() {
        // Saves the grid being redrawn for every single row in the list
        redrawDelayedUpdate.update();
    }

    private void refresh() {
        dataProvider.refresh();
    }

    /**
     * Add the columns to the table.
     */
    private void initTable() {

        DataGridUtil.addColumnSortHandler(dataGrid, findJobNodeCriteria, this::refresh);

        // Enabled.
        final Column<JobNode, TickBoxState> enabledColumn = new OrderByColumn<JobNode, TickBoxState>(
                TickBoxCell.create(false, false),
                FindJobNodeCriteria.FIELD_ID_ENABLED,
                true) {
            @Override
            public TickBoxState getValue(final JobNode row) {
                return TickBoxState.fromBoolean(row.isEnabled());
            }
        };
        enabledColumn.setFieldUpdater((index, row, value) -> {
            row.setEnabled(value.toBoolean());
            restFactory
                    .create(JOB_NODE_RESOURCE)
                    .call(res -> res.setEnabled(row.getId(), value.toBoolean()))
                    .taskListener(getView())
                    .exec();
        });
        dataGrid.addColumn(enabledColumn, "Enabled", 80);

        // Job Name
        final Column<JobNode, String> nameColumn = new Column<JobNode, String>(new TextCell()) {
            @Override
            public String getValue(final JobNode row) {
                return row.getJob().getName();
            }
        };
        dataGrid.addResizableColumn(nameColumn, "Job", 200);

        // Help
        dataGrid.addColumn(new InfoHelpLinkColumn<JobNode>() {
            @Override
            public Preset getValue(final JobNode row) {
                if (row != null) {
                    return SvgPresets.HELP;
                }
                return null;
            }

            @Override
            protected void showHelp(final JobNode row) {
                clientPropertyCache.get(result -> {
                    if (result != null) {
                        final String helpUrl = result.getHelpUrlJobs();
                        if (helpUrl != null && helpUrl.trim().length() > 0) {
                            // This is a bit fragile as if the headings change in the docs then the anchors
                            // won't work
                            final String url = helpUrl
                                    + formatAnchor(row.getJob().getName());
                            Window.open(url, "_blank", "");
                        } else {
                            AlertEvent.fireError(JobNodeListPresenter.this, "Help is not configured!", null);
                        }
                    }
                }, getView());
            }

        }, "<br/>", 20);

        // Node Name
        final Column<JobNode, String> nodeColumn = new OrderByColumn<JobNode, String>(
                new TextCell(),
                FindJobNodeCriteria.FIELD_ID_NODE,
                true) {
            @Override
            public String getValue(final JobNode row) {
                return row.getNodeName();
            }
        };
        dataGrid.addResizableColumn(nodeColumn, "Node", 200);

        // Schedule.
        final Column<JobNode, String> typeColumn = new Column<JobNode, String>(new TextCell()) {
            @Override
            public String getValue(final JobNode row) {
                final JobNode jobNode = row;
                final JobType jobType = jobNode.getJobType();
                if (JobType.CRON.equals(jobType)) {
                    return "Cron " + jobNode.getSchedule();
                } else if (JobType.FREQUENCY.equals(jobType)) {
                    return "Frequency " + jobNode.getSchedule();
                } else if (JobType.DISTRIBUTED.equals(jobType)) {
                    return "Distributed";
                }
                return null;
            }

            @Override
            public void onBrowserEvent(final Context context, final Element elem, final JobNode row,
                                       final NativeEvent event) {
                super.onBrowserEvent(context, elem, row, event);
                if (row != null && MouseUtil.isPrimary(event)) {
                    showSchedule(row);
                }
            }
        };
        dataGrid.addResizableColumn(typeColumn, "Type", 250);

        // Job Type.
        final Column<JobNode, JobType> typeEditColumn = new Column<JobNode, JobType>(new JobTypeCell()) {
            @Override
            public JobType getValue(final JobNode row) {
                if (row.getJobType() == null) {
                    return JobType.UNKNOWN;
                }
                return row.getJobType();
            }

            @Override
            public void onBrowserEvent(final Context context, final Element elem, final JobNode row,
                                       final NativeEvent event) {
                super.onBrowserEvent(context, elem, row, event);
                if (row != null && MouseUtil.isPrimary(event)) {
                    showSchedule(row);
                }
            }
        };
        dataGrid.addColumn(typeEditColumn, "", ColumnSizeConstants.ICON_COL);

        // Max.
        final Column<JobNode, Number> maxColumn = new Column<JobNode, Number>(new ValueSpinnerCell(1, 1000)) {
            @Override
            public Number getValue(final JobNode row) {
                if (JobType.DISTRIBUTED.equals(row.getJobType())) {
                    return new EditableInteger(row.getTaskLimit());
                }
                return null;
            }
        };

        maxColumn.setFieldUpdater((index, row, value) -> {
            row.setTaskLimit(value.intValue());
            restFactory
                    .create(JOB_NODE_RESOURCE)
                    .call(res -> res.setTaskLimit(row.getId(), value.intValue()))
                    .taskListener(getView())
                    .exec();
        });
        dataGrid.addColumn(maxColumn, "Max", 62);

        // Cur.
        final Column<JobNode, String> curColumn = new Column<JobNode, String>(new TextCell()) {
            @Override
            public String getValue(final JobNode row) {
                final JobNodeInfo jobNodeInfo = latestNodeInfo.get(row);
                if (jobNodeInfo != null) {
                    return ModelStringUtil.formatCsv(jobNodeInfo.getCurrentTaskCount());
                } else {
                    return "?";
                }
            }
        };
        dataGrid.addColumn(curColumn, "Cur", 59);

        // Last executed.
        final Column<JobNode, String> lastExecutedColumn = new Column<JobNode, String>(new TextCell()) {
            @Override
            public String getValue(final JobNode row) {
                final JobNodeInfo jobNodeInfo = latestNodeInfo.get(row);
                if (jobNodeInfo != null) {
                    return dateTimeFormatter.formatWithDuration(jobNodeInfo.getLastExecutedTime());
                } else {
                    return "?";
                }
            }
        };
        dataGrid.addColumn(lastExecutedColumn, "Last Executed", ColumnSizeConstants.DATE_AND_DURATION_COL);

        dataGrid.addEndColumn(new EndColumn<>());
    }

    private void showSchedule(final JobNode row) {
        restFactory
                .create(JOB_NODE_RESOURCE)
                .method(res -> res.info(row.getJob().getName(), row.getNodeName()))
                .onSuccess(result -> setSchedule(row, result))
                .onFailure(throwable -> setSchedule(row, null))
                .taskListener(getView())
                .exec();
    }

    private void setSchedule(final JobNode jobNode, JobNodeInfo jobNodeInfo) {
        final Schedule currentSchedule = JobNodeUtil.getSchedule(jobNode);
        if (currentSchedule != null) {
            if (jobNodeInfo == null) {
                jobNodeInfo = new JobNodeInfo();
            }

            schedulePresenter.setSchedule(currentSchedule, new ScheduleReferenceTime(
                    jobNodeInfo.getScheduleReferenceTime(),
                    jobNodeInfo.getLastExecutedTime()));
            schedulePresenter.show(schedule -> {
                JobNodeUtil.setSchedule(jobNode, schedule);
                restFactory
                        .create(JOB_NODE_RESOURCE)
                        .call(res -> res.setSchedule(jobNode.getId(), schedule))
                        .onSuccess(result ->
                                dataProvider.refresh())
                        .taskListener(getView())
                        .exec();
            });
        }
    }

    public void read(final Job job) {
        if (jobName == null) {
            jobName = job.getName();
            dataProvider.addDataDisplay(dataGrid);
        } else {
            jobName = job.getName();
            dataProvider.refresh();
        }
    }
}
