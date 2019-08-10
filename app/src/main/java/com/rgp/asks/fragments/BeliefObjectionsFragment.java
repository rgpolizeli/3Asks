package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rgp.asks.R;
import com.rgp.asks.adapters.ObjectionRVAdapter;
import com.rgp.asks.messages.OpenEditObjectionDialogEvent;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.viewmodel.BeliefViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class BeliefObjectionsFragment extends Fragment {

    private RecyclerView objectionsRecyclerView;
    private ObjectionRVAdapter objectionsRecyclerViewAdapter;
    private BeliefViewModel model;
    private AlertDialog newObjectionDialog;
    private AlertDialog editObjectionDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rgp.asks.R.layout.recycler_view_objection, container, false);

        EventBus.getDefault().register(this);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void setupFAB(ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton objectionsFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.addObjectionFab);
        objectionsFab.setOnClickListener(v -> showNewObjectionDialog());
    }

    private void setupRecyclerView(View rootView) {
        objectionsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.objectionRV);
        LinearLayoutManager objectionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        objectionsRecyclerView.setLayoutManager(objectionsRecyclerViewLayoutManager);
        objectionsRecyclerViewAdapter = new ObjectionRVAdapter();
        objectionsRecyclerView.setAdapter(objectionsRecyclerViewAdapter);
    }

    private void initViewModel() {
        model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);
        model.getObjections().observe(this, objections -> objectionsRecyclerViewAdapter.setObjections(objections));
    }

    private void initDialogs() {
        newObjectionDialog = createNewObjectionDialog();
        editObjectionDialog = createEditObjectionDialog();
    }

    private AlertDialog createEditObjectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_objection, null))
                .setPositiveButton(this.getString(R.string.objection_dialog_save_button), null)
                .setNegativeButton(this.getString(R.string.objection_dialog_cancel_button), null)
                .setNeutralButton(this.getString(R.string.objection_dialog_delete_button), null)
                .setTitle(this.getString(R.string.objection_dialog_edit_title));

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createNewObjectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_objection, null))
                .setPositiveButton(this.getString(R.string.objection_dialog_create_button), null)
                .setNegativeButton(this.getString(R.string.objection_dialog_cancel_button), null)
                .setTitle(this.getString(R.string.objection_dialog_create_title));

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void showNewObjectionDialog() {
        this.newObjectionDialog.show();
        final AlertDialog dialog = this.newObjectionDialog;

        this.newObjectionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            hideKeyboard(v);
            TextInputEditText objectionEditText = dialog.findViewById(R.id.objectionEditText);
            String newObjection = objectionEditText.getText().toString();

            if (newObjection.isEmpty()) {
                TextInputLayout inputLayout = dialog.findViewById(R.id.objectionTextInputLayout);
                inputLayout.setError(dialog.getContext().getString(R.string.objection_dialog_error_empty_objection)); // show error
            } else {
                model.createObjection(newObjection);
                clearObjectionDialog(dialog);
                dialog.dismiss();
            }
        });
        this.newObjectionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            hideKeyboard(v);
            clearObjectionDialog(dialog);
            dialog.cancel();
        });
    }

    private void showEditObjectionDialog(@NonNull final Objection objection) {
        this.editObjectionDialog.show();
        final AlertDialog dialog = this.editObjectionDialog;
        final TextInputEditText objectionEditText = dialog.findViewById(com.rgp.asks.R.id.objectionEditText);

        objectionEditText.setText(objection.getObjection());

        this.editObjectionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            hideKeyboard(v);
            String newObjection = objectionEditText.getText().toString();

            if (newObjection.isEmpty()) {
                TextInputLayout inputLayout = dialog.findViewById(R.id.objectionTextInputLayout);
                inputLayout.setError(dialog.getContext().getString(R.string.objection_dialog_error_empty_objection)); // show error
            } else {
                if (!newObjection.equals(objection.getObjection())) {
                    objection.setObjection(newObjection);
                    model.editObjection(objection);
                    clearObjectionDialog(dialog);
                    dialog.dismiss();
                }
            }
        });
        this.editObjectionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(v -> {
            hideKeyboard(v);
            clearObjectionDialog(dialog);
            dialog.cancel();
        });
        this.editObjectionDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(v -> {
            hideKeyboard(v);
            model.removeObjection(objection);
            clearObjectionDialog(dialog);
            dialog.dismiss();
        });
    }

    private void clearObjectionDialog(AlertDialog dialog) {
        TextInputLayout inputLayout = dialog.findViewById(com.rgp.asks.R.id.objectionTextInputLayout);
        TextInputEditText objectionEditText = dialog.findViewById(com.rgp.asks.R.id.objectionEditText);

        inputLayout.setError(null);
        objectionEditText.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenEditObjectionDialogEvent(OpenEditObjectionDialogEvent event) {
        Objection objection = objectionsRecyclerViewAdapter.getItem(event.objectionPositionInRecyclerView);
        if (objection != null) {
            this.showEditObjectionDialog(objection);
        } else {
            Toast.makeText(this.getActivity(), "This argument don't exist!", Toast.LENGTH_SHORT);
        }
    }
}