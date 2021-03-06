package udacity.com.bakingtime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;

import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.activity.DetailActivity;
import udacity.com.bakingtime.activity.MainActivity;
import udacity.com.bakingtime.activity.RecipeInfoListDetail;
import udacity.com.bakingtime.adapter.RecipeInfoListAdapter;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;

/**
 * Created by Laci on 10/04/2018.
 * This class creates the fragment of each recipe information list (ingredients and steps), and in
 * two pane mode, it also creates the fragment of selected list item detail introduction.
 */

public class RecipeInfoListFragment extends Fragment {

    public RecipeInfoListFragment () {}

    private RecipeInfoListAdapter mRecipeInfoListAdapter;
    private RecyclerView mRecipeInfoListRecyclerView;

    private int mClickedPosition;
    private Recipe mRecipe;
    private boolean mIsClicked;
    private boolean mIsRotated;
    private boolean mIsTwoPaneAvailable;
    private boolean mIsComingFromWidget;

    private static final String CLICK_SAVE_EXTRA_DATA = "click_save_extra_data";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Bundle receivedBundle = getArguments();
        if (receivedBundle != null) {
            mRecipe = receivedBundle.getParcelable(ApplicationHelper.RECIPE_EXTRA_DATA);
            mIsTwoPaneAvailable = receivedBundle.getBoolean(ApplicationHelper.TWO_PANE_EXTRA_DATA);
            mIsRotated = receivedBundle.getBoolean(ApplicationHelper.ROTATION_EXTRA_DATA);
            mIsComingFromWidget = receivedBundle.getBoolean(
                    ApplicationHelper.WIDGET_BOOLEAN_EXTRA_DATA
            );
        }

        if (savedInstanceState != null) {
            mClickedPosition = savedInstanceState.getInt(ApplicationHelper.POSITION_STATE_SAVE_KEY);
            mIsClicked = savedInstanceState.getBoolean(CLICK_SAVE_EXTRA_DATA);
            mRecipe = savedInstanceState.getParcelable(ApplicationHelper.RECIPE_EXTRA_DATA);
            mIsTwoPaneAvailable = savedInstanceState.getBoolean(ApplicationHelper.TWO_PANE_EXTRA_DATA);
            mIsRotated = savedInstanceState.getBoolean(ApplicationHelper.ROTATION_EXTRA_DATA);
            mIsComingFromWidget = savedInstanceState.getBoolean(
                    ApplicationHelper.WIDGET_BOOLEAN_EXTRA_DATA
            );
        }

        int spanCount = getContext().getResources().getInteger(R.integer.span_count);
        if (mIsTwoPaneAvailable) spanCount = 1;

        View rootView = inflater.inflate(
                R.layout.fragment_recipe_info_list,
                container,
                false);
        mRecipeInfoListRecyclerView = rootView.findViewById(R.id.rv_recipe_info);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
        mRecipeInfoListRecyclerView.setLayoutManager(layoutManager);
        mRecipeInfoListRecyclerView.setHasFixedSize(true);

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mIsTwoPaneAvailable) {
                    if (position > 0) mIsComingFromWidget = false;
                    DetailActivity.applyInfoListDetailFragment(position, mRecipe, getContext());
                    mRecipeInfoListAdapter = new RecipeInfoListAdapter(
                            getContext(),
                            mRecipe,
                            position,
                            mIsRotated
                    );
                    mRecipeInfoListRecyclerView.setAdapter(mRecipeInfoListAdapter);
                    mRecipeInfoListRecyclerView.scrollToPosition(position);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ApplicationHelper.RECIPE_EXTRA_DATA, mRecipe);
                    bundle.putInt(ApplicationHelper.RECIPE_INFO_LIST_POSITION_EXTRA_DATA, position);
                    Intent startRecipeDetailActivity = new Intent(
                            getContext(),
                            RecipeInfoListDetail.class
                    );
                    startRecipeDetailActivity.putExtras(bundle);
                    startActivity(startRecipeDetailActivity);
                }
                mClickedPosition = position;
                mIsClicked = true;
            }
        };
        mRecipeInfoListAdapter = new RecipeInfoListAdapter(
                getContext(),
                mRecipe,
                onClickListener,
                mIsRotated,
                mIsComingFromWidget
        );
        mRecipeInfoListRecyclerView.setAdapter(mRecipeInfoListAdapter);

        if (mIsTwoPaneAvailable && mIsComingFromWidget) {
            DetailActivity.applyInfoListDetailFragment(0, mRecipe, getContext());
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ApplicationHelper.POSITION_STATE_SAVE_KEY, mClickedPosition);
        outState.putBoolean(CLICK_SAVE_EXTRA_DATA, mIsClicked);
        outState.putParcelable(ApplicationHelper.RECIPE_EXTRA_DATA, mRecipe);
        outState.putBoolean(ApplicationHelper.TWO_PANE_EXTRA_DATA, mIsTwoPaneAvailable);
        outState.putBoolean(ApplicationHelper.ROTATION_EXTRA_DATA, true);
        outState.putBoolean(ApplicationHelper.WIDGET_BOOLEAN_EXTRA_DATA, mIsComingFromWidget);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mIsClicked) {
            mRecipeInfoListRecyclerView.scrollToPosition(mClickedPosition);
        }
    }
}
