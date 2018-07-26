package com.zachary.lynch.bakingapp;



import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import android.support.test.rule.ActivityTestRule;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.zachary.lynch.bakingapp.ui.MainActivity;



    @RunWith(AndroidJUnit4.class)
    public class TabletLayoutTest {
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
        public void doubleLayout(){
            onView(withId(R.id.tabletLayout)).check(doesNotExist());
        }
}
