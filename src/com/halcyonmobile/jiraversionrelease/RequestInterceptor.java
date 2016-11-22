package com.halcyonmobile.jiraversionrelease;

import com.sun.istack.internal.NotNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

class RequestInterceptor implements Interceptor {

    private static String authBasicKey;

    RequestInterceptor(@NotNull String credentials) {
        authBasicKey = credentials;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = request.newBuilder().addHeader("Authorization", "Basic " + authBasicKey).addHeader("Content-Type", "application/json").build();
        Response response = chain.proceed(newRequest);
        if (!response.isSuccessful()) {
            throw new IOException(response.toString());
        }
        return response;
    }
}
