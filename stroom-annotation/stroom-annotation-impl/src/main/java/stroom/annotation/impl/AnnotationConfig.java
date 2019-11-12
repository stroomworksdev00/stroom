package stroom.annotation.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import stroom.util.shared.IsConfig;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class AnnotationConfig implements IsConfig {
    private List<String> statusValues = new ArrayList<>();
    private String createText = "Create Annotation";

    public AnnotationConfig() {
        statusValues.add("New");
        statusValues.add("Assigned");
        statusValues.add("Closed");
    }

    @JsonProperty("statusValues")
    @JsonPropertyDescription("The different status values that can be set on an annotation")
    public List<String> getStatusValues() {
        return statusValues;
    }

    public void setStatusValues(final List<String> statusValues) {
        this.statusValues = statusValues;
    }

    @JsonProperty("createText")
    @JsonPropertyDescription("The text to display to create an annotation")
    public String getCreateText() {
        return createText;
    }

    public void setCreateText(final String createText) {
        this.createText = createText;
    }
}
