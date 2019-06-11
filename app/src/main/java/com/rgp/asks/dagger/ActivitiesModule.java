package com.rgp.asks.dagger;

import com.rgp.asks.activities.AddNewBeliefActivity;
import com.rgp.asks.activities.AsksActivity;
import com.rgp.asks.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivitiesModule {

    @ContributesAndroidInjector(modules = {RecyclerViewModule.class})
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector
    abstract AsksActivity contributeAsksActivityInjector();

    @ContributesAndroidInjector
    abstract AddNewBeliefActivity contributeAddNewBeliefActivityInjector();
}
