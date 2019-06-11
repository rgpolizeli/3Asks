package com.rgp.asks.dagger;

import com.rgp.asks.AsksApplication;

import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Component(
        modules = {
                AndroidInjectionModule.class,
                ActivitiesModule.class
        }
)
public interface AsksApplicationComponent {
    void inject(AsksApplication application);
}
