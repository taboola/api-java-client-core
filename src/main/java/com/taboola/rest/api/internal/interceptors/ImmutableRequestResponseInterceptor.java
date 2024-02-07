package com.taboola.rest.api.internal.interceptors;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taboola.rest.api.model.CommunicationInterceptor;

/**
 * Created by vladi.m
 * Date 20/11/2023
 * Time 15:20
 * Copyright Taboola
 */
public class ImmutableRequestResponseInterceptor implements Interceptor {

    private static final Logger logger = LogManager.getLogger(ImmutableRequestResponseInterceptor.class);
    private final CommunicationInterceptor interceptor;

    public ImmutableRequestResponseInterceptor(CommunicationInterceptor interceptor) {
        Objects.requireNonNull(interceptor);
        this.interceptor = interceptor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        try {
            interceptor.before(chain.request());
        } catch (Throwable t) {
            logger.error("Failed to execute 'before' communication interceptor", t);
        }

        Response response = null;
        try {
            response = chain.proceed(chain.request());
            return response;
        } finally {
            try {
                interceptor.after(chain.request(), response);
            } catch (Throwable t) {
                logger.error("Failed to execute 'after' communication interceptor", t);
            }
        }
    }
}
