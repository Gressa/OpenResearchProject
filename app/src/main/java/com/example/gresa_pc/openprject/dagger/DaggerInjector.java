package com.example.gresa_pc.openprject.dagger;

import android.app.Application;

import com.example.gresa_pc.openprject.dagger.component.AppComponent;
import com.example.gresa_pc.openprject.dagger.component.DaggerAppComponent;

public class DaggerInjector extends Application{
   // private static AppComponent appComponent = DaggerAppComponent.builder().build();
    AppComponent appComponent;
    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().build();
    }
    public AppComponent getAppComponent() {
        return appComponent;
    }
}
