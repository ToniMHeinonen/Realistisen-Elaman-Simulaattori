package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import muuttaa.myohemmin.realistisenelamansimulaattori.CreateScene;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;

public class CreatedAnswerDialog extends Dialog implements
        View.OnClickListener {

    private CreateScene activity;
    private String text;
    private int position;

    /**
     * Initializes instance of this class.
     * @param a current activity
     */
    public CreatedAnswerDialog(CreateScene a, String text, int position) {
        super(a);
        this.activity = a;
        this.text = text;
        this.position = position;
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
        categoryView.setText(text);

        // Set listeners
        findViewById(R.id.duplicateBtn).setOnClickListener(this);
        findViewById(R.id.moveUpBtn).setOnClickListener(this);
        findViewById(R.id.moveDownBtn).setOnClickListener(this);
        findViewById(R.id.deleteButton).setOnClickListener(this);
        findViewById(R.id.closeButton).setOnClickListener(this);
    }

    /**
     * Listens for button clicks on view.
     * @param v clicked view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.duplicateBtn:
                activity.duplicateAnswer(position);
                dismiss();
                break;
            case R.id.moveUpBtn:
                dismiss();
                break;
            case R.id.moveDownBtn:
                dismiss();
                break;
            case R.id.deleteButton:
                Helper.showAlert(activity, text,
                        activity.getString(R.string.answerDeleteText),
                        activity.getString(R.string.scenarioDelete),
                        null,
                        (() -> deleteAnswer()), null);
                break;
            case R.id.closeButton:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * Deletes answer permanently.
     */
    private void deleteAnswer() {
        activity.deleteAnswer(position);
        dismiss();
    }
}
