package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public abstract class GlobalPrefs {
    private static SharedPreferences prefs;

    private static final String keyPrefs = "RLS_global_prefs";
    private static final String keyAllCategories = "all_categories";
    private static final String keyCategoriesAmount = "categories_amount";
    private static final ArrayList<String> categoriesList = new ArrayList<>();

    /**
     * Loads the saved values from file.
     * @param context activity context
     */
    public static void initialize(Context context) {
        prefs = context.getSharedPreferences(keyPrefs, 0);

        int categoriesAmount = prefs.getInt(keyCategoriesAmount, 0);

        for (int i = 0; i < categoriesAmount; i++) {
            categoriesList.add(prefs.getString(keyAllCategories + i, null));
        }
    }

    public static ArrayList<String> loadCategories() {
        return categoriesList;
    }

    public static void saveCategory(String category) {
        categoriesList.add(category);
        SharedPreferences.Editor editor = prefs.edit();
        int index = categoriesList.size() - 1;

        editor.putString(keyAllCategories + index, category);
        editor.putInt(keyCategoriesAmount, categoriesList.size());

        editor.commit();
    }
}
