package udacity.com.bakingtime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.activity.DetailActivity;
import udacity.com.bakingtime.activity.RecipeInfoListDetail;
import udacity.com.bakingtime.adapter.RecipeInfoListAdapter;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;

/**
 * Created by Laci on 10/04/2018.
 */

public class RecipeInfoListFragment extends Fragment {

    public RecipeInfoListFragment () {}

    private RecipeInfoListAdapter mRecipeInfoListAdapter;
    private RecyclerView mRecipeInfoListRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_recipe_info_list,
                container,
                false);
        mRecipeInfoListRecyclerView = rootView.findViewById(R.id.rv_recipe_info);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecipeInfoListRecyclerView.setLayoutManager(layoutManager);
        mRecipeInfoListRecyclerView.setHasFixedSize(true);
        final Bundle receivedBundle = getArguments();
        final Recipe recipe = receivedBundle.getParcelable(ApplicationHelper.RECIPE_EXTRA_DATA);
        final boolean isTwoPaneAvailable
                = receivedBundle.getBoolean(ApplicationHelper.TWO_PANE_EXTRA_DATA);
        final OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onItemClick(int position) {
                if (isTwoPaneAvailable) {
                    DetailActivity.displayInfoListDetails(position, recipe);
                    updateRecyclerViewClickedItem(position);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(ApplicationHelper.RECIPE_EXTRA_DATA, recipe);
                    bundle.putInt(ApplicationHelper.RECIPE_INFO_LIST_POSITION_EXTRA_DATA, position);
                    Intent startRecipeDetailActivity = new Intent(getContext(), RecipeInfoListDetail.class);
                    startRecipeDetailActivity.putExtras(bundle);
                    startActivity(startRecipeDetailActivity);
                }
            }
        };
        mRecipeInfoListAdapter = new RecipeInfoListAdapter(getContext(), recipe, onClickListener);
        mRecipeInfoListRecyclerView.setAdapter(mRecipeInfoListAdapter);
        return rootView;
    }

    private void updateRecyclerViewClickedItem(int position) {
        mRecipeInfoListAdapter = new RecipeInfoListAdapter(true, position);
        mRecipeInfoListRecyclerView.setAdapter(mRecipeInfoListAdapter);
        mRecipeInfoListRecyclerView.scrollToPosition(position);
    }
}
