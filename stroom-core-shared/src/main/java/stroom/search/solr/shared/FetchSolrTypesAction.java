/*
 * Copyright 2018 Crown Copyright
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

package stroom.search.solr.shared;

import stroom.entity.shared.Action;
import stroom.util.shared.SharedList;
import stroom.util.shared.SharedString;

public class FetchSolrTypesAction extends Action<SharedList<SharedString>> {
    private static final long serialVersionUID = -3560107233301674555L;

    private SolrIndex solrIndex;

    public FetchSolrTypesAction() {
    }

    public FetchSolrTypesAction(final SolrIndex solrIndex) {
        this.solrIndex = solrIndex;
    }

    public SolrIndex getSolrIndex() {
        return solrIndex;
    }

    @Override
    public String getTaskName() {
        return "Fetch Solr Types";
    }
}
