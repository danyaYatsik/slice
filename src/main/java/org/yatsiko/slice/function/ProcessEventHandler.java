package org.yatsiko.slice.function;

import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;
import org.yatsiko.slice.model.EventType;
import org.yatsiko.slice.model.ProcessEvent;
import org.yatsiko.slice.model.ProcessSnapshot;
import org.yatsiko.slice.model.ProcessStatus;

public class ProcessEventHandler extends RichFlatMapFunction<ProcessEvent, ProcessSnapshot> {

    private static final String PROCESS_STATE_NAME = "PROCESS_STATE_NAME";

    private ValueState<ProcessSnapshot> state;

    @Override
    public void flatMap(ProcessEvent event, Collector<ProcessSnapshot> out) throws Exception {
        //TODO: maybe handle state inconsistency
        final ProcessSnapshot existedSnapshot = state.value();
        final long startedAt = EventType.START.equals(event.type) ? event.time : existedSnapshot.startedAt;
        final ProcessStatus status = EventType.END.equals(event.type) ? ProcessStatus.END : ProcessStatus.RUNNING;
        final long secondsRunning = (event.time - startedAt) / 1000;

        final ProcessSnapshot snapshot = new ProcessSnapshot(event.processId, status, startedAt, secondsRunning, event.progress);
        state.update(snapshot);
        out.collect(snapshot);
    }

    @Override
    public void open(Configuration parameters) {
        final ValueStateDescriptor<ProcessSnapshot> stateDescriptor = new ValueStateDescriptor<>(PROCESS_STATE_NAME, ProcessSnapshot.class);
        state = getRuntimeContext().getState(stateDescriptor);
    }
}
