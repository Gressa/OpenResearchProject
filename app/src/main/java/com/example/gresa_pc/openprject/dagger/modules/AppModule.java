package com.example.gresa_pc.openprject.dagger.modules;

import android.content.Context;
import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.presenter.ParkingSitesPresenter;
import com.example.gresa_pc.openprject.service.ApiService;
import com.example.gresa_pc.openprject.engine.ParkingSitesEngine;
import com.example.gresa_pc.openprject.service.GoogleApiService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule{
    private App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides @Singleton
    public Context provideContext() {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    ParkingSitesEngine provideParkingSitesEngine(ApiService mApiService) {
        return new ParkingSitesEngine(mApiService);
    }

    @Provides
    @Singleton
    ParkingSitesPresenter provideParkingSitesPresenter(ParkingSitesEngine parkingSitesEngine){
        return new ParkingSitesPresenter(parkingSitesEngine);
    }

//    @Provides
//    @Singleton
//    DirectionFinderEngine provideDirectionFinderEngine(GoogleApiService googleApiService){
//        return new DirectionFinderEngine(googleApiService);
//    }
//
//    @Provides
//    @Singleton
//    DirectionFinderPresenter provideDirectionFinderPresenter(DirectionFinderEngine directionFinderEngine){
//        return new DirectionFinderPresenter(directionFinderEngine);
//    }

    @Provides
    @Singleton
    public ApiService provideApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.163:81/")
//                .baseUrl("http://192.168.1.109:81/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiService service = retrofit.create(ApiService.class);
        return  service;
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

        GoogleApiService service = retrofit.create(GoogleApiService.class);
        return  service;
    }
}
