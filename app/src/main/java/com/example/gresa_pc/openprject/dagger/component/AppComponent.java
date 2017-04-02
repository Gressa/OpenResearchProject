package com.example.gresa_pc.openprject.dagger.component;

import com.example.gresa_pc.openprject.MapsActivity;
import com.example.gresa_pc.openprject.dagger.modules.AppModule;
import com.example.gresa_pc.openprject.engine.DirectionFinderEngine;
import com.example.gresa_pc.openprject.engine.ParkingSitesEngine;

import javax.inject.Singleton;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(MapsActivity mainActivity);
    void inject(ParkingSitesEngine parkingSitesEngine);
    void inject(DirectionFinderEngine directionFinderEngine);
}
