package com.taboola.rest.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taboola.rest.api.exceptions.factories.DefaultExceptionFactory;
import com.taboola.rest.api.exceptions.factories.ExceptionFactory;
import com.taboola.rest.api.internal.CommunicationFactory;
import com.taboola.rest.api.internal.StringResponseFactories;
import com.taboola.rest.api.internal.config.CommunicationConfig;
import com.taboola.rest.api.internal.config.SerializationConfig;
import com.taboola.rest.api.internal.config.UserAgentHeader;
import com.taboola.rest.api.internal.serialization.SerializationMapperCreator;
import com.taboola.rest.api.model.RequestHeader;
import com.taboola.rest.api.model.StringResponseFactory;

/**
 * Created by vladi.m
 * Date 23/06/2021
 * Time 16:42
 * Copyright Taboola
 */
public class RestAPIClient {

    private static final Logger logger = LogManager.getLogger(RestAPIClient.class);

    private final CommunicationFactory communicator;

    private RestAPIClient(CommunicationFactory communicator) {
        this.communicator = communicator;
    }

    public <E> E createRetrofitEndpoint(Class<E> endpointClazz) {
        Objects.requireNonNull(endpointClazz, "clazz");
        logger.debug("creating endpoint using retrofit for class [{}]", endpointClazz::toString);
        return communicator.createRetrofitEndpoint(endpointClazz);
    }

    public static RestAPIClientBuilder builder() {
        return new RestAPIClientBuilder();
    }

    public static class RestAPIClientBuilder {
        private static final String VERSION = "1.0.1";
        private static final Integer DEFAULT_MAX_IDLE_CONNECTIONS = 5;
        private static final Long DEFAULT_KEEP_ALIVE_DURATION_MILLIS = 300_000L;
        private static final SerializationConfig DEFAULT_SERIALIZATION_CONFIG = new SerializationConfig();
        private static final String DEFAULT_REST_API_VERSION = "UNDEFINED";
        private static final String DEFAULT_USER_AGENT_SUFFIX = "UNDEFINED";
        private static final String DEFAULT_USER_AGENT_PREFIX = "UNDEFINED";
        private static final ExceptionFactory DEFAULT_EXCEPTION_FACTORY = new DefaultExceptionFactory();

        private String baseUrl;
        private Long writeTimeoutMillis;
        private Long connectionTimeoutMillis;
        private Long readTimeoutMillis;
        private Integer maxIdleConnections;
        private Long keepAliveDurationMillis;
        private Boolean debug;
        private SerializationConfig serializationConfig;
        private Collection<RequestHeader> headers;
        private String userAgentPrefix;
        private String userAgentSuffix;
        private String restAPIVersion;
        private ExceptionFactory exceptionFactory;
        private ObjectMapper objectMapper;
        private final StringResponseFactories stringResponseFactories = new StringResponseFactories();

        public RestAPIClientBuilder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public RestAPIClientBuilder setUserAgentSuffix(String userAgentSuffix) {
            this.userAgentSuffix = userAgentSuffix;
            return this;
        }

        public RestAPIClientBuilder setUserAgentPrefix(String userAgentPrefix) {
            this.userAgentPrefix = userAgentPrefix;
            return this;
        }

        public RestAPIClientBuilder setConnectionTimeoutMillis(Long connectionTimeoutMillis) {
            this.connectionTimeoutMillis = connectionTimeoutMillis;
            return this;
        }

        public RestAPIClientBuilder setReadTimeoutMillis(Long readTimeoutMillis) {
            this.readTimeoutMillis = readTimeoutMillis;
            return this;
        }

        public RestAPIClientBuilder setWriteTimeoutMillis(Long writeTimeoutMillis) {
            this.writeTimeoutMillis = writeTimeoutMillis;
            return this;
        }

        public RestAPIClientBuilder setMaxIdleConnections(Integer maxIdleConnections) {
            this.maxIdleConnections = maxIdleConnections;
            return this;
        }

        public RestAPIClientBuilder setKeepAliveDurationMillis(Long keepAliveDurationMillis) {
            this.keepAliveDurationMillis = keepAliveDurationMillis;
            return this;
        }

        public RestAPIClientBuilder setDebug(Boolean debug) {
            this.debug = debug;
            return this;
        }

        public RestAPIClientBuilder setSerializationConfig(SerializationConfig serializationConfig) {
            this.serializationConfig = serializationConfig;
            return this;
        }

        public RestAPIClientBuilder setHeaders(Collection<RequestHeader> headers) {
            this.headers = headers;
            return this;
        }

        public RestAPIClientBuilder setAPIVersion(String restAPIVersion) {
            this.restAPIVersion = restAPIVersion;
            return this;
        }

        public RestAPIClientBuilder setExceptionFactory(ExceptionFactory exceptionFactory) {
            this.exceptionFactory = exceptionFactory;
            return this;
        }

        public RestAPIClientBuilder setObjectMapper(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
            return this;
        }

        public RestAPIClientBuilder addStringBodyResponseFactory(Class<?> clazz, StringResponseFactory stringResponseFactory) {
            stringResponseFactories.addFactory(clazz, stringResponseFactory);
            return this;
        }

        public RestAPIClient build() {
            organizeState();
            String finalUserAgent = String.format("%s/%s/%s (%s)", userAgentPrefix, restAPIVersion, VERSION, userAgentSuffix);
            Collection<RequestHeader> headers = getAllHeaders(this.headers, finalUserAgent);
            CommunicationConfig config = new CommunicationConfig(baseUrl, connectionTimeoutMillis, readTimeoutMillis, writeTimeoutMillis, maxIdleConnections,
                    keepAliveDurationMillis, headers, debug, exceptionFactory, objectMapper, stringResponseFactories);
            return new RestAPIClient(new CommunicationFactory(config));
        }

        private Collection<RequestHeader> getAllHeaders(Collection<RequestHeader> clientHeaders, String finalUserAgent) {
            List<RequestHeader> headers = new ArrayList<>();
            if (clientHeaders != null) {
                headers.addAll(clientHeaders.stream().
                        filter(Objects::nonNull)
                        .collect(Collectors.toList()));
            }
            headers.add(new UserAgentHeader(finalUserAgent));
            return headers;
        }

        private void organizeState() {
            if (baseUrl == null) {
                throw new IllegalStateException("Missing base url");
            }

            if (connectionTimeoutMillis == null) {
                connectionTimeoutMillis = 0L;
            }

            if (readTimeoutMillis == null) {
                readTimeoutMillis = 0L;
            }

            if (writeTimeoutMillis == null) {
                writeTimeoutMillis = 0L;
            }

            if (maxIdleConnections == null) {
                maxIdleConnections = DEFAULT_MAX_IDLE_CONNECTIONS;
            }

            if (keepAliveDurationMillis == null) {
                keepAliveDurationMillis = DEFAULT_KEEP_ALIVE_DURATION_MILLIS;
            }

            if (userAgentSuffix == null) {
                userAgentSuffix = DEFAULT_USER_AGENT_SUFFIX;
            }

            if (userAgentPrefix == null) {
                userAgentPrefix = DEFAULT_USER_AGENT_PREFIX;
            }

            if (restAPIVersion == null) {
                restAPIVersion = DEFAULT_REST_API_VERSION;
            }

            if (debug == null) {
                debug = false;
            }

            if (serializationConfig == null) {
                serializationConfig = DEFAULT_SERIALIZATION_CONFIG;
            }

            if (exceptionFactory == null) {
                exceptionFactory = DEFAULT_EXCEPTION_FACTORY;
            }

            if (objectMapper == null) {
                objectMapper = SerializationMapperCreator.createObjectMapper(serializationConfig);
            }
        }
    }


}
