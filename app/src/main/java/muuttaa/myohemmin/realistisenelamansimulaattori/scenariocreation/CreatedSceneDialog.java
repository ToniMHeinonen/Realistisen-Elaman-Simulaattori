package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import muuttaa.myohemmin.realistisenelamansimulaattori.CreateScenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;

public class CreatedSceneDialog extends Dialog implements
        View.OnClickListener {

    private CreateScenario activity;
    private Scene scene;

    /**
     * Initializes instance of this class.
     * @param a current activity
     */
    public CreatedSceneDialog(CreateScenario a, Scene scene) {
        super(a);
        this.activity = a;
        this.scene = scene;
    }

    /**
     * Initializes necessary values at start.
     * @param savedInstanceState previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.created_scene_dialog);

        TextView categoryView = findViewById(R.id.name);
        categoryView.setText(scene.getName());

        // Set listeners
        findViewById(R.id.duplicateBtn).setOnClickListener(this);
        findViewById(R.id.moveUpBtn).setOnClickListener(this);
        findViewById(R.id.moveDownBtn).setOnClickListener(this);
        findViewById(R.id.deleteButton).setOnClickListener(this);
        findViewById(R.id.closeButton).setOnClickListener(this);
    }

    /**
     * Listens button clicks on view.
     * @param v clicked view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.duplicateBtn:
                activity.duplicateScene(scene);
                dismiss();
                break;
            case R.id.moveUpBtn:
                dismiss();
                break;
            case R.id.moveDownBtn:
                dismiss();
                break;
            case R.id.deleteButton:
                Helper.showAlert(activity, scene.getName(),
                        activity.getString(R.string.sceneDeleteText),
                        activity.getString(R.string.scenarioDelete),
                        null,
                        (() -> deleteScene()), null);
                break;
            case R.id.closeButton:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * Deletes scene permanently.
     */
    private void deleteScene() {
        activity.deleteScene(scene);
        dismiss();
    }
}
