package com.zachary.lynch.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.zachary.lynch.bakingapp.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.zachary.lynch.bakingapp.ui.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class CardListViewTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new
            ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp() throws Exception{
        mActivityTestRule.getActivity()
                .getSupportFragmentManager()
                .beginTransaction();

    }
    @Test
    public void testListViewClicked(){

        onView(withId(R.id.mainFragmentListView)).check(matches(isDisplayed()));
        onData(anything()).inAdapterView(withId(R.id.mainFragmentListView)).atPosition(0).perform(click());

    }

}
