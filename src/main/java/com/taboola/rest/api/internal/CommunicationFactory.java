package com.taboola.rest.api.internal;

import java.util.concurrent.TimeUnit;

import com.taboola.rest.api.internal.config.CommunicationConfig;
import com.taboola.rest.api.internal.interceptors.CommunicationInterceptor;
import com.taboola.rest.api.internal.interceptors.HeadersInterceptor;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by vladi
 * Date: 10/4/2017
 * Time: 10:54 PM
 * By Taboola
 */
public final class CommunicationFactory {

    private final Retrofit retrofit;

    public CommunicationFactory(CommunicationConfig communicationConfig) {
        Retrofit.Builder retrofitBuilder = createRetrofitBuilder(communicationConfig);

        this.retrofit = retrofitBuilder.baseUrl(communicationConfig.getBaseUrl()).build();
    }

    private HttpLoggingInterceptor createLoggingInterceptor(CommunicationConfig config) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new CommunicationInterceptor());
        if (config.isDebug()) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.redactHeader("Authorization");
            loggingInterceptor.redactHeader("Cookie");
        } else {
            loggingInterceptor.setLevel(config.getLoggingLevel());
        }

        return loggingInterceptor;
    }

    private Retrofit.Builder createRetrofitBuilder(CommunicationConfig config) {
        return new Retrofit.Builder()
                .addConverterFactory(StringConverterFactory.create(config.getStringResponseFactories()))
                .addConverterFactory(JacksonConverterFactory.create(config.getObjectMapper()))
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create(config.getExceptionFactory(), config.getStringResponseFactories()))
                .client(createOkHttpClient(config));
    }

    private OkHttpClient createOkHttpClient(CommunicationConfig config) {
        return new OkHttpClient.Builder()
                .addInterceptor(new HeadersInterceptor(config.getHeaders()))
                .addInterceptor(createLoggingInterceptor(config))
                .readTimeout(config.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS)
                .connectTimeout(config.getConnectionTimeoutMillis(), TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(config.getMaxIdleConnections(),
                        config.getKeepAliveDurationMillis(), TimeUnit.MILLISECONDS))
                .build();
    }

    public <E> E createRetrofitEndpoint(Class<E> clazz) {
        return retrofit.create(clazz);
    }
}
