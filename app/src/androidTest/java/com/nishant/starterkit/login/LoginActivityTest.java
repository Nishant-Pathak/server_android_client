package com.nishant.starterkit.login;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.nishant.starterkit.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.Visibility;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Test for login screen, which prompts for login credentials and perform login
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginActivityTest {

  @Rule
  public ActivityTestRule<LoginActivity> mLoginActivityTestRule =
    new ActivityTestRule<>(LoginActivity.class);

  @Test
  public void clickLoginButton() {
    onView(withId(R.id.sign_in_button)).perform(click());
    // check the out come.
  }
}