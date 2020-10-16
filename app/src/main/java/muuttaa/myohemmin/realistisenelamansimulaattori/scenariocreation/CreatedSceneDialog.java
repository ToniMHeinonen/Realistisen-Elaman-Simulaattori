package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import muuttaa.myohemmin.realistisenelamansimulaattori.CreateScenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.MoveCheck;

public class CreatedSceneDialog extends Dialog implements
        View.OnClickListener {

    private CreateScenario activity;
    private Scene scene;
    private MoveCheck moveCheck;

    /**
     * Initializes instance of this class.
     * @param a current activity
     */
    public CreatedSceneDialog(CreateScenario a, Scene scene, MoveCheck moveCheck) {
        super(a);
        this.activity = a;
        this.scene = scene;
        this.moveCheck = moveCheck;
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

        // Disable up and down buttons if index is not valid
        Button moveUp = findViewById(R.id.moveUpBtn);
        moveUp.setEnabled(moveCheck.isCanMoveUp());
        moveUp.setOnClickListener(this);

        Button moveDown = findViewById(R.id.moveDownBtn);
        moveDown.setEnabled(moveCheck.isCanMoveDown());
        moveDown.setOnClickListener(this);

        // Set listeners
        findViewById(R.id.duplicateBtn).setOnClickListener(this);
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
                activity.moveScene(scene, true);
                dismiss();
                break;
            case R.id.moveDownBtn:
                activity.moveScene(scene, false);
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
