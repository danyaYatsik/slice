package org.yatsiko.slice.model;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

public class ProcessEvent {
    @JsonProperty("processId")
    public String processId;
    @JsonProperty("eventTime")
    public long time;
    @JsonProperty("eventType")
    public EventType type;
    @JsonProperty("progress")
    public double progress;

    @Override
    public String toString() {
        return "Event{" +
                "processId='" + processId + '\'' +
                ", time=" + time +
                ", type=" + type +
                ", progress=" + progress +
                '}';
    }
}
