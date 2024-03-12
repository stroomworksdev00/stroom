package stroom.pipeline.shared;

import stroom.datasource.api.v2.ConditionSet;
import stroom.datasource.api.v2.FieldType;
import stroom.datasource.api.v2.QueryField;
import stroom.docref.DocRef;

import java.util.Arrays;
import java.util.List;

public class ReferenceDataFields {

    public static final DocRef REF_STORE_PSEUDO_DOC_REF = new DocRef(
            "Searchable",
            "Reference Data Store",
            "Reference Data Store (This Node Only)");
    public static final QueryField FEED_NAME_FIELD = QueryField
            .builder()
            .name("Feed Name")
            .fieldType(FieldType.TEXT)
            .conditionSet(ConditionSet.REF_DATA_TEXT)
            .queryable(true)
            .build();
    public static final QueryField KEY_FIELD = QueryField
            .builder()
            .name("Key")
            .fieldType(FieldType.TEXT)
            .conditionSet(ConditionSet.REF_DATA_TEXT)
            .queryable(true)
            .build();
    public static final QueryField VALUE_FIELD = QueryField
            .builder()
            .name("Value")
            .fieldType(FieldType.TEXT)
            .conditionSet(ConditionSet.REF_DATA_TEXT)
            .queryable(true)
            .build();
    public static final QueryField VALUE_REF_COUNT_FIELD = QueryField.createInteger(
            "Value Reference Count", false);
    public static final QueryField MAP_NAME_FIELD = QueryField
            .builder()
            .name("Map Name")
            .fieldType(FieldType.TEXT)
            .conditionSet(ConditionSet.REF_DATA_TEXT)
            .queryable(true)
            .build();
    public static final QueryField CREATE_TIME_FIELD = QueryField
            .createDate("Create Time", true);
    public static final QueryField EFFECTIVE_TIME_FIELD = QueryField
            .createDate("Effective Time", true);
    public static final QueryField LAST_ACCESSED_TIME_FIELD = QueryField
            .createDate("Last Accessed Time", true);
    public static final QueryField PIPELINE_FIELD = QueryField
            .builder()
            .name("Reference Loader Pipeline")
            .fieldType(FieldType.DOC_REF)
            .conditionSet(ConditionSet.REF_DATA_DOC_REF)
            .docRefType(PipelineDoc.DOCUMENT_TYPE)
            .queryable(true)
            .build();
    public static final QueryField PROCESSING_STATE_FIELD = QueryField
            .createText("Processing State", false);
    public static final QueryField STREAM_ID_FIELD = QueryField.createId(
            "Stream ID", false);
    public static final QueryField PART_NO_FIELD = QueryField.createLong(
            "Part Number", false);
    public static final QueryField PIPELINE_VERSION_FIELD = QueryField.createText(
            "Pipeline Version", false);

    public static final List<QueryField> FIELDS = Arrays.asList(
            FEED_NAME_FIELD,
            KEY_FIELD,
            VALUE_FIELD,
            VALUE_REF_COUNT_FIELD,
            MAP_NAME_FIELD,
            CREATE_TIME_FIELD,
            EFFECTIVE_TIME_FIELD,
            LAST_ACCESSED_TIME_FIELD,
            PIPELINE_FIELD,
            PROCESSING_STATE_FIELD,
            STREAM_ID_FIELD,
            PART_NO_FIELD,
            PIPELINE_VERSION_FIELD);

    public static List<QueryField> getFields() {
        return FIELDS;
    }
}
