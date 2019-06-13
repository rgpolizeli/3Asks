package com.rgp.asks.listeners;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import com.rgp.asks.persistence.entity.Episode;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import java.text.DateFormat;
import java.util.Calendar;

public class EpisodeDateOnDateSetListener implements DatePickerDialog.OnDateSetListener {
    private EpisodeViewModel model;
    private EditText episodeDateEditText;

    EpisodeDateOnDateSetListener(EpisodeViewModel model, EditText episodeDateEditText) {
        this.model = model;
        this.episodeDateEditText = episodeDateEditText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Episode e = model.getModifiableEpisodeCopy();
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        e.setDate(c.getTime());
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
    }
}
