package com.example.demo.function.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConsumesListOfMapFunction implements Function<List<Map<String, Object>>, List<Object>> {

    @Override
    public List<Object> apply(List<Map<String, Object>> maps) {
        return maps.stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
    }
}