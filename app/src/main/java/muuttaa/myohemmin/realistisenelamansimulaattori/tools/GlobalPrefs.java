package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

import android.content.SharedPreferences;

import java.util.ArrayList;

import muuttaa.myohemmin.realistisenelamansimulaattori.InitializeActivity;

public abstract class GlobalPrefs {
    private static SharedPreferences prefs;

    private static final String keyPrefs = "RLS_global_prefs";

    // Header text
    private static final String keyHeaderText = "header_text";

    // Categories
    private static final String keyAllCategories = "all_categories";
    private static final String keyCategoriesAmount = "categories_amount";
    private static final String keyCategoryOpen = "category_open";
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

    // Scenario creation images
    private final static String keyBackgroundPos = "background_position";
    private final static String keyForegroundPos = "foreground_position";
    private final static String keyPersonPos = "person_position";
    private final static String keyFacePos = "face_position";

    // Tutorial
    private final static String keyTutorialScenario = "tutorial_scenario";
    private final static String keyTutorialScene = "tutorial_scene";
    private final static String keyTutorialAnswer = "tutorial_answer";

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

    //****************** HEADER TEXT ******************//

    /**
     * Loads header text for ChooseScenarioActivity.
     * @return saved header text
     */
    public static String loadHeaderText() {
        return prefs.getString(keyHeaderText, "");
    }

    /**
     * Saves header text for ChooseScenarioActivity.
     * @param text header text to save
     */
    public static void saveHeaderText(String text) {
        prefs.edit().putString(keyHeaderText, text).apply();
    }

    //****************** CATEGORIES ******************//

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
     * Loads whether group should be open or not.
     * @param category category to check
     * @return true if open
     */
    public static boolean loadCategoryOpen(String category) {
        return prefs.getBoolean(keyCategoryOpen + category, true);
    }

    /**
     * Saves whether group should be open or not.
     * @param category category to check
     * @param open true if open
     */
    public static void saveCategoryOpen(String category, boolean open) {
        prefs.edit().putBoolean(keyCategoryOpen + category, open).apply();
    }

    //****************** SORTING SCENARIOS ******************//

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

    //****************** FONT ******************//

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

    //****************** AUDIO VOLUME ******************//

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

    //****************** LANGUAGE ******************//

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

    //****************** CREATE SCENE IMAGE SPINNER ******************//

    /**
     * Loads the background image spinner position in CreateScene.
     * @return background image spinner position
     */
    public static int loadBackgroundPos() {
        return prefs.getInt(keyBackgroundPos, 0);
    }

    /**
     * Loads the background image spinner position in CreateScene.
     * @param position background image spinner position
     */
    public static void saveBackgroundPos(int position) {
        prefs.edit().putInt(keyBackgroundPos, position).apply();
    }

    /**
     * Loads the foreground image spinner position in CreateScene.
     * @return foreground image spinner position
     */
    public static int loadForegroundPos() {
        return prefs.getInt(keyForegroundPos, 0);
    }

    /**
     * Loads the foreground image spinner position in CreateScene.
     * @param position foreground image spinner position
     */
    public static void saveForegroundPos(int position) {
        prefs.edit().putInt(keyForegroundPos, position).apply();
    }

    /**
     * Loads the person image spinner position in CreateScene.
     * @return person image spinner position
     */
    public static int loadPersonPos() {
        return prefs.getInt(keyPersonPos, 0);
    }

    /**
     * Loads the person image spinner position in CreateScene.
     * @param position person image spinner position
     */
    public static void savePersonPos(int position) {
        prefs.edit().putInt(keyPersonPos, position).apply();
    }

    /**
     * Loads the face image spinner position in CreateScene.
     * @return face image spinner position
     */
    public static int loadFacePos() {
        return prefs.getInt(keyFacePos, 0);
    }

    /**
     * Loads the face image spinner position in CreateScene.
     * @param position face image spinner position
     */
    public static void saveFacePos(int position) {
        prefs.edit().putInt(keyFacePos, position).apply();
    }

    //****************** TUTORIAL ******************//

    /**
     * Loads whether to show scenario tutorial or not.
     * @return true if to show
     */
    public static boolean loadTutorialScenario() {
        return prefs.getBoolean(keyTutorialScenario, true);
    }

    /**
     * Loads whether to show scenario tutorial or not.
     * @param show true if to show
     */
    public static void saveTutorialScenario(boolean show) {
        prefs.edit().putBoolean(keyTutorialScenario, show).apply();
    }

    /**
     * Loads whether to show scene tutorial or not.
     * @return true if to show
     */
    public static boolean loadTutorialScene() {
        return prefs.getBoolean(keyTutorialScene, true);
    }

    /**
     * Loads whether to show scene tutorial or not.
     * @param show true if to show
     */
    public static void saveTutorialScene(boolean show) {
        prefs.edit().putBoolean(keyTutorialScene, show).apply();
    }

    /**
     * Loads whether to show answer tutorial or not.
     * @return true if to show
     */
    public static boolean loadTutorialAnswer() {
        return prefs.getBoolean(keyTutorialAnswer, true);
    }

    /**
     * Loads whether to show answer tutorial or not.
     * @param show true if to show
     */
    public static void saveTutorialAnswer(boolean show) {
        prefs.edit().putBoolean(keyTutorialAnswer, show).apply();
    }

    //****************** DEBUGGING ******************//

    /**
     * Clears all scenario item preferences.
     *
     * Mainly used for debugging.
     */
    public static void clearGlobalPreferences() {
        prefs.edit().clear().commit();
    }
}
