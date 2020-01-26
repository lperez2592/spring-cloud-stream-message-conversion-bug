package com.example.demo.function.impl;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConsumesListOfMapMessageFunction implements Function<Message<List<Map<String, Object>>>, Message<List<Object>>> {
    private Function<List<Map<String, Object>>, List<Object>> consumesListOfMap2;

    public ConsumesListOfMapMessageFunction(Function<List<Map<String, Object>>, List<Object>> consumesListOfMap2) {
        this.consumesListOfMap2 = consumesListOfMap2;
    }

    @Override
    public Message<List<Object>> apply(Message<List<Map<String, Object>>> listMessage) {

        List<Object> outgoingPayload = consumesListOfMap2.apply(listMessage.getPayload());

        return MessageBuilder.withPayload(outgoingPayload).build();
    }
}