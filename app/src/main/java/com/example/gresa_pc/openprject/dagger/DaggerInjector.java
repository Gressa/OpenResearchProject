package com.example.gresa_pc.openprject.dagger;

import com.example.gresa_pc.openprject.dagger.component.AppComponent;
import com.example.gresa_pc.openprject.dagger.component.DaggerAppComponent;

public class DaggerInjector {
    private static AppComponent appComponent = DaggerAppComponent.builder().build();

    public static AppComponent get() {
        return appComponent;
    }
}
