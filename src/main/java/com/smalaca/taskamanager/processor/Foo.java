package com.smalaca.taskamanager.processor;

import java.util.UUID;

public class Foo {
    public static String bar(Long id) {
        return UUID.randomUUID().toString();
    }
}
