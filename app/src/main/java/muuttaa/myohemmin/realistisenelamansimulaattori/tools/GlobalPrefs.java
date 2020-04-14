package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

import android.content.SharedPreferences;

import java.util.ArrayList;

import muuttaa.myohemmin.realistisenelamansimulaattori.InitializeActivity;

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

    // Audio
    private final static String keySoundVolume = "sound_volume";
    private final static String keyMusicVolume = "music_volume";

    // Language
    private final static String keyLanguage = "language";

    /**
     * Loads correct preferences file and fills categoriesList.
     */
    static {
        prefs = InitializeActivity.getContext().getSharedPreferences(keyPrefs, 0);

        int categoriesAmount = prefs.getInt(keyCategoriesAmount, 0);

        for (int i = 0; i < categoriesAmount; i++) {
            categoriesList.add(prefs.getString(keyAllCategories + i, null));
        }
    }

    /**
     * Calls static code block.
     */
    public static void initialize() {
        // This calls the static code block and loads necessary preferences
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

    /**
     * Loads font style.
     * @return saved font style
     */
    public static FontStyle loadFontStyle() {
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

    /**
     * Saves font style.
     * @param style new font style
     */
    public static void saveFontStyle(FontStyle style) {
        prefs.edit().putString(keyFontStyle, style.name()).commit();
    }

    /**
     * Loads the spinner position of font size.
     * @return spinner position
     */
    public static int loadFontSizePos() {
        return prefs.getInt(keyFontSizePos, 0);
    }

    /**
     * Saves the spinner position of the font size.
     * @param pos current position
     */
    public static void saveFontSizePos(int pos) {
        prefs.edit().putInt(keyFontSizePos, pos).commit();
    }

    /**
     * Loads the spinner position of font color.
     * @return spinner position
     */
    public static int loadFontColorPos() {
        return prefs.getInt(keyFontColorPos, 0);
    }

    /**
     * Saves the spinner position of the font color.
     * @param pos current position
     */
    public static void saveFontColorPos(int pos) {
        prefs.edit().putInt(keyFontColorPos, pos).commit();
    }

    /**
     * Loads the volume of the sound.
     * @return sound volume
     */
    public static float loadSoundVolume() {
        return prefs.getFloat(keySoundVolume, 0.8f);
    }

    /**
     * Saves the volume of the sound.
     * @param volume current sound volume
     */
    public static void saveSoundVolume(float volume) {
        prefs.edit().putFloat(keySoundVolume, volume).commit();
    }

    /**
     * Loads the volume of the music.
     * @return music volume
     */
    public static float loadMusicVolume() {
        return prefs.getFloat(keyMusicVolume, 0.8f);
    }

    /**
     * Saves the volume of the music.
     * @param volume current music volume
     */
    public static void saveMusicVolume(float volume) {
        prefs.edit().putFloat(keyMusicVolume, volume).commit();
    }

    /**
     * Loads the app language.
     * @return app language
     */
    public static String loadLanguage() {
        return prefs.getString(keyLanguage, "");
    }

    /**
     * Saves the app language.
     * @param languageCode selected language
     */
    public static void saveLanguage(String languageCode) {
        prefs.edit().putString(keyLanguage, languageCode).commit();
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
