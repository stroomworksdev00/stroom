/*
 * Copyright 2022 Crown Copyright
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

package stroom.alert.rule.client.presenter;

import stroom.alert.rule.client.presenter.AlertRuleSettingsPresenter.AlertRuleSettingsView;
import stroom.alert.rule.shared.AbstractAlertRule;
import stroom.alert.rule.shared.AlertRuleDoc;
import stroom.alert.rule.shared.AlertRuleType;
import stroom.alert.rule.shared.QueryLanguageVersion;
import stroom.alert.rule.shared.ThresholdAlertRule;
import stroom.docref.DocRef;
import stroom.document.client.event.DirtyUiHandlers;
import stroom.editor.client.presenter.EditorPresenter;
import stroom.entity.client.presenter.DocumentSettingsPresenter;

import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.View;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;

import javax.inject.Provider;

public class AlertRuleSettingsPresenter
        extends DocumentSettingsPresenter<AlertRuleSettingsView, AlertRuleDoc>
        implements DirtyUiHandlers {

    private final EditorPresenter codePresenter;
    private boolean readOnly = true;

    @Inject
    public AlertRuleSettingsPresenter(final EventBus eventBus,
                                      final AlertRuleSettingsView view,
                                      final Provider<EditorPresenter> editorPresenterProvider) {
        super(eventBus, view);

        codePresenter = editorPresenterProvider.get();
        codePresenter.setMode(AceEditorMode.STROOM_QUERY);
        registerHandler(codePresenter.addValueChangeHandler(event -> setDirty(true)));
        registerHandler(codePresenter.addFormatHandler(event -> setDirty(true)));
//            codePresenter.setReadOnly(readOnly);
        codePresenter.getFormatAction().setAvailable(!readOnly);
        if (getEntity() != null && getEntity().getQuery() != null) {
            codePresenter.setText(getEntity().getQuery());
        }

        view.setUiHandlers(this);
        view.setQueryWidget(codePresenter.getWidget());
    }

    @Override
    protected void onRead(final DocRef docRef, final AlertRuleDoc alertRule) {
        getView().setDescription(alertRule.getDescription());
        getView().setLanguageVersion(alertRule.getLanguageVersion());
        if (alertRule.getQuery() != null) {
            codePresenter.setText(alertRule.getQuery());
        }
        getView().setEnabled(alertRule.isEnabled());
        getView().setAlertRuleType(alertRule.getAlertRuleType());

        final AbstractAlertRule abstractAlertRule = alertRule.getAlertRule();
        if (abstractAlertRule instanceof ThresholdAlertRule) {
            final ThresholdAlertRule thresholdAlertRule = (ThresholdAlertRule) abstractAlertRule;
            getView().setExecutionDelay(thresholdAlertRule.getExecutionDelay());
            getView().setExecutionFrequency(thresholdAlertRule.getExecutionFrequency());
            getView().setThresholdField(thresholdAlertRule.getThresholdField());
            getView().setThreshold(thresholdAlertRule.getThreshold());
        }
    }

    @Override
    protected AlertRuleDoc onWrite(final AlertRuleDoc alertRule) {
        AbstractAlertRule rule = null;
        if (AlertRuleType.THRESHOLD.equals(getView().getAlertRuleType())) {
            rule = ThresholdAlertRule.builder()
                    .executionDelay(getView().getExecutionDelay())
                    .executionFrequency(getView().getExecutionFrequency())
                    .thresholdField(getView().getThresholdField())
                    .threshold(getView().getThreshold())
                    .build();
        }

        return alertRule.copy()
                .description(getView().getDescription())
                .languageVersion(getView().getLanguageVersion())
                .query(codePresenter.getText())
                .enabled(getView().isEnabled())
                .alertRuleType(getView().getAlertRuleType())
                .alertRule(rule)
                .build();
    }

    @Override
    public void onDirty() {
        setDirty(true);
    }

    @Override
    public String getType() {
        return AlertRuleDoc.DOCUMENT_TYPE;
    }

    public interface AlertRuleSettingsView extends View, HasUiHandlers<DirtyUiHandlers> {

        String getDescription();

        void setDescription(final String description);

        QueryLanguageVersion getLanguageVersion();

        void setLanguageVersion(final QueryLanguageVersion languageVersion);

        void setQueryWidget(Widget widget);

        boolean isEnabled();

        void setEnabled(final boolean enabled);

        AlertRuleType getAlertRuleType();

        void setAlertRuleType(AlertRuleType alertRuleType);

        String getExecutionDelay();

        void setExecutionDelay(String executionDelay);

        String getExecutionFrequency();

        void setExecutionFrequency(String executionFrequency);

        String getThresholdField();

        void setThresholdField(String thresholdField);

        long getThreshold();

        void setThreshold(long threshold);
    }
}