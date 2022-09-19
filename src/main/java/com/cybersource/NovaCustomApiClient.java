package com.cybersource;

import Invokers.ApiClient;
import Invokers.ApiException;
import Invokers.ApiResponse;
import Model.AccessTokenResponse;
import com.google.gson.reflect.TypeToken;
import okhttp3.Call;
import okhttp3.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Type;

public class NovaCustomApiClient extends ApiClient {
    private static Logger logger = LogManager.getLogger(NovaCustomApiClient.class);

    @Override
    public <T> ApiResponse<T> execute(Call call, Type returnType) throws ApiException {
        try {
            this.apiRequestMetrics.setComputeTime((System.nanoTime() - this.getComputationStartTime()) / 1000000);
            Response response = call.execute();
            responseCode = String.valueOf(response.code());
            status = response.message();


            if(returnType == new TypeToken< AccessTokenResponse >(){}.getType()) {
                logger.debug("Response :\n" + response.peekBody(Long.MAX_VALUE).string());
            }

            T data = handleResponse(response, returnType);

            if (returnType != null || call.request().method().equalsIgnoreCase("DELETE") || responseCode.equalsIgnoreCase("202")) {
                response.body().close();
            }

            logger.info("HTTP Response Body :\n{}", data);

            return new ApiResponse<T>(response.code(), response.headers().toMultimap(), data);
        } catch (IOException e) {
            logger.error("ApiException : " + e.getMessage());
            throw new ApiException(e);
        }
        catch (NullPointerException e) {
            logger.error("ApiException : " + e.getMessage());
            throw new ApiException(e);
        }
    }

}
