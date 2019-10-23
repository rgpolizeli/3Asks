package com.rgp.asks.listeners;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class EpisodeDateOnDateSetDialogListener implements DatePickerDialog.OnDateSetListener {
    private TextView episodeDateTextView;

    EpisodeDateOnDateSetDialogListener(TextView episodeDateTextView) {
        this.episodeDateTextView = episodeDateTextView;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        episodeDateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
    }
}
