package stroom.node.client;

import stroom.dispatch.client.RestFactory;
import stroom.node.shared.ClusterNodeInfo;
import stroom.node.shared.FetchNodeStatusResponse;
import stroom.node.shared.FindNodeStatusCriteria;
import stroom.node.shared.NodeResource;

import com.google.gwt.core.client.GWT;

import java.util.List;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NodeManager {

    private static final NodeResource NODE_RESOURCE = GWT.create(NodeResource.class);

    private final RestFactory restFactory;

    @Inject
    NodeManager(final RestFactory restFactory) {
        this.restFactory = restFactory;
    }

    public void fetchNodeStatus(final Consumer<FetchNodeStatusResponse> dataConsumer,
                                final Consumer<Throwable> throwableConsumer,
                                final FindNodeStatusCriteria findNodeStatusCriteria) {
        restFactory
                .resource(NODE_RESOURCE)
                .method(res -> res.find(findNodeStatusCriteria))
                .onSuccess(dataConsumer)
                .onFailure(throwableConsumer)
                .exec();
    }

    public void ping(final String nodeName,
                     final Consumer<Long> pingConsumer,
                     final Consumer<Throwable> throwableConsumer) {
        restFactory
                .resource(NODE_RESOURCE)
                .method(res -> res.ping(nodeName))
                .onSuccess(pingConsumer)
                .onFailure(throwableConsumer)
                .exec();
    }

    public void info(final String nodeName,
                     final Consumer<ClusterNodeInfo> infoConsumer,
                     final Consumer<Throwable> throwableConsumer) {
        restFactory
                .resource(NODE_RESOURCE)
                .method(res -> res.info(nodeName))
                .onSuccess(infoConsumer)
                .onFailure(throwableConsumer)
                .exec();
    }

    public void setPriority(final String nodeName,
                            final int priority,
                            final Consumer<Boolean> resultConsumer) {
        restFactory
                .resource(NODE_RESOURCE)
                .method(res -> res.setPriority(nodeName, priority))
                .onSuccess(resultConsumer)
                .exec();
    }

    public void setEnabled(final String nodeName,
                           final boolean enabled,
                           final Consumer<Boolean> resultConsumer) {
        restFactory
                .resource(NODE_RESOURCE)
                .method(res -> res.setEnabled(nodeName, enabled))
                .onSuccess(resultConsumer)
                .exec();
    }

    public void listAllNodes(final Consumer<List<String>> nodeListConsumer,
                             final Consumer<Throwable> throwableConsumer) {
        restFactory
                .resource(NODE_RESOURCE)
                .method(NodeResource::listAllNodes)
                .onSuccess(nodeListConsumer)
                .onFailure(throwableConsumer)
                .exec();
    }

    public void listEnabledNodes(final Consumer<List<String>> nodeListConsumer,
                                 final Consumer<Throwable> throwableConsumer) {
        restFactory
                .resource(NODE_RESOURCE)
                .method(NodeResource::listEnabledNodes)
                .onSuccess(nodeListConsumer)
                .onFailure(throwableConsumer)
                .exec();
    }
}
