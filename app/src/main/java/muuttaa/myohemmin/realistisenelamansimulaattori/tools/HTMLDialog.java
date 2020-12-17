package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import muuttaa.myohemmin.realistisenelamansimulaattori.R;

/**
 * Handles user changing app settings.
 * @author Toni Heinonen
 * @author toni1.heinonen@gmail.com
 * @version 1.0.2
 * @since 1.0
 */
public class HTMLDialog extends Dialog implements
        View.OnClickListener {

    public enum HTMLText {
        SCENARIO,
        SCENE,
        ANSWER,
        CREDITS
    }

    private Activity activity;
    private HTMLText htmlText;

    /**
     * Initializes necessary values.
     * @param a current activity
     * @param htmlText selected text to show
     */
    public HTMLDialog(Activity a, HTMLText htmlText) {
        super(a);
        this.activity = a;
        this.htmlText = htmlText;
    }

    /**
     * Initializes views.
     * @param savedInstanceState previous Activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.html_dialog);

        // Set dialog window size to 90% of the screen width and height
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels);
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels);
        getWindow().setLayout(width, height);

        initializeViews();

        // Set listeners for ok button
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    /**
     * Changes texts of views and shows necessary views.
     */
    private void initializeViews() {
        switch (htmlText) {
            case SCENARIO:
                setTutorialText(activity.getString(R.string.tutorial_scenario));
                break;
            case SCENE:
                setTutorialText(activity.getString(R.string.tutorial_scene));
                break;
            case ANSWER:
                setTutorialText(activity.getString(R.string.tutorial_answer));
                break;
            case CREDITS:
                setTutorialText(activity.getString(R.string.credits_html));
                break;
        }
    }

    /**
     * Formats html text correctly to TextView.
     * @param text html text to set on TextView
     */
    private void setTutorialText(String text) {
        TextView textView = findViewById(R.id.tutorialText);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            textView.setText(Html.fromHtml(text));
        }

        // If the textView has links, make them clickable
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    /**
     * Listens for view clicks.
     * @param v clicked view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                confirm();
                break;
            default:
                break;
        }
    }

    /**
     * Checks tutorial as read.
     */
    private void confirm() {
        switch (htmlText) {
            case SCENARIO:
                GlobalPrefs.saveTutorialScenario(false);
                break;
            case SCENE:
                GlobalPrefs.saveTutorialScene(false);
                break;
            case ANSWER:
                GlobalPrefs.saveTutorialAnswer(false);
                break;
        }

        dismiss();
    }
}
