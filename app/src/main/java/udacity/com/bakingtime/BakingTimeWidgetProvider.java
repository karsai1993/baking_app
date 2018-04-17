package udacity.com.bakingtime;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.List;

import udacity.com.bakingtime.activity.RecipeInfoListDetail;
import udacity.com.bakingtime.model.Recipe;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link BakingTimeWidgetProviderConfigureActivity BakingTimeWidgetProviderConfigureActivity}
 */
public class BakingTimeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        List<Recipe> recipeList = BakingTimeWidgetProviderConfigureActivity.getRecipeList();
        if (recipeList == null) return;
        int position = BakingTimeWidgetProviderConfigureActivity.getPosition();
        Recipe recipe = recipeList.get(position);

        RemoteViews views = new RemoteViews(
                context.getPackageName(),
                R.layout.baking_time_widget_provider
        );

        Intent appIntent = new Intent(context, RecipeInfoListDetail.class);
        appIntent.setAction(Intent.ACTION_VIEW);
        appIntent.putExtra(ApplicationHelper.RECIPE_EXTRA_DATA, recipe);
        appIntent.putExtra(ApplicationHelper.RECIPE_INFO_LIST_POSITION_EXTRA_DATA, 0);
        PendingIntent appPendingIntent = PendingIntent.getActivity(
                context,
                0,
                appIntent,
                0
        );
        views.setOnClickPendingIntent(R.id.ll_widget, appPendingIntent);

        Intent intent = new Intent(context, ListWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setTextViewText(R.id.tv_widget_title, recipe.getName());
        views.setRemoteAdapter(R.id.widget_view, intent);

        views.setEmptyView(R.id.widget_view, R.id.empty_view);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
            Toast.makeText(context, "created " + String.valueOf(appWidgetId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BakingTimeWidgetProviderConfigureActivity.deleteRecipePref(context, appWidgetId);
            Toast.makeText(context, "deleted " + String.valueOf(appWidgetId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

