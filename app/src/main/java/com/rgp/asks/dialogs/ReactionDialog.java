package com.rgp.asks.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.interfaces.ReactionDialogListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ReactionDialog extends DialogFragment {

    //ReactionDialog fragment tag
    public static final String FRAGMENT_TAG_REACTION_DIALOG = "REACTION_DIALOG_FRAGMENT";

    // ReactionDialog state variables keys //
    private static final String BUNDLE_KEY_FIRST_CREATION_FLAG = "BUNDLE_KEY_FIRST_CREATION_FLAG";
    private static final String BUNDLE_KEY_DIALOG_MODE = "BUNDLE_KEY_DIALOG_MODE";
    private static final String BUNDLE_KEY_REACTION_ID_TO_EDIT = "BUNDLE_KEY_REACTION_ID_TO_EDIT";
    private static final String BUNDLE_KEY_REACTION_TO_EDIT = "BUNDLE_KEY_REACTION_TO_EDIT";
    private static final String BUNDLE_KEY_REACTION_CLASS_TO_EDIT = "BUNDLE_KEY_REACTION_CLASS_TO_EDIT";

    // ReactionDialog possible modes //
    private static final int CREATE_MODE = 0;
    private static final int EDIT_MODE = 1;

    // ReactionDialog state variables //
    private boolean firstCreation = true; //this is a lock variable to prevent a dialog that is reopening due to a configuration change to reintroduce the initial field values in edit mode.
    private int dialogMode = -1;
    private int reactionIdToEdit;
    @Nullable
    private String reactionToEdit;
    @Nullable
    private String reactionClassToEdit;
    @Nullable
    private ReactionDialogListener listener; // Listener to communicate with WhatFragment

    @Override
    public void onAttach(@NonNull Context context) throws ClassCastException {
        super.onAttach(context);
        try {
            this.listener = (ReactionDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_reaction, container, false);
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
            clearReactionDialog(view);
        }

        setupReactionClassSpinnerListeners(view);
        setupButtonsListener(view);

        Button deleteButton = view.findViewById(R.id.neutralReactionButton);

        if (this.dialogMode == CREATE_MODE) {
            getDialog().setTitle(getContext().getString(R.string.reaction_dialog_create_title));
            deleteButton.setVisibility(View.GONE);
            Button createButton = view.findViewById(R.id.positiveReactionButton);
            createButton.setText(getContext().getString(R.string.reaction_dialog_create_button));
            createButton.setOnClickListener(createAlertDialogPositiveButtonListenerInCreateMode());
        } else {
            getDialog().setTitle(getContext().getString(R.string.reaction_dialog_edit_title));
            deleteButton.setVisibility(View.VISIBLE);
            Button saveButton = view.findViewById(R.id.positiveReactionButton);
            saveButton.setText(getContext().getString(R.string.reaction_dialog_save_button));
            saveButton.setOnClickListener(createAlertDialogPositiveButtonListenerInEditMode());
            EditText reactionEditText = view.findViewById(com.rgp.asks.R.id.reactionEditText);
            Spinner reactionClassSpinner = view.findViewById(com.rgp.asks.R.id.reactionClassSpinner);
            if (this.firstCreation) {
                reactionEditText.setText(this.reactionToEdit);
                reactionClassSpinner.setSelection(getIndex(reactionClassSpinner, this.reactionClassToEdit));
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(BUNDLE_KEY_FIRST_CREATION_FLAG, false);
        savedInstanceState.putInt(BUNDLE_KEY_DIALOG_MODE, this.dialogMode);
        savedInstanceState.putInt(BUNDLE_KEY_REACTION_ID_TO_EDIT, this.reactionIdToEdit);
        savedInstanceState.putString(BUNDLE_KEY_REACTION_TO_EDIT, this.reactionToEdit);
        savedInstanceState.putString(BUNDLE_KEY_REACTION_CLASS_TO_EDIT, this.reactionClassToEdit);
    }

    private void restoreDialogState(@NonNull Bundle savedInstanceState) {
        this.firstCreation = savedInstanceState.getBoolean(BUNDLE_KEY_FIRST_CREATION_FLAG);
        this.dialogMode = savedInstanceState.getInt(BUNDLE_KEY_DIALOG_MODE);
        this.reactionIdToEdit = savedInstanceState.getInt(BUNDLE_KEY_REACTION_ID_TO_EDIT);
        this.reactionToEdit = savedInstanceState.getString(BUNDLE_KEY_REACTION_TO_EDIT);
        this.reactionClassToEdit = savedInstanceState.getString(BUNDLE_KEY_REACTION_CLASS_TO_EDIT);
    }

    /**
     * Create and set listeners to reaction category spinner.
     *
     * @throws NullPointerException
     */
    private void setupReactionClassSpinnerListeners(@NonNull View view) throws NullPointerException {
        Spinner reactionClassSpinner = view.findViewById(R.id.reactionClassSpinner);
        reactionClassSpinner.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        });
    }

    private void setupButtonsListener(@NonNull View view) {
        Button cancelButton = view.findViewById(R.id.negativeReactionButton);
        Button deleteButton = view.findViewById(R.id.neutralReactionButton);
        cancelButton.setOnClickListener(createAlertDialogNegativeButtonListener());
        deleteButton.setOnClickListener(createAlertDialogNeutralButtonListener());
    }

    private View.OnClickListener createAlertDialogPositiveButtonListenerInCreateMode() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            TextInputEditText reactionEditText = v.getRootView().findViewById(R.id.reactionEditText);
            Spinner reactionClassSpinner = v.getRootView().findViewById(R.id.reactionClassSpinner);

            String newReaction = reactionEditText.getText().toString();
            String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

            if (newReaction.isEmpty()) {
                TextInputLayout inputLayout = v.getRootView().findViewById(R.id.reactionTextInputLayout);
                inputLayout.setError(getContext().getString(R.string.reaction_dialog_error_empty_reaction)); // show error
            } else {
                Toast.makeText(getContext(), R.string.toast_message_creating_reaction, Toast.LENGTH_SHORT).show();
                listener.onReactionDialogCreateButtonClick(newReaction, newReactionClass);
                dismiss();
            }
        };
    }

    private View.OnClickListener createAlertDialogPositiveButtonListenerInEditMode() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            TextInputEditText reactionEditText = v.getRootView().findViewById(R.id.reactionEditText);
            Spinner reactionClassSpinner = v.getRootView().findViewById(R.id.reactionClassSpinner);

            String newReaction = reactionEditText.getText().toString();
            String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

            if (newReaction.isEmpty()) {
                TextInputLayout inputLayout = v.getRootView().findViewById(R.id.reactionTextInputLayout);
                inputLayout.setError(getContext().getString(R.string.reaction_dialog_error_empty_reaction)); // show error
            } else {
                if (!newReaction.equals(this.reactionToEdit) || !newReactionClass.equals(this.reactionClassToEdit)) {
                    listener.onReactionDialogSaveButtonClick(this.reactionIdToEdit, newReaction, newReactionClass);
                    dismiss();
                }
            }
        };
    }

    private View.OnClickListener createAlertDialogNegativeButtonListener() {
        return v -> {
            hideKeyboard(v);
            dismiss();
        };
    }

    private View.OnClickListener createAlertDialogNeutralButtonListener() {
        return v -> {
            hideKeyboard(v);
            listener.onReactionDialogDeleteButtonClick(this.reactionIdToEdit);
            dismiss();
        };
    }

    private void clearReactionDialog(View dialogView) throws NullPointerException {
        TextInputLayout inputLayout = dialogView.findViewById(R.id.reactionTextInputLayout);
        EditText reactionEditText = dialogView.findViewById(R.id.reactionEditText);
        Spinner reactionClassSpinner = dialogView.findViewById(R.id.reactionClassSpinner);

        inputLayout.setError(null);
        reactionEditText.setText("");
        reactionClassSpinner.setSelection(0);
    }

    public void showInCreateMode(@NonNull FragmentManager fragmentManager) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_REACTION_DIALOG) == null) {
            this.dialogMode = CREATE_MODE;
            show(fragmentManager, FRAGMENT_TAG_REACTION_DIALOG);
        }
    }

    public void showInEditMode(@NonNull final FragmentManager fragmentManager, int reactionId, @NonNull final String reactionToEdit, @NonNull final String reactionClassToEdit) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_REACTION_DIALOG) == null) {
            this.dialogMode = EDIT_MODE;
            this.reactionIdToEdit = reactionId;
            this.reactionToEdit = reactionToEdit;
            this.reactionClassToEdit = reactionClassToEdit;
            show(fragmentManager, FRAGMENT_TAG_REACTION_DIALOG);
        }
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setReactionDialogListener(@NonNull ReactionDialogListener listener) {
        this.listener = listener;
    }
}
