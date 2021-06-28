package com.taboola.rest.api;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.taboola.rest.api.exceptions.RestAPIException;

public class RestAPIClientTest {

    public interface TestEndpoint {
        @GET("/for-testing")
        @Headers("Content-Type: application/json")
        Object getTestObject(@Header("Authorization") String accessToken) throws RestAPIException;
    }

    @Test
    public void testBuild_whenEmptyBuilder_expectingClientBuilt() {
        RestAPIClient restAPIClient = RestAPIClient.builder().setBaseUrl("http://example.com/").build();
        assertNotNull("Missing rest client instance", restAPIClient);
        assertNotNull("Failed to generate endpoint", restAPIClient.createRetrofitEndpoint(TestEndpoint.class));
    }
}