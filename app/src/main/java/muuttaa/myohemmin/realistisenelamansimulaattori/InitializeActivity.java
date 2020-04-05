package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class InitializeActivity extends ParentActivity {

    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;

        // Initialize abstract preferences classes
        ScenarioItemPrefs.initialize();
        GlobalPrefs.initialize();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        // Start first activity
        startActivity(new Intent(this, ChooseScenarioActivity.class));

        finish();
    }

    /**
     * Gets context.
     *
     * Used in CategoriesListAdapter to get application context in order to
     * access saved xml Strings.
     * @return application context
     */
    public static Context getContext(){
        return mContext;
    }
}
