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
import com.rgp.asks.messages.CreatedBeliefEvent;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WhyFragment extends Fragment {

    private BeliefRecyclerViewAdapter beliefsRecyclerViewAdapter;
    private EpisodeViewModel model;
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
                beliefsRecyclerViewAdapter,
                fragmentView.findViewById(R.id.search)
        );
        initViewModel();
        int episodeIdToLoad = model.getEpisodeId();
        if (episodeIdToLoad != -1) {
            model.getBeliefsForEpisode().observe(this, beliefs -> {
                beliefsRecyclerViewAdapter.setBeliefs(beliefs);
                searcher.restoreSearchIfNecessary();
            });
        } else {
            //todo:err
        }
    }

    /**
     * Handle click on addBeliefButtonView and create a new empty Belief.
     *
     */
    private void setupFAB() {
        FloatingActionButton beliefsFab = requireParentFragment().requireView().findViewById(R.id.addBeliefFab);
        beliefsFab.setOnClickListener(v -> createNewBelief());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initViewModel() {
        model = ViewModelProviders.of(requireParentFragment()).get(EpisodeViewModel.class);
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView beliefsRecyclerView = rootView.findViewById(R.id.recyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerViewAdapter = new BeliefRecyclerViewAdapter(getResources().getString(R.string.destination_asks_unnamed_belief), createOnItemRecyclerViewClickListener());
        beliefsRecyclerView.setAdapter(beliefsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Belief belief = ((BeliefRecyclerViewAdapter) recyclerView.getAdapter()).getItem(position);

            if (belief != null) {
                this.startEditBeliefActivity(belief.getId(), belief.getBelief());
            } else {
                Toast.makeText(requireContext(), "This belief don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void createNewBelief() {
        model.createBeliefForEpisode("");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedBeliefEvent(CreatedBeliefEvent event) {
        this.startEditBeliefActivity(event.beliefId, event.thought);
    }

    private void startEditBeliefActivity(int beliefId, @NonNull String thought) {
        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(Constants.ARG_BELIEF_ID, beliefId);
        argumentsBundle.putString(Constants.ARG_BELIEF_TITLE, thought);
        Navigation.findNavController(requireActivity(), R.id.nav_host_fragment).navigate(R.id.action_asksActivity_to_addNewBeliefFragment, argumentsBundle);
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
