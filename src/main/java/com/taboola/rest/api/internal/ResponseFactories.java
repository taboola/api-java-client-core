package com.taboola.rest.api.internal;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class ResponseFactories {
    private final Map<Type, BiFunction<Map<String, List<String>>, String, Object>> typeToResponseFactory = new HashMap<>();

    public void addResponseFactory(Type type, BiFunction<Map<String, List<String>>, String, Object> responseFactory) {
        typeToResponseFactory.put(type, responseFactory);
    }

    public BiFunction<Map<String, List<String>>, String, Object> getResponseFactory(Type type){
        return typeToResponseFactory.get(type);
    }

    public boolean isExist(Type type){
        return typeToResponseFactory.containsKey(type);
    }

    @Override
    public String toString() {
        return "ResponseFactories{" +
                "typeToResponseFactory.keys=" + typeToResponseFactory.keySet() +
                '}';
    }
}
