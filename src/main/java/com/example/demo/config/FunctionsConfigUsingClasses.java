package com.example.demo.config;

import com.example.demo.function.impl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Configuration
public class FunctionsConfigUsingClasses {
    @Bean
    public Function<String, String> toJson2() {
        return new ToJsonFunction();
    }

    @Bean
    public Function<String, String> toXml2() {
        return new ToXmlFunction();
    }

    @Bean
    public Function<String, List<Map<String,Object>>> returnsListOfMap2(
            Function<String, String> toJson2,
            Function<String, String> toXml2
    ){
        return new ReturnsListOfMapFunction(toJson2, toXml2);
    }

    @Bean
    public Function<List<Map<String,Object>>, List<Object>> consumesListOfMap2(){
        return new ConsumesListOfMapFunction();
    }

    @Bean
    public Function<Message<String>, Message<List<Map<String,Object>>>> returnsListOfMapMessage2(
            Function<String, List<Map<String,Object>>> returnsListOfMap2
    ){
        return new ReturnsListOfMapMessageFunction(returnsListOfMap2);
    }

    @Bean
    public Function<Message<List<Map<String,Object>>>, Message<List<Object>>> consumesListOfMapMessage2(
            Function<List<Map<String,Object>>, List<Object>> consumesListOfMap2
    ){
        return new ConsumesListOfMapMessageFunction(consumesListOfMap2);
    }
}
