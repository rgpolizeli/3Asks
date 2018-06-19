package com.example.myapplication.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.EpisodeViewModel;

public class EpisodeDescriptionTextWatcher implements TextWatcher {
    private EpisodeViewModel model;

    public EpisodeDescriptionTextWatcher(EpisodeViewModel model){
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
        e.setDescription(s.toString());
    }
}
