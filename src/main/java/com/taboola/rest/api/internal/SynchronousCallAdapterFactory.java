package com.taboola.rest.api.internal;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.taboola.rest.api.exceptions.RestAPIConnectivityException;
import com.taboola.rest.api.exceptions.RestAPIRequestException;
import com.taboola.rest.api.exceptions.factories.ExceptionFactory;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by vladi
 * Date: 11/19/2017
 * Time: 11:53 PM
 * By Taboola
 */
public class SynchronousCallAdapterFactory extends CallAdapter.Factory {

    private static final Logger logger = LogManager.getLogger(SynchronousCallAdapterFactory.class);
    private static final int UNAUTHORIZED_HTTP_STATUS_CODE = 401;
    private static final int BAD_REQUEST_HTTP_STATUS_CODE = 400;
    private static final int INTERNAL_SERVER_ERROR_HTTP_STATUS_CODE = 500;
    private final ExceptionFactory exceptionFactory;
    private final StringResponseFactories stringResponseFactories;

    public static SynchronousCallAdapterFactory create(ExceptionFactory exceptionFactory,
                                                       StringResponseFactories stringResponseFactories) {
        return new SynchronousCallAdapterFactory(exceptionFactory, stringResponseFactories);
    }

    private SynchronousCallAdapterFactory(ExceptionFactory exceptionFactory,
                                          StringResponseFactories stringResponseFactories) {
        this.exceptionFactory = exceptionFactory;
        this.stringResponseFactories = stringResponseFactories;
    }

    @Override
    public CallAdapter<Object, Object> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (returnType instanceof Call ||
                returnType.toString().contains("retrofit2.Call")) {

            return null;
        }

        return new CallAdapter<Object, Object>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            public Object adapt(Call<Object> call) {
                Object obj = null;
                try {
                    Response<Object> response = call.execute();
                    if (response.isSuccessful()) {
                        if (stringResponseFactories.isExist(returnType)) {
                            obj = stringResponseFactories.getFactory(returnType).handlerResponse(response.headers().toMultimap(), (String) response.body());
                        } else {
                            obj = response.body();
                        }
                    } else {
                        int responseCode = response.code();
                        if (responseCode == UNAUTHORIZED_HTTP_STATUS_CODE) {
                            throwIfNotNull(exceptionFactory.createUnauthorizedException(safeCreateCauseException(response)));

                        } else if (responseCode >= BAD_REQUEST_HTTP_STATUS_CODE && responseCode < INTERNAL_SERVER_ERROR_HTTP_STATUS_CODE) {
                            String message = response.message();
                            throwIfNotNull(exceptionFactory.createRequestException(responseCode, safeGetErrorPayloadBytes(response, message, responseCode), message));
                        }

                        throwIfNotNull(exceptionFactory.createConnectivityException(safeCreateCauseException(response), responseCode));
                    }

                } catch (IOException e) {
                    logger.error(e);
                    throwIfNotNull(exceptionFactory.createConnectivityException(e));
                }

                return obj;
            }
        };
    }

    private byte[] safeGetErrorPayloadBytes(Response<Object> response, String message, int responseCode) throws IOException {
        try {
            return response.errorBody().bytes();
        } catch (Throwable t) {
            logger.warn("Failed to extract byte[] from response error body", t);
            throw new RestAPIRequestException("message: %s, responseCode: %s", MessageHandlingUtils.normalizeErrorMsg(message), responseCode);
        }
    }

    private IOException safeCreateCauseException(Response<Object> response) {
        try {
            return new IOException(response.errorBody().string());
        } catch (Throwable t) {
            logger.warn("Failed to parse API error response", t);
            return new IOException("Failed to parse API error response", t);
        }
    }

    private void throwIfNotNull(RuntimeException e) {
        if (e != null) {
            throw e;
        }
    }
}
