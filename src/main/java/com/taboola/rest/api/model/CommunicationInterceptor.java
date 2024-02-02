package com.taboola.rest.api.model;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vladi.m
 * Date 02/02/2024
 * Time 10:01
 * Copyright Taboola
 */
public interface CommunicationInterceptor {

    void before(final Request request);

    void after(final Request request, final Response response);
}
