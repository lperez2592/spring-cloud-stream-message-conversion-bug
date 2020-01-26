package com.example.demo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.SmartMessageConverter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    private FunctionCatalog functionCatalog;

    @Autowired
    private SmartMessageConverter smartMessageConverter;

    @Test
    public void testComposedFunctionUsingLambdas() {

        Function<String, List<String>> functionToInvoke = functionCatalog.lookup("returnsListOfMap|consumesListOfMap");

        List<String> outputMessagePayloads = functionToInvoke.apply("Some input message payload");

        assertThat(outputMessagePayloads).isNotNull();
        assertThat(outputMessagePayloads).isNotEmpty();
    }

    @Test
    public void testComposedFunctionUsingClasses() {

        Function<String, List<String>> functionToInvoke = functionCatalog.lookup("returnsListOfMap2|consumesListOfMap2");

        List<String> outputMessagePayloads = functionToInvoke.apply("Some input message payload");

        assertThat(outputMessagePayloads).isNotNull();
        assertThat(outputMessagePayloads).isNotEmpty();
    }

    @Test
    public void testComposedMessageFunctionUsingLambdas() {

        Function<Message<String>, Message<List<String>>> functionToInvoke = functionCatalog.lookup("returnsListOfMapMessage|consumesListOfMapMessage");

        Message<List<String>> outputMessagePayloads = functionToInvoke.apply(MessageBuilder.withPayload("Some input message payload").build());

        assertThat(outputMessagePayloads).isNotNull();
        assertThat(outputMessagePayloads.getPayload()).isNotEmpty();
    }

    /**
     * Should be equivalent to testComposedMessageFunctionUsingLambdas, but blows up with NPE due to SmartMessageConverter failure
     */
    @Test
    public void testComposedMessageFunctionUsingClasses() {

        Function<Message<String>, Message<List<String>>> functionToInvoke = functionCatalog.lookup("returnsListOfMapMessage2|consumesListOfMapMessage2");

        Message<List<String>> outputMessagePayloads = functionToInvoke.apply(MessageBuilder.withPayload("Some input message payload").build());

        assertThat(outputMessagePayloads).isNotNull();
        assertThat(outputMessagePayloads.getPayload()).isNotEmpty();
    }

    /**
     * MappingJackson2MessageConverter.convertFromInternal correctly does not try to convert the list because it recognizes that the target class is already the same as the payload class
     */
    @Test
    public void testSmartMessageConverter_convertsMessageOfListOfMap_whenCalledWithoutConversionHint() {

        Map<String, Object> hashMap = new HashMap<String, Object>() {{
            put("a key", "a value");
        }};

        List<Map<String, Object>> list = Arrays.asList(hashMap);

        Message<List<Map<String, Object>>> message = MessageBuilder
                .withPayload(
                        list
                )
                .build();

        Class<? extends List> listClass = list.getClass();

        Object o = smartMessageConverter.fromMessage(message, listClass);

        assertThat(o).isNotNull();
        assertThat(o).isInstanceOf(List.class);

        List outputList = (List) o;

        assertThat(outputList).hasSize(1);

        Object outputMap = outputList.get(0);

        assertThat(outputMap).isNotNull();
        assertThat(outputMap).isInstanceOf(Map.class);
    }

    /**
     * MappingJackson2MessageConverter.convertFromInternal correctly does not try to convert the list because it recognizes that the target class is already the same as the payload class
     */
    @Test
    public void testSmartMessageConverter_convertsMessageOfListOfMap_whenCalledWithNullConversionHint(){
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("a key", "a value");

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(hashMap);

        Message<List<Map<String, Object>>> message = MessageBuilder
                .withPayload(list)
                .build();


        Class<? extends List> listClass = list.getClass();

        Object o = smartMessageConverter.fromMessage(message, listClass, null);

        assertThat(o).isNotNull();
        assertThat(o).isInstanceOf(List.class);

        List outputList = (List) o;

        assertThat(outputList).hasSize(1);

        Object outputMap = outputList.get(0);

        assertThat(outputMap).isNotNull();
        assertThat(outputMap).isInstanceOf(Map.class);
    }

    /**
     * SmartMessageConverter implementations should know how to convert the message but instead puts nulls in the list of maps
     *
     * ApplicationJsonMessageMarshallingConverter.convertFromInternal -> convertParameterizedType fails to convert the List<Map>
     * It does not check that the target class is already the same as the payload class
     */
    @Test
    public void testSmartMessageConverter_convertsMessageOfListOfMap_whenCalledWithNonNullConversionHint() {

        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("a key", "a value");

        List<Map<String, Object>> list = new ArrayList<>();
        list.add(hashMap);

        Message<List<Map<String, Object>>> message = MessageBuilder
                .withPayload(list)
                .build();

        Class<? extends Message> messageClass = message.getClass();

        Class<? extends List> listClass = list.getClass();

        ParameterizedTypeImpl parameterizedType = ParameterizedTypeImpl.make(messageClass, new Type[]{listClass}, null);

        Object o = smartMessageConverter.fromMessage(message, listClass, parameterizedType);

        assertThat(o).isNotNull();
        assertThat(o).isInstanceOf(List.class);

        List outputList = (List) o;

        assertThat(outputList).hasSize(1);

        Object outputMap = outputList.get(0);

        assertThat(outputMap).isNotNull();
        assertThat(outputMap).isInstanceOf(Map.class);
    }

}

