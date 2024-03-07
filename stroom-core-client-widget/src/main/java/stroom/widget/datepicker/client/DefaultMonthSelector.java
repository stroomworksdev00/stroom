/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package stroom.widget.datepicker.client;

import stroom.docref.HasDisplayValue;
import stroom.item.client.SelectionBox;
import stroom.svg.shared.SvgImage;
import stroom.widget.button.client.InlineSvgButton;
import stroom.widget.datepicker.client.IntlDateTimeFormat.FormatOptions;
import stroom.widget.datepicker.client.IntlDateTimeFormat.FormatOptions.Year;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.Objects;

/**
 * A simple {@link MonthSelector} used for the default date picker. It allows to select months and
 * years but the name is not changed for backward compatibility. Also not extensible as we wish to
 * evolve it freely over time.
 */

public final class DefaultMonthSelector extends MonthSelector {

    private InlineSvgButton monthBackwards;
    private InlineSvgButton monthForwards;
    private FlowPanel layout;
    private InlineSvgButton yearBackwards;
    private InlineSvgButton yearForwards;
    private SelectionBox<IntItem> monthSelect;
    private SelectionBox<IntItem> yearSelect;
    private int monthColumn;

    /**
     * Returns the button for moving to the previous month.
     */
    public Element getBackwardButtonElement() {
        return monthBackwards.getElement();
    }

    /**
     * Returns the button for moving to the next month.
     */
    public Element getForwardButtonElement() {
        return monthForwards.getElement();
    }

    /**
     * Returns the button for moving to the previous year.
     */
    public Element getYearBackwardButtonElement() {
        return yearBackwards.getElement();
    }

    /**
     * Returns the button for moving to the next year.
     */
    public Element getYearForwardButtonElement() {
        return yearForwards.getElement();
    }

    /**
     * Returns the ListBox for selecting the month
     */
    public SelectionBox<IntItem> getMonthSelectListBox() {
        return monthSelect;
    }

    /**
     * Returns the ListBox for selecting the year
     */
    public SelectionBox<IntItem> getYearSelectListBox() {
        return yearSelect;
    }

    @Override
    protected void refresh() {
        if (isDatePickerConfigChanged()) {
            // if the config has changed since the last refresh, rebuild the grid
            setupGrid();
        }

        setDate(getModel().getCurrentMonth());
    }

    @Override
    protected void setup() {
        // previous, next buttons
        monthBackwards = createNavigationButton(SvgImage.ARROW_LEFT, -1, css().previousButton());
        monthForwards = createNavigationButton(SvgImage.ARROW_RIGHT, 1, css().nextButton());
        yearBackwards = createNavigationButton(SvgImage.ARROW_LEFT, -12, css().previousYearButton());
        yearForwards = createNavigationButton(SvgImage.ARROW_RIGHT, 12, css().nextYearButton());

        // month and year selector
        monthSelect = createMonthSelect();
        yearSelect = createYearSelect();

        // Set up grid.
        layout = new FlowPanel();
        layout.setStyleName(css().monthSelector());

        setupGrid();

        initWidget(layout);
    }

    private InlineSvgButton createNavigationButton(
            final SvgImage svgImage, final int noOfMonths, String styleName) {
        InlineSvgButton button = new InlineSvgButton();
        button.setSvg(svgImage);

        button.addClickHandler(event -> addMonths(noOfMonths));

        button.addStyleName(styleName);

        return button;
    }

    private SelectionBox<IntItem> createMonthSelect() {
        final SelectionBox<IntItem> monthListBox = new SelectionBox<>();

        for (int i = 0; i < CalendarModel.MONTHS_IN_YEAR; i++) {
            monthListBox.addItem(new IntItem(getModel().formatMonth(i), i));
        }

        monthListBox.addValueChangeHandler(e -> {
            int previousMonth = getModel().getCurrentMonth().getMonth();
            int newMonth = monthListBox.getValue().getValue();
            int delta = newMonth - previousMonth;
            addMonths(delta);
        });

        return monthListBox;
    }

    private SelectionBox<IntItem> createYearSelect() {
        final SelectionBox<IntItem> yearListBox = new SelectionBox<>();
        yearListBox.addValueChangeHandler(e -> {
            int previousYear = getModel().getCurrentMonth().getFullYear();
            int newYear = yearListBox.getValue().getValue();
            int delta = newYear - previousYear;
            addMonths(delta * CalendarModel.MONTHS_IN_YEAR);
        });

        return yearListBox;
    }


    private boolean isDatePickerConfigChanged() {
        boolean isMonthCurrentlySelectable = monthSelect.getParent() != null;
        boolean isYearNavigationCurrentlyEnabled = yearBackwards.getParent() != null;

        return getDatePicker().isYearAndMonthDropdownVisible() != isMonthCurrentlySelectable ||
                getDatePicker().isYearArrowsVisible() != isYearNavigationCurrentlyEnabled;
    }

    private void setDate(UTCDate date) {
        if (getDatePicker().isYearAndMonthDropdownVisible()) {
            // setup months dropdown
            int month = date.getMonth();
            monthSelect.setValue(new IntItem(getModel().formatMonth(month), month));

            // setup years dropdown
            yearSelect.clear();

            int year = date.getFullYear();
            int startYear = year - getNoOfYearsToDisplayBefore();
            int endYear = year + getNoOfYearsToDisplayAfter();

            UTCDate newDate = UTCDate.create();

            for (int i = startYear; i <= endYear; i++) {
                newDate.setFullYear(i);

                yearSelect.addItem(new IntItem(formatYear(newDate), i));
            }
            yearSelect.setValue(new IntItem(formatYear(UTCDate.create(year, 0)), year));
        } else {
//            layout.getElement().setInnerHTML(getModel().formatCurrentMonthAndYear());
        }
    }

    private String formatYear(UTCDate newDate) {
        final FormatOptions yearFormat = FormatOptions.builder().year(Year.NUMERIC).build();
        return IntlDateTimeFormat.format(newDate, IntlDateTimeFormat.DEFAULT_LOCALE, yearFormat);
    }

    private int getNoOfYearsToDisplayBefore() {
        return (getDatePicker().getVisibleYearCount() - 1) / 2;
    }

    private int getNoOfYearsToDisplayAfter() {
        return getDatePicker().getVisibleYearCount() / 2;
    }

    private void setupGrid() {
        layout.clear();

        // Month/Year column
        if (getDatePicker().isYearAndMonthDropdownVisible()) {
            final FlowPanel year = new FlowPanel();
            year.setStyleName(css().year());
            year.add(yearSelect);
            year.add(yearBackwards);
            year.add(yearForwards);

            final FlowPanel month = new FlowPanel();
            month.setStyleName(css().month());
            month.add(monthSelect);
            month.add(monthBackwards);
            month.add(monthForwards);

//            if (getModel().isMonthBeforeYear()) {
//                layout.add(month);
//                layout.add(year);
//            } else {
            layout.add(year);
            layout.add(month);
//            }
        }
    }

    public static class IntItem implements HasDisplayValue {

        private final String label;
        private final int value;

        public IntItem(final String label, final int value) {
            this.label = label;
            this.value = value;
        }

        @Override
        public String getDisplayValue() {
            return label;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            final IntItem that = (IntItem) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}
