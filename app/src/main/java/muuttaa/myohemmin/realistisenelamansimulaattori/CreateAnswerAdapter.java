package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import muuttaa.myohemmin.realistisenelamansimulaattori.data.Answer;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Debug;

/**
 * Handles ListView with ListOfView and Profile.
 * @author Toni Heinonen
 * @author toni1.heinonen@gmail.com
 * @version 1.0
 * @since 1.0
 */
public class CreateAnswerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Answer> listData;
    private LayoutInflater layoutInflater;
    private CreateScene listener;

    /**
     * Adapter for handling ListView with list and profile.
     * @param aContext current context
     * @param listData data to be added to ListView
     * @param listener click listener
     */
    public CreateAnswerAdapter(Context aContext, ArrayList<Answer> listData, CreateScene listener) {
        this.context = aContext;
        this.listData = listData;
        this.listener = listener;
        layoutInflater = LayoutInflater.from(aContext);
    }

    /**
     * Returns size of the data.
     * @return size of the data
     */
    @Override
    public int getCount() {
        return listData.size();
    }

    /**
     * Returns item at given position.
     * @param position index of the item
     * @return item
     */
    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    /**
     * Returns position of the item.
     * @param position index of the item
     * @return position of the item
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Creates view of the given data.
     * @param position index of the data
     * @param convertView part of the view reserved for current data
     * @param parent listview parent
     * @return view of the data
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.answer_view, null);
        }

        Answer item = (Answer) getItem(position);

        if (item != null) {
            Debug.print("CreateAnswer", "getView", item.toString(), 1);
            LinearLayout layout = view.findViewById(R.id.answerLayout);
            layout.setClickable(true);
            layout.setLongClickable(true);
            layout.setTag(position);
            layout.setOnClickListener(listener);
            layout.setOnLongClickListener(listener);

            if (item.getColor().equals("green")) {
                layout.setBackgroundResource(context.getResources().getIdentifier("green_button",
                        "drawable", context.getPackageName()));
            } else if (item.getColor().equals("yellow")) {
                layout.setBackgroundResource(context.getResources().getIdentifier("yellow_button",
                        "drawable", context.getPackageName()));
            } else if (item.getColor().equals("red")) {
                layout.setBackgroundResource(context.getResources().getIdentifier("red_button",
                        "drawable", context.getPackageName()));
            }

            TextView text = view.findViewById(R.id.answerText);
            text.setText(listData.get(position).getText());

            TextView goesToScene = view.findViewById(R.id.answerGoesTo);
            goesToScene.setText(listData.get(position).getGoesToScene());
        }

        return view;
    }
}

