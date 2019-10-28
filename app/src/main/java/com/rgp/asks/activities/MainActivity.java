package com.rgp.asks.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.ad.Ader;
import com.rgp.asks.ad.AderCreator;
import com.rgp.asks.ad.OnAdLoadingErrorListener;
import com.rgp.asks.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements AderCreator, OnAdLoadingErrorListener {

    private MainViewModel mainViewModel;
    private Ader ader;
    private AlertDialog adLoadErrorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        initViewModel();
        createAder();
    }

    @Override
    public void createAder() {
        this.ader = new Ader(this, this, this.mainViewModel);
        this.adLoadErrorDialog = createAdLoadErrorDialog();
    }

    @Override
    @NonNull
    public AlertDialog createAdLoadErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setMessage(getString(R.string.onAdLoadingErrorMessage))
                .setPositiveButton(getString(R.string.onAdLoadingErrorConfirmButton), null)
        ;
        builder.setOnDismissListener(dialog -> {
            this.ader.forceRequestToAdLoad();
        });
        return builder.create();
    }

    @Override
    public void openAdLoadingErrorDialog() {
        if (!this.adLoadErrorDialog.isShowing()) {
            this.adLoadErrorDialog.show();
        }
    }

    @Override
    public void onAdLoadingError() {
        openAdLoadingErrorDialog();
    }

    @Override
    public void requestToShowAd() {
        this.ader.requestToShowAd();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.destination_episodes);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                default:
                    getSupportActionBar().show();
                    resetAppBarScroll();
                    resetFloatingActionButton();
                    break;
            }
        });
    }

    private void resetAppBarScroll() {
        ((AppBarLayout) findViewById(R.id.appbar)).setExpanded(true, true);
    }

    private void resetFloatingActionButton() {
        FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);
        if (floatingActionButton != null) {
            floatingActionButton.setVisibility(View.GONE);
            floatingActionButton.setOnClickListener(null);
        }
    }

    private void initViewModel() {
        this.mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        if (item.getItemId() == android.R.id.home) {
            this.getOnBackPressedDispatcher().onBackPressed();
        }
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}