package udacity.com.bakingtime.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    private Context mContext;
    private Recipe mRecipe;
    private static OnClickListener mOnClickListener;
    private static boolean mIsClicked;
    private static int mClickedPosition;
    private boolean mIsRotated;
    private boolean mIsComingFromWidget;

    public RecipeInfoListAdapter(
            Context context,
            Recipe recipe,
            OnClickListener onClickListener,
            boolean isRotated,
            boolean isFromWidget) {
        this.mContext = context;
        this.mRecipe = recipe;
        this.mOnClickListener = onClickListener;
        this.mIsRotated = isRotated;
        if (!mIsRotated && !mIsClicked) this.mClickedPosition = -1;
        this.mIsClicked = false;
        this.mIsComingFromWidget = isFromWidget;
    }

    public RecipeInfoListAdapter(
            Context context,
            Recipe recipe,
            int clickedPosition,
            boolean isRotated) {
        this.mIsClicked = true;
        this.mContext = context;
        this.mRecipe = recipe;
        this.mClickedPosition = clickedPosition;
        this.mIsRotated = isRotated;
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
        if (mIsComingFromWidget) {
            if (position == 0) {
                applyClickedBackground(holder.recipeInfoListItemLinearLayout);
            } else {
                applyNonClickedBackground(holder.recipeInfoListItemLinearLayout);
            }
        } else if (mIsRotated) {
            if (mClickedPosition != -1 && mClickedPosition == position) {
                applyClickedBackground(holder.recipeInfoListItemLinearLayout);
            } else {
                applyNonClickedBackground(holder.recipeInfoListItemLinearLayout);
            }
        } else {
            if (mIsClicked && mClickedPosition == position) {
                applyClickedBackground(holder.recipeInfoListItemLinearLayout);
            } else {
                applyNonClickedBackground(holder.recipeInfoListItemLinearLayout);
            }
        }
    }

    private void applyClickedBackground(LinearLayout linearLayout) {
        linearLayout.setBackground(
                mContext.getResources()
                        .getDrawable(R.drawable.recipe_info_list_item_clicked_background)
        );
    }

    private void applyNonClickedBackground(LinearLayout linearLayout) {
        linearLayout.setBackground(
                mContext.getResources()
                        .getDrawable(R.drawable.recipe_info_list_item_background)
        );
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
