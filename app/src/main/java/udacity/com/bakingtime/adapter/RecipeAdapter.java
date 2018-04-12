package udacity.com.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 08/04/2018.
 */

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>{

    private Context mContext;
    private List<Recipe> mRecipeList;
    private OnClickListener mOnClickListener;

    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        this.mContext = context;
        this.mOnClickListener = (OnClickListener) context;
        this.mRecipeList = recipeList;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeViewHolder(
                LayoutInflater
                        .from(mContext)
                        .inflate(
                                R.layout.recipe_list_item,
                                parent,
                                false
                        )
        );
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        Recipe currRecipe = mRecipeList.get(position);
        holder.recipeName.setText(currRecipe.getName());
        holder.recipeServingNum.setText(
                String.valueOf(currRecipe.getServingNum())
        );
        holder.recipeIngredientNum.setText(
                String.valueOf(currRecipe.getIngredientList().size())
        );
        holder.recipeStepNum.setText(
                String.valueOf(getMaxStepCount(currRecipe.getStepList()))
        );
    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    private int getMaxStepCount(List<Step> stepList) {
        return stepList.get(stepList.size() - 1).getId();
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeName;
        TextView recipeIngredientNum;
        TextView recipeStepNum;
        TextView recipeServingNum;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.tv_recipe_name);
            recipeIngredientNum = itemView.findViewById(R.id.tv_recipe_ingredient_number);
            recipeStepNum = itemView.findViewById(R.id.tv_recipe_step_number);
            recipeServingNum = itemView.findViewById(R.id.tv_recipe_serving_number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onItemClick(getAdapterPosition());
        }
    }
}
