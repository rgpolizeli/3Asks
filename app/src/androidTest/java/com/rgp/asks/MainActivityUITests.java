/*
package com.rgp.asks;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.rgp.asks.activities.MainActivity;
import com.rgp.asks.fragments.AsksFragment;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.util.Calendar;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

@RunWith(AndroidJUnit4.class)
public class MainActivityUITests {

    @Rule
    public IntentsTestRule<MainActivity> mainActivityActivityTestRule =
            new IntentsTestRule<>(MainActivity.class);

    @Test
    public void clickOnNewEpisodeFloatingActionButton_OpenNewEpisodeDialog() {
        //given: newEpisodeFloatingActionButton.
        //when: user click on it.
        //then: newEpisodeDialog is show.
        openNewEpisodeDialog();
        onView(withId(R.id.episodeDialog)).check(matches(isDisplayed()));
    }

    private void openNewEpisodeDialog() {
        onView(withId(R.id.newEpisodeFloatingActionButton)).perform(click());
    }

    private void clickOnPositiveButtonOfNewEpisodeDialog() {
        onView(withText(R.string.episode_dialog_positive_button))
                .inRoot(isDialog())
                .perform(click())
        ;
    }

    private void clickOnNegativeButtonOfNewEpisodeDialog() {
        onView(withText(R.string.episode_dialog_negative_button))
                .inRoot(isDialog())
                .perform(click())
        ;
    }

    @Test
    public void clickOnPositiveButtonOfNewEpisodeDialog_WithEmptyEpisodeName_ShowError() {
        openNewEpisodeDialog();
        clickOnPositiveButtonOfNewEpisodeDialog();
        onView(withText(R.string.episode_dialog_error_empty_episode_name)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnNegativeButtonOfNewEpisodeDialog_WithAnErrorInEpisodeName_ResetStateOfNewEpisodeDialog() {
        //given: newEpisodeDialog with an error message in field newEpisodeNameTextInputLayout.
        //when: user click on cancel button.
        //then: newEpisodeDialog is reset to a default state.
        openNewEpisodeDialog();
        clickOnPositiveButtonOfNewEpisodeDialog();
        onView(withText(R.string.episode_dialog_error_empty_episode_name)).check(matches(isDisplayed()));
        clickOnNegativeButtonOfNewEpisodeDialog();
        openNewEpisodeDialog();
        onView(withText(R.string.episode_dialog_error_empty_episode_name)).check(doesNotExist());

        String defaultEpisodeName = "";
        //onView(withId(R.id.episodeEditText)).check(matches(withText(defaultEpisodeName)));

        String defaultDate = DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());
        //onView(withId(R.id.episodeDateEditText)).check(matches(withText(defaultDate)));

        String defaultPeriodInSpinner = mainActivityActivityTestRule.getActivity().getResources().getStringArray(R.array.episode_period_array)[0];
        onView(withId(R.id.episodePeriodSpinner)).check(matches(withSpinnerText(containsString(defaultPeriodInSpinner))));
    }

    @Test
    public void clickOnNegativeButtonOfNewEpisodeDialog_WithoutAnErrorInEpisodeName_ResetStateOfNewEpisodeDialog() {
        //given: newEpisodeDialog without an error message in field newEpisodeNameTextInputLayout, but with typed text and selected a date and period.
        //when: user click on cancel button.
        //then: newEpisodeDialog is reset to a default state.

    }

    @Test
    public void clickOnActionHelpMenuOption_OpenHelpInfoDialog() {
        //given: the menu option action_help
        //when: user click on it.
        //then: helpInfoDialog is show.
        onView(withId(R.id.action_help))
                .perform(click());
        onView(withId(R.id.helpInfoDialog)).check(matches(isDisplayed()));
        onView(withId(R.id.helpInfoDialogCloseImageButton)).check(matches(isDisplayed()));
        onView(withId(R.id.websiteTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.howItWorksTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.exampleOfUseTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.termsOfUseTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.privacyPolicyTextView)).check(matches(isDisplayed()));
        onView(withId(R.id.contactTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void afterActivityOnCreate_ShowLoadingBar() {
        //ondata in recycler view, check if indeterminatebar is not displayed.
    }

    @Test
    public void clickOnItemInEpisodesRecyclerView_OpenAsksActivity() {
        //given: the EpisodesRecyclerView isn't empty
        //when: user click on any episode.
        //then: AsksFragment is opened.

        if (getRVcount() > 0) {
            onView(withId(R.id.episodesRecyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            intended(hasComponent(AsksFragment.class.getName()));
        }
    }

    private int getRVcount() {
        RecyclerView recyclerView = mainActivityActivityTestRule.getActivity().findViewById(R.id.episodesRecyclerView);
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        if (adapter == null) {
            return 0;
        } else {
            return adapter.getItemCount();
        }
    }

    //click on positive button of the newEpisodeDialog, create episode and go to AsksFragment.

    //click on negative button, close dialog, clean views to default values.

    //reopen the newEpisodeDialog after clicked in negative button

    //reopen the newEpisodeDialog after change orientation of activity or minimize it
    //after typed a valid inputs
    //after try create a episode with invalid inputs, ie, with an error message.

    // try create an episode without name, check erro messsage

    // click onhelp info menu, open dialog

    //click on close button of help info dialog

    // check if episode name is in top of other elements
    // check if date and period is side by side

    //check the date format

    //check the string in spinner

    //when episodes loaded, check whether or not the loading isn't showed, and recycler view is.


}
*/