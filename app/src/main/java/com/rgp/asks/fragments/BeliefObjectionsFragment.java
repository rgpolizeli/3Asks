package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.adapters.ObjectionRVAdapter;
import com.rgp.asks.dialogs.ObjectionDialog;
import com.rgp.asks.interfaces.ObjectionDialogListener;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.viewmodel.BeliefViewModel;

public class BeliefObjectionsFragment extends Fragment implements ObjectionDialogListener {

    private RecyclerView objectionsRecyclerView;
    private ObjectionRVAdapter objectionsRecyclerViewAdapter;
    private BeliefViewModel model;
    private ObjectionDialog objectionDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(com.rgp.asks.R.layout.recycler_view_objection, container, false);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private ObjectionDialog createObjectionDialog() {
        return new ObjectionDialog();
    }

    private void showObjectionDialogInCreateMode() {
        this.objectionDialog.showInCreateMode(getChildFragmentManager());
    }

    private void showObjectionDialogInEditMode(int objectionId, @NonNull String objection) {
        this.objectionDialog.showInEditMode(getChildFragmentManager(), objectionId, objection);
    }

    private void setupFAB(@NonNull ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton objectionsFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.addObjectionFab);
        objectionsFab.setOnClickListener(v -> showObjectionDialogInCreateMode());
    }

    private void setupRecyclerView(@NonNull View rootView) {
        objectionsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.objectionRV);
        LinearLayoutManager objectionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        objectionsRecyclerView.setLayoutManager(objectionsRecyclerViewLayoutManager);
        objectionsRecyclerViewAdapter = new ObjectionRVAdapter(createOnItemRecyclerViewClickListener());
        objectionsRecyclerView.setAdapter(objectionsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Objection objection = ((ObjectionRVAdapter) recyclerView.getAdapter()).getItem(position);

            if (objection != null) {
                this.showObjectionDialogInEditMode(objection.getId(), objection.getObjection());
            } else {
                Toast.makeText(this.getActivity(), "This objection don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initViewModel() {
        model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);
        model.getObjections().observe(this, objections -> objectionsRecyclerViewAdapter.setObjections(objections));
    }

    private void initDialogs() {
        this.objectionDialog = createObjectionDialog();
        this.objectionDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public void onObjectionDialogCreateButtonClick(@NonNull String newObjection) {
        model.createObjection(newObjection);
    }

    @Override
    public void onObjectionDialogSaveButtonClick(int objectionId, @NonNull String newObjection) {
        Objection objection = new Objection(objectionId, newObjection, model.getBelief().getValue().getId());
        model.editObjection(objection);
    }

    @Override
    public void onObjectionDialogDeleteButtonClick(int objectionId) {
        Objection objectionToDelete = new Objection(objectionId, "", model.getBelief().getValue().getId());
        model.removeObjection(objectionToDelete);
    }
}