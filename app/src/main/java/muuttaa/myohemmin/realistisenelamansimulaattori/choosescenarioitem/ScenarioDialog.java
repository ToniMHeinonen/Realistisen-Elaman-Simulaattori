package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import muuttaa.myohemmin.realistisenelamansimulaattori.AlertButtonListener;
import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.Helper;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.ScenarioActivity;

public class ScenarioDialog extends Dialog implements
        View.OnClickListener, AlertButtonListener {

    private ChooseScenarioActivity activity;
    private ScenarioItem scenario;

    private final int RESET = 0, DELETE = 1;

    /**
     * Initializes instance of this class.
     * @param a current activity
     */
    public ScenarioDialog(ChooseScenarioActivity a, ScenarioItem scenario) {
        super(a);
        this.activity = a;
        this.scenario = scenario;
    }

    /**
     * Initializes necessary values at start.
     * @param savedInstanceState previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scenario_dialog);

        TextView categoryView = findViewById(R.id.name);
        categoryView.setText(scenario.getName());

        boolean editable = false;

        // Set listeners
        Button modifyBtn = findViewById(R.id.modifyButton);
        Button deleteBtn = findViewById(R.id.deleteButton);

        if (editable) {
            modifyBtn.setOnClickListener(this);
            deleteBtn.setOnClickListener(this);
        } else {
            modifyBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
        }

        findViewById(R.id.playButton).setOnClickListener(this);
        findViewById(R.id.resetButton).setOnClickListener(this);
        findViewById(R.id.closeButton).setOnClickListener(this);
    }

    /**
     * Listens button clicks on view.
     * @param v clicked view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.playButton:
                Intent intent = new Intent(activity, ScenarioActivity.class);
                intent.putExtra("scenario", scenario.getName());
                intent.putExtra("scenarioID", scenario.getId());
                activity.startActivity(intent);
                activity.playSound(activity.S_CORRECT);
                dismiss();
                break;
            case R.id.resetButton:
                Helper.showAlert(activity, RESET, scenario.getName(),
                        activity.getString(R.string.scenarioResetText), this);
                break;
            case R.id.modifyButton:
                dismiss();
                break;
            case R.id.deleteButton:
                Helper.showAlert(activity, DELETE, scenario.getName(),
                        activity.getString(R.string.scenarioDeleteText), this);
                break;
            case R.id.closeButton:
                cancel();
                break;
            default:
                break;
        }
    }


    @Override
    public void positiveButtonClicked(int alertID) {
        if (alertID == RESET) {
            ScenarioItemPrefs.resetScenarioItem(scenario.getId());
            // Reload activity
            activity.startActivity(new Intent(activity, ChooseScenarioActivity.class));
            activity.finish();
        } else if (alertID == DELETE) {
            // Delete scenario
        }
    }

    @Override
    public void negativeButtonClicked(int alertID) {}
}
