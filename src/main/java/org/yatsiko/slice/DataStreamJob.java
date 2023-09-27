/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yatsiko.slice;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.yatsiko.slice.function.ProcessEventHandler;
import org.yatsiko.slice.function.ProcessSnapshotFilter;
import org.yatsiko.slice.service.serialization.ProcessEventDeserializer;

import java.util.Objects;

public class DataStreamJob {

    private static final ProcessEventDeserializer eventDeserializer = new ProcessEventDeserializer();

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        final DataStream<String> dataStream = env.socketTextStream("localhost", 9999);

        dataStream.map(eventDeserializer::deserialize)
                .filter(Objects::nonNull)
                .keyBy(event -> event.processId)
                .flatMap(new ProcessEventHandler())
                .windowAll(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .apply(new ProcessSnapshotFilter())
                .print();

        env.execute();
    }
}