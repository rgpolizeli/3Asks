package com.rgp.asks.listeners;

import android.view.View;
import android.widget.AdapterView;

import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.EpisodeViewModel;

public class OnItemSelectedListenerEpisodePeriod implements AdapterView.OnItemSelectedListener {
    private EpisodeViewModel model;

    public OnItemSelectedListenerEpisodePeriod(EpisodeViewModel model) {
        this.model = model;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String period = parent.getItemAtPosition(position).toString();
        Episode e = model.getModifiableEntityCopy();
        e.setPeriod(period);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
