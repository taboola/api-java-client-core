package com.taboola.rest.api.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taboola.rest.api.exceptions.RestAPIConnectivityException;
import com.taboola.rest.api.exceptions.RestAPIRequestException;
import com.taboola.rest.api.exceptions.RestAPIUnauthorizedException;
import com.taboola.rest.api.exceptions.factories.ExceptionFactory;
import com.taboola.rest.api.model.APIError;

import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by vladi
 * Date: 11/19/2017
 * Time: 11:53 PM
 * By Taboola
 */
public class SynchronousCallAdapterFactory  extends CallAdapter.Factory {

    private static final Logger logger = LogManager.getLogger(SynchronousCallAdapterFactory.class);
    private static final int UNAUTHORIZED_HTTP_STATUS_CODE = 401;
    private static final int BAD_REQUEST_HTTP_STATUS_CODE = 400;
    private static final int INTERNAL_SERVER_ERROR_HTTP_STATUS_CODE = 500;

    private final ObjectMapper objectMapper;
    private final ExceptionFactory exceptionFactory;

    public static SynchronousCallAdapterFactory create(ObjectMapper objectMapper, ExceptionFactory exceptionFactory) {
        return new SynchronousCallAdapterFactory(objectMapper, exceptionFactory);
    }

    private SynchronousCallAdapterFactory(ObjectMapper objectMapper, ExceptionFactory exceptionFactory) {
        this.objectMapper = objectMapper;
        this.exceptionFactory = exceptionFactory;
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
                    if(response.isSuccessful()) {
                        obj = response.body();
                    } else {
                        int responseCode = response.code();
                        if(responseCode == UNAUTHORIZED_HTTP_STATUS_CODE) {
                            exceptionFactory.handleAndThrowUnauthorizedException(safeCreateCauseException(response));

                        } else if(responseCode >= BAD_REQUEST_HTTP_STATUS_CODE && responseCode < INTERNAL_SERVER_ERROR_HTTP_STATUS_CODE) {
                            exceptionFactory.handleAndThrowRequestException(responseCode, normalizeError(parseError(response)));
                        }

                        exceptionFactory.handleAndThrowConnectivityException(safeCreateCauseException(response), responseCode);
                    }

                } catch (IOException e) {
                    logger.error(e);
                    exceptionFactory.handleAndThrowConnectivityException(e);
                }

                if(obj == null) {
                    throw new RestAPIConnectivityException();
                }

                return obj;
            }
        };
    }

    private IOException safeCreateCauseException(Response<Object> response) {
        try {
            return new IOException(response.errorBody().string());
        } catch (Throwable t) {
            logger.warn("Failed to parse API error response", t);
            return new IOException("Failed to parse API error response", t);
        }
    }

    private APIError parseError(Response errorResponse) {
        ResponseBody errorBody = errorResponse.errorBody();
        try {
            return objectMapper.readValue(errorBody.bytes(), APIError.class);
        } catch (Throwable e) {
            logger.warn("Failed to parse API error response object [{}]", errorResponse.message());
            return new APIError(errorResponse.message(), errorResponse.code());
        }
    }

    APIError normalizeError(APIError error) {
        String message = error.getMessage();
        if(message != null) {
            error.setMessage(message.replaceAll("%", "%%"));
        }

        return error;
    }
}
