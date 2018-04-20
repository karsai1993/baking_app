package udacity.com.bakingtime;

import android.content.Intent;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import udacity.com.bakingtime.activity.RecipeInfoListDetail;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Laci on 21/04/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecipeInfoListDetailTest {

    @Rule
    public IntentsTestRule<RecipeInfoListDetail> mRecipeInfoListDetailIntentsTestRule
            = new IntentsTestRule<>(RecipeInfoListDetail.class, false, false);

    private Recipe mRecipe;
    private List<Ingredient> mIngredientList;
    private List<Step> mStepList;

    private static final String INGREDIENT_ITEM_NAME = "Item";
    private static final String STEP_1_SHORT_NAME = "short1";

    @Before
    public void initialActivity() {
        mIngredientList = new ArrayList<>();
        mStepList = new ArrayList<>();
        for (int i = 0; i < 3; i ++) {
            mIngredientList.add(new Ingredient(1, "CUP", "Item"));
        }
        for (int i = 1; i < 100; i++) {
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
        mRecipe = new Recipe(
                "Recipe #1",
                mIngredientList,
                mStepList,
                8,
                ""
        );
        Intent intent = new Intent();
        intent.putExtra(ApplicationHelper.RECIPE_EXTRA_DATA, mRecipe);
        intent.putExtra(ApplicationHelper.RECIPE_INFO_LIST_POSITION_EXTRA_DATA, 0);
        mRecipeInfoListDetailIntentsTestRule.launchActivity(intent);
    }

    @Test
    public void testIngredientsAppearance() {
        onView(withText(ApplicationHelper.INGREDIENTS_NAME)).check(matches(isDisplayed()));
        onView(withId(R.id.rv_recipe_info_list_ingredients))
                .check(matches(hasDescendant(withText(INGREDIENT_ITEM_NAME))));
    }

    @Test
    public void testNavigationButtonClick() {
        onView(withText(ApplicationHelper.INGREDIENTS_NAME)).check(matches(isDisplayed()));
        onView(withId(R.id.ib_detail_nav_btn_next)).perform(click());
        onView(withText(STEP_1_SHORT_NAME)).check(matches(isDisplayed()));
    }

}
