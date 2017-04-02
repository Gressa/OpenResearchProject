package com.example.gresa_pc.openprject.dagger.modules;

import android.app.ProgressDialog;
import android.content.Context;
import com.example.gresa_pc.openprject.dagger.App;
import com.example.gresa_pc.openprject.presenter.MapsPresenter;
import com.example.gresa_pc.openprject.service.ApiService;
import com.example.gresa_pc.openprject.service.GoogleApiService;
import com.example.gresa_pc.openprject.ui.view.DirectionFinderContract;
import com.example.gresa_pc.openprject.ui.view.ParkingSitesContract;
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
    ParkingSitesContract provideParkingSitesEngine(ApiService mApiService) {
        return new com.example.gresa_pc.openprject.engine.ParkingSitesEngine(mApiService);
    }

    @Provides
    @Singleton
    DirectionFinderContract provideDirectionFinderEngine(GoogleApiService googleApiService){
        return new com.example.gresa_pc.openprject.engine.DirectionFinderEngine(googleApiService);
    }

    @Provides
    @Singleton
    ProgressDialog provideProgressDialog(){
        return new ProgressDialog(app.getApplicationContext());
    }

//    @Provides
//    @Singleton
//    ParkingSitesPresenter provideParkingSitesPresenter(ParkingSitesContract parkingSitesEngine){
//        return new ParkingSitesPresenter(parkingSitesEngine);
//    }
//
//    @Provides
//    @Singleton
//    DirectionFinderPresenter provideDirectionFinderPresenter(DirectionFinderContract directionFinderEngine){
//        return new DirectionFinderPresenter(directionFinderEngine);
//    }

    @Provides
    @Singleton
    MapsPresenter provideMapPresenter(DirectionFinderContract iDirectionFinderEngine, ParkingSitesContract iParkingSitesEngine){
        return new MapsPresenter(iDirectionFinderEngine,iParkingSitesEngine);
    }

    @Provides
    @Singleton
    public ApiService provideApiService() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.43.53:81/")
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
