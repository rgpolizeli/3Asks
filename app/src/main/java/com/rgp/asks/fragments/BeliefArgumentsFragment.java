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
import com.rgp.asks.adapters.ArgumentRVAdapter;
import com.rgp.asks.dialogs.ArgumentDialog;
import com.rgp.asks.interfaces.ArgumentDialogListener;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.viewmodel.BeliefViewModel;

public class BeliefArgumentsFragment extends Fragment implements ArgumentDialogListener {

    private RecyclerView argumentsRecyclerView;
    private ArgumentRVAdapter argumentsRecyclerViewAdapter;
    private BeliefViewModel model;
    private ArgumentDialog argumentDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.rgp.asks.R.layout.recycler_view_argument, container, false);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private void setupRecyclerView(@NonNull View rootView) {
        argumentsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.argumentRV);
        LinearLayoutManager argumentsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        argumentsRecyclerView.setLayoutManager(argumentsRecyclerViewLayoutManager);
        argumentsRecyclerViewAdapter = new ArgumentRVAdapter(createOnItemRecyclerViewClickListener());
        argumentsRecyclerView.setAdapter(argumentsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Argument argument = ((ArgumentRVAdapter) recyclerView.getAdapter()).getItem(position);

            if (argument != null) {
                this.showArgumentDialogInEditMode(argument.getId(), argument.getArgument());
            } else {
                Toast.makeText(this.getActivity(), "This argument don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private ArgumentDialog createArgumentDialog() {
        return new ArgumentDialog();
    }

    private void showArgumentDialogInCreateMode() {
        this.argumentDialog.showInCreateMode(getChildFragmentManager());
    }

    private void showArgumentDialogInEditMode(int argumentId, @NonNull String argument) {
        this.argumentDialog.showInEditMode(getChildFragmentManager(), argumentId, argument);
    }

    private void setupFAB(@NonNull ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton argumentsFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.addArgumentFab);
        argumentsFab.setOnClickListener(v -> showArgumentDialogInCreateMode());
    }

    private void initViewModel() {
        model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);
        model.getArguments().observe(this, arguments -> argumentsRecyclerViewAdapter.setArguments(arguments));
    }

    private void initDialogs() {
        this.argumentDialog = createArgumentDialog();
        this.argumentDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    @Override
    public void onArgumentDialogCreateButtonClick(@NonNull String newArgument) {
        model.createArgument(newArgument);
    }

    @Override
    public void onArgumentDialogSaveButtonClick(int argumentId, @NonNull String newArgument) {
        Argument argument = new Argument(argumentId, newArgument, model.getBelief().getValue().getId());
        model.editArgument(argument);
    }

    @Override
    public void onArgumentDialogDeleteButtonClick(int argumentId) {
        Argument argumentToDelete = new Argument(argumentId, "", model.getBelief().getValue().getId());
        model.removeArgument(argumentToDelete);
    }
}
