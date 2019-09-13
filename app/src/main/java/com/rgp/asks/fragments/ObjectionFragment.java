package com.rgp.asks.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.interfaces.OnDeletedEntityListener;
import com.rgp.asks.interfaces.OnUpdatedEntityListener;
import com.rgp.asks.persistence.entity.Objection;
import com.rgp.asks.viewmodel.ObjectionViewModel;
import com.rgp.asks.views.TextInputLayout;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ObjectionFragment extends Fragment implements OnUpdatedEntityListener, OnDeletedEntityListener {

    private TextInputLayout objectionTextInputLayout;

    private OnUpdatedEntityListener onUpdatedEntityListener;
    private OnDeletedEntityListener onDeletedEntityListener;

    private ObjectionViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                openUnsavedDialog();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_objection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragmentView, Bundle savedInstanceState) {

        setupFloatingActionButton(
                ColorStateList.valueOf(getResources().getColor(R.color.secondaryColor)),
                R.drawable.ic_save_white_24dp,
                v -> {
                    hideKeyboard(v);
                    save();
                }
        );
        initViewModel();
        int id = requireArguments().getInt(Constants.ARG_ID);
        this.model.setId(id);
        if (id != -1) {
            this.model.getEntityLiveData().observe(this, objection -> {
                initViews(fragmentView);
                if (model.isInFirstLoad()) {
                    loadFragmentFromViewModel(objection);
                    model.initModifiableCopy(objection);
                    model.setIsInFirstLoad(false);
                }
                setupViewListeners();
            });
        } else {
            //todo: err
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        this.onUpdatedEntityListener = this;
        this.onDeletedEntityListener = this;
    }

    @Override
    public void onStop() {
        super.onStop();
        this.onUpdatedEntityListener = null;
        this.onDeletedEntityListener = null;
    }

    private void initViewModel() {
        this.model = ViewModelProviders.of(this).get(ObjectionViewModel.class);
    }

    private AlertDialog createUnsavedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder
                .setMessage(this.getString(R.string.save_dialog_title))
                .setPositiveButton(this.getString(R.string.episode_save_dialog_save_button), (dialog, id) -> {
                    this.model.update(this.onUpdatedEntityListener);
                    finish();
                })
                .setNegativeButton(this.getString(R.string.episode_save_dialog_discard_button), (dialog, id) -> {
                    finish();
                })
        ;
        return builder.create();
    }

    private void finish() {
        Navigation.findNavController(requireActivity().findViewById(R.id.nav_host_fragment)).navigateUp();
    }

    private void openUnsavedDialog() {
        AlertDialog unsavedDialog = createUnsavedDialog();
        if (model.wasChanged()) {
            unsavedDialog.show();
        } else {
            finish();
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @NonNull
    private FloatingActionButton getFloatingActionButton() {
        return requireActivity().findViewById(R.id.floatingActionButton);
    }

    private void setupFloatingActionButton(@NonNull ColorStateList backgroundTintList, int imageResourceId, @NonNull View.OnClickListener onClickListener) {
        FloatingActionButton floatingActionButton = getFloatingActionButton();
        floatingActionButton.setBackgroundTintList(backgroundTintList);
        floatingActionButton.setImageResource(imageResourceId);
        floatingActionButton.setOnClickListener(onClickListener);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private void initViews(@NonNull View rootView) {
        this.objectionTextInputLayout = rootView.findViewById(R.id.objectionTextInputLayout);
    }

    private void save() {
        if (this.model.wasChanged()) {
            this.model.update(this.onUpdatedEntityListener);
        } else {
            this.onUpdatedEntityListener.onUpdatedEntity(1);
        }
    }

    private void loadFragmentFromViewModel(@NonNull Objection objection) {
        this.objectionTextInputLayout.setValue(objection.getObjection());
    }

    private void setupViewListeners() {
        objectionTextInputLayout.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Objection objection = model.getModifiableCopy();
                objection.setObjection(s.toString());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_delete, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            this.model.delete(this.onDeletedEntityListener);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDeletedEntity(int id) {
        Toast.makeText(requireActivity(), "Deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onUpdatedEntity(int numberOfUpdatedRows) {
        if (numberOfUpdatedRows > 0) {
            Toast.makeText(requireActivity(), "Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireActivity(), "Error in save!", Toast.LENGTH_SHORT).show();
        }
    }
}