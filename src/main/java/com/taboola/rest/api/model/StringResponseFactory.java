package com.taboola.rest.api.model;

import java.util.List;
import java.util.Map;

/**
 * Created by vladi.m
 * Date 14/07/2021
 * Time 09
 * Copyright Taboola
 */
public interface StringResponseFactory {

    Object handlerResponse(Map<String, List<String>> responseHeaders, String responsePayload);
}
