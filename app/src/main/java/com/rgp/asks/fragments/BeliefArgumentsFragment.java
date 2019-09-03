package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.ArgumentRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Searcher;
import com.rgp.asks.dialogs.ArgumentDialog;
import com.rgp.asks.interfaces.ArgumentDialogListener;
import com.rgp.asks.persistence.entity.Argument;
import com.rgp.asks.viewmodel.BeliefViewModel;

public class BeliefArgumentsFragment extends Fragment implements ArgumentDialogListener {

    private ArgumentRecyclerViewAdapter argumentsRecyclerViewAdapter;
    private BeliefViewModel model;
    private ArgumentDialog argumentDialog;
    private Searcher searcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        setupFAB();
        setupRecyclerView(fragmentView);
        this.searcher = new Searcher(
                ((MainActivity) requireActivity()).getSupportActionBar(),
                requireParentFragment().requireView().findViewById(R.id.disableSwipeViewPager),
                requireParentFragment().requireView().findViewById(R.id.tabs),
                argumentsRecyclerViewAdapter,
                fragmentView.findViewById(R.id.search)
        );
        initDialogs();
        initViewModel();
        this.model.getArgumentsLiveData().observe(this, arguments -> {
            argumentsRecyclerViewAdapter.setArguments(arguments);
            searcher.restoreSearchIfNecessary();
        });
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView argumentsRecyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager argumentsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        argumentsRecyclerView.setLayoutManager(argumentsRecyclerViewLayoutManager);
        argumentsRecyclerViewAdapter = new ArgumentRecyclerViewAdapter(createOnItemRecyclerViewClickListener());
        argumentsRecyclerView.setAdapter(argumentsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Argument argument = ((ArgumentRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);

            if (argument != null) {
                this.showArgumentDialogInEditMode(argument.getId(), argument.getArgument());
            } else {
                Toast.makeText(requireContext(), "This argument don't exist!", Toast.LENGTH_SHORT).show();
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

    private void setupFAB() {
        FloatingActionButton argumentsFab = requireParentFragment().requireView().findViewById(com.rgp.asks.R.id.addArgumentFab);
        argumentsFab.setOnClickListener(v -> showArgumentDialogInCreateMode());
    }

    private void initViewModel() {
        model = ViewModelProviders.of(requireParentFragment()).get(BeliefViewModel.class);
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
        Argument argument = new Argument(argumentId, newArgument, this.model.getBeliefId());
        model.editArgument(argument);
    }

    @Override
    public void onArgumentDialogDeleteButtonClick(int argumentId) {
        Argument argumentToDelete = new Argument(argumentId, "", this.model.getBeliefId());
        model.removeArgument(argumentToDelete);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_search, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            searcher.openSearch();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
