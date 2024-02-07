package com.taboola.rest.api.model;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by vladi.m
 * Date 02/02/2024
 * Time 10:35
 * Copyright Taboola
 */
public class NoOpCommunicationInterceptor implements CommunicationInterceptor{
    @Override
    public void before(Request request) {}

    @Override
    public void after(Request request, Response response) {}
}
