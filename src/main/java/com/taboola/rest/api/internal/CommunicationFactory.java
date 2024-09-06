package com.taboola.rest.api.internal;

import java.util.concurrent.TimeUnit;

import com.taboola.rest.api.internal.config.CommunicationConfig;
import com.taboola.rest.api.internal.interceptors.CommunicationInterceptor;
import com.taboola.rest.api.internal.interceptors.HeadersInterceptor;
import com.taboola.rest.api.internal.interceptors.ImmutableRequestResponseInterceptor;
import com.taboola.rest.api.model.HttpLoggingLevel;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by vladi
 * Date: 10/4/2017
 * Time: 10:54 PM
 * By Taboola
 */
public final class CommunicationFactory {
    private static final Logger logger = LogManager.getLogger(CommunicationFactory.class);
    private final Retrofit retrofit;

    public CommunicationFactory(CommunicationConfig communicationConfig) {
        Retrofit.Builder retrofitBuilder = createRetrofitBuilder(communicationConfig);

        this.retrofit = retrofitBuilder.baseUrl(communicationConfig.getBaseUrl()).build();
    }

    private HttpLoggingInterceptor createLoggingInterceptor(CommunicationConfig config) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new CommunicationInterceptor());
        if (config.isDebug()) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(getHttpLoggingInterceptorLevel(config.getLoggingLevel()));
        }
        if (loggingInterceptor.getLevel() == HttpLoggingInterceptor.Level.BODY || loggingInterceptor.getLevel() == HttpLoggingInterceptor.Level.HEADERS) {
            loggingInterceptor.redactHeader("Authorization");
            loggingInterceptor.redactHeader("Cookie");
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
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HeadersInterceptor(config.getHeaders()))
                .addInterceptor(createLoggingInterceptor(config))
                .addInterceptor(new ImmutableRequestResponseInterceptor(config.getCommunicationInterceptor()))
                .readTimeout(config.getReadTimeoutMillis(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeoutMillis(), TimeUnit.MILLISECONDS)
                .connectTimeout(config.getConnectionTimeoutMillis(), TimeUnit.MILLISECONDS)
                .connectionPool(new ConnectionPool(config.getMaxIdleConnections(),
                        config.getKeepAliveDurationMillis(), TimeUnit.MILLISECONDS));

        config.getInterceptors().forEach(builder::addInterceptor);

        return builder.build();
    }


    public static HttpLoggingInterceptor.Level getHttpLoggingInterceptorLevel(HttpLoggingLevel httpLoggingLevel) {
        switch (httpLoggingLevel) {
            case NONE:
                return HttpLoggingInterceptor.Level.NONE;
            case BASIC:
                return HttpLoggingInterceptor.Level.BASIC;
            case HEADERS:
                return HttpLoggingInterceptor.Level.HEADERS;
            case BODY:
                return HttpLoggingInterceptor.Level.BODY;
            default:
                logger.error("Getting unknown HttpLoggingLevel [{}]", httpLoggingLevel.name());
                return HttpLoggingInterceptor.Level.BASIC;
        }
    }

    public <E> E createRetrofitEndpoint(Class<E> clazz) {
        return retrofit.create(clazz);
    }
}
