package com.taboola.rest.api.exceptions.factories;

import com.taboola.rest.api.exceptions.RestAPIConnectivityException;
import com.taboola.rest.api.exceptions.RestAPIRequestException;
import com.taboola.rest.api.exceptions.RestAPIUnauthorizedException;
import com.taboola.rest.api.internal.MessageHandlingUtils;

/**
 * Created by vladi.m
 * Date 28/06/2021
 * Time 12:10
 * Copyright Taboola
 */
public class DefaultExceptionFactory implements ExceptionFactory {

    @Override
    public RuntimeException createUnauthorizedException(Throwable cause) {
        return new RestAPIUnauthorizedException(cause);
    }

    @Override
    public RuntimeException createRequestException(int responseCode, byte[] errorPayloadBytes, String message) {
        return new RestAPIRequestException("message: %s, responseCode: %s", MessageHandlingUtils.normalizeErrorMsg(message), responseCode);
    }

    @Override
    public RuntimeException createConnectivityException(Throwable cause, int responseCode) {
        return new RestAPIConnectivityException(cause, responseCode);
    }

    @Override
    public RuntimeException createConnectivityException(Throwable cause) {
        return new RestAPIConnectivityException(cause);
    }
}
