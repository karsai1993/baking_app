package udacity.com.bakingtime.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 10/04/2018.
 */

public class RecipeInfoListAdapter extends RecyclerView.Adapter<RecipeInfoListAdapter.RecipeInfoListViewHolder>{

    private Context mContext;
    private Recipe mRecipe;
    private OnClickListener mOnClickListener;

    public RecipeInfoListAdapter(Context context, Recipe recipe, OnClickListener onClickListener) {
        this.mContext = context;
        this.mRecipe = recipe;
        this.mOnClickListener = onClickListener;
    }

    @Override
    public RecipeInfoListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecipeInfoListViewHolder(
                LayoutInflater.from(mContext)
                .inflate(
                        R.layout.recipe_info_list_item,
                        parent,
                        false
                )
        );
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(RecipeInfoListViewHolder holder, int position) {
        if (position == 0) {
            holder.recipeInfoListItemName.setText(CommonApplicationFields.INGREDIENTS_NAME);
            holder.recipeInfoListItemName.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.recipeInfoListItemNumber.setText(String.valueOf(mRecipe.getIngredientList().size()));
            holder.recipeInfoListItemIcon.setImageResource(R.drawable.ic_icons8_ingredients_96);
            holder.recipeInfoListItemQuantityLinearLayout.setBackground(
                    mContext.getResources()
                            .getDrawable(R.drawable.recipe_info_list_ingredients_quantity_background));
        } else {
            List<Step> stepList = mRecipe.getStepList();
            Step step = stepList.get(position - 1);
            holder.recipeInfoListItemName.setText(step.getShortDescription());
            holder.recipeInfoListItemName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            holder.recipeInfoListItemNumber.setText(String.valueOf(step.getId()) + ".");
            holder.recipeInfoListItemIcon.setImageResource(R.drawable.ic_icons8_footsteps_96);
            holder.recipeInfoListItemQuantityLinearLayout.setBackground(
                    mContext.getResources()
                            .getDrawable(R.drawable.recipe_info_list_steps_quantity_background));
        }
    }

    @Override
    public int getItemCount() {
        return mRecipe.getStepList().size() + 1;
    }

    class RecipeInfoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeInfoListItemName;
        ImageView recipeInfoListItemIcon;
        TextView recipeInfoListItemNumber;
        LinearLayout recipeInfoListItemQuantityLinearLayout;

        public RecipeInfoListViewHolder(View itemView) {
            super(itemView);
            recipeInfoListItemName = itemView.findViewById(R.id.tv_recipe_info_list_item_name);
            recipeInfoListItemIcon = itemView.findViewById(R.id.iv_recipe_info_list_item_icon);
            recipeInfoListItemNumber = itemView.findViewById(R.id.tv_recipe_info_list_item_number);
            recipeInfoListItemQuantityLinearLayout = itemView.findViewById(R.id.ll_recipe_info_list_quantity);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onItemClick(getAdapterPosition());
        }
    }
}
