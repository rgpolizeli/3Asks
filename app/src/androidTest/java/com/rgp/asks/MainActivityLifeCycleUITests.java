package com.rgp.asks;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.rgp.asks.activities.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class MainActivityLifeCycleUITests {
    @Test
    public void afterActivityOnCreate_ShowLoadingBar() {
        ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class);

        onView(withId(R.id.indeterminateBar)).check(matches(isDisplayed()));
        onView(withId(R.id.episodesRecyclerView)).check(matches(not(isDisplayed())));
    }
}
