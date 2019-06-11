package com.rgp.asks.dialogs;

import android.content.Context;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.listeners.EpisodeDateDialogOnClick;

import java.text.DateFormat;
import java.util.Calendar;

public class NewEpisodeDialog {

    @Nullable private View newEpisodeDialogView;
    @Nullable private AlertDialog alertDialog;
    @Nullable private View.OnClickListener positiveButtonOnClickListener;
    @Nullable private View.OnClickListener negativeButtonOnClickListener;

    public void createNewEpisodeDialogView(@NonNull LayoutInflater inflater, int layoutId) throws InflateException {
        this.newEpisodeDialogView = inflater.inflate(layoutId, null);
    }

    /**
     * Create and set listeners to date input.
     * @throws NullPointerException
     */
    public void setupDateOnClickListener() throws NullPointerException{
        TextInputEditText episodeDateEditText = newEpisodeDialogView.findViewById(R.id.episodeDateEditText);
        episodeDateEditText.setOnClickListener(new EpisodeDateDialogOnClick(episodeDateEditText));
        Calendar c = Calendar.getInstance();
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(c.getTime()));
    }

    /**
     * Create an {@link AlertDialog} and {@link View.OnClickListener}s to positive and negative buttons.
     * It is necessary to configure listeners with function getButton().setOnClickListener() because the function setPositiveButton dismiss the alertDialog when clicked even an input error occurs.
     * @param context
     * @param controller
     */

    public void createAlertDialog(@NonNull Context context, @NonNull final I_EpisodeCreatorController controller) {

        this.positiveButtonOnClickListener = createAlertDialogPositiveButtonListener(context,controller);
        this.negativeButtonOnClickListener = createAlertDialogNegativeButtonListener(context);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(newEpisodeDialogView)
                .setPositiveButton(context.getString(R.string.new_episode_dialog_positive_button_label), null)
                .setNegativeButton(context.getString(R.string.new_episode_dialog_negative_button_label), null)
                .setTitle(context.getString(R.string.new_episode_dialog_title_label));
        this.alertDialog = builder.create();

        this.alertDialog.setOnShowListener(dialog -> {
            this.alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this.positiveButtonOnClickListener);
            this.alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(this.negativeButtonOnClickListener);
        });
    }

    private View.OnClickListener createAlertDialogPositiveButtonListener(@NonNull Context context, @NonNull final I_EpisodeCreatorController controller) throws NullPointerException{
        return v -> {
            TextInputEditText episodeNameInputEditText = NewEpisodeDialog.this.newEpisodeDialogView.findViewById(R.id.episodeEditText);
            TextInputEditText episodeDateEditText = NewEpisodeDialog.this.newEpisodeDialogView.findViewById(R.id.episodeDateEditText);
            Spinner episodePeriodSpinner = NewEpisodeDialog.this.newEpisodeDialogView.findViewById(R.id.episodePeriodSpinner);

            String newEpisodeName = episodeNameInputEditText.getText().toString();
            String newEpisodeDate = episodeDateEditText.getText().toString();
            String newEpisodePeriod = episodePeriodSpinner.getSelectedItem().toString();

            if (newEpisodeName.isEmpty()) {
                TextInputLayout inputLayout = NewEpisodeDialog.this.newEpisodeDialogView.findViewById(R.id.newEpisodeNameTextInputLayout);
                inputLayout.setError(context.getString(R.string.new_episode_dialog_error_empty_episode_name)); // show error
            } else {
                Toast.makeText(context, R.string.toast_message_creating_episode, Toast.LENGTH_SHORT).show();
                controller.createEpisode(newEpisodeName, newEpisodeDate, newEpisodePeriod);
                NewEpisodeDialog.this.clearNewEpisodeDialog();
                NewEpisodeDialog.this.alertDialog.dismiss();
            }
        };
    }

    private View.OnClickListener createAlertDialogNegativeButtonListener(@NonNull Context context){
        return v -> {
            NewEpisodeDialog.this.clearNewEpisodeDialog();
            NewEpisodeDialog.this.alertDialog.cancel();
        };
    }

    private void clearNewEpisodeDialog() throws NullPointerException{
        TextInputLayout inputLayout = this.newEpisodeDialogView.findViewById(R.id.newEpisodeNameTextInputLayout);
        EditText episodeNameInputEditText = this.newEpisodeDialogView.findViewById(com.rgp.asks.R.id.episodeEditText);
        EditText episodeDateEditText = this.newEpisodeDialogView.findViewById(com.rgp.asks.R.id.episodeDateEditText);
        Spinner episodePeriodSpinner = this.newEpisodeDialogView.findViewById(com.rgp.asks.R.id.episodePeriodSpinner);

        inputLayout.setError(null);
        episodeNameInputEditText.setText("");
        episodeDateEditText.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime()));
        episodePeriodSpinner.setSelection(0);
    }

    public void show(){
        if (this.alertDialog != null) this.alertDialog.show();
    }

    public void dismiss(){
        if (this.alertDialog != null) this.alertDialog.dismiss();
    }
}
