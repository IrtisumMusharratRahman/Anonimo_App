package com.project.anonimo.repository.API.ApiClient;

import com.google.gson.GsonBuilder;
import com.project.anonimo.repository.API.ApiInterface.ApiInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static ApiClient instance = null;
    private ApiInterface apiInterface;

    private ApiClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized ApiClient getInstance() {
        if (instance == null) {
            instance = new ApiClient();
        }
        return instance;
    }

    public ApiInterface getApiInterface() {
        return apiInterface;
    }
}
