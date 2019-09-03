/*
package com.rgp.asks.dialogs;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.rgp.asks.R;
import com.rgp.asks.adapters.ReactionRecyclerViewAdapter;
import com.rgp.asks.auxiliaries.Constants;
import com.rgp.asks.fragments.AsksFragment;
import com.rgp.asks.fragments.WhatFragment;
import com.rgp.asks.interfaces.ReactionDialogListener;
import com.rgp.asks.persistence.entity.Reaction;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
public class ReactionDialogTests {

    @Rule
    public IntentsTestRule<AsksFragment> asksActivityActivityTestRule =
            new IntentsTestRule<>(AsksFragment.class, false, false);
    //need a valid episode id to load.
    // and this episode must has reactions to test reaction dialog in edit mode.
    int episodeId = 5;
    //the episode id must have reactions and the viewholder in this position in RecyclerView will be clicked.
    int positionOfReactionInRecyclerView = 0;

    @Before
    public void initWhatFragment() {
        openAsksActivityWithIntent();
        goToTabWithText(R.string.tab_what);
    }

    //////////////////////
    // TESTS CREATE MODE //
    /////////////////////

    @Test
    public void clickOnReactionFloatingActionButton_OpenReactionDialog_InEmptyState() {
        //given: addReactionFab.

        //when: user click on it.
        openReactionDialogInCreateMode();

        //then: reactionDialog is showed in empty state.
        testEmptyStateOfReactionDialogInCreateMode();
    }

    @Test
    public void clickOnPositiveButtonOfReactionDialog_WithEmptyReaction_ShowError() {
        openReactionDialogInCreateMode();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_create_button);
        onView(withText(R.string.reaction_dialog_error_empty_reaction))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        ;
    }

    @Test
    public void clickOnNegativeButtonOfReactionDialog_WithAnErrorInReaction_ResetStateOfReactionDialog() {
        //given: reactionDialog with an error message in field reactionTextInputLayout.
        //when: user click on cancel button and after reopen reaction dialog.
        //then: reactionDialog is reset to a default state.
        clickOnPositiveButtonOfReactionDialog_WithEmptyReaction_ShowError();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);
        clickOnReactionFloatingActionButton_OpenReactionDialog_InEmptyState();
    }

    @Test
    public void clickOnNegativeButtonOfReactionDialog_WithValidReaction_ResetStateOfReactionDialog() {
        //given: reactionDialog with a valid value in reactionTextInputLayout.
        openReactionDialogInCreateMode();
        typeReaction("depressed");

        //when: user click on cancel button and after reopen reaction dialog.
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);

        //then: reactionDialog is reset to a default state.
        clickOnReactionFloatingActionButton_OpenReactionDialog_InEmptyState();
    }

    @Test
    public void changeScreenOrientation_WithAnErrorInReaction_SaveStateOfReactionDialogInCreateMode() {

        //given: reactionDialog with an error message in field reactionTextInputLayout.
        clickOnPositiveButtonOfReactionDialog_WithEmptyReaction_ShowError();
        String reaction = getValueOfReactionEditText();
        String reactionClass = getValueOfReactionClassSpinner();

        //when: user rotate screen.
        changeScreenOrientation();

        //then: reactionDialog is recreated with the same state.
        testTitleOfDialog(R.string.reaction_dialog_create_title);
        testTextOfButtonOfDialog(R.id.positiveReactionButton, R.string.reaction_dialog_create_button);
        testNeutralButtonOfReactionDialogInCreateMode();
        testTextOfButtonOfDialog(R.id.negativeReactionButton, R.string.reaction_dialog_cancel_button);
        testInvalidTypedReactionMessageIsDisplayedOnReactionDialog();
        testValueOfReactionEditText(reaction);
        testValueOfReactionClassSpinner(reactionClass);
    }

    @Test
    public void changeScreenOrientation_WithValidReaction_SaveStateOfReactionDialogInCreateMode() {

        //given: reactionDialog with a valid value in reactionTextInputLayout.
        openReactionDialogInCreateMode();
        String reaction = "depressed";
        typeReaction(reaction);
        String reactionClass = selectReactionClassInSpinner(1);

        //when: user rotate screen.
        changeScreenOrientation();

        //then: reactionDialog is recreated with the same state.
        testTitleOfDialog(R.string.reaction_dialog_create_title);
        testTextOfButtonOfDialog(R.id.positiveReactionButton, R.string.reaction_dialog_create_button);
        testNeutralButtonOfReactionDialogInCreateMode();
        testTextOfButtonOfDialog(R.id.negativeReactionButton, R.string.reaction_dialog_cancel_button);
        testInvalidTypedReactionMessageIsNotDisplayedOnReactionDialog();
        testValueOfReactionEditText(reaction);
        testValueOfReactionClassSpinner(reactionClass);
    }

    //////////////////////
    // TESTS EDIT MODE //
    /////////////////////

    @Test
    public void clickOnItemInReactionsRecyclerView_OpenReactionDialogInEditMode_InDefaultState() {
        //given: reactionsRecyclerView.

        //when: user click on one item in it.
        openReactionDialogInEditMode();

        //then: reactionDialog is showed in default state of the edit mode.
        testDefaultStateOfReactionDialogInEditMode();
    }

    @Test
    public void reClickOnItemInReactionsRecyclerView_reOpenReactionDialogInEditMode_InDefaultState() {
        //given: user already clicked on item in reactionsRecyclerView, the reactionDialog opened, user dismiss this reactionDialog.
        openReactionDialogInEditMode();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);

        //when: user reclick on the same item.
        openReactionDialogInEditMode();

        //then: reactionDialog is showed in default state of the edit mode.
        testDefaultStateOfReactionDialogInEditMode();
    }

    @Test
    public void reClickOnItemInReactionsRecyclerView_changeScreenOrientation_reOpenReactionDialogInEditMode_InDefaultState() {
        //given: user already clicked on item in reactionsRecyclerView, the reactionDialog opened, user dismiss this reactionDialog.
        openReactionDialogInEditMode();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);

        //when: user reclick on the same item and change screen orientation.
        openReactionDialogInEditMode();
        changeScreenOrientation();

        //then: reactionDialog is showed in default state of the edit mode.
        testDefaultStateOfReactionDialogInEditMode();
    }

    @Test
    public void clickOnSaveButtonOfReactionDialog_WithEmptyReaction_ShowError_Dismiss_reClickOnItemInReactionsRecyclerView_reOpenReactionDialogInEditMode_InDefaultState() {
        //given: user already clicked on item in reactionsRecyclerView, the reactionDialog opened, user dismiss this reactionDialog.
        openReactionDialogInEditMode();
        clearReactionEditText();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_save_button);
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);

        //when: user reclick on the same item.
        openReactionDialogInEditMode();

        //then: reactionDialog is showed in default state of the edit mode.
        testDefaultStateOfReactionDialogInEditMode();
    }

    @Test
    public void clickOnSaveButtonOfReactionDialog_WithEmptyReaction_ShowError() {
        //given: reactionDialog in edit mode with empty reaction.
        openReactionDialogInEditMode();
        clearReactionEditText();

        //when: user click on save button.
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_save_button);

        //then: invalid typed reaction error message is displayed.
        testInvalidTypedReactionMessageIsDisplayedOnReactionDialog();
    }

    @Test
    public void clickOnSaveButtonOfReactionDialog_WithEmptyReaction_changeScreenOrientation_ShowError() {
        //given: reactionDialog in edit mode with empty reaction.
        openReactionDialogInEditMode();
        clearReactionEditText();

        //when: user click on save button and change screen orientation.
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_save_button);
        changeScreenOrientation();

        //then: invalid typed reaction error message is displayed.
        testInvalidTypedReactionMessageIsDisplayedOnReactionDialog();
    }

    @Test
    public void changeScreenOrientation_WithUnmodifiedReaction_SaveStateOfReactionDialogInEditMode() {
        //given: reactionDialog in edit mode with unmodified values in reactionEditText and reactionClassSpinner.
        openReactionDialogInEditMode();

        //when user rotate screen
        changeScreenOrientation();

        //then
        testDefaultStateOfReactionDialogInEditMode();
    }

    @Test
    public void changeScreenOrientation_WithValidModifiedReaction_SaveStateOfReactionDialogInEditMode() {
        //given: reactionDialog in edit mode with valid modified values in reactionEditText and reactionClassSpinner.
        openReactionDialogInEditMode();
        clearReactionEditText();
        String reaction = "depressed";
        typeReaction(reaction);
        String reactionClass = selectReactionClassInSpinner(2);

        //when user rotate screen
        changeScreenOrientation();

        //then
        testValidModifiedStateOfReactionDialogInEditMode(reaction, reactionClass);
    }

    @Test
    public void reClickOnItemInReactionsRecyclerView_reOpenReactionDialogInEditMode_InDefaultState_changeScreenOrientation_WithValidModifiedReaction_SaveStateOfReactionDialogInEditMode() {
        //given: reactionDialog in edit mode with valid modified values in reactionEditText and reactionClassSpinner.
        openReactionDialogInEditMode();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);

        //when user rotate screen
        openReactionDialogInEditMode();
        clearReactionEditText();
        String reaction = "depressed";
        typeReaction(reaction);
        String reactionClass = selectReactionClassInSpinner(2);
        changeScreenOrientation();

        //then
        testValidModifiedStateOfReactionDialogInEditMode(reaction, reactionClass);
    }

    @Test
    public void openInCreateMode_Dismiss_OpenInEditMode_InDefaultState() {
        //given: user open reactionDialog in create mode and after dismiss it.
        openReactionDialogInCreateMode();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);

        //when: user open the reactionDialog in edit mode.
        openReactionDialogInEditMode();

        //then: reactionDialog is in edit mode with default state.
        testDefaultStateOfReactionDialogInEditMode();
    }

    @Test
    public void openInEditMode_Dismiss_OpenInCreateMode_InEmptyState() {
        //given: user open reactionDialog in edit mode and after dismiss it.
        openReactionDialogInEditMode();
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_cancel_button);

        //when: user open the reactionDialog in create mode.
        openReactionDialogInCreateMode();

        //then: reactionDialog is in create mode with empty state.
        testEmptyStateOfReactionDialogInCreateMode();
    }

    ////////////
    // LISTENER TESTS
    ////////////

    @Test
    public void clickOnCreateButton_CallListener() {
        //given: reactionDialog with valid inputs.
        openReactionDialogInCreateMode();

        String reaction = "depressed";
        typeReaction(reaction);
        String reactionClass = selectReactionClassInSpinner(1);

        ReactionDialogListener listener = mock(ReactionDialogListener.class);
        ReactionDialog reactionDialog = getReactionDialogFragmentFromWhatFragment(getWhatFragmentFromAsksActivity());
        reactionDialog.setReactionDialogListener(listener);

        //when: user clicks on createButton.
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_create_button);

        //then: the listener is called with correct parameters.
        verify(listener, times(1)).onReactionDialogCreateButtonClick(reaction, reactionClass);
    }

    @Test
    public void clickOnSaveButton_WithValidModifiedReaction_CallListener() {
        //given: reactionDialog with valid modification of edited reaction.
        Reaction reactionToEdit = getReactionAtPositionOnRecyclerView(this.positionOfReactionInRecyclerView);
        openReactionDialogInEditMode();
        clearReactionEditText();

        String newReaction = "depressed";
        typeReaction(newReaction);
        String newReactionClass = selectReactionClassInSpinner(1);

        ReactionDialogListener listener = mock(ReactionDialogListener.class);
        ReactionDialog reactionDialog = getReactionDialogFragmentFromWhatFragment(getWhatFragmentFromAsksActivity());
        reactionDialog.setReactionDialogListener(listener);

        //when: user clicks on saveButton.
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_save_button);

        //then: the listener is called with correct parameters.
        verify(listener, times(1)).onReactionDialogSaveButtonClick(reactionToEdit.getId(), newReaction, newReactionClass);
    }

    @Test
    public void clickOnSaveButton_WithoutValidModifiedReaction_NotCallListener() {
        //given: reactionDialog without modification of edited reaction.
        Reaction reactionToEdit = getReactionAtPositionOnRecyclerView(this.positionOfReactionInRecyclerView);
        openReactionDialogInEditMode();

        ReactionDialogListener listener = mock(ReactionDialogListener.class);
        ReactionDialog reactionDialog = getReactionDialogFragmentFromWhatFragment(getWhatFragmentFromAsksActivity());
        reactionDialog.setReactionDialogListener(listener);

        //when: user clicks on saveButton.
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_save_button);

        //then: the listener is called with correct parameters.
        verify(listener, times(0)).onReactionDialogSaveButtonClick(reactionToEdit.getId(), reactionToEdit.getReaction(), reactionToEdit.getReactionCategory());
    }

    @Test
    public void clickOnDeleteButton_CallListener() {
        //given: reactionDialog of edited reaction.
        Reaction reactionToEdit = getReactionAtPositionOnRecyclerView(this.positionOfReactionInRecyclerView);
        openReactionDialogInEditMode();

        ReactionDialogListener listener = mock(ReactionDialogListener.class);
        ReactionDialog reactionDialog = getReactionDialogFragmentFromWhatFragment(getWhatFragmentFromAsksActivity());
        reactionDialog.setReactionDialogListener(listener);

        //when: user clicks on deleteButton.
        clickOnButtonOfDialogWithText(R.string.reaction_dialog_delete_button);

        //then: the listener is called with correct parameters.
        verify(listener, times(1)).onReactionDialogDeleteButtonClick(reactionToEdit.getId());
    }

    ///////////////////////
    // Auxiliary methods //
    //////////////////////

    private void changeScreenOrientation() {
        int initialOrientation = asksActivityActivityTestRule.getActivity().getResources().getConfiguration().orientation;
        if (initialOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            asksActivityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            asksActivityActivityTestRule.getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void openReactionDialogInCreateMode() {
        onView(withId(R.id.addReactionFab)).perform(click());
    }

    private void openReactionDialogInEditMode() {
        onView(withId(R.id.reactionsRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(this.positionOfReactionInRecyclerView, click()))
        ;
    }

    private void clickOnButtonOfDialogWithText(int textOfButtonStringId) {
        onView(withText(textOfButtonStringId))
                .inRoot(isDialog())
                .perform(click())
        ;
    }

    private void openAsksActivityWithIntent() {
        Intent intent = new Intent();
        intent.putExtra(Constants.ARG_EPISODE_ID, this.episodeId);
        asksActivityActivityTestRule.launchActivity(intent);
    }

    private void goToTabWithText(int textOfTabStringId) {
        onView(withText(textOfTabStringId)).perform(click());
    }

    private void typeReaction(@NonNull String validReaction) {
        //onView(withId(R.id.reactionEditText))
        //       .inRoot(isDialog())
        //     .perform(typeText(validReaction))
        //;
    }

    private void clearReactionEditText() {
        //onView(withId(R.id.reactionEditText))
        //        .inRoot(isDialog())
        //        .perform(clearText())
        //;
    }

    private String selectReactionClassInSpinner(int option) {
        String validReactionClass = asksActivityActivityTestRule.getActivity().getResources().getStringArray(R.array.reaction_categories_array)[option];
        onView(withId(R.id.reactionClassSpinner))
                .inRoot(isDialog())
                .perform(click())
        ;
        onData(allOf(is(instanceOf(String.class)), is(validReactionClass)))
                .inRoot(isPlatformPopup())
                .perform(click())
        ;
        return validReactionClass;
    }

    private String getValueOfReactionClassSpinner() {
        WhatFragment whatFragment = getWhatFragmentFromAsksActivity();
        ReactionDialog reactionDialog = getReactionDialogFragmentFromWhatFragment(whatFragment);
        Spinner reactionClassSpinner = reactionDialog.getView().findViewById(R.id.reactionClassSpinner);
        return reactionClassSpinner.getSelectedItem().toString();
    }

    private String getValueOfReactionEditText() {
        WhatFragment whatFragment = getWhatFragmentFromAsksActivity();
        ReactionDialog reactionDialog = getReactionDialogFragmentFromWhatFragment(whatFragment);
        //EditText reactionEditText = reactionDialog.getView().findViewById(R.id.reactionEditText);
        //return reactionEditText.getText().toString();
        return "";
    }

    private WhatFragment getWhatFragmentFromAsksActivity() {
        for (Fragment f : this.asksActivityActivityTestRule.getActivity().getSupportFragmentManager().getFragments()) {
            if (f instanceof WhatFragment) {
                return (WhatFragment) f;
            }
        }
        return null;
    }

    private ReactionDialog getReactionDialogFragmentFromWhatFragment(WhatFragment whatFragment) {
        return (ReactionDialog) whatFragment.getChildFragmentManager().findFragmentByTag(ReactionDialog.FRAGMENT_TAG_REACTION_DIALOG);
    }

    private void testTitleOfDialog(int titleStringId) {
        onView(withText(titleStringId))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));
    }

    private void testTextOfButtonOfDialog(int buttonId, int textStringId) {
        onView(withId(buttonId))
                .inRoot(isDialog())
                .check(matches(withText(textStringId)))
        ;
    }

    private void testNeutralButtonOfReactionDialogInCreateMode() {
        onView(withId(R.id.neutralReactionButton))
                .inRoot(isDialog())
                .check(matches(not(isDisplayed())))
        ;
    }

    private void testNeutralButtonOfReactionDialogInEditMode() {
        onView(withId(R.id.neutralReactionButton))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        ;
    }

    private void testInvalidTypedReactionMessageIsDisplayedOnReactionDialog() {
        onView(withText(R.string.reaction_dialog_error_empty_reaction))
                .inRoot(isDialog())
                .check(matches(isDisplayed()))
        ;
    }

    private void testInvalidTypedReactionMessageIsNotDisplayedOnReactionDialog() {
        onView(withText(R.string.reaction_dialog_error_empty_reaction))
                .inRoot(isDialog())
                .check(doesNotExist())
        ;
    }

    private void testValueOfReactionEditText(String reactionToTest) {
        //onView(withId(R.id.reactionEditText))
        //       .inRoot(isDialog())
        //     .check(matches(withText(reactionToTest)))
        //;
    }

    private void testValueOfReactionClassSpinner(String reactionClassToTest) {
        onView(withId(R.id.reactionClassSpinner))
                .inRoot(isDialog())
                .check(matches(withSpinnerText(containsString(reactionClassToTest))))
        ;
    }

    private Reaction getReactionAtPositionOnRecyclerView(int position) {
        WhatFragment whatFragment = getWhatFragmentFromAsksActivity();
        RecyclerView reactionsRecyclerView = getReactionsRecyclerViewFromWhatFragment(whatFragment);
        ReactionRecyclerViewAdapter reactionRVAdapter = getReactionRVAdapterFromReactionsRecyclerView(reactionsRecyclerView);
        return reactionRVAdapter.getItem(position);
    }

    private RecyclerView getReactionsRecyclerViewFromWhatFragment(WhatFragment whatFragment) {
        return whatFragment.getView().findViewById(R.id.reactionsRecyclerView);
    }

    private ReactionRecyclerViewAdapter getReactionRVAdapterFromReactionsRecyclerView(RecyclerView reactionsRecyclerView) {
        return (ReactionRecyclerViewAdapter) reactionsRecyclerView.getAdapter();
    }

    private void testEmptyStateOfReactionDialogInCreateMode() {
        onView(withId(R.id.reactionDialog)).check(matches(isDisplayed()));
        testTitleOfDialog(R.string.reaction_dialog_create_title);
        testTextOfButtonOfDialog(R.id.positiveReactionButton, R.string.reaction_dialog_create_button);
        testNeutralButtonOfReactionDialogInCreateMode();
        testTextOfButtonOfDialog(R.id.negativeReactionButton, R.string.reaction_dialog_cancel_button);
        String defaultReaction = "";
        testValueOfReactionEditText(defaultReaction);
        String defaultReactionClassInSpinner = asksActivityActivityTestRule.getActivity().getResources().getStringArray(R.array.reaction_categories_array)[0];
        testValueOfReactionClassSpinner(defaultReactionClassInSpinner);
        testInvalidTypedReactionMessageIsNotDisplayedOnReactionDialog();
    }

    private void testDefaultStateOfReactionDialogInEditMode() {
        onView(withId(R.id.reactionDialog)).check(matches(isDisplayed()));
        testTitleOfDialog(R.string.reaction_dialog_edit_title);
        testTextOfButtonOfDialog(R.id.positiveReactionButton, R.string.reaction_dialog_save_button);
        testNeutralButtonOfReactionDialogInEditMode();
        testTextOfButtonOfDialog(R.id.negativeReactionButton, R.string.reaction_dialog_cancel_button);
        Reaction reactionToEdit = getReactionAtPositionOnRecyclerView(this.positionOfReactionInRecyclerView);
        testValueOfReactionEditText(reactionToEdit.getReaction());
        testValueOfReactionClassSpinner(reactionToEdit.getReactionCategory());
        testInvalidTypedReactionMessageIsNotDisplayedOnReactionDialog();
    }

    private void testValidModifiedStateOfReactionDialogInEditMode(@NonNull String reaction, @NonNull String reactionClass) {
        onView(withId(R.id.reactionDialog)).check(matches(isDisplayed()));
        testTitleOfDialog(R.string.reaction_dialog_edit_title);
        testTextOfButtonOfDialog(R.id.positiveReactionButton, R.string.reaction_dialog_save_button);
        testNeutralButtonOfReactionDialogInEditMode();
        testTextOfButtonOfDialog(R.id.negativeReactionButton, R.string.reaction_dialog_cancel_button);
        testValueOfReactionEditText(reaction);
        testValueOfReactionClassSpinner(reactionClass);
        testInvalidTypedReactionMessageIsNotDisplayedOnReactionDialog();
    }
}
 */
