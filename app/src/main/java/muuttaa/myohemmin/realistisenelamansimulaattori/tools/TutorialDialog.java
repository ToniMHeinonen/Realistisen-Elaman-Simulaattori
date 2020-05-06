package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
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
        TextView topic = findViewById(R.id.settingTopic);
        TextView text = findViewById(R.id.settingText);

        switch (tutorial) {
            case SCENARIO:
                topic.setText(activity.getString(R.string.tutorial_scenario));
                text.setText(activity.getString(R.string.tutorial_scenario_text));
                break;
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
