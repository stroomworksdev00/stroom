package stroom.security.shared;

import stroom.util.shared.BaseCriteria;
import stroom.util.shared.UserName;
import stroom.util.shared.filter.FilterFieldDefinition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Arrays;
import java.util.List;

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(Include.NON_NULL)
public class FindApiKeyCriteria extends BaseCriteria {

    public static final String FIELD_NAME = "Name";
    public static final String FIELD_OWNER_DISPLAY_NAME = "Owner";
    public static final String FIELD_COMMENTS = "Comments";
    public static final String FIELD_ENABLED = "Enabled";
    public static final String FIELD_EXPIRE_TIME = "Expire Time";

    public static final FilterFieldDefinition FIELD_DEF_NAME = FilterFieldDefinition.defaultField(FIELD_NAME);
    public static final FilterFieldDefinition FIELD_DEF_OWNER_DISPLAY_NAME = FilterFieldDefinition.qualifiedField(
            FIELD_OWNER_DISPLAY_NAME);
    public static final FilterFieldDefinition FIELD_DEF_COMMENTS = FilterFieldDefinition.qualifiedField(
            FIELD_COMMENTS);
    public static final FilterFieldDefinition FIELD_DEF_ENABLED = FilterFieldDefinition.qualifiedField(
            FIELD_ENABLED);

    public static final List<FilterFieldDefinition> FILTER_FIELD_DEFINITIONS = Arrays.asList(
            FIELD_DEF_NAME,
            FIELD_DEF_OWNER_DISPLAY_NAME,
            FIELD_DEF_COMMENTS,
            FIELD_DEF_ENABLED);

    @JsonProperty
    private String quickFilterInput;
    @JsonProperty
    private UserName owner;

    public FindApiKeyCriteria() {
    }

    @JsonCreator
    public FindApiKeyCriteria(@JsonProperty("quickFilter") final String quickFilterInput,
                              @JsonProperty("owner") final UserName owner) {
        this.quickFilterInput = quickFilterInput;
        this.owner = owner;
    }

    public String getQuickFilterInput() {
        return quickFilterInput;
    }

    public void setQuickFilterInput(final String quickFilterInput) {
        this.quickFilterInput = quickFilterInput;
    }

    public UserName getOwner() {
        return owner;
    }

    public void setOwner(final UserName owner) {
        this.owner = owner;
    }
}
