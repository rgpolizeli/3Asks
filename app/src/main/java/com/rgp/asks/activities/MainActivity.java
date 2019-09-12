package com.rgp.asks.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.rgp.asks.R;
import com.rgp.asks.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        initViewModel();
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
        ViewModelProviders.of(this).get(MainViewModel.class);
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