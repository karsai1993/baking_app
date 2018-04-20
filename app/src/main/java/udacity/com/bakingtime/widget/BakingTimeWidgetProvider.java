package udacity.com.bakingtime.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.List;

import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.R;
import udacity.com.bakingtime.activity.DetailActivity;
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

        float smallestWidth = BakingTimeWidgetProviderConfigureActivity.getSmallestWidth();
        Intent appIntent;
        if (smallestWidth > 600) {
            appIntent = new Intent(context, DetailActivity.class);
            appIntent.putParcelableArrayListExtra(
                    ApplicationHelper.RECIPE_LIST_EXTRA_DATA,
                    (ArrayList<Recipe>) recipeList);
            appIntent.putExtra(ApplicationHelper.RECIPE_POSITION_EXTRA_DATA, position);
            appIntent.putExtra(ApplicationHelper.WIDGET_BOOLEAN_EXTRA_DATA, true);
        } else {
            appIntent = new Intent(context, RecipeInfoListDetail.class);
            appIntent.putExtra(ApplicationHelper.RECIPE_EXTRA_DATA, recipe);
            appIntent.putExtra(ApplicationHelper.RECIPE_INFO_LIST_POSITION_EXTRA_DATA, 0);
        }
        PendingIntent appPendingIntent = PendingIntent.getActivity(
                context,
                appWidgetId,
                appIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
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
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            BakingTimeWidgetProviderConfigureActivity.deleteRecipePref(context, appWidgetId);
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

