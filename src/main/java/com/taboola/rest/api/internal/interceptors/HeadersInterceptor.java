package com.taboola.rest.api.internal.interceptors;

import java.io.IOException;

import com.taboola.rest.api.model.RequestHeader;
import com.taboola.rest.api.model.RequestHeadersSupplier;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 10/21/20.
 */
public class HeadersInterceptor implements Interceptor {
    private final RequestHeadersSupplier requestHeadersSupplier;

    public HeadersInterceptor(RequestHeadersSupplier requestHeadersSupplier) {
        this.requestHeadersSupplier = requestHeadersSupplier;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        requestHeadersSupplier.get()
                .stream()
                .filter(this::validateHeader)
                .forEach(header -> builder.header(header.getName(), header.getValue()));
        return chain.proceed(builder.build());
    }

    private boolean validateHeader(RequestHeader header) {
        return header.getName() != null && header.getValue() != null;
    }
}
