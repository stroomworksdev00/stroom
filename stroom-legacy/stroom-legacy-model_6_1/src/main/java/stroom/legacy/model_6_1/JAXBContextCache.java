package stroom.legacy.model_6_1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Deprecated
public final class JAXBContextCache {
    private static final Map<Class<?>, JAXBContext> map = new ConcurrentHashMap<>();

    public static JAXBContext get(final Class<?> clazz) {
        return map.computeIfAbsent(clazz, k -> {
            try {
                return JAXBContext.newInstance(clazz);
            } catch (final JAXBException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }
}
