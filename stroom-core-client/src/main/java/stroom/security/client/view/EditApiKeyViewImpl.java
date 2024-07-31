package stroom.security.client.view;

import stroom.preferences.client.UserPreferencesManager;
import stroom.security.client.presenter.EditApiKeyPresenter.EditApiKeyView;
import stroom.security.client.presenter.EditApiKeyPresenter.Mode;
import stroom.svg.client.SvgPresets;
import stroom.util.client.ClipboardUtil;
import stroom.widget.button.client.ButtonPanel;
import stroom.widget.button.client.ButtonView;
import stroom.widget.customdatebox.client.MyDateBox;
import stroom.widget.form.client.FormGroup;
import stroom.widget.popup.client.view.HideRequestUiHandlers;
import stroom.widget.tickbox.client.view.CustomCheckBox;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class EditApiKeyViewImpl
        extends ViewWithUiHandlers<HideRequestUiHandlers>
        implements EditApiKeyView {

    private final Widget widget;
    private Mode mode;
    private boolean canSelectOwner;

    @UiField
    SimplePanel owner;
    @UiField
    TextBox nameTextBox;
    @UiField
    TextArea commentsTextArea;
    @UiField
    MyDateBox expiresOnDateBox;
    @UiField
    CustomCheckBox enabledCheckBox;

    @UiField
    FormGroup prefixFormGroup;
    @UiField
    TextBox prefixTextBox;

    @UiField
    FormGroup apiKeyFormGroup;
    @UiField
    TextArea apiKeyTextArea;

    @UiField
    ButtonPanel apiKeyButtonPanel;
    @UiField
    Label copyToClipboardLabel;

    @Inject
    public EditApiKeyViewImpl(final Binder binder,
                              final UserPreferencesManager userPreferencesManager) {
        widget = binder.createAndBindUi(this);
        widget.addAttachHandler(event -> focus());

        apiKeyTextArea.setEnabled(false);
        prefixTextBox.setEnabled(false);
        expiresOnDateBox.setUtc(userPreferencesManager.isUtc());
        final ButtonView copyApiKeyToClipboardBtn = apiKeyButtonPanel.addButton(SvgPresets.COPY.title(
                "Copy API Key to clipboard"));
        copyApiKeyToClipboardBtn.setEnabled(true);
        // When the copy btn is clicked show a label to give feedback to user, but then remove it after a time
        copyApiKeyToClipboardBtn.addClickHandler(event -> {
            ClipboardUtil.copy(apiKeyTextArea.getText());
            copyToClipboardLabel.setText("API Key copied to clipboard");
            final Timer timer = new Timer() {
                @Override
                public void run() {
                    copyToClipboardLabel.setText("");
                }
            };
            timer.schedule(3_000);
        });
    }

    @Override
    public void setCanSelectOwner(final boolean canSelectOwner) {
        this.canSelectOwner = canSelectOwner;
        setOwnerControlsVisibility();
    }

    private void setOwnerControlsVisibility() {
        if (canSelectOwner && Mode.PRE_CREATE.equals(mode)) {
            owner.setVisible(true);
        } else {
            owner.setVisible(false);
        }
    }

    @Override
    public void setMode(final Mode mode) {
        this.mode = mode;
        final boolean isPrefixVisible;
        final boolean isApiKeyVisible;
        if (Mode.PRE_CREATE.equals(mode)) {
            isPrefixVisible = false;
            isApiKeyVisible = false;
        } else if (Mode.POST_CREATE.equals(mode)) {
            isPrefixVisible = true;
            isApiKeyVisible = true;
        } else {
            isPrefixVisible = true;
            isApiKeyVisible = false;
        }
        prefixTextBox.setVisible(isPrefixVisible);
        prefixFormGroup.setVisible(isPrefixVisible);
        apiKeyTextArea.setVisible(isApiKeyVisible);
        apiKeyFormGroup.setVisible(isApiKeyVisible);
        apiKeyButtonPanel.setVisible(isApiKeyVisible);
        setEnabledStates(mode);
        setOwnerControlsVisibility();
    }

    private void setEnabledStates(final Mode mode) {
        if (Mode.PRE_CREATE.equals(mode)) {
            expiresOnDateBox.setEnabled(true);
            nameTextBox.setEnabled(true);
            commentsTextArea.setEnabled(true);
            enabledCheckBox.setEnabled(true);
        } else if (Mode.POST_CREATE.equals(mode)) {
            // POST_CREATE is just to view what has been created, so user can't change anything
            expiresOnDateBox.setEnabled(false);
            nameTextBox.setEnabled(false);
            commentsTextArea.setEnabled(false);
            enabledCheckBox.setEnabled(false);
        } else if (Mode.EDIT.equals(mode)) {
            expiresOnDateBox.setEnabled(false);
            nameTextBox.setEnabled(true);
            commentsTextArea.setEnabled(true);
            enabledCheckBox.setEnabled(true);
        }
    }

    @Override
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public void setOwnerView(final View view) {

    }

    @Override
    public void setName(final String name) {
        nameTextBox.setText(name);
    }

    @Override
    public void setPrefix(final String prefix) {
        prefixTextBox.setText(prefix);
    }

    @Override
    public String getName() {
        return nameTextBox.getText();
    }

    @Override
    public void setApiKey(final String apiKey) {
        apiKeyTextArea.setValue(apiKey);
    }

    @Override
    public void setComments(final String comments) {
        commentsTextArea.setText(comments);
    }

    @Override
    public String getComments() {
        return commentsTextArea.getText();
    }

    @Override
    public void setExpiresOn(final Long expiresOn) {
        expiresOnDateBox.setMilliseconds(expiresOn);
    }

    @Override
    public Long getExpiresOnMs() {
        return expiresOnDateBox.getMilliseconds();
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        enabledCheckBox.setValue(isEnabled);
    }

    @Override
    public boolean isEnabled() {
        return enabledCheckBox.getValue();
    }

    @Override
    public void focus() {
        Scheduler.get().scheduleDeferred(() -> {
            nameTextBox.setFocus(true);
        });
    }

    @Override
    public void reset(final Long milliseconds) {
        nameTextBox.setText("");
        commentsTextArea.setText("");
        owner = null;
        expiresOnDateBox.setMilliseconds(milliseconds);
        enabledCheckBox.setValue(true);
        prefixTextBox.setText("");
        apiKeyTextArea.setText("");
    }

    @Override
    public Widget asWidget() {
        return widget;
    }


    // --------------------------------------------------------------------------------


    public interface Binder extends UiBinder<Widget, EditApiKeyViewImpl> {

    }
}
