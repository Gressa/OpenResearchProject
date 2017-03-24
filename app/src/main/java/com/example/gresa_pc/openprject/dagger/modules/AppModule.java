package com.example.gresa_pc.openprject.dagger.modules;

import com.example.gresa_pc.openprject.service.ApiService;
import com.example.gresa_pc.openprject.presenter.ParkingSitesEngine;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    @Provides
    @Singleton
    ParkingSitesEngine provideParkingSitesEngine() { return new ParkingSitesEngine(apiService()); }

    @Provides
    @Singleton
    public ApiService apiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.163:81/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);
        return  service;
    }
}
