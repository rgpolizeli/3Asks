package com.rpolizeli.asks.listeners;

import android.view.View;
import android.widget.AdapterView;

import com.rpolizeli.asks.persistence.entity.Episode;
import com.rpolizeli.asks.viewmodel.EpisodeViewModel;

public class OnItemSelectedListenerEpisodePeriod implements AdapterView.OnItemSelectedListener {
    private EpisodeViewModel model;

    public OnItemSelectedListenerEpisodePeriod(EpisodeViewModel model){
        this.model = model;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String period = parent.getItemAtPosition(position).toString();
        Episode e = model.getModifiableEpisodeCopy();
        e.setPeriod(period);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
