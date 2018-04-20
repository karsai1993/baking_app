package udacity.com.bakingtime.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import udacity.com.bakingtime.R;
import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.utils.JsonUtils;
import udacity.com.bakingtime.utils.NetworkUtils;

/**
 * The configuration screen for the {@link BakingTimeWidgetProvider BakingTimeWidgetProvider} AppWidget.
 */
public class BakingTimeWidgetProviderConfigureActivity extends Activity {

    private static final String PREFS_NAME = "udacity.com.bakingtime.widget.BakingTimeWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String NETWORK_ERROR = "No network available! Please, check it and come back!";
    private static final String FETCH_ERROR = "Fetch error! Please, try it again!";
    private static final String USER_ERROR = "Please, select one item to continue!";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = BakingTimeWidgetProviderConfigureActivity.this;
            mWindowManager = getWindowManager();

            int chosenIndex = mRadioGroup.getCheckedRadioButtonId();
            if (chosenIndex == -1) {
                Toast.makeText(
                        BakingTimeWidgetProviderConfigureActivity.this,
                        USER_ERROR,
                        Toast.LENGTH_LONG
                ).show();
                return;
            }
            RadioButton chosenRadioButton = (RadioButton) mRadioGroup.getChildAt(chosenIndex);
            String chosenRecipeName = chosenRadioButton.getText().toString();
            for (int i = 0; i < mRecipeList.size(); i++) {
                if (mRecipeList.get(i).getName().equals(chosenRecipeName)) {
                    mPosition = i;
                }
            }

            saveRecipePref(context, mAppWidgetId, mRecipeList.get(mPosition).getIngredientList());
            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            BakingTimeWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId);
            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    static List<Recipe> getRecipeList() {
        return mRecipeList;
    }

    static int getPosition() {
        return mPosition;
    }

    /**
     * Function to get the smallest width of the screen
     * source: https://stackoverflow.com/questions/15055458/detect-7-inch-and-10-inch-tablet-programmatically
     * @return
     */
    static float getSmallestWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float scaleFactor = metrics.density;
        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;
        return Math.min(widthDp, heightDp);
    }

    private static RadioGroup mRadioGroup;
    private TextView mInstruction;
    private Button mBtn;
    private static List<Recipe> mRecipeList;
    private static int mPosition;
    private static WindowManager mWindowManager;

    public BakingTimeWidgetProviderConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveRecipePref(Context context, int appWidgetId, List<Ingredient> ingredientList) {
        Set<String> ingredientSet = new HashSet<>();
        for (Ingredient ingredient : ingredientList) {
            ingredientSet.add(ingredient.getName());
        }
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putStringSet(PREF_PREFIX_KEY + appWidgetId, ingredientSet);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static Set<String> loadRecipePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getStringSet(PREF_PREFIX_KEY + appWidgetId, null);
    }

    static void deleteRecipePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.baking_time_widget_provider_configure);
        mInstruction = findViewById(R.id.tv_instruction);
        mBtn = findViewById(R.id.add_button);
        mBtn.setOnClickListener(mOnClickListener);
        mRadioGroup = findViewById(R.id.rg_options);

        if (NetworkUtils.isNetworkAvailable(this)) {
            new RecipeWidgetProcessor().execute();
        } else {
            displayProblem(NETWORK_ERROR);
        }

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    private void displayProblem(String problemText) {
        mInstruction.setText(problemText);
        mInstruction.setTextColor(Color.RED);
        mRadioGroup.setVisibility(View.GONE);
        mBtn.setEnabled(false);
    }

    class RecipeWidgetProcessor extends AsyncTask<Void, Void, List<Recipe>> {

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
            if (recipeList != null) {
                mRecipeList = recipeList;
                for (int i = 0; i < recipeList.size(); i++) {
                    RadioButton radioButton = new RadioButton(BakingTimeWidgetProviderConfigureActivity.this);
                    radioButton.setId(i);
                    radioButton.setText(recipeList.get(i).getName());
                    mRadioGroup.addView(radioButton, i);
                }
            } else {
                displayProblem(FETCH_ERROR);
            }
        }
    }
}

