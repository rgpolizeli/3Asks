package com.rpolizeli.asks.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import com.rpolizeli.asks.persistence.entity.Episode;
import com.rpolizeli.asks.viewmodel.EpisodeViewModel;

public class EpisodeTextWatcher implements TextWatcher {
    private EpisodeViewModel model;

    public EpisodeTextWatcher(EpisodeViewModel model){
        this.model = model;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        Episode e = model.getModifiableEpisodeCopy();
        e.setEpisode(s.toString());
    }
}
