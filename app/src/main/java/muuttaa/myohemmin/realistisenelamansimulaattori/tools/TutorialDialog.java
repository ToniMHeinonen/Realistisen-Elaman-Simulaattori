package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
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
public class TutorialDialog extends Dialog implements
        View.OnClickListener {

    public enum Tutorial {
        SCENARIO,
        SCENE,
        ANSWER
    }

    private Activity activity;
    private Tutorial tutorial;

    /**
     * Initializes necessary values.
     * @param a current activity
     * @param tutorial selected tutorial
     */
    public TutorialDialog(Activity a, Tutorial tutorial) {
        super(a);
        this.activity = a;
        this.tutorial = tutorial;
    }

    /**
     * Initializes views.
     * @param savedInstanceState previous Activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.tutorial_dialog);

        // Set dialog window size to 90% of the screen width and height
        int width = (int)(activity.getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(activity.getResources().getDisplayMetrics().heightPixels*0.90);
        getWindow().setLayout(width, height);

        initializeViews();

        // Set listeners for ok button
        findViewById(R.id.confirm).setOnClickListener(this);
    }

    /**
     * Changes texts of views and shows necessary views.
     */
    private void initializeViews() {
        switch (tutorial) {
            case SCENARIO:
                setTutorialText(activity.getString(R.string.tutorial_scenario));
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
        switch (tutorial) {
            case SCENARIO:
                GlobalPrefs.saveTutorialScenario(false);
                dismiss();
                break;
            case SCENE:
                GlobalPrefs.saveTutorialScene(false);
                dismiss();
                break;
            case ANSWER:
                GlobalPrefs.saveTutorialAnswer(false);
                dismiss();
                break;
        }
    }
}
