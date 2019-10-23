package com.rgp.asks.listeners;

import android.text.Editable;
import android.text.TextWatcher;

import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class EpisodeDateTextWatcher implements TextWatcher {
    private EpisodeViewModel model;

    public EpisodeDateTextWatcher(EpisodeViewModel model) {
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
        Date date;
        try {
            date = DateFormat.getDateInstance(DateFormat.SHORT).parse(s.toString());
        } catch (ParseException e1) {
            date = new Date();
        }
        e.setDate(date);
    }
}
