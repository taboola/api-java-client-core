package com.taboola.rest.api.exceptions.factories;

/**
 * Created by vladi.m
 * Date 28/06/2021
 * Time 12:06
 * Copyright Taboola
 */
public interface ExceptionFactory {

    void handleAndThrowUnauthorizedException(Throwable cause);

    void handleAndThrowRequestException(int responseCode, byte[] errorBytes, String message);

    void handleAndThrowConnectivityException(Throwable cause, int responseCode);

    void handleAndThrowConnectivityException(Throwable cause);
}
