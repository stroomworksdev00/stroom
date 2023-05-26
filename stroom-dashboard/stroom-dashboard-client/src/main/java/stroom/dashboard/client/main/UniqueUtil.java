package stroom.dashboard.client.main;

import stroom.dashboard.client.main.ComponentRegistry.ComponentType;
import stroom.util.shared.RandomId;

import java.util.Set;

public class UniqueUtil {

    public static String makeUniqueName(final String fieldName,
                                        final Set<String> currentNames) {
        String name = fieldName;
        String suffix = "";
        int count = 1;

        // See if we can get a numeric part off the end of the field name.
        int index = fieldName.lastIndexOf(" ");
        if (index != -1) {
            final String part1 = fieldName.substring(0, index);
            final String part2 = fieldName.substring(index + 1);
            try {
                count = Integer.parseInt(part2);
                name = part1;
            } catch (final RuntimeException e) {
                // Ignore.
            }
        }

        while (currentNames.contains(name + suffix)) {
            count++;
            suffix = " " + count;
        }
        return name + suffix;
    }

    public static String createUniqueComponentId(final ComponentType type,
                                                 final Set<String> existingIds) {
        String id = type.getId() + "-" + RandomId.createId(5);
        // Make sure we don't duplicate ids.
        while (existingIds.contains(id)) {
            id = type.getId() + "-" + RandomId.createId(5);
        }
        return id;
    }

    public static String createUniqueFieldId(final String componentId, final Set<String> usedFieldIds) {
        String id = componentId + "|" + RandomId.createId(5);
        // Make sure we don't duplicate ids.
        while (usedFieldIds.contains(id)) {
            id = componentId + "|" + RandomId.createId(5);
        }
        usedFieldIds.add(id);
        return id;
    }
}
