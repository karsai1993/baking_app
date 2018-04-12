package udacity.com.bakingtime.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.adapter.RecipeInfoListItemIngredientsAdapter;
import udacity.com.bakingtime.model.Ingredient;

/**
 * Created by Laci on 11/04/2018.
 */

public class RecipeInfoListIngredientsFragment extends Fragment {

    public RecipeInfoListIngredientsFragment () {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_info_list_ingredients, container, false);
        List<Ingredient> ingredients = getArguments().getParcelableArrayList(CommonApplicationFields.INGREDIENT_LIST_EXTRA_DATA);
        RecyclerView ingredientsRecyclerView = rootView.findViewById(R.id.rv_recipe_info_list_ingredients);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        ingredientsRecyclerView.setLayoutManager(layoutManager);
        ingredientsRecyclerView.hasFixedSize();
        RecipeInfoListItemIngredientsAdapter recipeInfoListItemIngredientsAdapter = new RecipeInfoListItemIngredientsAdapter(getContext(), ingredients);
        ingredientsRecyclerView.setAdapter(recipeInfoListItemIngredientsAdapter);
        return rootView;
    }
}