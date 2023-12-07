package com.taboola.rest.api;

import com.taboola.rest.api.exceptions.RestAPIException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;

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

    @Test
    public void testRun_WithCustomInterceptor_ExpectInterceptorCalled() throws IOException {
        Interceptor customInterceptor = Mockito.mock(Interceptor.class);

        Mockito.when(customInterceptor.intercept(Mockito.any()))
            .then((Answer<Response>) invocationOnMock -> {
            Interceptor.Chain chain = invocationOnMock.getArgumentAt(0, Interceptor.Chain.class);
            return new Response.Builder()
                .request(chain.request())
                .code(200)
                .protocol(Protocol.HTTP_1_1)
                .message("OK")
                .body(ResponseBody.create(MediaType.parse("application/json"), "{\"test\":\"test\"}"))
                .build();
        });

        RestAPIClient restAPIClient = RestAPIClient.builder().setBaseUrl("http://example.com/")
            .setInterceptors(customInterceptor)
            .build();

        assertNotNull("Missing rest client instance", restAPIClient);

        TestEndpoint endpoint = restAPIClient.createRetrofitEndpoint(TestEndpoint.class);
        assertNotNull("Failed to generate endpoint", endpoint);

        endpoint.getTestObject("test");

        Mockito.verify(customInterceptor, Mockito.times(1)).intercept(Mockito.any());
    }
}