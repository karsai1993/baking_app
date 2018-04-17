package udacity.com.bakingtime;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import udacity.com.bakingtime.model.Ingredient;
import udacity.com.bakingtime.model.Recipe;

/**
 * Created by Laci on 15/04/2018.
 */

public class ListWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int mAppWidgetId;
    private Set<String> mIngredientSet;
    private List<String> mIngredientList;

    public ListRemoteViewsFactory(Context applicationContext, Intent intent) {
        this.mContext = applicationContext;
        this.mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mIngredientSet = BakingTimeWidgetProviderConfigureActivity.loadRecipePref(
                mContext,
                mAppWidgetId
        );
        Iterator<String> iterator = mIngredientSet.iterator();
        mIngredientList = new ArrayList<>();
        while (iterator.hasNext()) {
            mIngredientList.add(iterator.next());
        }
    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return mIngredientSet.size();
    }

    /**
     * This method acts like the onBindViewHolder method in an Adapter
     *
     * @param position The current position of the item in the GridView to be displayed
     * @return The RemoteViews object to display for the provided postion
     */
    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(
                mContext.getPackageName(),
                R.layout.widget_ingredients_item
        );
        views.setTextViewText(R.id.tv_widget_item_name, mIngredientList.get(position));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1; // Treat all items in the GridView the same
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}