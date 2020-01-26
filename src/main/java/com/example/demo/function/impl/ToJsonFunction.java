package com.example.demo.function.impl;

import java.util.function.Function;

public class ToJsonFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        String json = "{\"message\" : \"" + o + "\"}";
        return json;
    }
}
