package udacity.com.bakingtime.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.fragment.RecipeInfoListIngredientsFragment;
import udacity.com.bakingtime.fragment.RecipeInfoListStepsFragment;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 10/04/2018.
 * This class invokes the fragment of selected list item detail introduction.
 */

public class RecipeInfoListDetail extends AppCompatActivity {

    @BindView(R.id.ib_detail_nav_btn_prev)
    ImageButton prevImageBtn;
    @BindView(R.id.ib_detail_nav_btn_next)
    ImageButton nextImageBtn;

    private List<Ingredient> mIngredientList;
    private List<Step> mStepList;
    private int mPosition;
    private ActionBar mActionBar;
    private List<String> mFragmentTagList;
    private int mMaxNum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info_list_detail);
        ButterKnife.bind(this);

        Bundle receivedBundle = getIntent().getExtras();
        Recipe recipe = receivedBundle.getParcelable(ApplicationHelper.RECIPE_EXTRA_DATA);
        mIngredientList = recipe.getIngredientList();
        mStepList = recipe.getStepList();

        mMaxNum = mStepList.size() + 1;
        populateFragmentTagList(mMaxNum);

        mActionBar = getSupportActionBar();

        mPosition = receivedBundle.getInt(
                ApplicationHelper.RECIPE_INFO_LIST_POSITION_EXTRA_DATA
        );
        if (savedInstanceState == null) {
            displayFragment();
        }
    }

    private void populateFragmentTagList(int maxNum) {
        mFragmentTagList = new ArrayList<>();
        for (int i = 0; i < maxNum; i++) {
            mFragmentTagList.add(getResources().getString(R.string.hashtag) + i);
        }
    }

    private void displayFragment() {
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (mPosition) {
            case 0:
                mActionBar.setTitle(ApplicationHelper.INGREDIENTS_NAME);
                fragment = new RecipeInfoListIngredientsFragment();
                Bundle ingredientsBundle = new Bundle();
                ingredientsBundle.putParcelableArrayList(
                        ApplicationHelper.INGREDIENT_LIST_EXTRA_DATA,
                        (ArrayList<Ingredient>) mIngredientList
                );
                fragment.setArguments(ingredientsBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_info_list_detail_fragment, fragment)
                        .commit();
                break;
            default:
                int stepListPosition = mPosition - 1;
                mActionBar.setTitle(mStepList.get(stepListPosition).getShortDescription());
                if (fragmentManager.findFragmentByTag(mFragmentTagList.get(mPosition)) == null) {
                    fragment = new RecipeInfoListStepsFragment();
                    Bundle stepsBundle = new Bundle();
                    stepsBundle.putParcelable(
                            ApplicationHelper.STEP_EXTRA_DATA,
                            mStepList.get(stepListPosition)
                    );
                    fragment.setArguments(stepsBundle);
                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_info_list_detail_fragment, fragment, mFragmentTagList.get(mPosition))
                            .commit();
                }
                break;
        }
        ApplicationHelper.navigationButtonHandler(
                mPosition,
                mStepList.size() + 1,
                prevImageBtn,
                nextImageBtn);
    }

    public void onNavigationButtonClicked(View view) {
        if (view.getId() == R.id.ib_detail_nav_btn_prev) {
            mPosition--;
        } else {
            mPosition++;
        }
        populateFragmentTagList(mMaxNum);
        displayFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ApplicationHelper.POSITION_STATE_SAVE_KEY, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt(ApplicationHelper.POSITION_STATE_SAVE_KEY);
        displayFragment();
    }
}
