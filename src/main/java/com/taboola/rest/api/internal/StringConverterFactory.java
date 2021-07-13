package com.taboola.rest.api.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by vladi
 * Date: 1/14/2018
 * Time: 11:37 PM
 * By Taboola
 */
public class StringConverterFactory extends Converter.Factory {

    private final ResponseFactories responseFactories;

    public static StringConverterFactory create(ResponseFactories responseFactories) {
        return new StringConverterFactory(responseFactories);
    }

    private StringConverterFactory(ResponseFactories responseFactories) {
        this.responseFactories = responseFactories;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (String.class.equals(type) || responseFactories.isExist(type)) {
            return ResponseBody::string;
        }
        return null;
    }
}
