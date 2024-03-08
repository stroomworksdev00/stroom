package stroom.task.impl;

import stroom.cluster.task.api.NodeNotFoundException;
import stroom.cluster.task.api.NullClusterStateException;
import stroom.cluster.task.api.TargetNodeSetFactory;
import stroom.datasource.api.v2.DateField;
import stroom.datasource.api.v2.FieldInfo;
import stroom.datasource.api.v2.FindFieldInfoCriteria;
import stroom.docref.DocRef;
import stroom.entity.shared.ExpressionCriteria;
import stroom.expression.matcher.ExpressionMatcher;
import stroom.expression.matcher.ExpressionMatcherFactory;
import stroom.query.common.v2.FieldInfoResultPageBuilder;
import stroom.query.language.functions.FieldIndex;
import stroom.query.language.functions.Val;
import stroom.query.language.functions.ValInteger;
import stroom.query.language.functions.ValLong;
import stroom.query.language.functions.ValNull;
import stroom.query.language.functions.ValString;
import stroom.query.language.functions.ValuesConsumer;
import stroom.searchable.api.Searchable;
import stroom.security.api.SecurityContext;
import stroom.security.shared.PermissionNames;
import stroom.task.api.TaskContext;
import stroom.task.api.TaskContextFactory;
import stroom.task.shared.TaskProgressResponse;
import stroom.task.shared.TaskResource;
import stroom.util.shared.ResultPage;

import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

class SearchableTaskProgress implements Searchable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchableTaskProgress.class);
    private static final DocRef TASK_MANAGER_PSEUDO_DOC_REF = new DocRef("Searchable", "Task Manager", "Task Manager");

    private final Executor executor;
    private final TaskContextFactory taskContextFactory;
    private final TargetNodeSetFactory targetNodeSetFactory;
    private final TaskResource taskResource;
    private final SecurityContext securityContext;
    private final ExpressionMatcherFactory expressionMatcherFactory;

    @Inject
    SearchableTaskProgress(final Executor executor,
                           final TaskContextFactory taskContextFactory,
                           final TargetNodeSetFactory targetNodeSetFactory,
                           final TaskResource taskResource,
                           final SecurityContext securityContext,
                           final ExpressionMatcherFactory expressionMatcherFactory) {
        this.executor = executor;
        this.taskContextFactory = taskContextFactory;
        this.targetNodeSetFactory = targetNodeSetFactory;
        this.taskResource = taskResource;
        this.securityContext = securityContext;
        this.expressionMatcherFactory = expressionMatcherFactory;
    }

    @Override
    public DocRef getDocRef() {
        if (securityContext.hasAppPermission(PermissionNames.MANAGE_TASKS_PERMISSION)) {
            return TASK_MANAGER_PSEUDO_DOC_REF;
        }
        return null;
    }

    @Override
    public ResultPage<FieldInfo> getFieldInfo(final FindFieldInfoCriteria criteria) {
        return FieldInfoResultPageBuilder.builder(criteria).addAll(TaskManagerFields.getFields()).build();
    }

    @Override
    public Optional<String> fetchDocumentation(final DocRef docRef) {
        return Optional.empty();
    }

    @Override
    public DateField getTimeField() {
        return TaskManagerFields.SUBMIT_TIME;
    }

    @Override
    public void search(final ExpressionCriteria criteria,
                       final FieldIndex fieldIndex,
                       final ValuesConsumer consumer) {
        securityContext.secure(PermissionNames.MANAGE_TASKS_PERMISSION, () -> {
            final Map<String, TaskProgressResponse> nodeResponses = searchAllNodes();

            final ExpressionMatcher expressionMatcher = expressionMatcherFactory.create(
                    TaskManagerFields.getFieldMap());

            nodeResponses.values()
                    .stream()
                    .map(ResultPage::getValues)
                    .flatMap(List::stream)
                    .map(taskProgress -> {
                        final Map<String, Object> attributeMap = new HashMap<>();
                        attributeMap.put(TaskManagerFields.FIELD_NODE, taskProgress.getNodeName());
                        attributeMap.put(TaskManagerFields.FIELD_NAME, taskProgress.getTaskName());
                        attributeMap.put(TaskManagerFields.FIELD_USER, taskProgress.getUserName());
                        attributeMap.put(TaskManagerFields.FIELD_SUBMIT_TIME, taskProgress.getSubmitTimeMs());
                        attributeMap.put(TaskManagerFields.FIELD_AGE, taskProgress.getAgeMs());
                        attributeMap.put(TaskManagerFields.FIELD_INFO, taskProgress.getTaskInfo());
                        return attributeMap;
                    })
                    .filter(attributeMap -> expressionMatcher.match(attributeMap, criteria.getExpression()))
                    .forEach(attributeMap -> {
                        final String[] fields = fieldIndex.getFields();
                        final Val[] arr = new Val[fields.length];
                        for (int i = 0; i < fields.length; i++) {
                            final String fieldName = fields[i];
                            Val val = ValNull.INSTANCE;
                            if (fieldName != null) {
                                final Object o = attributeMap.get(fieldName);
                                if (o != null) {
                                    if (o instanceof String) {
                                        val = ValString.create((String) o);
                                    } else if (o instanceof Long) {
                                        val = ValLong.create((long) o);
                                    } else if (o instanceof Integer) {
                                        val = ValInteger.create((int) o);
                                    }
                                }
                            }
                            arr[i] = val;
                        }
                        consumer.accept(Val.of(arr));
                    });
        });
    }

    private Map<String, TaskProgressResponse> searchAllNodes() {
        final Function<TaskContext, Map<String, TaskProgressResponse>> function = taskContext -> {
            final Map<String, TaskProgressResponse> nodeResponses = new ConcurrentHashMap<>();

            try {
                // Get the nodes that we are going to send the entity event to.
                final Set<String> targetNodes = targetNodeSetFactory.getEnabledActiveTargetNodeSet();

                final CountDownLatch countDownLatch = new CountDownLatch(targetNodes.size());

                // Only send the event to remote nodes and not this one.
                // Send the entity event.
                targetNodes.forEach(nodeName -> {
                    final Supplier<TaskProgressResponse> supplier = taskContextFactory.childContextResult(taskContext,
                            "Getting progress from node '" + nodeName + "'",
                            tc ->
                                    taskResource.list(nodeName));
                    CompletableFuture
                            .supplyAsync(supplier, executor)
                            .whenComplete((r, t) -> {
                                if (r != null) {
                                    nodeResponses.putIfAbsent(nodeName, r);
                                }
                                countDownLatch.countDown();
                            });
                });

                // Wait for all requests to complete.
                countDownLatch.await();

            } catch (final NullClusterStateException | NodeNotFoundException | InterruptedException e) {
                LOGGER.warn(e.getMessage());
                LOGGER.debug(e.getMessage(), e);
            } catch (final RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
            }
            return nodeResponses;
        };
        return taskContextFactory.contextResult("Search Task Progress", function).get();
    }
}
