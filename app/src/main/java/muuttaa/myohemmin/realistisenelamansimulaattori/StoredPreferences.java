package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class StoredPreferences {
    private static SharedPreferences prefs;

    private static final String keyPrefs = "RLS_prefs";

    public static void initialize(Context context) {
        prefs = context.getSharedPreferences(keyPrefs, 0);
    }
}
