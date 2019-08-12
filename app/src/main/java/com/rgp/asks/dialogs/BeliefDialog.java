package com.rgp.asks.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.interfaces.BeliefDialogListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BeliefDialog extends DialogFragment {

    //BeliefDialog fragment tag
    public static final String FRAGMENT_TAG_BELIEF_DIALOG = "FRAGMENT_TAG_BELIEF_DIALOG";

    // BeliefDialog state variables keys //
    private static final String BUNDLE_KEY_BELIEF_ID_TO_EDIT = "BUNDLE_KEY_BELIEF_ID_TO_EDIT";
    private static final String BUNDLE_KEY_BELIEF_TO_EDIT = "BUNDLE_KEY_BELIEF_TO_EDIT";

    // BeliefDialog state variables //
    private int beliefIdToEdit;
    @Nullable
    private String beliefToEdit;
    @Nullable
    private BeliefDialogListener listener; // Listener to communicate with WhyFragment

    @Override
    public void onAttach(@NonNull Context context) throws ClassCastException {
        super.onAttach(context);
        try {
            this.listener = (BeliefDialogListener) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_belief, container, false);
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
            clearBeliefDialog(view);
        }

        setupButtonsListener(view);

        getDialog().setTitle(getContext().getString(R.string.belief_dialog_title));
        Button createButton = view.findViewById(R.id.positiveBeliefButton);
        createButton.setText(getContext().getString(R.string.belief_dialog_create_button));
        createButton.setOnClickListener(createDialogPositiveButtonListener());

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(BUNDLE_KEY_BELIEF_ID_TO_EDIT, this.beliefIdToEdit);
        savedInstanceState.putString(BUNDLE_KEY_BELIEF_TO_EDIT, this.beliefToEdit);
    }

    private void restoreDialogState(@NonNull Bundle savedInstanceState) {
        this.beliefIdToEdit = savedInstanceState.getInt(BUNDLE_KEY_BELIEF_ID_TO_EDIT);
        this.beliefToEdit = savedInstanceState.getString(BUNDLE_KEY_BELIEF_TO_EDIT);
    }

    private void setupButtonsListener(@NonNull View view) {
        Button cancelButton = view.findViewById(R.id.negativeBeliefButton);
        cancelButton.setOnClickListener(createDialogNegativeButtonListener());
    }

    private View.OnClickListener createDialogPositiveButtonListener() throws NullPointerException {
        return v -> {
            hideKeyboard(v);
            TextInputEditText beliefEditText = v.getRootView().findViewById(R.id.beliefEditText);

            String newBelief = beliefEditText.getText().toString();

            if (newBelief.isEmpty()) {
                TextInputLayout inputLayout = v.getRootView().findViewById(R.id.beliefTextInputLayout);
                inputLayout.setError(getContext().getString(R.string.belief_dialog_error_empty_belief)); // show error
            } else {
                Toast.makeText(getContext(), R.string.toast_message_creating_belief, Toast.LENGTH_SHORT).show();
                listener.onBeliefDialogCreateButtonClick(newBelief);
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

    private void clearBeliefDialog(@NonNull View dialogView) throws NullPointerException {
        TextInputLayout inputLayout = dialogView.findViewById(R.id.beliefTextInputLayout);
        EditText beliefEditText = dialogView.findViewById(R.id.beliefEditText);

        inputLayout.setError(null);
        beliefEditText.setText("");
    }

    public void show(@NonNull FragmentManager fragmentManager) throws NullPointerException {
        if (fragmentManager.findFragmentByTag(FRAGMENT_TAG_BELIEF_DIALOG) == null) {
            super.show(fragmentManager, FRAGMENT_TAG_BELIEF_DIALOG);
        }
    }

    private void hideKeyboard(@NonNull View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setBeliefDialogListener(@NonNull BeliefDialogListener listener) {
        this.listener = listener;
    }
}
