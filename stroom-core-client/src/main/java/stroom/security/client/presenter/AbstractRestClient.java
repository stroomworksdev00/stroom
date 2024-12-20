package stroom.security.client.presenter;

import stroom.dispatch.client.RestFactory;
import stroom.task.client.TaskMonitorFactory;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.EventBus;

public abstract class AbstractRestClient implements HasHandlers {

    protected final EventBus eventBus;
    protected final RestFactory restFactory;

    public AbstractRestClient(final EventBus eventBus,
                              final RestFactory restFactory) {
        this.eventBus = eventBus;
        this.restFactory = restFactory;
    }

    @Override
    public void fireEvent(final GwtEvent<?> gwtEvent) {
        eventBus.fireEvent(gwtEvent);
    }
}
