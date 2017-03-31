package com.example.gresa_pc.openprject.dagger;

import android.app.Application;
import com.example.gresa_pc.openprject.dagger.component.AppComponent;
import com.example.gresa_pc.openprject.dagger.component.DaggerAppComponent;
import com.example.gresa_pc.openprject.dagger.modules.AppModule;

public class App extends Application{
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }
    public AppComponent getAppComponent() {
        return appComponent;
    }
}
