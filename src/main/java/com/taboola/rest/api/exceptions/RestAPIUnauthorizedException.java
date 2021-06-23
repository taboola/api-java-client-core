package com.taboola.rest.api.exceptions;

/**
 * Created by vladi
 * Date: 10/30/2017
 * Time: 10:51 PM
 * By Taboola
 */
public class RestAPIUnauthorizedException extends RestAPIException {

    private static final String ERROR_STR = "Unauthorized, expired token or invalid credentials";

    public RestAPIUnauthorizedException() {
        super(ERROR_STR);
    }

    public RestAPIUnauthorizedException(Throwable cause) {
        super(cause, ERROR_STR);
    }
}
