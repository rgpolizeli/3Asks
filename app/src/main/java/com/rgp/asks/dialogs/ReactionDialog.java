package com.rgp.asks.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.interfaces.ReactionDialogListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ReactionDialog extends DialogFragment implements DialogInterface.OnDismissListener {

    //ReactionDialog fragment tag
    public static final String FRAGMENT_TAG_REACTION_DIALOG = "REACTION_DIALOG_FRAGMENT";
    // ReactionDialog state variables keys //
    private static final String BUNDLE_KEY_FIRST_CREATION_FLAG = "BUNDLE_KEY_FIRST_CREATION_FLAG";
    private static final String BUNDLE_KEY_DIALOG_MODE = "BUNDLE_KEY_DIALOG_MODE";
    private static final String BUNDLE_KEY_REACTION_TO_EDIT = "BUNDLE_KEY_REACTION_TO_EDIT";
    private static final String BUNDLE_KEY_REACTION_CLASS_TO_EDIT = "BUNDLE_KEY_REACTION_CLASS_TO_EDIT";
    // ReactionDialog possible modes //
    private static final int CREATE_MODE = 0;
    private static final int EDIT_MODE = 1;
    // ReactionDialog state variables //
    //this is a lock variable to prevent a dialog that is reopening due to a configuration change to reintroduce the initial field values in edit mode.
    private boolean firstCreation = true;
    private int dialogMode = -1;
    @Nullable
    private String reactionToEdit;
    @Nullable
    private String reactionClassToEdit;
    // Listener to communicate with WhatFragment
    @Nullable
    private ReactionDialogListener listener;

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
    public void onDismiss(DialogInterface dialogInterface) {
        //if(!getActivity().isChangingConfigurations()){
        //dismissAllowingStateLoss();
        //}
        super.onDismiss(dialogInterface);

        //clearReactionDialog(getView());
        //super.onDismiss(dialogInterface);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_new_reaction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setupReactionEditTextListeners(view);
        /*
        setupReactionClassSpinnerListeners(view);
        setupButtonsListener(view);

        Button deleteButton = view.findViewById(R.id.neutralReactionButton);

        if(savedInstanceState != null){
            restoreDialogState(savedInstanceState);
        }

        if (this.dialogMode == CREATE_MODE) {
            clearReactionDialog(getView());
            getDialog().setTitle(getContext().getString(R.string.reaction_dialog_create_title));
            deleteButton.setVisibility(View.GONE);
            Button createButton = view.findViewById(R.id.positiveReactionButton);
            createButton.setText(getContext().getString(R.string.reaction_dialog_create_button));
            createButton.setOnClickListener(createAlertDialogPositiveButtonListenerInCreateMode());
        } else {
            clearReactionDialog(getView());
            getDialog().setTitle(getContext().getString(R.string.reaction_dialog_edit_title));
            deleteButton.setVisibility(View.VISIBLE);
            Button saveButton = view.findViewById(R.id.positiveReactionButton);
            saveButton.setText(getContext().getString(R.string.reaction_dialog_save_button));
            saveButton.setOnClickListener(createAlertDialogPositiveButtonListenerInEditMode());
            EditText reactionEditText = view.findViewById(com.rgp.asks.R.id.reactionEditText);
            Spinner reactionClassSpinner = view.findViewById(com.rgp.asks.R.id.reactionClassSpinner);
            if(this.firstCreation){
                reactionEditText.setText(this.reactionToEdit);
                reactionClassSpinner.setSelection(getIndex(reactionClassSpinner, this.reactionClassToEdit));
            }
        }
        */
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
        savedInstanceState.putString(BUNDLE_KEY_REACTION_TO_EDIT, this.reactionToEdit);
        savedInstanceState.putString(BUNDLE_KEY_REACTION_CLASS_TO_EDIT, this.reactionClassToEdit);
    }
    /*
    private String getValueFromReactionEditText(){
        View reactionDialogView = getView();
        if(reactionDialogView != null){
            EditText reactionEditText = getView().findViewById(R.id.reactionEditText);
            return reactionEditText.getText().toString();
        }
        return "";
    }

    private String getValueFromReactionClassSpinner(){
        View reactionDialogView = getView();
        if(reactionDialogView != null){
            Spinner reactionClassSpinner = getView().findViewById(R.id.reactionClassSpinner);
            return reactionClassSpinner.getSelectedItem().toString();
        }
        return "";
    }
    */

    private void restoreDialogState(@NonNull Bundle savedInstanceState) {
        this.firstCreation = savedInstanceState.getBoolean(BUNDLE_KEY_FIRST_CREATION_FLAG);
        this.dialogMode = savedInstanceState.getInt(BUNDLE_KEY_DIALOG_MODE);
        this.reactionToEdit = savedInstanceState.getString(BUNDLE_KEY_REACTION_TO_EDIT);
        this.reactionClassToEdit = savedInstanceState.getString(BUNDLE_KEY_REACTION_CLASS_TO_EDIT);
    }

    private void setupReactionEditTextListeners(@NonNull View view) {

        EditText reactionEditText = view.findViewById(R.id.reactionEditText);
        reactionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ReactionDialog.this.reactionToEdit = s.toString();
            }
        });
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
        /*
        reactionClassSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ReactionDialog.this.reactionClassToEdit = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        */
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
                //controller.createReaction(newReaction, newReactionClass);
                //clearReactionDialog(v.getRootView());
                getDialog().dismiss();
            }
        };
    }

    private View.OnClickListener createAlertDialogPositiveButtonListenerInEditMode() throws NullPointerException {
        return v -> {
            final TextInputEditText reactionEditText = v.getRootView().findViewById(R.id.reactionEditText);
            final Spinner reactionClassSpinner = v.getRootView().findViewById(R.id.reactionClassSpinner);
            hideKeyboard(v);
            String newReaction = reactionEditText.getText().toString();
            String newReactionClass = reactionClassSpinner.getSelectedItem().toString();

            if (newReaction.isEmpty()) {
                TextInputLayout inputLayout = v.getRootView().findViewById(R.id.reactionTextInputLayout);
                inputLayout.setError(getContext().getString(R.string.reaction_dialog_error_empty_reaction)); // show error
            } else {
                //if (!newReaction.equals(reaction.getReaction()) || !newReactionClass.equals(reaction.getReactionCategory())) {
                //reaction.setReaction(newReaction);
                //reaction.setReactionCategory(newReactionClass);
                //model.editReaction(reaction);
                // clearReactionDialog(dialog);
                // dialog.dismiss();
                //}
            }
        };
    }

    private View.OnClickListener createAlertDialogNegativeButtonListener() {
        return v -> {
            hideKeyboard(v);
            //clearReactionDialog(v.getRootView());
            getDialog().cancel();
        };
    }

    private View.OnClickListener createAlertDialogNeutralButtonListener() {
        return v -> {
            hideKeyboard(v);
            //model.removeReaction(reaction);
            //clearReactionDialog(v.getRootView());
            //getDialog().dismiss();
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

    public void showInEditMode(@NonNull final FragmentManager fragmentManager, @NonNull final String reactionToEdit, @NonNull final String reactionClassToEdit) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_REACTION_DIALOG) == null) {
            this.dialogMode = EDIT_MODE;
            this.reactionToEdit = reactionToEdit;
            this.reactionClassToEdit = reactionClassToEdit;
            show(fragmentManager, FRAGMENT_TAG_REACTION_DIALOG);
        }
        /*this.alertDialog.setTitle(this.alertDialog.getContext().getString(R.string.reaction_dialog_edit_title));
        this.alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setVisibility(View.VISIBLE);

        this.alertDialog.show();

        final TextInputEditText reactionEditText = this.alertDialog.findViewById(R.id.reactionEditText);
        final Spinner reactionClassSpinner = this.alertDialog.findViewById(R.id.reactionClassSpinner);

        reactionEditText.setText(reaction.getReaction());
        reactionClassSpinner.setSelection(getIndex(reactionClassSpinner, reaction.getReactionCategory()));

        this.alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this.positiveButtonInEditModeOnClickListener);
        this.alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(this.negativeButtonOnClickListener);
        this.alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(this.neutralButtonOnClickListener);
        */
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

    public void onDestroy() {
        super.onDestroy();
    }

}
