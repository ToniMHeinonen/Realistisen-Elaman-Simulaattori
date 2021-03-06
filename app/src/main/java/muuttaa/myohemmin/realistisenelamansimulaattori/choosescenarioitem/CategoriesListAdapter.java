package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.InitializeActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;

public class CategoriesListAdapter extends BaseExpandableListAdapter {

    private ChooseScenarioActivity activity;
    private Context context;
    private List<String> parentItem;
    private HashMap<String, List<ScenarioItem>> childItem;

    private static final int GROUP_TYPE_1 = 0;
    private static final int GROUP_TYPE_2 = 1;

    /**
     * Initializes instance of this class.
     * @param activity current activity
     * @param parentItem topic names of categories
     * @param childItem items inside categories
     */
    public CategoriesListAdapter(ChooseScenarioActivity activity, List<String> parentItem,
                                 HashMap<String, List<ScenarioItem>> childItem) {
        this.activity = activity;
        this.context = activity.getBaseContext(); // Get base context to apply styles correctly
        this.parentItem = parentItem;
        this.childItem = childItem;
    }

    /**
     * Gets item at given position.
     * @param listPosition category position
     * @param expandedListPosition item position inside category
     * @return list item
     */
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.childItem.get(this.parentItem.get(listPosition))
                .get(expandedListPosition);
    }

    /**
     * Gets item id in given position.
     *
     * Currently items don't have ids, so just return the position.
     * @param listPosition category position
     * @param expandedListPosition item position inside category
     * @return id of the child
     */
    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    /**
     * Gets item view at given position.
     * @param listPosition category position
     * @param expandedListPosition item position inside category
     * @param isLastChild whether it is the last child of category
     * @param convertView view of the item
     * @param parent parent of the convertView
     * @return view of the item
     */
    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ScenarioItem item = (ScenarioItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.scenario_item, null);
        }

        TextView text1 = convertView.findViewById(R.id.scenarioPercentage);
        TextView text2 = convertView.findViewById(R.id.scenarioName);
        int percent = item.getPercentageCompleted();
        text1.setText(percent + " %");
        text2.setText(""+item.getName());

        LinearLayout layout = convertView.findViewById(R.id.buttonLayout);

        // If scenario has not been played yet, keep original button color
        if (item.getLastTimePlayed().getTime() != 0) {
            if (percent < 50) {
                layout.setBackground(activity.getResources().getDrawable(R.drawable.red_button));
            } else if (percent < 100) {
                layout.setBackground(activity.getResources().getDrawable(R.drawable.yellow_button));
            } else {
                layout.setBackground(activity.getResources().getDrawable(R.drawable.green_button));
            }
        } else {
            layout.setBackground(activity.getResources().getDrawable(R.drawable.custom_button));
        }

        return convertView;
    }

    /**
     * Gets how many children given category has.
     * @param listPosition category position
     * @return amount of childen in category
     */
    @Override
    public int getChildrenCount(int listPosition) {
        return this.childItem.get(this.parentItem.get(listPosition))
                .size();
    }

    /**
     * Gets category at given position.
     * @param listPosition position of the category
     * @return category
     */
    @Override
    public Object getGroup(int listPosition) {
        return this.parentItem.get(listPosition);
    }

    /**
     * Gets the amount of categories.
     * @return amount of categories
     */
    @Override
    public int getGroupCount() {
        return this.parentItem.size();
    }

    /**
     * Gets id of the given category.
     *
     * Currently categories don't have ids, so just return the position.
     * @param listPosition position of the category
     * @return id of the category
     */
    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    /**
     * Gets category view at given position.
     * @param listPosition category position
     * @param isExpanded whether it is expanded or not
     * @param convertView view of the category
     * @param parent parent object of the category
     * @return category view
     */
    @Override
    public View getGroupView(final int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final String listTitle = (String) getGroup(listPosition);

        int groupType = getGroupType(listPosition);

        if (convertView == null) {
            switch (groupType) {
                case GROUP_TYPE_1 :
                    convertView = layoutInflater.inflate(R.layout.category_topic_default, null);
                    break;
                default:
                    convertView = layoutInflater.inflate(R.layout.category_topic, null);
                    break;
            }
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        switch (groupType) {
            case GROUP_TYPE_1 :
                break;
            default:
                Button optionsButton = convertView.findViewById(R.id.listOptions);
                optionsButton.setOnClickListener((v) -> activity.openCategoryOptions(listTitle));
                break;

        }

        return convertView;
    }

    /**
     * Returns the amount of group types.
     * @return group type amount
     */
    @Override
    public int getGroupTypeCount() {
        return 2;
    }

    /**
     * Returns the group type in the given position.
     * @param groupPosition position to check
     * @return group type
     */
    @Override
    public int getGroupType(int groupPosition) {
        final String listTitle = (String) getGroup(groupPosition);

        if (listTitle == null || Helper.isCategoryFromResources(listTitle) ||
                listTitle.equals(context.getString(R.string.scenarios))) {
            return GROUP_TYPE_1;
        } else {
            return GROUP_TYPE_2;
        }
    }

    /**
     * Gets whether object has stable ids or not.
     * @return whether object has stable ids or not
     */
    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * Gets whether child is selectable at a given position.
     * @param listPosition category position
     * @param expandedListPosition item position inside category
     * @return whether child is selectable
     */
    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    /**
     * Updates categories and scenarios.
     * @param listDataHeader categories
     * @param listChildData scenarios
     */
    public void setNewItems(List<String> listDataHeader,HashMap<String, List<ScenarioItem>> listChildData) {
        this.parentItem = listDataHeader;
        this.childItem = listChildData;
        notifyDataSetChanged();
    }

    /**
     * Inserts data to categories and returns categorized values.
     * @param scenarios list of scenarios to check for category
     * @return map of categorized values
     */
    public static LinkedHashMap<String, List<ScenarioItem>> getData(List<ScenarioItem> scenarios,
    HashSet<String> resourcesCategories) {
        LinkedHashMap<String, List<ScenarioItem>> ParentItem = new LinkedHashMap<>();

        // Add categories from resources and user created to same list
        ArrayList<String> resCategories = new ArrayList<>(resourcesCategories); // Resources
        Collections.sort(resCategories); // Sort resources alphabetically
        ArrayList<String> allCategories = new ArrayList<>(GlobalPrefs.loadCategories()); // User created
        allCategories.addAll(resCategories); // Add resources categories after user created
        LinkedHashMap<String, List<ScenarioItem>> categoryData = new LinkedHashMap<>();

        // Add all categories to hash map which hold category name and array list
        for (int i = 0; i < allCategories.size(); i++) {
            categoryData.put(allCategories.get(i), new ArrayList<ScenarioItem>());
        }

        // Create list which holds scenarios which category is null
        List<ScenarioItem> noCategory = new ArrayList<>();

        // Loop through all scenarios and add them to the correct category
        for (int i = 0; i < scenarios.size(); i++) {
            ScenarioItem item = scenarios.get(i);
            String category = item.getCategory();

            // If category is null, add it to the default category
            if (category == null) {
                noCategory.add(item);
            } else {
                List<ScenarioItem> categoryList = categoryData.get(category);
                // If user moves scenario to for example "Traveling" and then changes language
                // categoryList will be null or if category gets deleted from resources
                if (categoryList != null)
                    categoryData.get(category).add(item);
                else
                    noCategory.add(item);
            }
        }

        // Get default category name from resources
        String noCategoryName = InitializeActivity.getContext().getResources().getString(R.string.scenarios);
        ParentItem.put(noCategoryName, noCategory);

        Iterator it = categoryData.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            ParentItem.put((String) pair.getKey(), (List<ScenarioItem>)pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        return ParentItem;
    }
}
