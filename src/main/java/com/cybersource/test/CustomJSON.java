package com.cybersource.test;

import Invokers.ApiClient;
import Invokers.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.util.Objects;

/**
 * This fixes the issue with Serializable exception serializing ResponseBody
 * Extends the JSON class performing the serialization, customise the method if needed or just return null/EmptyString from it seems to fix the
 * error for now.
 */
public class CustomJSON extends JSON {
    /**
     * JSON constructor.
     *
     * @param apiClient An instance of ApiClient
     */

    private Gson gson;
    public CustomJSON(ApiClient apiClient) {
        super(apiClient);
        gson = new GsonBuilder().create();    }

    public String serialize(Object obj) {
//        try {
//           return obj == null ? Strings.EMPTY : ((ResponseBody)obj).string(); // Stream close using this?
//        } catch (IOException e) {
//            return null;
//          //  throw new RuntimeException(e);
//        }
        return Strings.EMPTY;
    }
}
