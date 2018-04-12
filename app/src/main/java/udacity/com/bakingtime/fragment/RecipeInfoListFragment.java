package udacity.com.bakingtime.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.activity.RecipeInfoListDetail;
import udacity.com.bakingtime.adapter.RecipeInfoListAdapter;
import udacity.com.bakingtime.model.Recipe;

/**
 * Created by Laci on 10/04/2018.
 */

public class RecipeInfoListFragment extends Fragment {

    public RecipeInfoListFragment () {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_info_list, container, false);
        RecyclerView recipeInfoListRecyclerView = rootView.findViewById(R.id.rv_recipe_info);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),  1);
        recipeInfoListRecyclerView.setLayoutManager(layoutManager);
        recipeInfoListRecyclerView.hasFixedSize();
        final Recipe recipe = getArguments().getParcelable(CommonApplicationFields.RECIPE_EXTRA_DATA);
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onItemClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(CommonApplicationFields.RECIPE_EXTRA_DATA, recipe);
                bundle.putInt(CommonApplicationFields.RECIPE_INFO_LIST_POSITION_EXTRA_DATA, position);
                Intent startRecipeInfoListDetailActivity = new Intent(getContext(), RecipeInfoListDetail.class);
                startRecipeInfoListDetailActivity.putExtras(bundle);
                startActivity(startRecipeInfoListDetailActivity);
            }
        };
        RecipeInfoListAdapter recipeInfoListAdapter = new RecipeInfoListAdapter(getContext(), recipe, onClickListener);
        recipeInfoListRecyclerView.setAdapter(recipeInfoListAdapter);
        return rootView;
    }
}
