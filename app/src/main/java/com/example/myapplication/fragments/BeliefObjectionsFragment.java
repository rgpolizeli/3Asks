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
import com.example.myapplication.adapters.ObjectionRVAdapter;
import com.example.myapplication.messages.OpenEditObjectionDialogEvent;
import com.example.myapplication.persistence.entity.Objection;
import com.example.myapplication.viewmodel.BeliefViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class BeliefObjectionsFragment extends Fragment {

    private RecyclerView objectionsRecyclerView;
    private ObjectionRVAdapter objectionsRecyclerViewAdapter;
    private BeliefViewModel model;
    private AlertDialog newObjectionDialog;
    private AlertDialog editObjectionDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.recycler_view_objection, container, false);

        EventBus.getDefault().register(this);

        setupFAB(container);

        setupRecyclerView(rootView);

        initDialogs();

        initViewModel();

        return rootView;
    }

    private void setupFAB(ViewGroup container){
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)container.getParent();
        FloatingActionButton objectionsFab = coordinatorLayout.findViewById(R.id.addObjectionFab);
        objectionsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewObjectionDialog();
            }
        });
    }

    private void setupRecyclerView(View rootView){
        objectionsRecyclerView = rootView.findViewById(R.id.objectionRV);
        LinearLayoutManager objectionsRecyclerViewLayoutManager = new LinearLayoutManager(rootView.getContext());
        objectionsRecyclerView.setLayoutManager(objectionsRecyclerViewLayoutManager);
        objectionsRecyclerViewAdapter = new ObjectionRVAdapter();
        objectionsRecyclerView.setAdapter(objectionsRecyclerViewAdapter);
    }

    private void initViewModel(){
        model = ViewModelProviders.of(this.getActivity()).get(BeliefViewModel.class);
        model.getObjections().observe(this, new Observer<List<Objection>>() {
            @Override
            public void onChanged(@Nullable final List<Objection> objections) {
                objectionsRecyclerViewAdapter.setObjections(objections);
            }
        });
    }

    private void initDialogs(){
        newObjectionDialog = createNewObjectionDialog();
        editObjectionDialog = createEditObjectionDialog();
    }

    private AlertDialog createEditObjectionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_objection, null))
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Delete",null)
                .setTitle("Edit the objection");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private AlertDialog createNewObjectionDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        LayoutInflater inflater = this.getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_new_objection, null))
                .setPositiveButton("Create", null)
                .setNegativeButton("Cancel", null)
                //.setNeutralButton("Delete",null)
                .setTitle("Create an objection");

        AlertDialog dialog = builder.create();
        return dialog;
    }

    private void showNewObjectionDialog(){
        this.newObjectionDialog.show();
        final AlertDialog dialog = this.newObjectionDialog;

        this.newObjectionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                TextInputEditText objectionEditText = dialog.findViewById(R.id.objectionEditText);
                String newObjection = objectionEditText.getText().toString();

                if(newObjection.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(R.id.objectionTextInputLayout);
                    inputLayout.setError("Objection is required!"); // show error
                } else{
                    model.createObjection(newObjection);
                    clearObjectionDialog(dialog);
                    dialog.dismiss();
                }
            }
        });
        this.newObjectionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearObjectionDialog(dialog);
                dialog.cancel();
            }
        });
    }

    private void showEditObjectionDialog(@NonNull final Objection objection){
        this.editObjectionDialog.show();
        final AlertDialog dialog = this.editObjectionDialog;
        final TextInputEditText objectionEditText = dialog.findViewById(R.id.objectionEditText);

        objectionEditText.setText(objection.getObjection());

        this.editObjectionDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                String newObjection = objectionEditText.getText().toString();

                if(newObjection.isEmpty()){
                    TextInputLayout inputLayout = dialog.findViewById(R.id.objectionTextInputLayout);
                    inputLayout.setError("Objection is required!"); // show error
                } else{
                    if ( !newObjection.equals(objection.getObjection()) ){
                        objection.setObjection(newObjection);
                        model.editObjection(objection);
                        clearObjectionDialog(dialog);
                        dialog.dismiss();
                    }
                }
            }
        });
        this.editObjectionDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                clearObjectionDialog(dialog);
                dialog.cancel();
            }
        });
        this.editObjectionDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                model.removeObjection(objection);
                clearObjectionDialog(dialog);
                dialog.dismiss();
            }
        });
    }

    private void clearObjectionDialog(AlertDialog dialog){
        TextInputLayout inputLayout = dialog.findViewById(R.id.objectionTextInputLayout);
        TextInputEditText objectionEditText = dialog.findViewById(R.id.objectionEditText);

        inputLayout.setError(null);
        objectionEditText.setText("");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOpenEditObjectionDialogEvent(OpenEditObjectionDialogEvent event){
        Objection objection = objectionsRecyclerViewAdapter.getItem(event.objectionPositionInRecyclerView);
        if (objection != null){
            this.showEditObjectionDialog(objection);
        } else{
            Toast.makeText(this.getActivity(),"This argument don't exist!", Toast.LENGTH_SHORT);
        }
    }
}