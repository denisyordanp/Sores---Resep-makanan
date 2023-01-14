package com.socialite.sores.ui


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.microsoft.appcenter.espresso.Factory
import com.microsoft.appcenter.espresso.ReportHelper
import com.socialite.sores.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Rule
    @JvmField
    var reportHelper: ReportHelper? = Factory.getReportHelper()

    @After
    fun tearDown() {
        reportHelper?.label("Finishing test")
    }

    @Test
    fun mainActivityTest() {
        val frameLayout = onView(
            allOf(
                withId(R.id.recipesFragment), withContentDescription("Recipes"),
                withParent(withParent(withId(R.id.bottomNavigationView))),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))

        val frameLayout2 = onView(
            allOf(
                withId(R.id.favoriteRecipesFragment), withContentDescription("Favorite Recipes"),
                withParent(withParent(withId(R.id.bottomNavigationView))),
                isDisplayed()
            )
        )
        frameLayout2.check(matches(isDisplayed()))

        val frameLayout3 = onView(
            allOf(
                withId(R.id.foodJokeFragment), withContentDescription("Joke"),
                withParent(withParent(withId(R.id.bottomNavigationView))),
                isDisplayed()
            )
        )
        frameLayout3.check(matches(isDisplayed()))
    }
}
