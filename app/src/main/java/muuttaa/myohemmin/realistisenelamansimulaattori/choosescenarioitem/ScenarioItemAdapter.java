package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class ScenarioItemAdapter extends BaseAdapter {

    private ArrayList<ScenarioItem> listData;
    private LayoutInflater layoutInflater;

    /**
     * Initializes scenario item adapter.
     * @param aContext activity context
     * @param listData list of Scenario Items
     */
    public ScenarioItemAdapter(Context aContext, ArrayList<ScenarioItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    /**
     * Returns the size of the list.
     * @return size of the list
     */
    @Override
    public int getCount() {
        return listData.size();
    }

    /**
     * Returns item at index.
     * @param position index
     * @return item at index
     */
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    /**
     * Returns given position as a long for some reason.
     * @param position index
     * @return given position as long
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Inflates view with correct data.
     * @param position index of item
     * @param convertView converted view
     * @param parent parent object
     * @return converted view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.scenario_item, null);
            holder = new ViewHolder();
            holder.txtScenarioName = convertView.findViewById(R.id.scenarioName);
            holder.txtScenarioPercentage = convertView.findViewById(R.id.scenarioPercentage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtScenarioName.setText(listData.get(position).getName());
        holder.txtScenarioPercentage.setText(String.valueOf(listData.get(position).getPercentageCompleted()));

        return convertView;
    }

    /**
     * Holds values for views of ScenarioItem.
     */
    static class ViewHolder {
        TextView txtScenarioName;
        TextView txtScenarioPercentage;
    }
}
