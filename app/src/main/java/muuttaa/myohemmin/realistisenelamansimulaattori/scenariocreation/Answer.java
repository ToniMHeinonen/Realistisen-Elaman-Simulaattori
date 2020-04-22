package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.content.Context;
import android.util.Log;

import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class Answer {

    private String text;
    private String goesToScene;
    private String color;
    private Context context;

    public Answer(Context con, String text, String goesToScene, String color) {
        this.text = text;
        this.goesToScene = goesToScene;
        this.color = color;
        this.context = con;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGoesToScene() {
        String m = goesToScene;
        if(m.equals("null")){
            m = context.getString(R.string.null_value);
        }
        return m;
    }

    public void setGoesToScene(String goesToScene) {
        this.goesToScene = goesToScene;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        String m = goesToScene;
        if(m.equals("null")){
            m = context.getString(R.string.null_value);
        }
        return "Answer{" +
                "text='" + text + '\'' +
                ", goesToScene='" + m + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
