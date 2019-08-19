package com.rgp.asks.fragments;

import android.content.Intent;
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
import com.rgp.asks.activities.AddNewBeliefActivity;
import com.rgp.asks.adapters.BeliefRVAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.dialogs.BeliefDialog;
import com.rgp.asks.interfaces.BeliefDialogListener;
import com.rgp.asks.messages.CreatedBeliefEvent;
import com.rgp.asks.persistence.entity.Belief;
import com.rgp.asks.viewmodel.EpisodeViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class WhyFragment extends Fragment implements BeliefDialogListener {

    private BeliefRVAdapter beliefsRecyclerViewAdapter;
    private EpisodeViewModel model;
    private BeliefDialog beliefDialog;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(com.rgp.asks.R.layout.fragment_why_ask, container, false);
        setupFAB(container);
        setupRecyclerView(rootView);
        initDialogs();
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
     * Handle click on addBeliefButtonView and open BeliefDialog.
     *
     * @param container is the viewGroup of this fragment.
     */
    private void setupFAB(@NonNull ViewGroup container) {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) container.getParent();
        FloatingActionButton beliefsFab = coordinatorLayout.findViewById(com.rgp.asks.R.id.addBeliefFab);
        beliefsFab.setOnClickListener(v -> showBeliefDialog());
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

    private void initDialogs() {
        this.beliefDialog = createBeliefDialog();
        this.beliefDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
    }

    private void initViewModel() {
        model = ViewModelProviders.of(getActivity()).get(EpisodeViewModel.class);
    }

    private void setupRecyclerView(@NonNull View rootView) {
        RecyclerView beliefsRecyclerView = rootView.findViewById(com.rgp.asks.R.id.beliefsRecyclerView);
        beliefsRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        beliefsRecyclerViewAdapter = new BeliefRVAdapter(createOnItemRecyclerViewClickListener());
        beliefsRecyclerView.setAdapter(beliefsRecyclerViewAdapter);
    }

    private View.OnClickListener createOnItemRecyclerViewClickListener() {
        return v -> {
            RecyclerView recyclerView = (RecyclerView) v.getParent();
            int position = recyclerView.getChildAdapterPosition(v);
            Belief belief = ((BeliefRVAdapter) recyclerView.getAdapter()).getItem(position);

            if (belief != null) {
                this.startEditBeliefActivity(belief.getId());
            } else {
                Toast.makeText(this.getActivity(), "This belief don't exist!", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private BeliefDialog createBeliefDialog() {
        return new BeliefDialog();
    }

    private void showBeliefDialog() {
        this.beliefDialog.show(getChildFragmentManager());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCreatedBeliefEvent(CreatedBeliefEvent event) {

    }

    private void startEditBeliefActivity(int beliefId) {
        Intent intent = new Intent(this.getActivity(), AddNewBeliefActivity.class);
        intent.putExtra(Constants.ARG_BELIEF, beliefId);
        startActivity(intent);
    }

    @Override
    public void onBeliefDialogCreateButtonClick(@NonNull String newBelief) {
        this.model.createBeliefForEpisode(newBelief);
    }
}
