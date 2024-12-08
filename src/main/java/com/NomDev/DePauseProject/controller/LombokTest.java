package com.NomDev.DePauseProject.controller;

import lombok.Data;

@Data
public class LombokTest {
    private String field1;
    private int field2;

    public static void main(String[] args) {
        LombokTest test = new LombokTest();
        test.setField1("Test");
        test.setField2(42);

        System.out.println(test.getField1()); // Ожидается "Test"
        System.out.println(test.getField2()); // Ожидается 42
    }
}