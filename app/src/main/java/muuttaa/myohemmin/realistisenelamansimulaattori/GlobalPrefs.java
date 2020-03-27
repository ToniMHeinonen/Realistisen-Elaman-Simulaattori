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

    // Font
    private final static String keyFontStyle = "font_style";
    private final static String keyFontSizePos = "font_size_position";
    private final static String keyFontColorPos = "font_color_position";

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
        categoriesList.add(index, newName);

        updateCurrentCategories();
    }

    /**
     * Moves category to different position.
     * @param categoryToMove category to be moved
     * @param dropLocation location of where the category will be moved
     */
    public static void moveCategory(String categoryToMove, String dropLocation) {
        int dropIndex = categoriesList.indexOf(dropLocation);

        // If dropped on top of default category, move below it
        if (dropIndex == -1)
            dropIndex = 0;

        categoriesList.remove(categoryToMove);
        categoriesList.add(dropIndex, categoryToMove);

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

    public static FontStyle getFontStyle() {
        String defaultValue = FontStyle.SmallBlack.name();
        FontStyle style;
        // Check if user has previously saved fontstyle which does not exist anymore
        try {
            style = FontStyle.valueOf(prefs.getString(keyFontStyle, defaultValue));
        } catch (Exception e) {
            style = FontStyle.valueOf(defaultValue);
        }
        return style;
    }

    public static void setFontStyle(FontStyle style) {
        prefs.edit().putString(keyFontStyle, style.name()).commit();
    }

    public static int getFontSizePos() {
        return prefs.getInt(keyFontSizePos, 0);
    }

    public static void setFontSizePos(int pos) {
        prefs.edit().putInt(keyFontSizePos, pos).commit();
    }

    public static int getFontColorPos() {
        return prefs.getInt(keyFontColorPos, 0);
    }

    public static void setFontColorPos(int pos) {
        prefs.edit().putInt(keyFontColorPos, pos).commit();
    }

    /**
     * Clears all scenario item preferences.
     *
     * Mainly used for debugging.
     */
    public static void clearGlobalPreferences() {
        prefs.edit().clear().commit();
    }
}
