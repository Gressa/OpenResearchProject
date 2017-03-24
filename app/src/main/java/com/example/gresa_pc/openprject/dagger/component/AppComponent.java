package com.example.gresa_pc.openprject.dagger.component;

import com.example.gresa_pc.openprject.MapsActivity;
import com.example.gresa_pc.openprject.dagger.modules.AppModule;
import javax.inject.Singleton;
import dagger.Component;


@Component(modules = {AppModule.class})
@Singleton
public interface AppComponent {
    void inject(MapsActivity activity);
}
