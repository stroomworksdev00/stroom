/*
 * Copyright 2016 Crown Copyright
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

package stroom.node.impl;

import stroom.util.shared.BaseResultList;
import stroom.util.shared.ResultList;
import stroom.node.shared.DBTableService;
import stroom.node.shared.DBTableStatus;
import stroom.node.shared.FindSystemTableStatusAction;
import stroom.security.Security;
import stroom.task.api.AbstractTaskHandler;

import javax.inject.Inject;


class FindSystemTableStatusHandler
        extends AbstractTaskHandler<FindSystemTableStatusAction, ResultList<DBTableStatus>> {
    private final DBTableService dbTableService;
    private final Security security;

    @Inject
    FindSystemTableStatusHandler(final DBTableService dbTableService,
                                 final Security security) {
        this.dbTableService = dbTableService;
        this.security = security;
    }

    @Override
    public BaseResultList<DBTableStatus> exec(final FindSystemTableStatusAction action) {
        return security.secureResult(() -> BaseResultList.createUnboundedList(dbTableService.findSystemTableStatus(action.getCriteria())));
    }
}
