package udacity.com.bakingtime.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import udacity.com.bakingtime.ApplicationHelper;
import udacity.com.bakingtime.R;

/**
 * Created by Laci on 12/04/2018.
 * This class creates the fragment of welcome screen in two pane mode.
 */

public class RecipeInfoListEmptyDetailFragment extends Fragment {

    public RecipeInfoListEmptyDetailFragment() {}

    private static final String EMPTY_FRAGMENT_MESSAGE_FIRST_PART = "Choose one of the details of ";
    private static final String EMPTY_FRAGMENT_MESSAGE_SECOND_PART = " on the left!";

    TextView emptyDetailFragmentTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_recipe_info_list_detail_empty,
                container,
                false
        );
        String recipeTitle = getArguments().getString(ApplicationHelper.RECIPE_NAME_EXTRA_DATA);
        emptyDetailFragmentTextView = rootView.findViewById(R.id.tv_empty_detail);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(EMPTY_FRAGMENT_MESSAGE_FIRST_PART);
        stringBuilder.append(recipeTitle);
        stringBuilder.append(EMPTY_FRAGMENT_MESSAGE_SECOND_PART);
        emptyDetailFragmentTextView.setText(stringBuilder.toString());
        return rootView;
    }
}
