package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import android.content.SharedPreferences;

import java.util.Date;

import muuttaa.myohemmin.realistisenelamansimulaattori.InitializeActivity;

public abstract class ScenarioItemPrefs {
    private static SharedPreferences prefs;

    private static final String keyPrefs = "RLS_scenario_item_prefs";
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
     * @param scenarioID id of the scenario
     * @param percentage percentage completed
     */
    public static void savePercentage(int scenarioID, int percentage) {
        prefs.edit().putInt(keyPercentage + scenarioID, percentage).commit();
    }

    /**
     * Loads saved percentage from memory to given scenario.
     * @param scenarioID id of the scenario
     * @return percentage of the scenario
     */
    public static int loadPercentage(int scenarioID) {
        return prefs.getInt(keyPercentage + scenarioID, 0);
    }

    /**
     * Saves current time as last time played to given scenario.
     * @param scenarioID id of the scenario
     */
    public static void saveLastTimePlayed(int scenarioID) {
        SharedPreferences.Editor editor = prefs.edit();
        // Get current time and convert to long
        Date date = new Date();
        long millis = date.getTime();

        editor.putLong(keyRecent + scenarioID, millis);
        editor.commit();
    }

    /**
     * Loads the date of last time played on given scenario.
     * @param scenarioID id of the scenario
     * @return date of last time played
     */
    public static Date loadLastTimePlayed(int scenarioID) {
        return new Date(prefs.getLong(keyRecent + scenarioID, 0));
    }

    /**
     * Loads the category of the scenario.
     * @param scenarioID id of the scenario
     * @return category of the item
     */
    public static String loadCategory(int scenarioID) {
        return prefs.getString(keyCategory + scenarioID, null);
    }

    /**
     * Saves given category to the item.
     * @param scenarioID id of the scenario
     * @param category new category of the item
     */
    public static void saveCategory(int scenarioID, String category) {
        prefs.edit().putString(keyCategory + scenarioID, category).commit();
    }

    /**
     * Resets necessary scenario item preferences.
     * @param scenarioID id of the scenario
     */
    public static void resetScenarioItem(int scenarioID) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(keyRecent + scenarioID, 0);
        editor.putInt(keyPercentage + scenarioID, 0);

        editor.commit();
    }

    /**
     * Lowers the id of scenario by one.
     *
     * This is only used when scenario is deleted.
     * @param newID new id for the scenario
     * @param scenario scenario values to transfer
     */
    public static void changeScenarioID(int newID, ScenarioItem scenario) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong(keyRecent + newID, scenario.getLastTimePlayed().getTime());
        editor.putInt(keyPercentage + newID, scenario.getPercentageCompleted());
        editor.putString(keyCategory + newID, scenario.getCategory());

        editor.commit();
    }

    /**
     * Removes the last value, so it does not stay hanging in preferences.
     * @param id last value id
     */
    public static void removeLastValueAfterDeletingScenario(int id) {
        SharedPreferences.Editor editor = prefs.edit();

        editor.remove(keyRecent + id);
        editor.remove(keyPercentage + id);
        editor.remove(keyCategory + id);

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
