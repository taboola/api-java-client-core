package com.taboola.rest.api.internal;

/**
 * Created by vladi.m
 * Date 28/06/2021
 * Time 16:52
 * Copyright Taboola
 */
public class MessageHandlingUtils {

    public static String normalizeErrorMsg(String message) {
        if(message != null) {
            return message.replaceAll("%", "%%");
        }

        return "";
    }
}
