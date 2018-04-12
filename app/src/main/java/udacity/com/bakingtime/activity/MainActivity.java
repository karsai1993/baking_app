package udacity.com.bakingtime.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.com.bakingtime.CommonApplicationFields;
import udacity.com.bakingtime.OnClickListener;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.adapter.RecipeAdapter;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.utils.JsonUtils;
import udacity.com.bakingtime.utils.NetworkUtils;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    @BindView(R.id.pb_loading)
    ProgressBar mLoadingProgressBar;
    @BindView(R.id.tv_fetch_error)
    TextView mFetchErrorTextView;
    @BindView(R.id.tv_network_error)
    TextView mNetworkErrorTextView;
    @BindView(R.id.rv_recipes)
    RecyclerView mRecipeRecyclerView;

    private List<Recipe> mRecipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 1);
        mRecipeRecyclerView.setLayoutManager(layoutManager);
        mRecipeRecyclerView.setHasFixedSize(true);

        getRecipesFromInternet(this);
    }

    private void getRecipesFromInternet(Context context) {
        if (NetworkUtils.isNetworkAvailable(context)) {
            showData();
            new RecipeProcessor().execute();
        } else {
            showNetworkError();
        }
    }

    private void showData() {
        mRecipeRecyclerView.setVisibility(View.VISIBLE);
        mFetchErrorTextView.setVisibility(View.INVISIBLE);
        mNetworkErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void showFetchError() {
        mFetchErrorTextView.setVisibility(View.VISIBLE);
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
        mNetworkErrorTextView.setVisibility(View.INVISIBLE);
    }

    private void showNetworkError() {
        mNetworkErrorTextView.setVisibility(View.VISIBLE);
        mFetchErrorTextView.setVisibility(View.INVISIBLE);
        mRecipeRecyclerView.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            getRecipesFromInternet(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onItemClick(int position) {
        //Recipe selectedRecipe = mRecipeList.get(position);
        Intent startDetailActivity = new Intent(MainActivity.this, DetailActivity.class);
        startDetailActivity.putParcelableArrayListExtra(CommonApplicationFields.RECIPE_LIST_EXTRA_DATA, (ArrayList<? extends Parcelable>) mRecipeList);
        startDetailActivity.putExtra(CommonApplicationFields.RECIPE_POSITION_EXTRA_DATA, position);
        startActivity(startDetailActivity);
    }

    class RecipeProcessor extends AsyncTask<Void, Void, List<Recipe>> {

        @Override
        protected void onPreExecute() {
            mLoadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Recipe> doInBackground(Void... voids) {
            URL recipeRequestUrl = NetworkUtils.buildUrl();
            try {
                String recipeResponseAsJson = NetworkUtils.getResponse(recipeRequestUrl);
                return JsonUtils.getRecipesFromJson(recipeResponseAsJson);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Recipe> recipeList) {
            mLoadingProgressBar.setVisibility(View.INVISIBLE);
            if (recipeList != null) {
                mRecipeList = recipeList;
                RecipeAdapter recipeAdapter = new RecipeAdapter(MainActivity.this, recipeList);
                mRecipeRecyclerView.setAdapter(recipeAdapter);
            } else {
                showFetchError();
            }
        }
    }
}
