package com.taboola.rest.api.exceptions;

import com.taboola.rest.api.model.APIError;

/**
 * Created by vladi
 * Date: 9/13/2017
 * Time: 12:23 AM
 * By Taboola
 */
public abstract class RestAPIException extends RuntimeException {

    private APIError error = APIError.EMPTY;

    public RestAPIException(Throwable cause, String message, Object ... params) {
        super(String.format(message, params), cause);
    }

    public RestAPIException(String message, Object ... params) {
        super(String.format(message, params));
    }

    public RestAPIException(APIError error, String message, Object ... params) {
        super(String.format(message + parseError(error), params));
        this.error = error;
    }

    public RestAPIException(APIError error, String message, Throwable t, Object ... params) {
        super(String.format(message + parseError(error), params), t);
        this.error = error;
    }

    private static String parseError(APIError error) {
        if(error == null || APIError.EMPTY.equals(error)) {
            return "";
        }

        return String.format(". Response payload status [%s], message [%s], offending field [%s], message code [%s]", error.getHttpStatus(), error.getMessage(), error.getOffendingField(), error.getMessageCode());
    }

    public APIError getError() {
        return error;
    }
}
