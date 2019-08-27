package com.rgp.asks.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.adapters.BeliefRVAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.messages.CreatedBeliefEvent;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WhyFragment extends Fragment {

    private BeliefRVAdapter beliefsRecyclerViewAdapter;
    private EpisodeViewModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.rgp.asks.R.layout.fragment_why_ask, container, false);
        setupFAB(container);
        setupRecyclerView(rootView);
        initViewModel();
        int episodeIdToLoad = model.getEpisodeId();
        if (episodeIdToLoad != -1) {
            model.getBeliefsForEpisode().observe(this, beliefs -> beliefsRecyclerViewAdapter.setBeliefs(beliefs));
        } else {
            //todo:err
        }
        return rootView;
    }

    /**
     * Handle click on addBeliefButtonView and create a new empty Belief.
     *
     * @param container is the viewGroup of this fragment.
     */
    private void setupFAB(@NonNull ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton beliefsFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.addBeliefFab);
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
        RecyclerView beliefsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.beliefsRecyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerViewAdapter = new BeliefRVAdapter(getResources().getString(R.string.destination_asks_unnamed_belief), createOnItemRecyclerViewClickListener());
        beliefsRecyclerView.setAdapter(beliefsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Belief belief = ((BeliefRVAdapter) recyclerView.getAdapter()).getItem(position);

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
}
