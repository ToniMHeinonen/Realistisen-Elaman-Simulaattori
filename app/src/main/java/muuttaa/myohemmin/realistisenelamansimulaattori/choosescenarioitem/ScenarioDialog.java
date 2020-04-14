package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.CreateScenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.Helper;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.ScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;

public class ScenarioDialog extends Dialog implements
        View.OnClickListener {

    private ChooseScenarioActivity activity;
    private ScenarioItem scenario;

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

        SaveSystemPreferences sys = new SaveSystemPreferences(activity);
        boolean editable = sys.checkIfScenarioIsUserCreated(scenario.getName());

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
                Helper.showAlert(activity, scenario.getName(),
                        activity.getString(R.string.scenarioResetText),
                        activity.getString(R.string.scenarioReset),
                        null,
                        (() -> resetScenario()), null);
                break;
            case R.id.modifyButton:
                Intent i = new Intent(activity, CreateScenario.class);
                i.putExtra("edit", true);
                i.putExtra("name", scenario.getName());
                activity.startActivity(i);
                dismiss();
                break;
            case R.id.deleteButton:
                Helper.showAlert(activity, scenario.getName(),
                        activity.getString(R.string.scenarioDeleteText),
                        activity.getString(R.string.scenarioDelete),
                        null,
                        (() -> deleteScenario()), null);
                break;
            case R.id.closeButton:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * Resets necessary values of scenario.
     */
    private void resetScenario() {
        ScenarioItemPrefs.resetScenarioItem(scenario.getId());
        activity.refreshActivity();
        dismiss();
    }

    /**
     * Deletes scenario permanently.
     */
    private void deleteScenario() {
        activity.deleteScenario(scenario);
        dismiss();
    }
}
