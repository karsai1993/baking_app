package udacity.com.bakingtime.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;
import udacity.com.bakingtime.model.Step;

/**
 * Created by Laci on 08/04/2018.
 */

public class JsonUtils {

    private static final String NAME_KEY = "name";
    private static final String INGREDIENTS_KEY = "ingredients";
    private static final String QUANTITY_KEY = "quantity";
    private static final String MEASURE_KEY = "measure";
    private static final String INGREDIENT_KEY = "ingredient";
    private static final String STEPS_KEY = "steps";
    private static final String ID_KEY = "id";
    private static final String SHORT_DESCRIPTION_KEY = "shortDescription";
    private static final String DESCRIPTION_KEY = "description";
    private static final String VIDEO_URL_KEY = "videoURL";
    private static final String THUMBNAIL_URL_KEY = "thumbnailURL";
    private static final String SERVING_KEY = "servings";
    private static final String IMAGE_URL_KEY = "image";

    public static List<Recipe> getRecipesFromJson(String responseJson) throws JSONException {
        JSONArray recipeJsonArray = new JSONArray(responseJson);
        List<Recipe> recipeList = new ArrayList<>();
        for (int i = 0; i < recipeJsonArray.length(); i++) {
            JSONObject currRecipe = recipeJsonArray.getJSONObject(i);
            String name = currRecipe.optString(NAME_KEY);
            JSONArray ingredientJsonArray = currRecipe.optJSONArray(INGREDIENTS_KEY);
            List<Ingredient> ingredientList = new ArrayList<>();
            for (int j = 0; j < ingredientJsonArray.length(); j++) {
                JSONObject currIngredient = ingredientJsonArray.getJSONObject(j);
                int quantity = currIngredient.optInt(QUANTITY_KEY);
                String measure = currIngredient.optString(MEASURE_KEY);
                String ingredientName = currIngredient.optString(INGREDIENT_KEY);
                ingredientList.add( new Ingredient(quantity, measure, ingredientName) );
            }
            JSONArray stepJsonArray = currRecipe.optJSONArray(STEPS_KEY);
            List<Step> stepList = new ArrayList<>();
            for (int j = 0; j < stepJsonArray.length(); j++) {
                JSONObject currStep = stepJsonArray.getJSONObject(j);
                int id = currStep.getInt(ID_KEY);
                String shortDescription = currStep.optString(SHORT_DESCRIPTION_KEY);
                String description = currStep.optString(DESCRIPTION_KEY);
                String videoUrl = currStep.optString(VIDEO_URL_KEY);
                String thumbnailUrl = currStep.optString(THUMBNAIL_URL_KEY);
                stepList.add( new Step(id, shortDescription, description, videoUrl, thumbnailUrl) );
            }
            int servingNum = currRecipe.optInt(SERVING_KEY);
            String imageUrl = currRecipe.optString(IMAGE_URL_KEY);
            recipeList.add( new Recipe(name, ingredientList, stepList, servingNum, imageUrl) );
        }
        return recipeList;
    }

}
