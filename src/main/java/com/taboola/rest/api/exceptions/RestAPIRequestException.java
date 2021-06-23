package com.taboola.rest.api.exceptions;

import com.taboola.rest.api.model.APIError;

/**
 * Created by vladi
 * Date: 10/30/2017
 * Time: 11:12 PM
 * By Taboola
 */
public class RestAPIRequestException extends RestAPIException {

    public RestAPIRequestException(String message, Object ... params) {
        super(message, params);
    }

    public RestAPIRequestException(int responseCode, APIError error) {
        super(error, "Failed to perform API call with response code [%d]", responseCode);
    }
}
