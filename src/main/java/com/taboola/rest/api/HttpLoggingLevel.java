package com.taboola.rest.api;

import okhttp3.logging.HttpLoggingInterceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum HttpLoggingLevel {

    NONE,
    BASIC,
    HEADERS,
    BODY;

    private static final Logger logger = LogManager.getLogger(HttpLoggingLevel.class);

    public static HttpLoggingInterceptor.Level getHttpLoggingInterceptorLevel(HttpLoggingLevel httpLoggingLevel) {
        switch (httpLoggingLevel) {
            case NONE:
                return HttpLoggingInterceptor.Level.NONE;
            case BASIC:
                return  HttpLoggingInterceptor.Level.BASIC;
            case HEADERS:
                return HttpLoggingInterceptor.Level.HEADERS;
            case BODY:
                return  HttpLoggingInterceptor.Level.BODY;
            default:
                logger.error("Getting unknown HttpLoggingLevel [{}]", httpLoggingLevel.name());
                return  HttpLoggingInterceptor.Level.BASIC;
        }
    }
}
