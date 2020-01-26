package com.example.demo.function.impl;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ReturnsListOfMapMessageFunction implements Function<Message<String>, Message<List<Map<String, Object>>>> {
    private Function<String, List<Map<String, Object>>> returnsListOfMap2;

    public ReturnsListOfMapMessageFunction(Function<String, List<Map<String, Object>>> returnsListOfMap2) {
        this.returnsListOfMap2 = returnsListOfMap2;
    }

    @Override
    public Message<List<Map<String, Object>>> apply(Message<String> stringMessage) {
        List<Map<String, Object>> outgoingPayload = returnsListOfMap2.apply(stringMessage.getPayload());

        return MessageBuilder.withPayload(outgoingPayload).build();
    }

}