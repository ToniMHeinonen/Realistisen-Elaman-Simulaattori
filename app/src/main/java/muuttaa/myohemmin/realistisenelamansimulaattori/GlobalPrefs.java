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


    public static void deleteCategory(String category) {
        categoriesList.remove(category);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < categoriesList.size(); i++) {
            editor.putString(keyAllCategories + i, categoriesList.get(i));
        }

        editor.putInt(keyCategoriesAmount, categoriesList.size());

        editor.commit();
    }

    public static void renameCategory(String category, String newName) {
        int index = categoriesList.indexOf(category);
        categoriesList.remove(index);
        categoriesList.add(newName);
        SharedPreferences.Editor editor = prefs.edit();

        for (int i = 0; i < categoriesList.size(); i++) {
            editor.putString(keyAllCategories + i, categoriesList.get(i));
        }

        editor.commit();
    }

    public static int loadSortType() {
        return prefs.getInt(keySortType, 0);
    }

    public static void saveSortType(int type) {
        prefs.edit().putInt(keySortType, type).apply();
    }

    public static boolean loadSortAscending() {
        return prefs.getBoolean(keySortAscending, false);
    }

    public static void saveSortAscending(boolean ascending) {
        prefs.edit().putBoolean(keySortAscending, ascending).apply();
    }
}
