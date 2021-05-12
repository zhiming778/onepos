package com.example.onepos.model.api;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PosRetrofit {
    private static Retrofit retrofit;
    private static PosService service;
    private static final String BASE_URL = "https://dev.virtualearth.net/";

    private static Retrofit buildRetrofit() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(ContentValues.class, new RouteDeserializer())
                .registerTypeAdapter(List.class, new SuggestAddressesDeserializer())
                .create();
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static PosService buildService() {
        if (retrofit == null)
            retrofit = buildRetrofit();
        if (service == null)
            service = retrofit.create(PosService.class);
        return service;
    }
}
