package org.yatsiko.slice.function;

import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.yatsiko.slice.model.ProcessSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessSnapshotFilter implements AllWindowFunction<ProcessSnapshot, List<ProcessSnapshot>, TimeWindow> {
    @Override
    public void apply(TimeWindow window, Iterable<ProcessSnapshot> values, Collector<List<ProcessSnapshot>> out) {
        final Map<String, ProcessSnapshot> uniqueSnapshots = new HashMap<>();

        values.forEach(snapshot -> {
            final ProcessSnapshot existedSnapshot = uniqueSnapshots.get(snapshot.processId);

            if (existedSnapshot == null || existedSnapshot.secondsRunning < snapshot.secondsRunning) {
                uniqueSnapshots.put(snapshot.processId, snapshot);
            }
        });
        out.collect(new ArrayList<>(uniqueSnapshots.values()));
    }
}
