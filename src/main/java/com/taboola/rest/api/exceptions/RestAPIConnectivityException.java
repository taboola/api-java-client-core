package com.taboola.rest.api.exceptions;


/**
 * Created by vladi
 * Date: 10/30/2017
 * Time: 11:08 PM
 * By Taboola
 */
public class RestAPIConnectivityException extends RestAPIException {

    private static final String ERROR_STR = "Failed to perform API call, might be due to internet connectivity issues";
    private static final String ERROR_STR_WITH_RESPONSE_CODE = "Failed to perform API call with response code [%d], might be due to internet connectivity issues";

    public RestAPIConnectivityException() {
        super(ERROR_STR);
    }

    public RestAPIConnectivityException(Throwable cause) {
        super(cause, ERROR_STR);
    }

    public RestAPIConnectivityException(int responseCode) {
        super(ERROR_STR_WITH_RESPONSE_CODE, responseCode);
    }

    public RestAPIConnectivityException(Throwable cause, int responseCode) {
        super(cause, ERROR_STR_WITH_RESPONSE_CODE, responseCode);
    }
}
