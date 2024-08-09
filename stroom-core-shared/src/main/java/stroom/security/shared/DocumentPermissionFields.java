package stroom.security.shared;

import stroom.datasource.api.v2.ConditionSet;
import stroom.datasource.api.v2.FieldType;
import stroom.datasource.api.v2.QueryField;
import stroom.docref.DocRef;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DocumentPermissionFields {

    public static final String DOCUMENT_STORE_TYPE = "DocumentStore";
    public static final DocRef DOCUMENT_STORE_DOC_REF = DocRef.builder()
            .type(DOCUMENT_STORE_TYPE)
            .uuid(DOCUMENT_STORE_TYPE)
            .name(DOCUMENT_STORE_TYPE)
            .build();

    private static final List<QueryField> FIELDS = new ArrayList<>();
    private static final Map<String, QueryField> ALL_FIELD_MAP;

    public static final QueryField DOCUMENT = QueryField
            .builder()
            .fldName("Document")
            .fldType(FieldType.DOC_REF)
            .conditionSet(ConditionSet.DOC_DOC_IS)
            .queryable(true)
            .build();
    public static final QueryField CHILDREN = QueryField
            .builder()
            .fldName("Children")
            .fldType(FieldType.DOC_REF)
            .conditionSet(ConditionSet.DOC_DOC_OF)
            .queryable(true)
            .build();
    public static final QueryField DESCENDANTS = QueryField
            .builder()
            .fldName("Descendants")
            .fldType(FieldType.DOC_REF)
            .conditionSet(ConditionSet.DOC_DOC_OF)
            .queryable(true)
            .build();
    public static final QueryField USER = QueryField
            .builder()
            .fldName("User")
            .fldType(FieldType.USER_REF)
            .conditionSet(ConditionSet.DOC_USER_IS)
            .queryable(true)
            .build();
    public static final QueryField DOCUMENT_TYPE = QueryField
            .builder()
            .fldName("Document Type")
            .fldType(FieldType.TEXT)
            .conditionSet(ConditionSet.DEFAULT_TEXT)
            .queryable(true)
            .build();
    public static final QueryField DOCUMENT_NAME = QueryField
            .builder()
            .fldName("Document Name")
            .fldType(FieldType.TEXT)
            .conditionSet(ConditionSet.DEFAULT_TEXT)
            .queryable(true)
            .build();
    public static final QueryField DOCUMENT_UUID = QueryField
            .builder()
            .fldName("Document UUID")
            .fldType(FieldType.TEXT)
            .conditionSet(ConditionSet.DEFAULT_TEXT)
            .queryable(true)
            .build();
    public static final QueryField DOCUMENT_TAG = QueryField
            .builder()
            .fldName("Document Tag")
            .fldType(FieldType.TEXT)
            .conditionSet(ConditionSet.DEFAULT_TEXT)
            .queryable(true)
            .build();

    static {
        FIELDS.add(DOCUMENT);
        FIELDS.add(CHILDREN);
        FIELDS.add(DESCENDANTS);
        FIELDS.add(DOCUMENT_TYPE);
        FIELDS.add(DOCUMENT_NAME);
        FIELDS.add(DOCUMENT_UUID);
        FIELDS.add(DOCUMENT_TAG);
        FIELDS.add(USER);

        ALL_FIELD_MAP = FIELDS.stream().collect(Collectors.toMap(QueryField::getFldName, Function.identity()));
    }

    public static List<QueryField> getFields() {
        return new ArrayList<>(FIELDS);
    }

    public static Map<String, QueryField> getAllFieldMap() {
        return ALL_FIELD_MAP;
    }
}
