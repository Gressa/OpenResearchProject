package com.example.gresa_pc.openprject.dagger.modules;

import android.content.Context;

import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.engine.DirectionFinderEngine;
import com.example.gresa_pc.openprject.engine.ParkingSitesEngine;
import com.example.gresa_pc.openprject.presenter.MapsPresenter;
import com.example.gresa_pc.openprject.service.ApiService;
import com.example.gresa_pc.openprject.service.GoogleApiService;
import com.example.gresa_pc.openprject.engine.DirectionFinderContract;
import com.example.gresa_pc.openprject.engine.ParkingSitesContract;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    ParkingSitesContract provideParkingSitesEngine(ApiService mApiService) {
        return new ParkingSitesEngine(mApiService);
    }

    @Provides
    @Singleton
    DirectionFinderContract provideDirectionFinderEngine(GoogleApiService googleApiService) {
        return new DirectionFinderEngine(googleApiService);
    }

    @Provides
    @Singleton
    MapsPresenter provideMapPresenter(DirectionFinderContract directionFinderContract, ParkingSitesContract parkingSitesContract) {
        return new MapsPresenter(directionFinderContract, parkingSitesContract);
    }

    @Provides
    @Singleton
    public ApiService provideApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.163:81/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        return apiService;
    }


    @Provides
    @Singleton
    public GoogleApiService provideGoogleApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GoogleApiService googleApiService = retrofit.create(GoogleApiService.class);
        return googleApiService;
    }
}
