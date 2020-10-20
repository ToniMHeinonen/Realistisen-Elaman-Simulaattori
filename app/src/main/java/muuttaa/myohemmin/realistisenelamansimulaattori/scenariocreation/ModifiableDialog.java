package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.MoveCheck;

public class ModifiableDialog extends Dialog implements
        View.OnClickListener {

    private Context context;
    private ModifiableInterface activity;
    private String text;
    private int position;

    private MoveCheck moveCheck;
    private Button moveUpBtn, moveDownBtn;

    /**
     * Initializes instance of this class.
     * @param a current activity
     */
    public ModifiableDialog(Context context, ModifiableInterface a, String text, int position, MoveCheck moveCheck) {
        super(context);
        this.context = context;
        this.activity = a;
        this.text = text;
        this.position = position;
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
        setContentView(R.layout.modifiable_dialog);

        // Change window width to 80 % of screen size
        int width = (int)(context.getResources().getDisplayMetrics().widthPixels*0.80);
        getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView categoryView = findViewById(R.id.name);
        // Show only the beginning of the answer text
        if (text.length() > 50)
            text = text.substring(0, 50) + "...";
        categoryView.setText(text);

        // Set listeners
        moveUpBtn = findViewById(R.id.moveUpBtn);
        moveUpBtn.setOnClickListener(this);
        moveDownBtn = findViewById(R.id.moveDownBtn);
        moveDownBtn.setOnClickListener(this);
        toggleMoveButtons();
        findViewById(R.id.duplicateBtn).setOnClickListener(this);
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
                activity.duplicateModifiable(position);
                dismiss();
                break;
            case R.id.moveUpBtn:
                activity.moveModifiable(position, true);
                changePosition(--position);
                break;
            case R.id.moveDownBtn:
                activity.moveModifiable(position, false);
                changePosition(++position);
                break;
            case R.id.deleteButton:
                Helper.showAlert(context, text,
                        context.getString(R.string.modifiableDeleteText),
                        context.getString(R.string.scenarioDelete),
                        null,
                        (() -> deleteModifiable()), null);
                break;
            case R.id.closeButton:
                cancel();
                break;
            default:
                break;
        }
    }

    /**
     * Toggles the move up and down buttons on and off.
     */
    private void toggleMoveButtons() {
        moveCheck.refreshMovement(position);
        moveUpBtn.setEnabled(moveCheck.isCanMoveUp());
        moveDownBtn.setEnabled(moveCheck.isCanMoveDown());
    }

    /**
     * Changes the position of the selected item.
     * @param position new position
     */
    private void changePosition(int position) {
        int size = moveCheck.getListSize();
        // Show toast of the new position
        Toast.makeText(context,
                // Position + 1 since index starts at 0
                context.getString(R.string.moved_to, position + 1, size),
                Toast.LENGTH_SHORT).show();

        toggleMoveButtons();
    }

    /**
     * Deletes item permanently.
     */
    private void deleteModifiable() {
        activity.deleteModifiable(position);
        dismiss();
    }
}
