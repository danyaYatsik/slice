package org.yatsiko.slice.service.serialization;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.json.JsonMapper;
import org.yatsiko.slice.model.ProcessEvent;

import java.io.Serializable;

public class ProcessEventDeserializer implements Serializable {
    final ObjectMapper mapper;

    public ProcessEventDeserializer() {
        mapper = JsonMapper.builder().build();
    }

    public ProcessEvent deserialize(String json) {
        try {
            return mapper.readValue(json, ProcessEvent.class);
        } catch (Exception ignored) {
            // Just ignore invalid input for convenience during testing
            return null;
        }
    }
}
