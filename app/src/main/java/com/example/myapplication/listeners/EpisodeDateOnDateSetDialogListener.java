package com.example.myapplication.listeners;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.myapplication.persistence.entity.Episode;
import com.example.myapplication.viewmodel.EpisodeViewModel;

import java.text.DateFormat;
import java.util.Calendar;

public class EpisodeDateOnDateSetDialogListener implements DatePickerDialog.OnDateSetListener {
    EditText episodeDateEditText;

    EpisodeDateOnDateSetDialogListener(EditText episodeDateEditText){
        this.episodeDateEditText = episodeDateEditText;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year,month,dayOfMonth);
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime()));
    }
}
