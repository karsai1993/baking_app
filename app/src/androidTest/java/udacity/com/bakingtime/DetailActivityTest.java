package udacity.com.bakingtime;

import android.content.Intent;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import udacity.com.bakingtime.activity.DetailActivity;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Laci on 20/04/2018.
 */

@RunWith(AndroidJUnit4.class)
public class DetailActivityTest {

    @Rule
    public IntentsTestRule<DetailActivity> mActivityRule =
            new IntentsTestRule<>(DetailActivity.class, false, false);

    private List<Ingredient> mIngredientList;
    private List<Step> mStepList;
    private List<Recipe> mRecipeList;
    private int mRecyclerViewId;
    private Recipe mRecipe;

    private static final String LIST_ITEM_90 = "short90";
    private static final String RECIPE_ITEM_1_NAME = "Recipe #1";
    private static final String RECIPE_ITEM_2_NAME = "Recipe #2";

    @Before
    public void initialActivity() {
        mRecyclerViewId = R.id.rv_recipe_info;
        mIngredientList = new ArrayList<>();
        mStepList = new ArrayList<>();
        for (int i = 0; i < 3; i ++) {
            mIngredientList.add(new Ingredient(1, "CUP", "Item"));
        }
        for (int i = 0; i < 100; i++) {
            mStepList.add(
                    new Step(
                        i,
                        "short" + i,
                        "long" + i,
                        "",
                        ""
                    )
            );
        }
        mRecipeList = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            mRecipeList.add(
                    new Recipe(
                            "Recipe #" + i,
                            mIngredientList,
                            mStepList,
                            8,
                            ""
                    )
            );
        }
        Intent intent = new Intent();
        intent.putExtra(
                ApplicationHelper.RECIPE_LIST_EXTRA_DATA,
                (ArrayList<Recipe>) mRecipeList);
        intent.putExtra(ApplicationHelper.RECIPE_POSITION_EXTRA_DATA, 0);
        mActivityRule.launchActivity(intent);
    }

    @Test
    public void testTextAppearance() {
        hasContentChecker(mRecyclerViewId, ApplicationHelper.INGREDIENTS_NAME);
    }

    @Test
    public void testRecyclerViewScrolling() {
        notHasContentChecker(mRecyclerViewId, LIST_ITEM_90);
        onView(withId(mRecyclerViewId))
                .perform(RecyclerViewActions.scrollTo(hasDescendant(withText(LIST_ITEM_90))));
        hasContentChecker(mRecyclerViewId, LIST_ITEM_90);
    }

    @Test
    public void testNavigationButtonClick(){
        onView(withText(RECIPE_ITEM_1_NAME)).check(matches(isDisplayed()));
        onView(withId(R.id.ib_info_list_nav_btn_next)).perform(click());
        onView(withText(RECIPE_ITEM_2_NAME)).check(matches(isDisplayed()));
    }

    //Click test for smart phones with smallest width < 600
    @Test
    public void testOnItemClick() {
        onView(withId(mRecyclerViewId))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withText(ApplicationHelper.INGREDIENTS_NAME)).check(matches(isDisplayed()));
        mRecipe = mActivityRule.getActivity().getRecipe();
        intended(
                allOf(
                        hasExtra(ApplicationHelper.RECIPE_EXTRA_DATA, mRecipe),
                        hasExtra(ApplicationHelper.RECIPE_INFO_LIST_POSITION_EXTRA_DATA, 0)
                )
        );
    }

    public ViewInteraction hasContentChecker(int id, String text) {
        return onView(withId(id))
                .check(
                        matches(
                                hasDescendant(
                                        withText(
                                                text
                                        )
                                )
                        )
                );
    }

    public ViewInteraction notHasContentChecker(int id, String text) {
        return onView(withId(id))
                .check(
                        matches(
                                not(
                                    hasDescendant(
                                            withText(
                                                    text
                                            )
                                    )
                                )
                        )
                );
    }
}
