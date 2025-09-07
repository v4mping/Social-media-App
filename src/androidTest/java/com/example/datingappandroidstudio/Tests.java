package com.example.datingappandroidstudio;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.datingappandroidstudio.View.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class Tests {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void loginIfNeeded() throws InterruptedException {
        // Check if we're on login screen
        try {
            onView(withId(R.id.emailText)).check(matches(isDisplayed()));
            performLogin();
        } catch (Exception e) {
            // Already logged in
        }
    }

    private void performLogin() throws InterruptedException {
        // Enter test credentials
        onView(withId(R.id.emailText)).perform(typeText("agarcia@vassar.edu"), closeSoftKeyboard());
        onView(withId(R.id.passwordText)).perform(typeText("corona123"), closeSoftKeyboard());

        // Click login
        onView(withId(R.id.LogInButton)).perform(click());
        Thread.sleep(3000); // Wait for login to complete
    }

    // === 1. Navigation Tests ===
    @Test
    public void testNavigationBetweenScreens() throws InterruptedException {
        // Home -> Swiping (default)
        onView(withId(R.id.swipingMain)).check(matches(isDisplayed()));

        // Profile screen
        onView(withId(R.id.profileButton)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.profileMain)).check(matches(isDisplayed()));

        // Leaderboard screen
        onView(withId(R.id.leaderboardButton)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.leaderboardMain)).check(matches(isDisplayed()));
    }

    // === 3. Swiping Tests ===
    @Test
    public void testSwipingScreenElements() {
        onView(withId(R.id.matchButton)).check(matches(isDisplayed()));
        onView(withId(R.id.rejectButton)).check(matches(isDisplayed()));
        onView(withId(R.id.profileName)).check(matches(isDisplayed()));
        onView(withId(R.id.profileDetails)).check(matches(isDisplayed()));
        onView(withId(R.id.profileImage)).check(matches(isDisplayed()));
        onView(withId(R.id.prompt1)).check(matches(isDisplayed()));
        onView(withId(R.id.answer1)).check(matches(isDisplayed()));
    }

    // === 4. Leaderboard Test ===
    @Test
    public void testLeaderboardRefresh() throws InterruptedException {
        onView(withId(R.id.leaderboardButton)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.refreshButton)).perform(click());
        Thread.sleep(500);
        onView(withId(R.id.leaderboardMain)).check(matches(isDisplayed()));
    }
}