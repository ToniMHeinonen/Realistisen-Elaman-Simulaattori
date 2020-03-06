package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public abstract class StoredPreferences {
    private static SharedPreferences prefs;

    private static final String keyPrefs = "RLS_prefs";
    private static final String keyRecent = "recent_";
    private static final String keyPercentage = "percentage_";

    public static void initialize(Context context) {
        prefs = context.getSharedPreferences(keyPrefs, 0);
    }

    public static void savePercentage(String scenario, int percentage) {
        prefs.edit().putInt(keyPercentage + scenario, percentage);
        prefs.edit().commit();
    }

    public static int loadPercentage(String scenario) {
        return prefs.getInt(keyPercentage + scenario, 0);
    }

    public static void saveLastTimePlayed(String scenario) {
        // Get current time and convert to long
        Date date = new Date();
        long millis = date.getTime();

        prefs.edit().putLong(keyRecent + scenario, millis);
        prefs.edit().commit();
    }

    public static Date loadLastTimePlayed(String scenario) {
        return new Date(prefs.getLong(keyRecent + scenario, 0));
    }
}
