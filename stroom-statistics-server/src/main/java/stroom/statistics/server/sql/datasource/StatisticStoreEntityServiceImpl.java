/*
 * Copyright 2017 Crown Copyright
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
 */

package stroom.statistics.server.sql.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import stroom.entity.server.AutoMarshal;
import stroom.entity.server.DocumentEntityServiceImpl;
import stroom.entity.server.QueryAppender;
import stroom.entity.server.util.HqlBuilder;
import stroom.entity.server.util.StroomEntityManager;
import stroom.importexport.server.ImportExportHelper;
import stroom.security.SecurityContext;
import stroom.statistics.shared.StatisticStoreEntity;

import javax.inject.Inject;

@Transactional
@AutoMarshal
class StatisticStoreEntityServiceImpl
        extends DocumentEntityServiceImpl<StatisticStoreEntity, FindStatisticsEntityCriteria>
        implements StatisticStoreEntityService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatisticStoreEntityServiceImpl.class);

    @Inject
    StatisticStoreEntityServiceImpl(final StroomEntityManager entityManager,
                                    final ImportExportHelper importExportHelper,
                                    final SecurityContext securityContext) {
        super(entityManager, importExportHelper, securityContext);
        LOGGER.debug("StatisticsDataSourceServiceImpl initialised");
    }

    @Override
    public Class<StatisticStoreEntity> getEntityClass() {
        return StatisticStoreEntity.class;
    }

    @Override
    public FindStatisticsEntityCriteria createCriteria() {
        return FindStatisticsEntityCriteria.instance();
    }

    @Override
    protected QueryAppender<StatisticStoreEntity, FindStatisticsEntityCriteria> createQueryAppender(final StroomEntityManager entityManager) {
        return new StatisticStoreEntityQueryAppender(entityManager);
    }

    private static class StatisticStoreEntityQueryAppender extends QueryAppender<StatisticStoreEntity, FindStatisticsEntityCriteria> {
        private final StatisticsDataSourceMarshaller marshaller;

        StatisticStoreEntityQueryAppender(final StroomEntityManager entityManager) {
            super(entityManager);
            marshaller = new StatisticsDataSourceMarshaller();
        }

        @Override
        protected void appendBasicCriteria(final HqlBuilder sql, final String alias, final FindStatisticsEntityCriteria criteria) {
            super.appendBasicCriteria(sql, alias, criteria);

            if (criteria.getStatisticType() != null) {
                sql.appendValueQuery(alias + ".pStatisticType", criteria.getStatisticType().getPrimitiveValue());
            }
        }

        @Override
        protected void preSave(final StatisticStoreEntity entity) {
            super.preSave(entity);
            marshaller.marshal(entity);
        }

        @Override
        protected void postLoad(final StatisticStoreEntity entity) {
            marshaller.unmarshal(entity);
            super.postLoad(entity);
        }
    }
}
