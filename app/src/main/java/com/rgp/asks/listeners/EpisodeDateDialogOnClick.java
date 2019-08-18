package com.rgp.asks.listeners;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;

public class EpisodeDateDialogOnClick implements View.OnClickListener {
    private EpisodeDateOnDateSetDialogListener episodeDateOnDateSetDialogListener;
    private DatePickerDialog datePickerDialog;

    public EpisodeDateDialogOnClick(TextView episodeDateTextView) {
        this.episodeDateOnDateSetDialogListener = new EpisodeDateOnDateSetDialogListener(episodeDateTextView);
    }

    @Override
    public void onClick(View v) {
        if (v.isShown()) {
            final Calendar c = Calendar.getInstance();
            this.datePickerDialog = new DatePickerDialog(v.getContext(), this.episodeDateOnDateSetDialogListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            this.datePickerDialog.show();
        }
    }
}
