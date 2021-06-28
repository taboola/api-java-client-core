package com.taboola.rest.api.exceptions.factories;

/**
 * Created by vladi.m
 * Date 28/06/2021
 * Time 12:06
 * Copyright Taboola
 */
public interface ExceptionFactory {

    RuntimeException createUnauthorizedException(Throwable cause);

    RuntimeException createRequestException(int responseCode, byte[] errorBytes, String message);

    RuntimeException createConnectivityException(Throwable cause, int responseCode);

    RuntimeException createConnectivityException(Throwable cause);
}
