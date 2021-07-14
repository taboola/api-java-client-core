package com.taboola.rest.api.internal;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.taboola.rest.api.model.StringResponseFactory;

public class StringResponseFactories {
    private final Map<Type, StringResponseFactory> typeToResponseFactory = new HashMap<>();

    public void addFactory(Type type, StringResponseFactory stringResponseFactory) {
        typeToResponseFactory.put(type, stringResponseFactory);
    }

    public StringResponseFactory getFactory(Type type){
        return typeToResponseFactory.get(type);
    }

    public boolean isExist(Type type){
        return typeToResponseFactory.containsKey(type);
    }

    @Override
    public String toString() {
        return "StringResponseFactories{" +
                "typeToResponseFactory=" + typeToResponseFactory +
                '}';
    }
}
