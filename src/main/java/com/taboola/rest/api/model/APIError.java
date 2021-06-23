package com.taboola.rest.api.model;

/**
 * Created by vladi
 * Date: 10/12/2017
 * Time: 10:03 PM
 * By Taboola
 */
public class APIError {

    public static final APIError EMPTY = new APIError();

    private int httpStatus;
    private String message;
    private String offendingField;
    private String messageCode;

    public APIError() {
        //for jackson
    }

    public APIError(String message, int httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public APIError(String message, int httpStatus, String offendingField, String messageCode) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.offendingField = offendingField;
        this.messageCode = messageCode;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOffendingField() {
        return offendingField;
    }

    public void setOffendingField(String offendingField) {
        this.offendingField = offendingField;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    @Override
    public String toString() {
        return "APIError{" +
                "httpStatus=" + httpStatus +
                ", message='" + message + '\'' +
                ", offendingField='" + offendingField + '\'' +
                ", messageCode='" + messageCode + '\'' +
                '}';
    }
}
