package com.taboola.rest.api.internal;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.taboola.rest.api.exceptions.factories.DefaultExceptionFactory;

import retrofit2.Call;
import retrofit2.CallAdapter;

/**
 * Created by vladi
 * Date: 1/16/2018
 * Time: 10:16 PM
 * By Taboola
 */
public class SynchronousCallAdapterFactoryTest {

    private SynchronousCallAdapterFactory testInstance;

    public static class DummyTestClass { }

    @Before
    public void beforeTest() {
        testInstance = SynchronousCallAdapterFactory.create(new DefaultExceptionFactory(), new StringResponseFactories());
    }

    @Test
    public void testSynchronousAdapterForNonCallType() {
        CallAdapter<Object, Object> actualAdapter = testInstance.get(DummyTestClass.class, null, null);
        Assert.assertNotNull("Invalid adapter, expecting adapter", actualAdapter);
        Assert.assertEquals("Invalid adapter type", DummyTestClass.class, actualAdapter.responseType());

        actualAdapter = testInstance.get(Object.class, null, null);
        Assert.assertNotNull("Invalid adapter, expecting adapter", actualAdapter);
        Assert.assertEquals("Invalid adapter type", Object.class, actualAdapter.responseType());
    }

    @Test
    public void testSynchronousAdapterForCallType() {
        CallAdapter<Object, Object> actualAdapter = testInstance.get(Call.class, null, null);
        Assert.assertNull("Invalid adapter, expecting no adapter", actualAdapter);
    }
}
