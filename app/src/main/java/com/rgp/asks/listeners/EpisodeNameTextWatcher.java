package com.rgp.asks.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.EpisodeViewModel;

public class EpisodeNameTextWatcher implements TextWatcher {
    private EpisodeViewModel model;

    public EpisodeNameTextWatcher(EpisodeViewModel model) {
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
        Episode e = model.getModifiableEntityCopy();
        e.setEpisode(s.toString());
    }
}
