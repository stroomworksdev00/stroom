package stroom.processor.shared;

import stroom.datasource.api.v2.QueryField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessorFilterFields {

    public static final String FIELD_ID = "Id";

    private static final List<QueryField> FIELDS = new ArrayList<>();
    private static final Map<String, QueryField> FIELD_MAP;

    public static final QueryField ID = QueryField.createId("Processor Filter Id");
//    public static final QueryField CREATE_USER = QueryField.createText("Processor Filter Create User");
    public static final QueryField OWNER_UUID = QueryField.createText("Processor Filter Owner User UUID");
    public static final QueryField LAST_POLL_MS = QueryField.createLong("Processor Filter Last Poll Ms");
    public static final QueryField PRIORITY = QueryField.createInteger("Processor Filter Priority");
    public static final QueryField ENABLED = QueryField.createBoolean("Processor Filter Enabled");
    public static final QueryField DELETED = QueryField.createBoolean("Processor Filter Deleted");
    public static final QueryField PROCESSOR_ID = QueryField.createId("Processor Id");
    public static final QueryField UUID = QueryField.createText("Processor Filter UUID");

    static {
        FIELDS.add(ID);
        FIELDS.add(OWNER_UUID);
        FIELDS.add(LAST_POLL_MS);
        FIELDS.add(PRIORITY);
        FIELDS.add(ENABLED);
        FIELDS.add(DELETED);
        FIELDS.add(PROCESSOR_ID);
        FIELDS.add(UUID);
        FIELD_MAP = FIELDS.stream().collect(Collectors.toMap(QueryField::getFldName, Function.identity()));
    }

    public static List<QueryField> getFields() {
        return new ArrayList<>(FIELDS);
    }

    public static Map<String, QueryField> getFieldMap() {
        return FIELD_MAP;
    }
}
