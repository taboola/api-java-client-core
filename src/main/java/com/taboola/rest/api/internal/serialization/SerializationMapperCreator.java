package com.taboola.rest.api.internal.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.taboola.rest.api.internal.config.SerializationConfig;

public class SerializationMapperCreator {
    public static ObjectMapper createObjectMapper(SerializationConfig serializationConfig) {
        ObjectMapper objectMapper = new ObjectMapper();
        if(serializationConfig.shouldUseSnakeCase()) {
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        } else {
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        }
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        if (!serializationConfig.shouldDisableReadUnknownEnumValuesAsDefaultValue()) {
            objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true);
        }
        if (serializationConfig.shouldAllowNullAsDefaultValueForReadUnknownEnums()) {
            // NOTE: default enum value takes precedence over null
            objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        }

        serializationConfig.getMixins().forEach(objectMapper::addMixIn);

        if (serializationConfig.shouldIgnoreAnySetterAnnotation()) {
            objectMapper.setAnnotationIntrospector(new IgnoreAnySetterSerializationIntrospector());
        }

        return objectMapper;
    }
}
