package com.rgp.asks.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.interfaces.ArgumentDialogListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ArgumentDialog extends DialogFragment {

    //ArgumentDialog fragment tag
    public static final String FRAGMENT_TAG_ARGUMENT_DIALOG = "FRAGMENT_TAG_ARGUMENT_DIALOG";

    // ArgumentDialog state variables keys //
    private static final String BUNDLE_KEY_FIRST_CREATION_FLAG = "BUNDLE_KEY_FIRST_CREATION_FLAG";
    private static final String BUNDLE_KEY_DIALOG_MODE = "BUNDLE_KEY_DIALOG_MODE";
    private static final String BUNDLE_KEY_ARGUMENT_ID_TO_EDIT = "BUNDLE_KEY_ARGUMENT_ID_TO_EDIT";
    private static final String BUNDLE_KEY_ARGUMENT_TO_EDIT = "BUNDLE_KEY_ARGUMENT_TO_EDIT";

    // ArgumentDialog possible modes //
    private static final int CREATE_MODE = 0;
    private static final int EDIT_MODE = 1;

    // ArgumentDialog state variables //
    private boolean firstCreation = true; //this is a lock variable to prevent a dialog that is reopening due to a configuration change to reintroduce the initial field values in edit mode.
    private int dialogMode = -1;
    private int argumentIdToEdit;
    @Nullable
    private String argumentToEdit;
    @Nullable
    private ArgumentDialogListener listener; // Listener to communicate with WhatFragment

    @Override
    public void onAttach(@NonNull Context context) throws ClassCastException {
        super.onAttach(context);
        try {
            this.listener = (ArgumentDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_argument, container, false);
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
            clearArgumentDialog(view);
        }

        setupButtonsListener(view);

        Button deleteButton = view.findViewById(R.id.neutralArgumentButton);

        if (this.dialogMode == CREATE_MODE) {
            getDialog().setTitle(getContext().getString(R.string.argument_dialog_create_title));
            deleteButton.setVisibility(View.GONE);
            Button createButton = view.findViewById(R.id.positiveArgumentButton);
            createButton.setText(getContext().getString(R.string.argument_dialog_create_button));
            createButton.setOnClickListener(createDialogPositiveButtonListenerInCreateMode());
        } else {
            getDialog().setTitle(getContext().getString(R.string.argument_dialog_edit_title));
            deleteButton.setVisibility(View.VISIBLE);
            Button saveButton = view.findViewById(R.id.positiveArgumentButton);
            saveButton.setText(getContext().getString(R.string.argument_dialog_save_button));
            saveButton.setOnClickListener(createDialogPositiveButtonListenerInEditMode());
            EditText argumentEditText = view.findViewById(R.id.argumentEditText);
            if (this.firstCreation) {
                argumentEditText.setText(this.argumentToEdit);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(BUNDLE_KEY_FIRST_CREATION_FLAG, false);
        savedInstanceState.putInt(BUNDLE_KEY_DIALOG_MODE, this.dialogMode);
        savedInstanceState.putInt(BUNDLE_KEY_ARGUMENT_ID_TO_EDIT, this.argumentIdToEdit);
        savedInstanceState.putString(BUNDLE_KEY_ARGUMENT_TO_EDIT, this.argumentToEdit);
    }

    private void restoreDialogState(@NonNull Bundle savedInstanceState) {
        this.firstCreation = savedInstanceState.getBoolean(BUNDLE_KEY_FIRST_CREATION_FLAG);
        this.dialogMode = savedInstanceState.getInt(BUNDLE_KEY_DIALOG_MODE);
        this.argumentIdToEdit = savedInstanceState.getInt(BUNDLE_KEY_ARGUMENT_ID_TO_EDIT);
        this.argumentToEdit = savedInstanceState.getString(BUNDLE_KEY_ARGUMENT_TO_EDIT);
    }

    private void setupButtonsListener(@NonNull View view) {
        Button cancelButton = view.findViewById(R.id.negativeArgumentButton);
        Button deleteButton = view.findViewById(R.id.neutralArgumentButton);
        cancelButton.setOnClickListener(createDialogNegativeButtonListener());
        deleteButton.setOnClickListener(createDialogNeutralButtonListener());
    }

    private View.OnClickListener createDialogPositiveButtonListenerInCreateMode() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            TextInputEditText argumentEditText = v.getRootView().findViewById(R.id.argumentEditText);

            String newArgument = argumentEditText.getText().toString();

            if (newArgument.isEmpty()) {
                TextInputLayout inputLayout = v.getRootView().findViewById(R.id.argumentTextInputLayout);
                inputLayout.setError(getContext().getString(R.string.argument_dialog_error_empty_argument)); // show error
            } else {
                //Toast.makeText(getContext(), R.string.toast_message_creating_argument, Toast.LENGTH_SHORT).show();
                listener.onArgumentDialogCreateButtonClick(newArgument);
                dismiss();
            }
        };
    }

    private View.OnClickListener createDialogPositiveButtonListenerInEditMode() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            TextInputEditText argumentEditText = v.getRootView().findViewById(R.id.argumentEditText);

            String newArgument = argumentEditText.getText().toString();

            if (newArgument.isEmpty()) {
                TextInputLayout inputLayout = v.getRootView().findViewById(R.id.argumentTextInputLayout);
                inputLayout.setError(getContext().getString(R.string.argument_dialog_error_empty_argument)); // show error
            } else {
                if (!newArgument.equals(this.argumentToEdit)) {
                    listener.onArgumentDialogSaveButtonClick(this.argumentIdToEdit, newArgument);
                    dismiss();
                }
            }
        };
    }

    private View.OnClickListener createDialogNegativeButtonListener() {
        return v -> {
            hideKeyboard(v);
            dismiss();
        };
    }

    private View.OnClickListener createDialogNeutralButtonListener() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            listener.onArgumentDialogDeleteButtonClick(this.argumentIdToEdit);
            dismiss();
        };
    }

    private void clearArgumentDialog(@NonNull View dialogView) throws NullPointerException {
        TextInputLayout inputLayout = dialogView.findViewById(R.id.argumentTextInputLayout);
        EditText argumentEditText = dialogView.findViewById(R.id.argumentEditText);

        inputLayout.setError(null);
        argumentEditText.setText("");
    }

    public void showInCreateMode(@NonNull FragmentManager fragmentManager) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_ARGUMENT_DIALOG) == null) {
            this.dialogMode = CREATE_MODE;
            show(fragmentManager, FRAGMENT_TAG_ARGUMENT_DIALOG);
        }
    }

    public void showInEditMode(@NonNull final FragmentManager fragmentManager, int argumentId, @NonNull final String argumentToEdit) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_ARGUMENT_DIALOG) == null) {
            this.dialogMode = EDIT_MODE;
            this.argumentIdToEdit = argumentId;
            this.argumentToEdit = argumentToEdit;
            show(fragmentManager, FRAGMENT_TAG_ARGUMENT_DIALOG);
        }
    }

    private void hideKeyboard(@NonNull View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setArgumentDialogListener(@NonNull ArgumentDialogListener listener) {
        this.listener = listener;
    }
}
