package com.rgp.asks.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.rgp.asks.R;
import com.rgp.asks.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        initViewModel();
    }

    private void setupToolbar() {
        this.toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setTitle(R.string.destination_episodes);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                default:
                    this.toolbar.removeView(this.toolbar.findViewById(R.id.search));
                    this.toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    this.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                    this.toolbar.setContentInsetStartWithNavigation(16);
                    break;
                case R.id.searchFragment:
                    getLayoutInflater().inflate(R.layout.search_view, this.toolbar);
                    this.toolbar.setBackgroundColor(getResources().getColor(R.color.white));
                    this.toolbar.setContentInsetStartWithNavigation(0);
                    break;
            }
        });
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