package com.rgp.asks.listeners;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.Calendar;

public class EpisodeDateOnDateSetDialogListener implements DatePickerDialog.OnDateSetListener {
    private EditText episodeDateEditText;

    EpisodeDateOnDateSetDialogListener(EditText episodeDateEditText){
        this.episodeDateEditText = episodeDateEditText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year,month,dayOfMonth);
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
    }
}
