package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class FunctionsConfigUsingLambda {
    @Bean
    public Function<String, String> toJson() {
        return new Function<String, String>() {
            @Override
            public String apply(String o) {
                String json = "{\"message\" : \"" + o + "\"}";
                return json;
            }
        };
    }

    @Bean
    public Function<String, String> toXml() {
        return new Function<String, String>() {
            @Override
            public String apply(String o) {
                //language=XML
                String xml = "<message xmlns=\"http://www.w3.org/1999/XSL/Transform\">o</message>";
                return xml;
            }

        };
    }

    @Bean
    public Function<String, List<Map<String,Object>>> returnsListOfMap(
            Function<String, String> toJson,
            Function<String, String> toXml
    ){
        return new Function<String, List<Map<String,Object>>>(){

            @Override
            public List<Map<String, Object>> apply(String s) {
                String json = toJson.apply(s);
                String xml = toXml.apply(s);

                HashMap<String, Object> map1 = new HashMap<>();
                map1.put("json", json);

                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("xml", xml);

                return Arrays.asList(map1,map2);
            }
        };
    }

    @Bean
    public Function<List<Map<String,Object>>, List<Object>> consumesListOfMap(){
        return new Function<List<Map<String,Object>>, List<Object>>(){

            @Override
            public List<Object> apply(List<Map<String, Object>> maps) {
                return maps.stream().flatMap(m -> m.values().stream()).collect(Collectors.toList());
            }
        };
    }

    @Bean
    public Function<Message<String>, Message<List<Map<String,Object>>>> returnsListOfMapMessage(
            Function<String, List<Map<String,Object>>> returnsListOfMap
    ){
        return new Function<Message<String>, Message<List<Map<String,Object>>>>(){

            @Override
            public Message<List<Map<String, Object>>> apply(Message<String> stringMessage) {
                List<Map<String, Object>> outgoingPayload = returnsListOfMap.apply(stringMessage.getPayload());

                return MessageBuilder.withPayload(outgoingPayload).build();
            }
        };

    }

    @Bean
    public Function<Message<List<Map<String,Object>>>, Message<List<Object>>> consumesListOfMapMessage(
            Function<List<Map<String,Object>>, List<Object>> consumesListOfMap
    ){
        return new Function<Message<List<Map<String,Object>>>, Message<List<Object>>>(){

            @Override
            public Message<List<Object>> apply(Message<List<Map<String, Object>>> listMessage) {

                List<Object> outgoingPayload = consumesListOfMap.apply(listMessage.getPayload());

                return MessageBuilder.withPayload(outgoingPayload).build();
            }
        };
    }
}
