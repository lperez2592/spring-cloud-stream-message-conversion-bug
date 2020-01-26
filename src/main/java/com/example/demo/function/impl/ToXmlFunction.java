package com.example.demo.function.impl;

import java.util.function.Function;

public class ToXmlFunction implements Function<String, String> {
    @Override
    public String apply(String o) {
        //language=XML
        String xml = "<message xmlns=\"http://www.w3.org/1999/XSL/Transform\">o</message>";
        return xml;
    }

}