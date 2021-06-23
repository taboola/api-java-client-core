package com.taboola.rest.api.internal.interceptors;

import java.io.IOException;
import java.util.Collection;

import com.taboola.rest.api.model.RequestHeader;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 10/21/20.
 */
public class HeadersInterceptor implements Interceptor {
    private final Collection<RequestHeader> headers;

    public HeadersInterceptor(Collection<RequestHeader> headers) {
        this.headers = headers;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        headers.stream()
                .filter(this::validateHeader)
                .forEach(header -> builder.header(header.getName(), header.getValue()));
        return chain.proceed(builder.build());
    }

    private boolean validateHeader(RequestHeader header){
        return header.getName() != null && header.getValue() != null;
    }
}
