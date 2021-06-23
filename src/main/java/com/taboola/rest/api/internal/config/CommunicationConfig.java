package com.taboola.rest.api.internal.config;

import java.util.Collection;

import com.taboola.rest.api.model.RequestHeader;

/**
 * Created by vladi
 * Date: 1/16/2018
 * Time: 9:40 PM
 * By Taboola
 */
public class CommunicationConfig {

    private final String baseUrl;
    private final long connectionTimeoutMillis;
    private final long readTimeoutMillis;
    private final long writeTimeoutMillis;
    private final int maxIdleConnections;
    private final long keepAliveDurationMillis;
    private final boolean debug;
    private final Collection<RequestHeader> headers;

    public CommunicationConfig(String baseUrl, Long connectionTimeoutMillis, Long readTimeoutMillis,
                               Long writeTimeoutMillis, Integer maxIdleConnections, Long keepAliveDurationMillis, Collection<RequestHeader> headers, boolean debug) {
        this.baseUrl = baseUrl;
        this.connectionTimeoutMillis = connectionTimeoutMillis;
        this.readTimeoutMillis = readTimeoutMillis;
        this.writeTimeoutMillis = writeTimeoutMillis;
        this.maxIdleConnections = maxIdleConnections;
        this.keepAliveDurationMillis = keepAliveDurationMillis;
        this.headers = headers;
        this.debug = debug;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public long getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    public long getReadTimeoutMillis() {
        return readTimeoutMillis;
    }

    public long getWriteTimeoutMillis() {
        return writeTimeoutMillis;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public long getKeepAliveDurationMillis() {
        return keepAliveDurationMillis;
    }

    public Collection<RequestHeader> getHeaders(){
        return headers;
    }

    public boolean isDebug() {
        return debug;
    }

    @Override
    public String toString() {
        return "CommunicationConfig{" +
        "baseUrl='" + baseUrl + '\'' +
        ", connectionTimeoutMillis=" + connectionTimeoutMillis +
        ", readTimeoutMillis=" + readTimeoutMillis +
        ", writeTimeoutMillis=" + writeTimeoutMillis +
        ", maxIdleConnections=" + maxIdleConnections +
        ", keepAliveDurationMillis=" + keepAliveDurationMillis +
        ", headers='" + headers + '\'' +
        ", debug=" + debug +
        '}';
    }
}
