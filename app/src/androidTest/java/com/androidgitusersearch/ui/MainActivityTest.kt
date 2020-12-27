package com.androidgitusersearch.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.test.espresso.Espresso.*
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.PositionAssertions
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.filters.LargeTest
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import com.androidgitusersearch.R
import com.androidgitusersearch.model.RepoModel
import com.androidgitusersearch.model.UserModel
import com.androidgitusersearch.util.ConvertHelper
import com.androidgitusersearch.util.RecyclerViewHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf
import org.json.JSONArray
import org.junit.*
import org.junit.runner.RunWith

/**
 * Created by Royal Lachinov on 2020-12-24.
 */


@RunWith(AndroidJUnit4ClassRunner::class)
@LargeTest
class MainActivityTest{

    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun onCreateView() {

        /**
         * Checking if all views are not null and displayed
         */
        Assert.assertNotNull(R.id.edit_text_search)
        Assert.assertNotNull(R.id.button_search)
        Assert.assertNotNull(R.id.image_view_user_image)
        Assert.assertNotNull(R.id.text_view_user_name)
        Assert.assertNotNull(R.id.recycler_view_repos)
        Assert.assertNotNull(R.id.text_view_empty_message)
        Assert.assertNotNull(R.id.pb_repos)


        onView(
            AllOf.allOf(
                withId(R.id.edit_text_search),
                withHint(R.string.search_hint),
                withText("royallachinov")
            )
        )
            .check(PositionAssertions.isCompletelyAbove(withId(R.id.image_view_user_image)))
            .check(matches(isCompletelyDisplayed()))

        onView(
            AllOf.allOf(
                withId(R.id.button_search),
                withText(R.string.search)
            )
        )
            .check(PositionAssertions.isCompletelyRightOf(withId(R.id.edit_text_search)))
            .check(matches(isCompletelyDisplayed()))

    }

    @Test
    fun onViewCreated(){

        /**
         * In this function I fill recyclerview with mock data & and perform onItemClick action,
         * then checking updated date from snackBar
         */
        val userString = ConvertHelper.convertStreamToString(
            InstrumentationRegistry.getInstrumentation().context.resources.assets.open("user.json")
        )

        val userModel: UserModel = Gson().fromJson(userString, UserModel::class.java)

        onView(withId(R.id.image_view_user_image))
            .check(PositionAssertions.isCompletelyBelow(withId(R.id.edit_text_search)))
            .check(matches(loadUrlToImageView(userModel.avatarUrl!!)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.text_view_user_name))
            .perform(setTextInTextView(userModel.name))
            .check(PositionAssertions.isCompletelyBelow(withId(R.id.image_view_user_image)))
            .check(matches(isCompletelyDisplayed()))

        onView(withId(R.id.text_view_user_name))
            .check(matches(withText("Royal Lachinov")))

        val repoString = ConvertHelper.convertStreamToString(
            InstrumentationRegistry.getInstrumentation().context.resources.assets.open("list_of_repos.json")
        )

        val jsonArray = JSONArray(repoString)
        val repoList:MutableList<RepoModel> = mutableListOf()

        for (i in 0 until jsonArray.length()) {
            val jsonObj = jsonArray.getJSONObject(i)
            val repoName:String = jsonObj.getString("name")
            val repoDescription = jsonObj.getString("description")
            val repoUpdateDate = jsonObj.getString("updated_at")
            val repoStarCount = jsonObj.getInt("stargazers_count")
            val repoForkCount = jsonObj.getInt("forks")
            repoList.add(
                RepoModel(
                    repoName,
                    repoDescription,
                    repoUpdateDate,
                    repoStarCount,
                    repoForkCount
                )
            )
        }

        val repoFlow:Flow<PagingData<RepoModel>> = Pager(
            config = PagingConfig(pageSize = 1, enablePlaceholders = false),
            pagingSourceFactory = {
                FakeRepoMediator(repoList)
            }
        ).flow

        activityRule.scenario.onActivity { mainActivity ->
            GlobalScope.launch(Dispatchers.Main) {
                repoFlow.collect {
                    mainActivity?.repoAdapter?.submitData(it)//repoAdapter.submitData(it)
                }
            }
        }

        onView(withId(R.id.recycler_view_repos))
            .check(PositionAssertions.isCompletelyBelow(withId(R.id.text_view_user_name)))
            .check(matches(isDisplayed()))

        onView(withId(R.id.recycler_view_repos))
            .perform(actionOnItemAtPosition<RepoAdapter.ViewHolder>(0, ViewActions.click()))

        onView(withId(R.id.text_view_last_update_value))
            .check(matches(isDisplayed()))
            .check(
                matches(
                    withText(ConvertHelper.convertUpdateDate("2019-11-12T17:10:43Z"))
                )
            )
    }

    @Test
    fun onViewSearchChanged(){

        /**
         * In this function I change search edittext value, fetching real data from api &
         * filling recyclerview and perform onItemClick action, then checking updated date from snackBar
         */

        onView(
            AllOf.allOf(
                withId(R.id.edit_text_search),
                withHint(R.string.search_hint),
            )
        ).perform(ViewActions.typeText(""))

        onView(
            AllOf.allOf(
                withId(R.id.edit_text_search),
                withHint(R.string.search_hint),
            )
        ).perform(ViewActions.typeText("octocat"), ViewActions.closeSoftKeyboard())

        onView(
            AllOf.allOf(
                withId(R.id.button_search),
                withText(R.string.search)
            )
        ).perform(ViewActions.click())

        onView(withId(R.id.recycler_view_repos))
            .check(matches(isDisplayed()))

        onView(RecyclerViewHelper(R.id.recycler_view_repos)
            .atPositionOnView(0, R.id.text_view_repo_name))
            .check(matches(isDisplayed()))
            .check(
                matches(
                    withText("boysenberry-repo-1")
                )
            )

    }

    private fun loadUrlToImageView(profilePictureUrl:String) = object : TypeSafeMatcher<View>() {
        override fun describeTo(description: Description) {
            description.appendText("ImageView with url")
        }

        override fun matchesSafely(view: View): Boolean {

            Glide.with(view.context)
                .load(profilePictureUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.mipmap.ic_launcher_round)
                .into(view as ImageView)

            return true
        }
    }

    private fun setTextInTextView(value: String?): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return AllOf.allOf(isDisplayed(), isAssignableFrom(TextView::class.java))
            }

            override fun perform(uiController: UiController?, view: View) {
                (view as TextView).text = value
            }

            override fun getDescription(): String {
                return "replace text"
            }
        }
    }

}