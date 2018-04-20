package udacity.com.bakingtime;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import udacity.com.bakingtime.activity.MainActivity;
import udacity.com.bakingtime.model.Recipe;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Laci on 18/04/2018.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public IntentsTestRule<MainActivity> mMainActivityIntentsTestRule
            = new IntentsTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;
    private List<Recipe> mRecipeListForTest;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mMainActivityIntentsTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Before
    public void initialVariable() {
        mRecipeListForTest = mMainActivityIntentsTestRule.getActivity().getRecipeList();
    }

    @Test
    public void checkIfRecyclerViewHasCreated() {
        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        3,
                        click()
                )
        );
    }

    @Test
    public void checkIfClickContainingRightValues() {
        onView(withId(R.id.rv_recipes)).perform(
                RecyclerViewActions.actionOnItemAtPosition(
                        0,
                        click()
                )
        );
        intended(allOf(
                hasExtra(ApplicationHelper.RECIPE_LIST_EXTRA_DATA, mRecipeListForTest),
                hasExtra(ApplicationHelper.RECIPE_POSITION_EXTRA_DATA, 0)
        ));
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
