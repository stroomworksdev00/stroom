package stroom.statistics.stroomstats.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.docstore.api.DocumentSerialiser2;
import stroom.docstore.api.Serialiser2;
import stroom.docstore.api.Serialiser2Factory;
import stroom.util.xml.XMLMarshallerUtil;
import stroom.stats.shared.StroomStatsStoreDoc;
import stroom.stats.shared.StroomStatsStoreEntityData;

import javax.inject.Inject;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Map;

public class StroomStatsStoreSerialiser implements DocumentSerialiser2<StroomStatsStoreDoc> {
    private static final Logger LOGGER = LoggerFactory.getLogger(StroomStatsStoreSerialiser.class);

    private final Serialiser2<StroomStatsStoreDoc> delegate;

    @Inject
    public StroomStatsStoreSerialiser(final Serialiser2Factory serialiser2Factory) {
        delegate = serialiser2Factory.createSerialiser(StroomStatsStoreDoc.class);
    }

    @Override
    public StroomStatsStoreDoc read(final Map<String, byte[]> data) throws IOException {
        final StroomStatsStoreDoc document = delegate.read(data);
        return document;
    }

    @Override
    public Map<String, byte[]> write(final StroomStatsStoreDoc document) throws IOException {
        final Map<String, byte[]> data = delegate.write(document);
        return data;
    }

    public StroomStatsStoreEntityData getDataFromLegacyXML(final String xml) {
        if (xml != null) {
            try {
                final JAXBContext jaxbContext = JAXBContext.newInstance(StroomStatsStoreEntityData.class);
                return XMLMarshallerUtil.unmarshal(jaxbContext, StroomStatsStoreEntityData.class, xml);
            } catch (final JAXBException | RuntimeException e) {
                LOGGER.error("Unable to unmarshal dashboard config", e);
            }
        }

        return null;
    }
}