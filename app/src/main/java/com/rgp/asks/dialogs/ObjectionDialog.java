package com.rgp.asks.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.rgp.asks.R;
import com.rgp.asks.interfaces.ObjectionDialogListener;
import com.rgp.asks.views.TextInputLayout;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ObjectionDialog extends DialogFragment {

    //ObjectionDialog fragment tag
    public static final String FRAGMENT_TAG_OBJECTION_DIALOG = "FRAGMENT_TAG_OBJECTION_DIALOG";

    // ObjectionDialog state variables keys //
    private static final String BUNDLE_KEY_FIRST_CREATION_FLAG = "BUNDLE_KEY_FIRST_CREATION_FLAG";
    private static final String BUNDLE_KEY_DIALOG_MODE = "BUNDLE_KEY_DIALOG_MODE";
    private static final String BUNDLE_KEY_OBJECTION_ID_TO_EDIT = "BUNDLE_KEY_OBJECTION_ID_TO_EDIT";
    private static final String BUNDLE_KEY_OBJECTION_TO_EDIT = "BUNDLE_KEY_OBJECTION_TO_EDIT";

    // ObjectionDialog possible modes //
    private static final int CREATE_MODE = 0;
    private static final int EDIT_MODE = 1;

    // ObjectionDialog state variables //
    private boolean firstCreation = true; //this is a lock variable to prevent a dialog that is reopening due to a configuration change to reintroduce the initial field values in edit mode.
    private int dialogMode = -1;
    private int objectionIdToEdit;
    @Nullable
    private String objectionToEdit;
    @Nullable
    private ObjectionDialogListener listener; // Listener to communicate with WhatFragment

    @Override
    public void onAttach(@NonNull Context context) throws ClassCastException {
        super.onAttach(context);
        try {
            this.listener = (ObjectionDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_objection, container, false);
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
            clearObjectionDialog(view);
        }

        setupButtonsListener(view);

        Button deleteButton = view.findViewById(R.id.neutralObjectionButton);

        if (this.dialogMode == CREATE_MODE) {
            getDialog().setTitle(getContext().getString(R.string.objection_dialog_create_title));
            deleteButton.setVisibility(View.GONE);
            Button createButton = view.findViewById(R.id.positiveObjectionButton);
            createButton.setText(getContext().getString(R.string.objection_dialog_create_button));
            createButton.setOnClickListener(createDialogPositiveButtonListenerInCreateMode());
        } else {
            getDialog().setTitle(getContext().getString(R.string.objection_dialog_edit_title));
            deleteButton.setVisibility(View.VISIBLE);
            Button saveButton = view.findViewById(R.id.positiveObjectionButton);
            saveButton.setText(getContext().getString(R.string.objection_dialog_save_button));
            saveButton.setOnClickListener(createDialogPositiveButtonListenerInEditMode());
            TextInputLayout objectionEditText = view.findViewById(R.id.objectionTextInputLayout);
            if (this.firstCreation) {
                objectionEditText.setValue(this.objectionToEdit);
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(BUNDLE_KEY_FIRST_CREATION_FLAG, false);
        savedInstanceState.putInt(BUNDLE_KEY_DIALOG_MODE, this.dialogMode);
        savedInstanceState.putInt(BUNDLE_KEY_OBJECTION_ID_TO_EDIT, this.objectionIdToEdit);
        savedInstanceState.putString(BUNDLE_KEY_OBJECTION_TO_EDIT, this.objectionToEdit);
    }

    private void restoreDialogState(@NonNull Bundle savedInstanceState) {
        this.firstCreation = savedInstanceState.getBoolean(BUNDLE_KEY_FIRST_CREATION_FLAG);
        this.dialogMode = savedInstanceState.getInt(BUNDLE_KEY_DIALOG_MODE);
        this.objectionIdToEdit = savedInstanceState.getInt(BUNDLE_KEY_OBJECTION_ID_TO_EDIT);
        this.objectionToEdit = savedInstanceState.getString(BUNDLE_KEY_OBJECTION_TO_EDIT);
    }

    private void setupButtonsListener(@NonNull View view) {
        Button cancelButton = view.findViewById(R.id.negativeObjectionButton);
        Button deleteButton = view.findViewById(R.id.neutralObjectionButton);
        cancelButton.setOnClickListener(createDialogNegativeButtonListener());
        deleteButton.setOnClickListener(createDialogNeutralButtonListener());
    }

    private View.OnClickListener createDialogPositiveButtonListenerInCreateMode() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            TextInputLayout objectionEditText = v.getRootView().findViewById(R.id.objectionTextInputLayout);
            String newObjection = objectionEditText.getValue().toString();

            if (newObjection.isEmpty()) {
                objectionEditText.goToState(TextInputLayout.STATE_ERROR);
            } else {
                listener.onObjectionDialogCreateButtonClick(newObjection);
                dismiss();
            }
        };
    }

    private View.OnClickListener createDialogPositiveButtonListenerInEditMode() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            TextInputLayout objectionEditText = v.getRootView().findViewById(R.id.objectionTextInputLayout);
            String newObjection = objectionEditText.getValue().toString();

            if (newObjection.isEmpty()) {
                objectionEditText.goToState(TextInputLayout.STATE_ERROR);
            } else {
                if (!newObjection.equals(this.objectionToEdit)) {
                    listener.onObjectionDialogSaveButtonClick(this.objectionIdToEdit, newObjection);
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
            listener.onObjectionDialogDeleteButtonClick(this.objectionIdToEdit);
            dismiss();
        };
    }

    private void clearObjectionDialog(@NonNull View dialogView) throws NullPointerException {
        TextInputLayout objectionEditText = dialogView.findViewById(R.id.objectionTextInputLayout);
        objectionEditText.clear();
    }

    public void showInCreateMode(@NonNull FragmentManager fragmentManager) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_OBJECTION_DIALOG) == null) {
            this.dialogMode = CREATE_MODE;
            show(fragmentManager, FRAGMENT_TAG_OBJECTION_DIALOG);
        }
    }

    public void showInEditMode(@NonNull final FragmentManager fragmentManager, int objectionId, @NonNull final String objectionToEdit) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_OBJECTION_DIALOG) == null) {
            this.dialogMode = EDIT_MODE;
            this.objectionIdToEdit = objectionId;
            this.objectionToEdit = objectionToEdit;
            show(fragmentManager, FRAGMENT_TAG_OBJECTION_DIALOG);
        }
    }

    private void hideKeyboard(@NonNull View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setObjectionDialogListener(@NonNull ObjectionDialogListener listener) {
        this.listener = listener;
    }
}
