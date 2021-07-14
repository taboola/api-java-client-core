package com.taboola.rest.api.internal;

import okhttp3.ResponseBody;
import org.junit.Assert;
import org.junit.Test;
import retrofit2.Converter;

/**
 * Created by vladi
 * Date: 1/16/2018
 * Time: 10:11 PM
 * By Taboola
 */
public class StringConverterFactoryTest {

    public static class EntityModelExample {
        private String id;
        private String name;
    }

    @Test
    public void testResponseBodyConverter() {
        StringConverterFactory testInstance = StringConverterFactory.create(new StringResponseFactories());

        Converter<ResponseBody, ?> responseConverter = testInstance.responseBodyConverter(String.class, null, null);
        Assert.assertNotNull("Invalid converter, expecting converter", responseConverter);

        responseConverter = testInstance.responseBodyConverter(Object.class, null, null);
        Assert.assertNull("Invalid converter, expecting no converter", responseConverter);
    }

    @Test
    public void testResponseBodyConverterWithStringResponseFactory() {
        StringResponseFactories stringResponseFactories = new StringResponseFactories();
        stringResponseFactories.addFactory(EntityModelExample.class, null);
        StringConverterFactory testInstance = StringConverterFactory.create(stringResponseFactories);

        Converter<ResponseBody, ?> responseConverter = testInstance.responseBodyConverter(EntityModelExample.class, null, null);
        Assert.assertNotNull("Invalid converter, expecting converter", responseConverter);

        responseConverter = testInstance.responseBodyConverter(Object.class, null, null);
        Assert.assertNull("Invalid converter, expecting no converter", responseConverter);
    }
}
