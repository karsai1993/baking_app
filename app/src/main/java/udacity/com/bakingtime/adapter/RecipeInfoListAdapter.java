package udacity.com.bakingtime.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 10/04/2018.
 */

public class RecipeInfoListAdapter extends RecyclerView.Adapter<RecipeInfoListAdapter.RecipeInfoListViewHolder>{

    private static Context mContext;
    private static Recipe mRecipe;
    private static OnClickListener mOnClickListener;
    private boolean mIsClicked;
    private int mClickedPosition;

    public RecipeInfoListAdapter(Context context, Recipe recipe, OnClickListener onClickListener) {
        this.mContext = context;
        this.mRecipe = recipe;
        this.mOnClickListener = onClickListener;
    }

    public RecipeInfoListAdapter(boolean isClicked, int clickedPosition) {
        this.mIsClicked = isClicked;
        this.mClickedPosition = clickedPosition;
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
            holder.recipeInfoListItemName.setText(ApplicationHelper.INGREDIENTS_NAME);
            holder.recipeInfoListItemNumber.setVisibility(View.GONE);
        } else {
            Step step = mRecipe.getStepList().get(position - 1);
            holder.recipeInfoListItemName.setText(step.getShortDescription());
            holder.recipeInfoListItemNumber.setVisibility(View.VISIBLE);
            int stepId = step.getId();
            if (stepId == 0) {
                holder.recipeInfoListItemNumber.setVisibility(View.GONE);
            } else {
                holder.recipeInfoListItemNumber.setText(
                        mContext.getResources().getString(R.string.hashtag) + String.valueOf(stepId)
                );
            }
        }
        if (mIsClicked && mClickedPosition == position) {
            holder.recipeInfoListItemLinearLayout.setBackground(
                    mContext.getResources()
                            .getDrawable(R.drawable.recipe_info_list_item_clicked_background)
            );
        } else {
            holder.recipeInfoListItemLinearLayout.setBackground(
                    mContext.getResources()
                            .getDrawable(R.drawable.recipe_info_list_item_background)
            );
        }
    }

    @Override
    public int getItemCount() {
        return mRecipe.getStepList().size() + 1;
    }

    class RecipeInfoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView recipeInfoListItemName;
        TextView recipeInfoListItemNumber;
        LinearLayout recipeInfoListItemLinearLayout;

        public RecipeInfoListViewHolder(View itemView) {
            super(itemView);
            recipeInfoListItemName = itemView.findViewById(R.id.tv_recipe_info_list_item_name);
            recipeInfoListItemNumber = itemView.findViewById(R.id.tv_recipe_info_list_item_number);
            recipeInfoListItemLinearLayout = itemView.findViewById(R.id.ll_recipe_info_list_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnClickListener.onItemClick(getAdapterPosition());
        }
    }
}
