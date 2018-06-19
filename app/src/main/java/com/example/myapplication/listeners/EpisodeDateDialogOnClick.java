package com.example.myapplication.listeners;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.EditText;

import com.example.myapplication.viewmodel.EpisodeViewModel;

import java.util.Calendar;

public class EpisodeDateDialogOnClick implements View.OnClickListener {
    EditText episodeDateEditText;

    public EpisodeDateDialogOnClick(EditText episodeDateEditText){
        this.episodeDateEditText = episodeDateEditText;
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new EpisodeDateOnDateSetDialogListener(this.episodeDateEditText), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
