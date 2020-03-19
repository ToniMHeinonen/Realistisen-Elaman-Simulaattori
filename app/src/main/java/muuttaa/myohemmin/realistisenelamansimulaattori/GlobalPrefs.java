package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public abstract class GlobalPrefs {
    private static SharedPreferences prefs;

    private static final String keyPrefs = "RLS_global_prefs";

    // Categories
    private static final String keyAllCategories = "all_categories";
    private static final String keyCategoriesAmount = "categories_amount";
    private static final ArrayList<String> categoriesList = new ArrayList<>();

    // Sorting
    private static final String keySortType = "sort_type";
    private static final String keySortAscending = "sort_ascending";

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

    /**
     * Loads saved categories.
     * @return saved categories
     */
    public static ArrayList<String> loadCategories() {
        return categoriesList;
    }

    /**
     * Saves new category.
     * @param category name of the category
     */
    public static void saveCategory(String category) {
        categoriesList.add(category);
        SharedPreferences.Editor editor = prefs.edit();
        int index = categoriesList.size() - 1;

        editor.putString(keyAllCategories + index, category);
        editor.putInt(keyCategoriesAmount, categoriesList.size());

        editor.commit();
    }

    /**
     * Deletes given category.
     * @param category to delete
     */
    public static void deleteCategory(String category) {
        categoriesList.remove(category);
        updateCurrentCategories();
    }

    /**
     * Renames category.
     * @param category old name for category
     * @param newName new name for category
     */
    public static void renameCategory(String category, String newName) {
        // Replace category at given index
        int index = categoriesList.indexOf(category);
        categoriesList.remove(index);
        categoriesList.add(newName);

        updateCurrentCategories();
    }

    /**
     * Updates the current category keys and amount.
     */
    private static void updateCurrentCategories() {
        SharedPreferences.Editor editor = prefs.edit();

        // Update keys for categories left
        for (int i = 0; i < categoriesList.size(); i++) {
            editor.putString(keyAllCategories + i, categoriesList.get(i));
        }

        // Update the categories amount
        editor.putInt(keyCategoriesAmount, categoriesList.size());

        editor.commit();
    }

    /**
     * Loads current sorting type.
     * @return sorting type
     */
    public static int loadSortType() {
        return prefs.getInt(keySortType, 0);
    }

    /**
     * Saves current sorting type.
     * @param type sorting type
     */
    public static void saveSortType(int type) {
        prefs.edit().putInt(keySortType, type).apply();
    }

    /**
     * Loads current sorting direction.
     * @return sorting direction
     */
    public static boolean loadSortAscending() {
        return prefs.getBoolean(keySortAscending, false);
    }

    /**
     * Saves current sorting direction.
     * @param ascending sorting direction
     */
    public static void saveSortAscending(boolean ascending) {
        prefs.edit().putBoolean(keySortAscending, ascending).apply();
    }
}
