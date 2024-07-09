package stroom.job.shared;

import stroom.job.shared.JobNode.JobType;
import stroom.util.shared.scheduler.Schedule;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

@SuppressWarnings("ClassCanBeRecord")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchScheduleRequest {

    private final Set<Integer> jobNodeIds;
    private final JobType jobType;
    private final Schedule schedule;

    @JsonCreator
    public BatchScheduleRequest(@JsonProperty("jobNodeIds") final Set<Integer> jobNodeIds,
                                @JsonProperty("jobType") final JobType jobType,
                                @JsonProperty("schedule") final Schedule schedule) {
        this.jobNodeIds = jobNodeIds;
        this.jobType = jobType;
        this.schedule = schedule;
    }

    public Set<Integer> getJobNodeIds() {
        return jobNodeIds;
    }

    public JobType getJobType() {
        return jobType;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    @Override
    public String toString() {
        return "BatchScheduleRequest{" +
                "jobNodeIds=" + jobNodeIds +
                ", jobType=" + jobType +
                ", schedule=" + schedule +
                '}';
    }
}
