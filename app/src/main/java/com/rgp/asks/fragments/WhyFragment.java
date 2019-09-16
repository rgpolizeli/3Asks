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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.adapters.BeliefRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.auxiliaries.Searcher;
import com.rgp.asks.interfaces.OnFloatingActionButtonClickListener;
import com.rgp.asks.interfaces.OnInsertedEntityListener;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import java.util.List;

public class WhyFragment extends Fragment implements OnFloatingActionButtonClickListener, OnInsertedEntityListener {

    private Observer<List<Belief>> observer;
    private BeliefRecyclerViewAdapter recyclerViewAdapter;
    private EpisodeViewModel model;
    private Searcher searcher;
    private OnInsertedEntityListener onInsertedEntityListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createObserver();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_recycler_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {
        setupRecyclerView(fragmentView);
        this.searcher = new Searcher(
                ((MainActivity) requireActivity()).getSupportActionBar(),
                requireParentFragment().requireView().findViewById(R.id.disableSwipeViewPager),
                requireParentFragment().requireView().findViewById(R.id.tabs),
                getFloatingActionButton(),
                recyclerViewAdapter,
                fragmentView.findViewById(R.id.search),
                fragmentView.findViewById(R.id.searchHeaderTextView)
        );
        this.searcher.setSearchHeader(getString(R.string.search_header_beliefs));
        initViewModel();
        int episodeIdToLoad = model.getEpisodeId();
        if (episodeIdToLoad != -1) {
            model.getBeliefsForEpisode().removeObservers(this);
            model.getBeliefsForEpisode().observe(this, this.observer);
        } else {
            //todo:err
        }
    }

    @NonNull
    private FloatingActionButton getFloatingActionButton() {
        return requireActivity().findViewById(R.id.floatingActionButton);
    }

    public void onFloatingActionButtonClick() {
        if (this.model != null) {
            createNewBelief();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.onInsertedEntityListener = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.onInsertedEntityListener = null;
    }

    private void createObserver() {
        this.observer = observed -> {
            recyclerViewAdapter.setData(observed);
            searcher.restoreSearchIfNecessary();
        };
    }

    private void initViewModel() {
        model = ViewModelProviders.of(requireParentFragment()).get(EpisodeViewModel.class);
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView beliefsRecyclerView = rootView.findViewById(R.id.recyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        recyclerViewAdapter = new BeliefRecyclerViewAdapter(getResources().getString(R.string.destination_asks_unnamed_belief), createOnItemRecyclerViewClickListener());
        beliefsRecyclerView.setAdapter(recyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Belief belief = ((BeliefRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);

            if (belief != null) {
                this.startEditBeliefActivity(belief.getId());
            } else {
                Toast.makeText(requireContext(), "This belief don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void createNewBelief() {
        model.createBelief("", this.onInsertedEntityListener);
    }

    private void startEditBeliefActivity(int beliefId) {
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(Constants.ARG_ID, beliefId);
        navigateDown(R.id.nav_host_fragment, R.id.action_asksActivity_to_addNewBeliefFragment, argumentsBundle);
    }

    /**
     * Navigate to a child in NavGraph, applying the necessary layout modifications.
     *
     * @param navHostId   the navigation host's id.
     * @param navActionId the navigation action's id.
     * @param arguments   the arguments to the new destination.
     */
    private void navigateDown(int navHostId, int navActionId, Bundle arguments) {
        searcher.closeSearch();
        Navigation.findNavController(requireActivity(), navHostId).navigate(navActionId, arguments);
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

    @Override
    public void onInsertedEntity(int id) {
        startEditBeliefActivity(id);
    }

}
