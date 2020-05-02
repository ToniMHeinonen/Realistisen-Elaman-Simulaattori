package muuttaa.myohemmin.realistisenelamansimulaattori;

import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;

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
        setTheme(R.style.AppTheme);

        // Initialize abstract preferences classes
        ScenarioItemPrefs.initialize();
        GlobalPrefs.initialize();
        Helper.loadResourcesCategories();

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
