package stroom.dispatch.client;

import stroom.task.client.TaskHandler;
import stroom.task.client.TaskListener;

public class QuietTaskListener implements TaskListener {

    @Override
    public TaskHandler createTaskHandler(final String message) {
        return new TaskHandler() {
            @Override
            public void onStart() {

            }

            @Override
            public void onEnd() {

            }
        };
    }
}
