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
import java.util.List;

import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class CategoriesListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> ParentItem;
    private HashMap<String, List<ScenarioItem>> ChildItem;


    public CategoriesListAdapter(Context context, List<String> ParentItem,
                                 HashMap<String, List<ScenarioItem>> ChildItem) {
        this.context = context;
        this.ParentItem = ParentItem;
        this.ChildItem = ChildItem;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.ChildItem.get(this.ParentItem.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ScenarioItem item = (ScenarioItem) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.scenario_item, null);
        }
        TextView text1 = (TextView) convertView.findViewById(R.id.scenarioPercentage);
        TextView text2 = (TextView) convertView.findViewById(R.id.scenarioName);
        text1.setText(""+item.getPercentageCompleted());
        text2.setText(""+item.getName());
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.ChildItem.get(this.ParentItem.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.ParentItem.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.ParentItem.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.category_topic, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }


    public static HashMap<String, List<ScenarioItem>> getData(List<ScenarioItem> scenarios) {
        HashMap<String, List<ScenarioItem>> ParentItem = new HashMap<>();

        List<ScenarioItem> german = new ArrayList<>();
        german.add(new ScenarioItem("BMW"));

        List<ScenarioItem> usa = new ArrayList<>();
        usa.add(new ScenarioItem("Ford"));

        List<ScenarioItem> italy = new ArrayList<>();
        italy.add(new ScenarioItem("Fiat"));

        ParentItem.put("German", german);
        ParentItem.put("USA", usa);
        ParentItem.put("Italy", italy);

        return ParentItem;
    }
}
