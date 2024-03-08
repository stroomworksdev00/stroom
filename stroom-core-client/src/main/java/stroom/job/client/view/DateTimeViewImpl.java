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

package stroom.job.client.view;

import stroom.job.client.presenter.DateTimePopup.DateTimeView;
import stroom.widget.datepicker.client.CustomDatePicker;
import stroom.widget.datepicker.client.UTCDate;
import stroom.widget.datepicker.client.ValueChooser;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DateTimeViewImpl extends ViewImpl implements DateTimeView {

    private final Widget widget;

    @SuppressWarnings("unused")
    @UiField
    Label date;
    @SuppressWarnings("unused")
    @UiField
    CustomDatePicker datePicker;
    @SuppressWarnings("unused")
    @UiField
    Label time;
    @SuppressWarnings("unused")
    @UiField
    ValueChooser hour;
    @SuppressWarnings("unused")
    @UiField
    ValueChooser minute;
    @SuppressWarnings("unused")
    @UiField
    ValueChooser second;
    @SuppressWarnings("unused")
    @UiField
    ValueChooser millisecond;

    @SuppressWarnings("unused")
    @UiField
    Label today;
    @SuppressWarnings("unused")
    @UiField
    Label yesterday;
    @SuppressWarnings("unused")
    @UiField
    Label weekStart;
    @SuppressWarnings("unused")
    @UiField
    Label now;
    @SuppressWarnings("unused")
    @UiField
    Label midnight;
    @SuppressWarnings("unused")
    @UiField
    Label midday;

    private UTCDate value;
    private String currentDateString;
    private DateTimeModel dateTimeModel;

    @Inject
    public DateTimeViewImpl(final Binder binder) {
        widget = binder.createAndBindUi(this);
        datePicker.setYearAndMonthDropdownVisible(true);
        datePicker.setYearArrowsVisible(true);

        hour.setMin(0);
        hour.setMax(23);
        minute.setMin(0);
        minute.setMax(59);
        second.setMin(0);
        second.setMax(59);
        millisecond.setMin(0);
        millisecond.setMax(999);
    }

    private void setDatePickerTime(final UTCDate value) {
        final DateRecord dateRecord = dateTimeModel.parseDate(value);
        final UTCDate utc = UTCDate.create(
                dateRecord.getYear(),
                dateRecord.getMonth(),
                dateRecord.getDay(),
                0,
                0,
                0,
                0);

        datePicker.setCurrentMonth(utc);
        datePicker.setValue(utc);
        datePicker.setToday(getTodayUTC());
    }

    @Override
    public long getTime() {
        return (long) value.getTime();
    }

    @Override
    public void setTime(final long time) {
        this.value = UTCDate.create(time);
        update();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    @Override
    public void focus() {
        hour.focus();
    }

    private UTCDate getTodayUTC() {
        final DateRecord today = dateTimeModel.parseDate(UTCDate.create());
        return UTCDate.create(
                today.getYear(),
                today.getMonth(),
                today.getDay(),
                0,
                0,
                0,
                0);
    }

    @SuppressWarnings("unused")
    @UiHandler("today")
    public void onToday(final ClickEvent event) {
        final UTCDate utc = getTodayUTC();
        setDatePickerTime(utc);
        value = resolveDateTime(value);
        update();
    }

    @SuppressWarnings("unused")
    @UiHandler("yesterday")
    public void onYesterday(final ClickEvent event) {
        final UTCDate utc = getTodayUTC();
        utc.setDate(utc.getDate() - 1);
        setDatePickerTime(utc);
        value = resolveDateTime(value);
        update();
    }

    @SuppressWarnings("unused")
    @UiHandler("weekStart")
    public void onWeekStart(final ClickEvent event) {
        final UTCDate utc = getTodayUTC();
        utc.setDate(utc.getDate() - utc.getDay());
        setDatePickerTime(utc);
        value = resolveDateTime(value);
        update();
    }

    @SuppressWarnings("unused")
    @UiHandler("datePicker")
    public void onDatePicker(final ValueChangeEvent<UTCDate> event) {
        value = resolveDateTime(value);
        updateDateLabel();
        updateTime();
    }

    @SuppressWarnings("unused")
    @UiHandler("now")
    public void onNow(final ClickEvent event) {
        final TimeRecord timeRecord = dateTimeModel.parseTime(UTCDate.create());
        hour.setValue(timeRecord.getHour());
        minute.setValue(timeRecord.getMinute());
        second.setValue(timeRecord.getSecond());
        millisecond.setValue(timeRecord.getMillisecond());
        value = resolveDateTime(value);
        update();
    }

    @SuppressWarnings("unused")
    @UiHandler("midnight")
    public void onMidnight(final ClickEvent event) {
        hour.setValue(0);
        minute.setValue(0);
        second.setValue(0);
        millisecond.setValue(0);
        value = resolveDateTime(value);
        update();
    }

    @SuppressWarnings("unused")
    @UiHandler("midday")
    public void onMidday(final ClickEvent event) {
        hour.setValue(12);
        minute.setValue(0);
        second.setValue(0);
        millisecond.setValue(0);
        value = resolveDateTime(value);
        update();
    }

    @SuppressWarnings("unused")
    @UiHandler("hour")
    public void onHour(final ValueChangeEvent<Long> event) {
        final DateRecord dateBefore = dateTimeModel.parseDate(value);
        final int hour = this.hour.getIntValue();
        value = resolveDateTime(value, false);
        updateTime();

        // Deal with daylight savings offset changes that could switch the day.
        if (hour == 0) {
            final DateRecord dateAfter = dateTimeModel.parseDate(value);
            if (!dateBefore.equals(dateAfter)) {
                GWT.log("Fix hour for DST: " + dateBefore + " -> " + dateAfter);
                value.setTime(value.getTime() + DateTimeModel.MILLIS_IN_HOUR);
                updateTime();
            }
        }
    }

    @SuppressWarnings("unused")
    @UiHandler("minute")
    public void onMinute(final ValueChangeEvent<Long> event) {
        value = resolveDateTime(value, false);
        updateTime();
    }

    @SuppressWarnings("unused")
    @UiHandler("second")
    public void onSecond(final ValueChangeEvent<Long> event) {
        value.setSeconds(second.getIntValue());
        updateTimeLabel();
    }

    @SuppressWarnings("unused")
    @UiHandler("millisecond")
    public void onMillisecond(final ValueChangeEvent<Long> event) {
        value.setMilliseconds(millisecond.getIntValue());
        updateTimeLabel();
    }

    private UTCDate resolveDateTime(final UTCDate previousTime) {
        return resolveDateTime(previousTime, true);
    }

    private UTCDate resolveDateTime(final UTCDate previousTime, final boolean allowOffsetChange) {
        final UTCDate datePickerTime = datePicker.getValue();

        final long currentOffsetMs = dateTimeModel.getOffsetMillis(previousTime);
        final UTCDate date = UTCDate.create(
                datePickerTime.getFullYear(),
                datePickerTime.getMonth(),
                datePickerTime.getDate(),
                hour.getIntValue(),
                minute.getIntValue(),
                second.getIntValue(),
                millisecond.getIntValue());
        date.setTime(date.getTime() - currentOffsetMs);

        final long newOffsetMs = dateTimeModel.getOffsetMillis(date);
        if (allowOffsetChange && newOffsetMs != currentOffsetMs) {
            GWT.log("Offset changed: " + currentOffsetMs + " -> " + newOffsetMs);
            final UTCDate newDate = UTCDate.create(
                    datePickerTime.getFullYear(),
                    datePickerTime.getMonth(),
                    datePickerTime.getDate(),
                    hour.getIntValue(),
                    minute.getIntValue(),
                    second.getIntValue(),
                    millisecond.getIntValue());
            newDate.setTime(newDate.getTime() - newOffsetMs);
            return newDate;

        } else {
            return date;
        }
    }

    private void update() {
        updateDate();
        updateTime();
    }

    private void updateDate() {
        setDatePickerTime(value);
    }

    private void updateDateLabel() {
        final String dateString = dateTimeModel.formatDateLabel(value);
        this.date.setText(dateString);

        if (currentDateString == null || !currentDateString.equals(dateString)) {
            currentDateString = dateString;
            setDatePickerTime(value);
        }
    }

    private void updateTime() {
        final TimeOffset offset = dateTimeModel.getOffset(value);

        int hour = value.getHours() + offset.getHours();
        if (hour > 23) {
            hour = hour - 24;
        } else if (hour < 0) {
            hour = hour + 24;
        }

        int minute = value.getMinutes() + offset.getMinutes();
        if (minute > 59) {
            minute = minute - 60;
        } else if (minute < 0) {
            minute = minute + 60;
        }

        this.hour.setValue(hour);
        this.minute.setValue(minute);
        this.second.setValue(value.getSeconds());
        this.millisecond.setValue(value.getMilliseconds());

        updateDateLabel();
        updateTimeLabel();
    }

    private void updateTimeLabel() {
        final String timeString = dateTimeModel.formatTimeLabel(value);
        time.setText(timeString);
    }

    @Override
    public void setDateTimeModel(final DateTimeModel dateTimeModel) {
        this.dateTimeModel = dateTimeModel;
    }

    public interface Binder extends UiBinder<Widget, DateTimeViewImpl> {

    }
}
