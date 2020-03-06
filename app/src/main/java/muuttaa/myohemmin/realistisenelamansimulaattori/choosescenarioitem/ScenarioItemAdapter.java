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

    public ScenarioItemAdapter(Context aContext, ArrayList<ScenarioItem> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

    static class ViewHolder {
        TextView txtScenarioName;
        TextView txtScenarioPercentage;
    }
}
