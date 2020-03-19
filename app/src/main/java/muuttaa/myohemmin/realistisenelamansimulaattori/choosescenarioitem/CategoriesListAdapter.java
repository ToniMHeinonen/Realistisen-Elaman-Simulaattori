package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class CategoriesListAdapter extends BaseExpandableListAdapter {

    private ChooseScenarioActivity activity;
    private Context context;
    private List<String> parentItem;
    private HashMap<String, List<ScenarioItem>> childItem;

    /**
     * Initializes instance of this class.
     * @param activity current activity
     * @param parentItem topic names of categories
     * @param childItem items inside categories
     */
    public CategoriesListAdapter(ChooseScenarioActivity activity, List<String> parentItem,
                                 HashMap<String, List<ScenarioItem>> childItem) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
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
        text1.setText(item.getPercentageCompleted() + " %");
        text2.setText(""+item.getName());
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
        final String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_topic, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        Button optionsButton = convertView.findViewById(R.id.listOptions);
        optionsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                activity.openCategoryOptions(listTitle);
            }
        });

        return convertView;
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
    public static LinkedHashMap<String, List<ScenarioItem>> getData(List<ScenarioItem> scenarios) {
        LinkedHashMap<String, List<ScenarioItem>> ParentItem = new LinkedHashMap<>();

        ArrayList<String> categories = GlobalPrefs.loadCategories();
        LinkedHashMap<String, List<ScenarioItem>> categoryData = new LinkedHashMap<>();

        for (int i = 0; i < categories.size(); i++) {
            categoryData.put(categories.get(i), new ArrayList<ScenarioItem>());
        }

        List<ScenarioItem> noCategory = new ArrayList<>();

        for (int i = 0; i < scenarios.size(); i++) {
            ScenarioItem item = scenarios.get(i);
            String category = item.getCategory();

            if (category == null) {
                noCategory.add(item);
            } else {
                categoryData.get(category).add(item);
            }
        }

        String noCategoryName = ChooseScenarioActivity.getContext().getResources().getString(R.string.scenarios);
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
