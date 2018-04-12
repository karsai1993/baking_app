package udacity.com.bakingtime.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.fragment.RecipeInfoListFragment;
import udacity.com.bakingtime.model.Recipe;

/**
 * Created by Laci on 10/04/2018.
 */

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.ib_info_list_nav_btn_prev)
    ImageButton prevBtn;
    @BindView(R.id.ib_info_list_nav_btn_next)
    ImageButton nextBtn;

    private List<Recipe> mRecipeList;
    private int mPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Bundle receivedBundle = getIntent().getExtras();
        mRecipeList = receivedBundle.getParcelableArrayList(
                CommonApplicationFields.RECIPE_LIST_EXTRA_DATA
        );
        mPosition = receivedBundle.getInt(CommonApplicationFields.RECIPE_POSITION_EXTRA_DATA);

        displayDetails();
    }

    private void displayDetails() {
        getSupportActionBar().setTitle(mRecipeList.get(mPosition).getName());

        Fragment fragment = new RecipeInfoListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonApplicationFields.RECIPE_EXTRA_DATA, mRecipeList.get(mPosition));
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_info_fragment, fragment)
                .commit();

        CommonApplicationFields.navigationButtonHandler(
                mPosition,
                mRecipeList.size(),
                prevBtn,
                nextBtn
        );
    }

    public void onNavigationButtonClicked(View view) {
        int clickedBtnId = view.getId();
        if (clickedBtnId == R.id.ib_info_list_nav_btn_prev) {
            mPosition --;
        } else {
            mPosition ++;
        }
        displayDetails();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CommonApplicationFields.POSITION_STATE_SAVE_KEY, mPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPosition = savedInstanceState.getInt(CommonApplicationFields.POSITION_STATE_SAVE_KEY);
        displayDetails();
    }
}
