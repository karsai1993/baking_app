package udacity.com.bakingtime.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.adapter.RecipeInfoListItemIngredientsAdapter;
import udacity.com.bakingtime.model.Ingredient;

/**
 * Created by Laci on 11/04/2018.
 * This class creates the fragment of ingredient introduction.
 */

public class RecipeInfoListIngredientsFragment extends Fragment {

    public RecipeInfoListIngredientsFragment () {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_recipe_info_list_ingredients,
                container,
                false);
        List<Ingredient> ingredients = getArguments().getParcelableArrayList(
                ApplicationHelper.INGREDIENT_LIST_EXTRA_DATA
        );
        RecyclerView ingredientsRecyclerView
                = rootView.findViewById(R.id.rv_recipe_info_list_ingredients);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(
                getContext(),
                getContext().getResources().getInteger(R.integer.span_count)
        );
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.setHasFixedSize(true);
        RecipeInfoListItemIngredientsAdapter recipeInfoListItemIngredientsAdapter
                = new RecipeInfoListItemIngredientsAdapter(getContext(), ingredients);
        ingredientsRecyclerView.setAdapter(recipeInfoListItemIngredientsAdapter);
        return rootView;
    }
}
