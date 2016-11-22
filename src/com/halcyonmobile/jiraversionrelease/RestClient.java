package com.halcyonmobile.jiraversionrelease;

import com.google.gson.GsonBuilder;
import com.sun.istack.internal.NotNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

class RestClient {

    private static final int CONNECTION_TIMEOUT = 15;
    private static final int READ_TIMEOUT = 20;
    private static RestClient sRestClient;
    private Retrofit mRetrofit;

    private RestClient(@NotNull String baseUrl,@NotNull String credentials) {

        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new RequestInterceptor(credentials))
                .build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()))
                .client(client)
                .build();
    }

    static <T> T getService(@NotNull Class<T> serviceClass,@NotNull String baseUrl,@NotNull String credentials) {
        if (sRestClient == null) {
            sRestClient = new RestClient("https://" + baseUrl + "/rest/api/latest/", credentials);
        }
        return sRestClient.mRetrofit.create(serviceClass);
    }
}
