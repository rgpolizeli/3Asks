package com.example.myapplication.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.ArgumentRVAdapter;
import com.example.myapplication.messages.OpenEditArgumentDialogEvent;
import com.example.myapplication.persistence.entity.Argument;
import com.example.myapplication.viewmodel.BeliefViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class BeliefArgumentsFragment extends Fragment {

    private RecyclerView argumentsRecyclerView;
    private ArgumentRVAdapter argumentsRecyclerViewAdapter;
    private BeliefViewModel model;
    private AlertDialog newArgumentDialog;
    private AlertDialog editArgumentDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view_argument, container, false);

        EventBus.getDefault().register(this);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private void setupRecyclerView(View rootView){
        argumentsRecyclerView = rootView.findViewById(R.id.argumentRV);
        LinearLayoutManager argumentsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        argumentsRecyclerView.setLayoutManager(argumentsRecyclerViewLayoutManager);
        argumentsRecyclerViewAdapter = new ArgumentRVAdapter();
        argumentsRecyclerView.setAdapter(argumentsRecyclerViewAdapter);
    }

    private void setupFAB(ViewGroup container){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
        FloatingActionButton argumentsFab = coordinatorLayout.findViewById(R.id.addArgumentFab);
        argumentsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewArgumentDialog();
            }
        });
    }

    private void initViewModel(){
        model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);
        model.getArguments().observe(this, new Observer<List<Argument>>() {
            @Override
            public void onChanged(@Nullable final List<Argument> arguments) {
                argumentsRecyclerViewAdapter.setArguments(arguments);
            }
        });
    }

    private void initDialogs(){
        newArgumentDialog = createNewArgumentDialog();
        editArgumentDialog = createEditArgumentDialog();
    }

    private AlertDialog createEditArgumentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_argument, null))
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete",null)
                .setTitle("Edit the argument");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createNewArgumentDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_argument, null))
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                //.setNeutralButton("Delete",null)
                .setTitle("Create an argument");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void showNewArgumentDialog(){
        this.newArgumentDialog.show();
        final AlertDialog dialog = this.newArgumentDialog;

        this.newArgumentDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                TextInputEditText argumentEditText = dialog.findViewById(R.id.argumentEditText);
                String newArgument = argumentEditText.getText().toString();

                if(newArgument.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(R.id.argumentTextInputLayout);
                    inputLayout.setError("Argument is required!"); // show error
                } else{
                    model.createArgument(newArgument);
                    clearArgumentDialog(dialog);
                    dialog.dismiss();
                }
            }
        });
        this.newArgumentDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearArgumentDialog(dialog);
                dialog.cancel();
            }
        });
    }

    private void showEditArgumentDialog(@NonNull final Argument argument){
        this.editArgumentDialog.show();
        final AlertDialog dialog = this.editArgumentDialog;
        final TextInputEditText argumentEditText = dialog.findViewById(R.id.argumentEditText);

        argumentEditText.setText(argument.getArgument());

        this.editArgumentDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String newArgument = argumentEditText.getText().toString();

                if(newArgument.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(R.id.argumentTextInputLayout);
                    inputLayout.setError("Argument is required!"); // show error
                } else{
                    if ( !newArgument.equals(argument.getArgument()) ){
                        argument.setArgument(newArgument);
                        model.editArgument(argument);
                        clearArgumentDialog(dialog);
                        dialog.dismiss();
                    }
                }
            }
        });
        this.editArgumentDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearArgumentDialog(dialog);
                dialog.cancel();
            }
        });
        this.editArgumentDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                model.removeArgument(argument);
                clearArgumentDialog(dialog);
                dialog.dismiss();
            }
        });
    }

    private void clearArgumentDialog(AlertDialog dialog){
        TextInputLayout inputLayout = dialog.findViewById(R.id.argumentTextInputLayout);
        TextInputEditText argumentEditText = dialog.findViewById(R.id.argumentEditText);

        inputLayout.setError(null);
        argumentEditText.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenEditArgumentDialogEvent(OpenEditArgumentDialogEvent event){
        Argument argument = argumentsRecyclerViewAdapter.getItem(event.argumentPositionInRecyclerView);
        if (argument != null){
            this.showEditArgumentDialog(argument);
        } else{
            Toast.makeText(this.getActivity(),"This argument don't exist!", Toast.LENGTH_SHORT);
        }
    }
}
