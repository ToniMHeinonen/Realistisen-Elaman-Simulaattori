package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import muuttaa.myohemmin.realistisenelamansimulaattori.ChooseScenarioActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class CategoryDialog extends Dialog implements
        View.OnClickListener {

    private ChooseScenarioActivity activity;
    private String category;

    /**
     * Initializes instance of this class.
     * @param a current activity
     */
    public CategoryDialog(ChooseScenarioActivity a, String category) {
        super(a);
        this.activity = a;
        this.category = category;
    }

    /**
     * Initializes necessary values at start.
     * @param savedInstanceState previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.category_dialog);

        EditText categoryView = findViewById(R.id.categoryName);
        categoryView.setText(category);

        // Set listeners for confirm and cancel
        findViewById(R.id.renameCategory).setOnClickListener(this);
        findViewById(R.id.deleteCategory).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    /**
     * Listens button clicks on view.
     * @param v clicked view
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.renameCategory:
                EditText categoryView = findViewById(R.id.categoryName);
                String newName = categoryView.getText().toString();
                activity.renameCategory(category, newName);
                dismiss();
                break;
            case R.id.deleteCategory:
                activity.deleteCategory(category);
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
