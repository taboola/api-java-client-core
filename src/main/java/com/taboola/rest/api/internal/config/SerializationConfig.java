package com.taboola.rest.api.internal.config;

import java.util.HashMap;
import java.util.Map;

public class SerializationConfig {
    private Map<Class<?>, Class<?>> mixins;
    private boolean shouldIgnoreAnySetterAnnotation;
    private boolean shouldDisableReadUnknownEnumValuesAsDefaultValue;

    public SerializationConfig() {
        mixins = new HashMap<>();
        shouldIgnoreAnySetterAnnotation = false;
        shouldDisableReadUnknownEnumValuesAsDefaultValue = false;
    }

    public SerializationConfig setMixins(Map<Class<?>, Class<?>> mixins) {
        this.mixins = mixins;
        return this;
    }

    public SerializationConfig setShouldIgnoreAnySetterAnnotation() {
        this.shouldIgnoreAnySetterAnnotation = true;
        return this;
    }

    public SerializationConfig setShouldDisableReadUnknownEnumValuesAsDefaultValue() {
        this.shouldDisableReadUnknownEnumValuesAsDefaultValue = true;
        return this;
    }

    public Map<Class<?>, Class<?>> getMixins() {
        return mixins;
    }

    public boolean shouldIgnoreAnySetterAnnotation() {
        return shouldIgnoreAnySetterAnnotation;
    }
    public boolean shouldDisableReadUnknownEnumValuesAsDefaultValue() {
        return shouldDisableReadUnknownEnumValuesAsDefaultValue;
    }

    @Override
    public String toString() {
        return "SerializationConfig{" +
                "mixins=" + mixins +
                ", shouldIgnoreAnySetterAnnotation=" + shouldIgnoreAnySetterAnnotation +
                '}';
    }
}
