package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class HamburgerDialog extends Dialog implements
        android.view.View.OnClickListener {

    private ChooseScenarioActivity activity;

    public HamburgerDialog(ChooseScenarioActivity a) {
        super(a);
        this.activity = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.hamburger_dialog);

        // Set listeners for confirm and cancel
        findViewById(R.id.addCategory).setOnClickListener(this);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCategory:
                EditText category = findViewById(R.id.newCategory);
                activity.addCategory(category.getText().toString());
                dismiss();
                break;
            case R.id.settings:
                activity.settingsSelected();
                dismiss();
                break;
            case R.id.back:
                cancel();
                break;
            default:
                break;
        }
    }
}
