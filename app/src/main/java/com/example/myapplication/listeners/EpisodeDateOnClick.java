package com.example.myapplication.listeners;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.viewmodel.EpisodeViewModel;

import java.util.Calendar;

public class EpisodeDateOnClick implements View.OnClickListener {
    EpisodeViewModel model;
    EditText episodeDateEditText;

    public EpisodeDateOnClick(EpisodeViewModel model, EditText episodeDateEditText){
        this.model = model;
        this.episodeDateEditText = episodeDateEditText;
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new EpisodeDateOnDateSetListener(this.model,this.episodeDateEditText), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
