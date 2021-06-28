package com.taboola.rest.api.exceptions.factories;

import com.taboola.rest.api.exceptions.RestAPIConnectivityException;
import com.taboola.rest.api.exceptions.RestAPIRequestException;
import com.taboola.rest.api.exceptions.RestAPIUnauthorizedException;

/**
 * Created by vladi.m
 * Date 28/06/2021
 * Time 12:10
 * Copyright Taboola
 */
public class DefaultExceptionFactory implements ExceptionFactory {

    @Override
    public void handleAndThrowUnauthorizedException(Throwable cause) {
        throw new RestAPIUnauthorizedException(cause);
    }

    @Override
    public void handleAndThrowRequestException(int responseCode, byte[] errorPayloadBytes, String message) {
        throw new RestAPIRequestException("message: %s, responseCode: %s", message, responseCode);
    }

    @Override
    public void handleAndThrowConnectivityException(Throwable cause, int responseCode) {
        throw new RestAPIConnectivityException(cause, responseCode);
    }

    @Override
    public void handleAndThrowConnectivityException(Throwable cause) {
        throw new RestAPIConnectivityException(cause);
    }
}
