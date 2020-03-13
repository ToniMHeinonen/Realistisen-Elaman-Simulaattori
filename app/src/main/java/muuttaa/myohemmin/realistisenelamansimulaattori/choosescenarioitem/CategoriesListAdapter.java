package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class CategoriesListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> ParentItem;
    private HashMap<String, List<ScenarioItem>> ChildItem;

    /**
     * Initializes instance of this class.
     * @param context application context
     * @param ParentItem topic names of categories
     * @param ChildItem items inside categories
     */
    public CategoriesListAdapter(Context context, List<String> ParentItem,
                                 HashMap<String, List<ScenarioItem>> ChildItem) {
        this.context = context;
        this.ParentItem = ParentItem;
        this.ChildItem = ChildItem;
    }

    /**
     * Gets item at given position.
     * @param listPosition category position
     * @param expandedListPosition item position inside category
     * @return list item
     */
    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.ChildItem.get(this.ParentItem.get(listPosition))
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
        text1.setText(""+item.getPercentageCompleted());
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
        return this.ChildItem.get(this.ParentItem.get(listPosition))
                .size();
    }

    /**
     * Gets category at given position.
     * @param listPosition position of the category
     * @return category
     */
    @Override
    public Object getGroup(int listPosition) {
        return this.ParentItem.get(listPosition);
    }

    /**
     * Gets the amount of categories.
     * @return amount of categories
     */
    @Override
    public int getGroupCount() {
        return this.ParentItem.size();
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
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_topic, null);
        }
        TextView listTitleTextView = convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
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
     * Inserts data to categories and returns categorized values.
     * @param scenarios list of scenarios to check for category
     * @return map of categorized values
     */
    public static HashMap<String, List<ScenarioItem>> getData(List<ScenarioItem> scenarios) {
        HashMap<String, List<ScenarioItem>> ParentItem = new HashMap<>();

        ArrayList<String> categories = GlobalPrefs.loadCategories();
        HashMap<String, List<ScenarioItem>> categoryData = new HashMap<>();

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