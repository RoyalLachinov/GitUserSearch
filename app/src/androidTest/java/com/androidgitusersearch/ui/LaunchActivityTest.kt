package com.androidgitusersearch.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.androidgitusersearch.R
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Royal Lachinov on 2020-12-24.
 */

@RunWith(AndroidJUnit4ClassRunner::class)
class LaunchActivityTest{

    @Test
    fun testLaunchActivityView(){
        val activityScenario = ActivityScenario.launch(LaunchActivity::class.java)

        Espresso.onView(ViewMatchers.withId(R.id.searchInputLayout))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}