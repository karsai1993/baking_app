package udacity.com.bakingtime.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.adapter.RecipeInfoListAdapter;
import udacity.com.bakingtime.fragment.RecipeInfoListEmptyDetailFragment;
import udacity.com.bakingtime.fragment.RecipeInfoListFragment;
import udacity.com.bakingtime.fragment.RecipeInfoListIngredientsFragment;
import udacity.com.bakingtime.fragment.RecipeInfoListStepsFragment;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;

/**
 * Created by Laci on 10/04/2018.
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.ib_info_list_nav_btn_prev)
    ImageButton prevBtn;
    @BindView(R.id.ib_info_list_nav_btn_next)
    ImageButton nextBtn;

    private List<Recipe> mRecipeList;
    private int mRecipePosition;
    private boolean mIsTwoPaneModeAvailable;
    private ActionBar mActionBar;
    private static FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Bundle receivedBundle = getIntent().getExtras();
        mRecipeList = receivedBundle.getParcelableArrayList(
                ApplicationHelper.RECIPE_LIST_EXTRA_DATA
        );
        mRecipePosition = receivedBundle.getInt(ApplicationHelper.RECIPE_POSITION_EXTRA_DATA);

        mActionBar = getSupportActionBar();
        mFragmentManager = getSupportFragmentManager();

        if (findViewById(R.id.ll_recipe_info_list_detail) != null) {
            mIsTwoPaneModeAvailable = true;
        }

        if (savedInstanceState == null) {
            displayInfo(false);
        }
    }

    private void displayInfo(boolean isRotated) {
        mActionBar.setTitle(mRecipeList.get(mRecipePosition).getName());
        ApplicationHelper.navigationButtonHandler(
                mRecipePosition,
                mRecipeList.size(),
                prevBtn,
                nextBtn
        );
        applyInfoListFragment(isRotated);
        if (mIsTwoPaneModeAvailable && !isRotated) {
            applyEmptyInfoListDetailFragment();
        }
    }

    private void applyInfoListFragment(boolean isRotated) {
        //if (is) return;
        Fragment fragment = new RecipeInfoListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ApplicationHelper.RECIPE_EXTRA_DATA, mRecipeList.get(mRecipePosition));
        bundle.putBoolean(ApplicationHelper.TWO_PANE_EXTRA_DATA, mIsTwoPaneModeAvailable);
        bundle.putBoolean(ApplicationHelper.ROTATION_EXTRA_DATA, isRotated);
        fragment.setArguments(bundle);
        if (!isRotated) {
            mFragmentManager.beginTransaction()
                    .replace(R.id.recipe_info_fragment, fragment)
                    .commit();
        }
    }

    private void applyEmptyInfoListDetailFragment() {
        Fragment fragment = new RecipeInfoListEmptyDetailFragment();
        Bundle emptyFragmentBundle = new Bundle();
        emptyFragmentBundle.putString(
                ApplicationHelper.RECIPE_NAME_EXTRA_DATA,
                mActionBar.getTitle().toString()
        );
        fragment.setArguments(emptyFragmentBundle);
        mFragmentManager.beginTransaction()
                .replace(R.id.recipe_info_list_detail_fragment, fragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(ApplicationHelper.POSITION_STATE_SAVE_KEY, mRecipePosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRecipePosition = savedInstanceState.getInt(ApplicationHelper.POSITION_STATE_SAVE_KEY);
        displayInfo(true);
    }

    public void onNavigationButtonClicked(View view) {
        int clickedBtnId = view.getId();
        if (clickedBtnId == R.id.ib_info_list_nav_btn_prev) {
            mRecipePosition --;
        } else {
            mRecipePosition ++;
        }
        displayInfo(false);
    }

    public static void applyInfoListDetailFragment(int position, Recipe recipe) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new RecipeInfoListIngredientsFragment();
                Bundle ingredientsBundle = new Bundle();
                ingredientsBundle.putParcelableArrayList(
                        ApplicationHelper.INGREDIENT_LIST_EXTRA_DATA,
                        (ArrayList<Ingredient>) recipe.getIngredientList()
                );
                fragment.setArguments(ingredientsBundle);
                mFragmentManager.beginTransaction()
                        .replace(
                                R.id.recipe_info_list_detail_fragment,
                                fragment)
                        .commit();
                break;
            default:
                fragment = new RecipeInfoListStepsFragment();
                Bundle stepsBundle = new Bundle();
                stepsBundle.putParcelable(
                        ApplicationHelper.STEP_EXTRA_DATA,
                        recipe.getStepList().get(position - 1)
                );
                fragment.setArguments(stepsBundle);
                mFragmentManager.beginTransaction()
                        .replace(
                                R.id.recipe_info_list_detail_fragment,
                                fragment)
                        .commit();
                break;
        }
    }
}
