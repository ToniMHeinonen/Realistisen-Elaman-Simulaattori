package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import muuttaa.myohemmin.realistisenelamansimulaattori.CreateScene;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class CreateAnswerAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Answer> listData;
    private LayoutInflater layoutInflater;
    private CreateScene listener;

    /**
     * Adapter for handling ListView with Answer.
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
     * @param parent item parent
     * @return view of the data
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.answer_view, null);
        }

        Answer item = (Answer) getItem(position);

        if (item != null) {
            LinearLayout layout = view.findViewById(R.id.answerLayout);
            layout.setClickable(true);
            layout.setLongClickable(true);
            layout.setTag(position);
            layout.setOnClickListener(listener);
            layout.setOnLongClickListener(listener);

            if (item.getColor().equals("green")) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.green_button));
            } else if (item.getColor().equals("yellow")) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.yellow_button));
            } else if (item.getColor().equals("red")) {
                layout.setBackground(context.getResources().getDrawable(R.drawable.red_button));
            }

            TextView text = view.findViewById(R.id.answerText);
            text.setText(item.getText());

            TextView goesToScene = view.findViewById(R.id.answerGoesTo);
            goesToScene.setText(item.getGoesToScene());
        }

        return view;
    }
}

