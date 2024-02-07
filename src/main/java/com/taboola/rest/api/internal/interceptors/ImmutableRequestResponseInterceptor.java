package com.taboola.rest.api.internal.interceptors;

import okhttp3.Interceptor;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

import com.taboola.rest.api.model.CommunicationInterceptor;

/**
 * Created by vladi.m
 * Date 20/11/2023
 * Time 15:20
 * Copyright Taboola
 */
public class ImmutableRequestResponseInterceptor implements Interceptor {

    private final CommunicationInterceptor interceptor;

    public ImmutableRequestResponseInterceptor(CommunicationInterceptor interceptor) {
        Objects.requireNonNull(interceptor);
        this.interceptor = interceptor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        interceptor.before(chain.request());
        Response response = null;
        try {
            response = chain.proceed(chain.request());
            return response;
        } finally {
            interceptor.after(chain.request(), response);
        }
    }
}
