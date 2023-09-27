package org.yatsiko.slice.model;

public class ProcessSnapshot {
    public String processId;
    public ProcessStatus status;
    public long startedAt;
    public long secondsRunning;
    public double progress;

    public ProcessSnapshot() {
    }

    public ProcessSnapshot(String processId, ProcessStatus status, long startedAt, long secondsRunning, double progress) {
        this.processId = processId;
        this.status = status;
        this.startedAt = startedAt;
        this.secondsRunning = secondsRunning;
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "ProcessSnapshot{" +
                "processId='" + processId + '\'' +
                ", status=" + status +
                ", secondsRunning=" + secondsRunning +
                ", progress=" + progress +
                '}';
    }
}
