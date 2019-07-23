package com.rgp.asks.listeners;

import android.app.DatePickerDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.rgp.asks.viewmodel.EpisodeViewModel;

import java.util.Calendar;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EpisodeDateOnClick implements View.OnClickListener {
    private EpisodeViewModel model;
    private EditText episodeDateEditText;

    public EpisodeDateOnClick(EpisodeViewModel model, EditText episodeDateEditText) {
        this.model = model;
        this.episodeDateEditText = episodeDateEditText;
    }

    @Override
    public void onClick(View v) {

        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        final Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(v.getContext(), new EpisodeDateOnDateSetListener(this.model, this.episodeDateEditText), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }
}
