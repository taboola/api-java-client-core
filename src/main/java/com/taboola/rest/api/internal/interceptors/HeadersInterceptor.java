package com.taboola.rest.api.internal.interceptors;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import com.taboola.rest.api.model.RequestHeader;
import com.taboola.rest.api.model.RequestHeaders;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created on 10/21/20.
 */
public class HeadersInterceptor implements Interceptor {
    private final Collection<RequestHeader> headers;
    private final RequestHeaders requestHeaders;

    public HeadersInterceptor(Collection<RequestHeader> headers, RequestHeaders requestHeaders) {
        this.headers = headers;
        this.requestHeaders = requestHeaders;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();

        headers.stream()
                .filter(this::validateHeader)
                .forEach(header -> builder.header(header.getName(), header.getValue()));

        if (requestHeaders != null) {
            requestHeaders.get()
                    .entrySet()
                    .stream().filter(this::validateHeader)
                    .forEach(entry -> builder.header(entry.getKey(), entry.getValue()));
        }

        return chain.proceed(builder.build());
    }

    private boolean validateHeader(RequestHeader header) {
        return header.getName() != null && header.getValue() != null;
    }

    private boolean validateHeader(Map.Entry<String, String> entry) {
        return entry.getKey() != null && entry.getValue() != null;
    }
}
