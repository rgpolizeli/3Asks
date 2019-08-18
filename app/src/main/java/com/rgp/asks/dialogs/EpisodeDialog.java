package com.rgp.asks.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.rgp.asks.R;
import com.rgp.asks.interfaces.EpisodeDialogListener;
import com.rgp.asks.views.DateInputLayout;
import com.rgp.asks.views.SpinnerInputLayout;
import com.rgp.asks.views.TextInputLayout;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class EpisodeDialog extends DialogFragment {

    //EpisodeDialog fragment tag
    public static final String FRAGMENT_TAG_EPISODE_DIALOG = "FRAGMENT_TAG_EPISODE_DIALOG";

    // ArgumentDialog state variables keys //
    private static final String BUNDLE_KEY_EPISODE_ID_TO_EDIT = "BUNDLE_KEY_EPISODE_ID_TO_EDIT";
    private static final String BUNDLE_KEY_EPISODE_TO_EDIT = "BUNDLE_KEY_EPISODE_TO_EDIT";
    private static final String BUNDLE_KEY_EPISODE_DATE_TO_EDIT = "BUNDLE_KEY_EPISODE_DATE_TO_EDIT";
    private static final String BUNDLE_KEY_EPISODE_PERIOD_TO_EDIT = "BUNDLE_KEY_EPISODE_PERIOD_TO_EDIT";

    // ArgumentDialog state variables //
    private int episodeIdToEdit;
    @Nullable
    private String episodeToEdit;
    @Nullable
    private String episodeDateToEdit;
    @Nullable
    private String episodePeriodToEdit;
    @Nullable
    private EpisodeDialogListener listener; // Listener to communicate with WhatFragment

    @Override
    public void onAttach(@NonNull Context context) throws ClassCastException {
        super.onAttach(context);
        try {
            this.listener = (EpisodeDialogListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_episode, container, false);
    }

    /**
     * After system restore views' values incorrectly, this method will clear this operation and fill the field with the correct values. The set of listeners and others changes is performed here too.
     * <p> Since this method is executed after {@link androidx.fragment.app.Fragment#restoreViewState}, where {@link androidx.fragment.app.Fragment#mSavedFragmentState is restored (even using the {@link DialogFragment#dismissAllowingStateLoss}), this is where you should restore field values, otherwise if you clear the value of fields in {@link #onDestroy}, the value empty is restored even if field values are reintroduced in the {@link #onViewCreated} method.
     * </p>
     *
     * @param savedInstanceState the bundle with saved variables.
     */
    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        View view = getView();
        if (savedInstanceState != null) {
            restoreDialogState(savedInstanceState);
        } else {
            clearEpisodeDialog(view);
        }

        setupButtonsListener(view);

        getDialog().setTitle(getContext().getString(R.string.episode_dialog_title));
        Button createButton = view.findViewById(R.id.positiveEpisodeButton);
        createButton.setText(getContext().getString(R.string.episode_dialog_positive_button));
        createButton.setOnClickListener(createDialogPositiveButtonListener());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(BUNDLE_KEY_EPISODE_ID_TO_EDIT, this.episodeIdToEdit);
        savedInstanceState.putString(BUNDLE_KEY_EPISODE_TO_EDIT, this.episodeToEdit);
        savedInstanceState.putString(BUNDLE_KEY_EPISODE_DATE_TO_EDIT, this.episodeDateToEdit);
        savedInstanceState.putString(BUNDLE_KEY_EPISODE_PERIOD_TO_EDIT, this.episodePeriodToEdit);
    }

    private void restoreDialogState(@NonNull Bundle savedInstanceState) {
        this.episodeIdToEdit = savedInstanceState.getInt(BUNDLE_KEY_EPISODE_ID_TO_EDIT);
        this.episodeToEdit = savedInstanceState.getString(BUNDLE_KEY_EPISODE_TO_EDIT);
        this.episodeDateToEdit = savedInstanceState.getString(BUNDLE_KEY_EPISODE_DATE_TO_EDIT);
        this.episodePeriodToEdit = savedInstanceState.getString(BUNDLE_KEY_EPISODE_PERIOD_TO_EDIT);
    }

    private void setupButtonsListener(@NonNull View view) {
        Button cancelButton = view.findViewById(R.id.negativeEpisodeButton);
        cancelButton.setOnClickListener(createDialogNegativeButtonListener());
    }

    private View.OnClickListener createDialogPositiveButtonListener() throws NullPointerException {
        return v -> {
            hideKeyboard(v);

            TextInputLayout episodeNameTextInputLayout = v.getRootView().findViewById(R.id.episodeNameTextInputLayout);
            DateInputLayout dateInputLayout = v.getRootView().findViewById(R.id.episodeDateTextInputLayout);
            SpinnerInputLayout periodInputLayout = v.getRootView().findViewById(R.id.episodePeriodSpinner);

            String episodeName = episodeNameTextInputLayout.getValue().toString();
            String episodeDate = dateInputLayout.getValue();
            String episodePeriod = periodInputLayout.getValue();

            if (episodeName.isEmpty()) {
                episodeNameTextInputLayout.goToState(TextInputLayout.STATE_ERROR);
            } else {
                Toast.makeText(getContext(), R.string.toast_message_creating_episode, Toast.LENGTH_SHORT).show();
                listener.onEpisodeDialogCreateButtonClick(episodeName, episodeDate, episodePeriod);
                dismiss();
            }
        };
    }

    private View.OnClickListener createDialogNegativeButtonListener() {
        return v -> {
            hideKeyboard(v);
            dismiss();
        };
    }

    private void clearEpisodeDialog(@NonNull View dialogView) throws NullPointerException {
        TextInputLayout episodeNameTextInputLayout = dialogView.findViewById(R.id.episodeNameTextInputLayout);
        DateInputLayout dateInputLayout = dialogView.findViewById(R.id.episodeDateTextInputLayout);
        SpinnerInputLayout periodInputLayout = dialogView.findViewById(R.id.episodePeriodSpinner);

        episodeNameTextInputLayout.clear();
        dateInputLayout.clear();
        periodInputLayout.clear();
    }

    public void show(@NonNull FragmentManager fragmentManager) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_EPISODE_DIALOG) == null) {
            super.show(fragmentManager, FRAGMENT_TAG_EPISODE_DIALOG);
        }
    }

    private void hideKeyboard(@NonNull View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setEpisodeDialogListener(@NonNull EpisodeDialogListener listener) {
        this.listener = listener;
    }
}
