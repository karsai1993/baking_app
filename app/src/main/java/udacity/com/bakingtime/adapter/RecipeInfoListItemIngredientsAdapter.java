package udacity.com.bakingtime.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import udacity.com.bakingtime.R;
import udacity.com.bakingtime.model.Ingredient;

/**
 * Created by Laci on 11/04/2018.
 */

public class RecipeInfoListItemIngredientsAdapter extends RecyclerView.Adapter<RecipeInfoListItemIngredientsAdapter.RecipeInfoListItemIngredientsViewHolder>{

    private Context mContext;
    private List<Ingredient> mIngredientList;

    public RecipeInfoListItemIngredientsAdapter(Context context, List<Ingredient> ingredientList) {
        this.mContext = context;
        this.mIngredientList = ingredientList;
    }

    @Override
    public RecipeInfoListItemIngredientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeInfoListItemIngredientsViewHolder(
                LayoutInflater.from(mContext)
                .inflate(R.layout.recipe_info_list_ingredients_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(RecipeInfoListItemIngredientsViewHolder holder, int position) {
        holder.sequence.setText(String.valueOf(position + 1) + '.');
        Ingredient ingredient = mIngredientList.get(position);
        holder.name.setText(ingredient.getName());
        holder.quantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.measure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    class RecipeInfoListItemIngredientsViewHolder extends RecyclerView.ViewHolder {

        TextView sequence;
        TextView name;
        TextView quantity;
        TextView measure;

        public RecipeInfoListItemIngredientsViewHolder(View itemView) {
            super(itemView);
            sequence = itemView.findViewById(R.id.tv_recipe_info_list_ingredients_item_sequence);
            name = itemView.findViewById(R.id.tv_recipe_info_list_ingredients_item_name);
            quantity = itemView.findViewById(R.id.tv_recipe_info_list_ingredients_item_quantity);
            measure = itemView.findViewById(R.id.tv_recipe_info_list_ingredients_item_measure);
        }
    }
}
