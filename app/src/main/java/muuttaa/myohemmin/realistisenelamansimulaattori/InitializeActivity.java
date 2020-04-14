package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Locale;

public class InitializeActivity extends ParentActivity {

    private static Context mContext;

    /**
     * Initializes necessary values.
     * @param savedInstanceState previous instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mContext = this;

        // Initialize abstract preferences classes
        ScenarioItemPrefs.initialize();
        GlobalPrefs.initialize();

        // Set language
        String language = GlobalPrefs.loadLanguage();
        if (!language.equals(""))
            setAppLocale(language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialize);

        // Start first activity
        startActivity(new Intent(this, ChooseScenarioActivity.class));

        finish();
    }

    /**
     * Changes the language of the app.
     * @param localeCode language code
     */
    public static void setAppLocale(String localeCode) {
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN_MR1){
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
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
