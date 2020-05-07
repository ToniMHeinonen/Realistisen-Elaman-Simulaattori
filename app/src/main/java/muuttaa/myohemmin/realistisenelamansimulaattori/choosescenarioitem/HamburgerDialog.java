package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.CreateScenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.SettingsActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.HTMLDialog;

public class HamburgerDialog extends Dialog implements
        android.view.View.OnClickListener {

    private ChooseScenarioActivity activity;

    /**
     * Initializes instance of this class.
     * @param a current activity
     */
    public HamburgerDialog(ChooseScenarioActivity a) {
        super(a);
        this.activity = a;
    }

    /**
     * Initializes necessary values at start.
     * @param savedInstanceState previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hamburger_dialog);

        // Set listeners for confirm and cancel
        findViewById(R.id.addCategory).setOnClickListener(this);
        findViewById(R.id.createScenario).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.credits).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    /**
     * Listens button clicks on view.
     * @param v clicked view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCategory:
                EditText category = findViewById(R.id.newCategory);
                activity.addCategory(category.getText().toString());
                dismiss();
                break;
            case R.id.createScenario:
                activity.startActivity(new Intent(activity, CreateScenario.class));
                dismiss();
                break;
            case R.id.settings:
                activity.startActivity(new Intent(activity, SettingsActivity.class));
                activity.finish();
                dismiss();
                break;
            case R.id.credits:
                new HTMLDialog(activity, HTMLDialog.HTMLText.CREDITS).show();
                break;
            case R.id.back:
                cancel();
                break;
            default:
                break;
        }
    }
}
