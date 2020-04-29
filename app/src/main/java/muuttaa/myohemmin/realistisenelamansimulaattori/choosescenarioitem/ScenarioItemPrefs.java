package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.content.SharedPreferences;

import java.util.Date;

import muuttaa.myohemmin.realistisenelamansimulaattori.InitializeActivity;

public abstract class ScenarioItemPrefs {
    private static SharedPreferences prefs;

    private static final String keyPrefs = "RLS_scenario_item_prefs";

    // If you add new keys, remember to remove them in deleteScenarioItem()
    private static final String keyRecent = "recent_";
    private static final String keyPercentage = "percentage_";
    private static final String keyCategory = "category_";

    /**
     * Loads correct preferences file.
     */
    static {
        prefs = InitializeActivity.getContext().getSharedPreferences(keyPrefs, 0);
    }

    /**
     * Calls static code block.
     */
    public static void initialize() {
        // This calls the static code block and loads necessary preferences
    }

    /**
     * Saves given percentage to the given scenario.
     * @param scenario name of the scenario
     * @param percentage percentage completed
     */
    public static void savePercentage(String scenario, int percentage) {
        prefs.edit().putInt(keyPercentage + scenario, percentage).commit();
    }

    /**
     * Loads saved percentage from memory to given scenario.
     * @param scenario name of the scenario
     * @return percentage of the scenario
     */
    public static int loadPercentage(String scenario) {
        return prefs.getInt(keyPercentage + scenario, 0);
    }

    /**
     * Saves current time as last time played to given scenario.
     * @param scenario name of the scenario
     */
    public static void saveLastTimePlayed(String scenario) {
        SharedPreferences.Editor editor = prefs.edit();
        // Get current time and convert to long
        Date date = new Date();
        long millis = date.getTime();

        editor.putLong(keyRecent + scenario, millis);
        editor.commit();
    }

    /**
     * Loads the date of last time played on given scenario.
     * @param scenario name of the scenario
     * @return date of last time played
     */
    public static Date loadLastTimePlayed(String scenario) {
        return new Date(prefs.getLong(keyRecent + scenario, 0));
    }

    /**
     * Loads the category of the scenario.
     * @param scenario name of the scenario
     * @return category of the item
     */
    public static String loadCategory(String scenario) {
        return prefs.getString(keyCategory + scenario, null);
    }

    /**
     * Saves given category to the item.
     * @param scenario name of the scenario
     * @param category new category of the item
     */
    public static void saveCategory(String scenario, String category) {
        prefs.edit().putString(keyCategory + scenario, category).commit();
    }

    /**
     * Resets necessary scenario item preferences.
     * @param scenario name of the scenario
     */
    public static void resetScenarioItem(String scenario) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(keyRecent + scenario, 0);
        editor.putInt(keyPercentage + scenario, 0);

        editor.commit();
    }

    public static void deleteScenarioitem(String scenario) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(keyRecent + scenario);
        editor.remove(keyPercentage + scenario);
        editor.remove(keyCategory + scenario);

        editor.commit();
    }

    /**
     * Clears all scenario item preferences.
     *
     * Mainly used for debugging.
     */
    public static void clearScenarioItemPreferences() {
        prefs.edit().clear().commit();
    }
}
