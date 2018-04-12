package udacity.com.bakingtime.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Laci on 07/04/2018.
 */

public class Recipe implements Parcelable {

    private String mName;
    private List<Ingredient> mIngredientList;
    private List<Step> mStepList;
    private int mServingNum;
    private String mImageUrl;

    public Recipe (
            String name,
            List<Ingredient> ingredientList,
            List<Step> stepList,
            int servingNum,
            String imageUrl
    ) {
        this.mName = name;
        this.mIngredientList = ingredientList;
        this.mStepList = stepList;
        this.mServingNum = servingNum;
        this.mImageUrl = imageUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public List<Ingredient> getIngredientList() {
        return mIngredientList;
    }

    public void setIngredientList(List<Ingredient> ingredientList) {
        this.mIngredientList = ingredientList;
    }

    public List<Step> getStepList() {
        return mStepList;
    }

    public void setStepList(List<Step> stepList) {
        this.mStepList = stepList;
    }

    public int getServingNum() {
        return mServingNum;
    }

    public void setServingNum(int servingNum) {
        this.mServingNum = servingNum;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
    }

    protected Recipe(Parcel in) {
        mName = in.readString();
        mIngredientList = new ArrayList<>();
        in.readList(mIngredientList,getClass().getClassLoader());
        mStepList = new ArrayList<>();
        in.readList(mStepList, getClass().getClassLoader());
        mServingNum = in.readInt();
        mImageUrl = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeList(mIngredientList);
        dest.writeList(mStepList);
        dest.writeInt(mServingNum);
        dest.writeString(mImageUrl);
    }
}
