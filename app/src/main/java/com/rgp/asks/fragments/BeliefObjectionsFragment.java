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
import com.rgp.asks.adapters.ObjectionRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Searcher;
import com.rgp.asks.dialogs.ObjectionDialog;
import com.rgp.asks.interfaces.ObjectionDialogListener;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.viewmodel.BeliefViewModel;

public class BeliefObjectionsFragment extends Fragment implements ObjectionDialogListener {

    private ObjectionRecyclerViewAdapter objectionsRecyclerViewAdapter;
    private BeliefViewModel model;
    private ObjectionDialog objectionDialog;
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
                objectionsRecyclerViewAdapter,
                fragmentView.findViewById(R.id.search)
        );
        initDialogs();
        initViewModel();
        this.model.getObjectionsLiveData().observe(this, objections -> {
            objectionsRecyclerViewAdapter.setObjections(objections);
            searcher.restoreSearchIfNecessary();
        });
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

    private void setupFAB() {
        FloatingActionButton objectionsFab = requireParentFragment().requireView().findViewById(com.rgp.asks.R.id.addObjectionFab);
        objectionsFab.setOnClickListener(v -> showObjectionDialogInCreateMode());
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView objectionsRecyclerView = rootView.findViewById(R.id.recyclerView);
        LinearLayoutManager objectionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        objectionsRecyclerView.setLayoutManager(objectionsRecyclerViewLayoutManager);
        objectionsRecyclerViewAdapter = new ObjectionRecyclerViewAdapter(createOnItemRecyclerViewClickListener());
        objectionsRecyclerView.setAdapter(objectionsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Objection objection = ((ObjectionRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);

            if (objection != null) {
                this.showObjectionDialogInEditMode(objection.getId(), objection.getObjection());
            } else {
                Toast.makeText(requireContext(), "This objection don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void initViewModel() {
        model = ViewModelProviders.of(requireParentFragment()).get(BeliefViewModel.class);
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
        Objection objection = new Objection(objectionId, newObjection, this.model.getBeliefId());
        model.editObjection(objection);
    }

    @Override
    public void onObjectionDialogDeleteButtonClick(int objectionId) {
        Objection objectionToDelete = new Objection(objectionId, "", this.model.getBeliefId());
        model.removeObjection(objectionToDelete);
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