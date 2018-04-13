package udacity.com.bakingtime;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import udacity.com.bakingtime.fragment.RecipeInfoListIngredientsFragment;
import udacity.com.bakingtime.fragment.RecipeInfoListStepsFragment;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 10/04/2018.
 */

public class ApplicationHelper {
    public static final String RECIPE_EXTRA_DATA = "recipe_extra_data";
    public static final String TWO_PANE_EXTRA_DATA = "two_pane_extra_data";
    public static final String RECIPE_LIST_EXTRA_DATA = "recipe_list_extra_data";
    public static final String RECIPE_POSITION_EXTRA_DATA = "recipe_position_extra_data";
    public static final String RECIPE_INFO_LIST_POSITION_EXTRA_DATA = "recipe_info_list_position_extra_data";
    public static final String INGREDIENT_LIST_EXTRA_DATA = "ingredient_list_extra_data";
    public static final String STEP_EXTRA_DATA = "step_extra_data";
    public static final String RECIPE_NAME_EXTRA_DATA = "recipe_name_extra_data";

    public static final String INGREDIENTS_NAME = "Ingredients";

    public static final String POSITION_STATE_SAVE_KEY = "pos_save_key";

    public static void navigationButtonHandler(
            int position,
            int maxNum,
            ImageButton prevBtn,
            ImageButton nextBtn) {
        if (position == 0) {
            prevBtn.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        } else if (position == maxNum - 1) {
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        } else {
            prevBtn.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }
}
