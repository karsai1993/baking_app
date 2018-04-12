package udacity.com.bakingtime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.fragment.RecipeInfoListIngredientsFragment;
import udacity.com.bakingtime.fragment.RecipeInfoListStepsFragment;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 10/04/2018.
 */

public class RecipeInfoListDetail extends AppCompatActivity {

    @BindView(R.id.ib_detail_nav_btn_prev)
    ImageButton prevImageBtn;
    @BindView(R.id.ib_detail_nav_btn_next)
    ImageButton nextImageBtn;

    private Bundle mData;
    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private List<Ingredient> mIngredientList;
    private List<Step> mStepList;
    private int mPosition;
    private ActionBar mActionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info_list_detail);
        ButterKnife.bind(this);

        mData = getIntent().getExtras();

        Recipe recipe = mData.getParcelable(CommonApplicationFields.RECIPE_EXTRA_DATA);
        mIngredientList = recipe.getIngredientList();
        mStepList = recipe.getStepList();

        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();

        mPosition = mData.getInt(CommonApplicationFields.RECIPE_INFO_LIST_POSITION_EXTRA_DATA);
        displayFragment();
    }

    private void displayFragment() {
        switch (mPosition) {
            case 0:
                mActionBar.setTitle(CommonApplicationFields.INGREDIENTS_NAME);
                mFragment = new RecipeInfoListIngredientsFragment();
                Bundle ingredientsBundle = new Bundle();
                ingredientsBundle.putParcelableArrayList(CommonApplicationFields.INGREDIENT_LIST_EXTRA_DATA, (ArrayList<Ingredient>) mIngredientList);
                mFragment.setArguments(ingredientsBundle);
                mFragmentManager.beginTransaction().replace(R.id.recipe_info_list_detail_fragment, mFragment).commit();
                break;
            default:
                int stepListPosition = mPosition - 1;
                mActionBar.setTitle(mStepList.get(stepListPosition).getShortDescription());
                mFragment = new RecipeInfoListStepsFragment();
                Bundle stepsBundle = new Bundle();
                stepsBundle.putParcelable(CommonApplicationFields.STEP_EXTRA_DATA, mStepList.get(stepListPosition));
                mFragment.setArguments(stepsBundle);
                mFragmentManager.beginTransaction().replace(R.id.recipe_info_list_detail_fragment, mFragment).commit();
                break;
        }
        buttonHandler(mStepList.size() + 1);
    }

    private void buttonHandler(int numOfFragments) {
        if (mPosition == 0) {
            prevImageBtn.setVisibility(View.GONE);
            nextImageBtn.setVisibility(View.VISIBLE);
        } else if (mPosition == numOfFragments - 1) {
            prevImageBtn.setVisibility(View.VISIBLE);
            nextImageBtn.setVisibility(View.GONE);
        } else {
            prevImageBtn.setVisibility(View.VISIBLE);
            nextImageBtn.setVisibility(View.VISIBLE);
        }
    }

    public void onNavigationButtonClicked(View view) {
        if (view.getId() == R.id.ib_detail_nav_btn_prev) {
            mPosition--;
        } else {
            mPosition++;
        }
        displayFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CommonApplicationFields.POSITION_STATE_SAVE_KEY, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt(CommonApplicationFields.POSITION_STATE_SAVE_KEY);
        displayFragment();
    }
}
