package com.example.demo.function.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ReturnsListOfMapFunction implements Function<String, List<Map<String, Object>>> {
    private Function<String, String> toJson2;
    private Function<String, String> toXml2;

    public ReturnsListOfMapFunction(Function<String, String> toJson2, Function<String, String> toXml2) {
        this.toJson2 = toJson2;
        this.toXml2 = toXml2;
    }

    @Override
    public List<Map<String, Object>> apply(String s) {
        String json = toJson2.apply(s);
        String xml = toXml2.apply(s);

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("json", json);

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("xml", xml);

        return Arrays.asList(map1, map2);
    }
}