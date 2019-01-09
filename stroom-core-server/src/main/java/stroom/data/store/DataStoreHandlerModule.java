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

package stroom.data.store;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import stroom.task.api.TaskHandler;
import stroom.util.lifecycle.jobmanagement.ScheduledJobs;

public class DataStoreHandlerModule extends AbstractModule {
    @Override
    protected void configure() {
        final Multibinder<TaskHandler> taskHandlerBinder = Multibinder.newSetBinder(binder(), TaskHandler.class);
        taskHandlerBinder.addBinding().to(UpdateStatusHandler.class);
        taskHandlerBinder.addBinding().to(DownloadDataHandler.class);
        taskHandlerBinder.addBinding().to(FetchFieldsHandler.class);
        taskHandlerBinder.addBinding().to(StreamDownloadTaskHandler.class);
        taskHandlerBinder.addBinding().to(StreamUploadTaskHandler.class);
        taskHandlerBinder.addBinding().to(UploadDataHandler.class);

        final Multibinder<ScheduledJobs> jobs = Multibinder.newSetBinder(binder(), ScheduledJobs.class);
        jobs.addBinding().to(DataStoreJobs.class);
    }
}