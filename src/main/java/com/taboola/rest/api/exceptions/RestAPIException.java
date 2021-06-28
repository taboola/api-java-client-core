package com.taboola.rest.api.exceptions;

/**
 * Created by vladi
 * Date: 9/13/2017
 * Time: 12:23 AM
 * By Taboola
 */
public abstract class RestAPIException extends RuntimeException {

    public RestAPIException(Throwable cause, String message, Object ... params) {
        super(String.format(message, params), cause);
    }

    public RestAPIException(String message, Object ... params) {
        super(String.format(message, params));
    }
}
