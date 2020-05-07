package muuttaa.myohemmin.realistisenelamansimulaattori;

import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.CategoriesListAdapter;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.CategoryDialog;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.HamburgerDialog;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioDialog;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItem;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Debug;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ChooseScenarioActivity extends ParentActivity {

    private JsonInterface json;
    SaveSystemPreferences jsonPrefs = new SaveSystemPreferences(this);
    private ArrayList<ScenarioItem> scenarios = new ArrayList<>();
    private final int SORT_NAME = 0, SORT_RECENT = 1, SORT_PERCENTAGE = 2;
    private int sortBy;
    private ImageButton sortArrow;
    private boolean sortAscending;

    // Header
    private EditText header;

    // Categories
    private ExpandableListView categoriesListView;
    private CategoriesListAdapter categoriesListAdapter;
    private List<String> categoriesListTitle;
    private HashMap<String, List<ScenarioItem>> categoriesListDetail;
    private HashSet<String> resourcesCategories = new HashSet<>();

    // Sound
    public final int S_POPUP = R.raw.popup;
    public final int S_CORRECT = R.raw.correct;

    /**
     * Initializes instance of this activity and all necessary values.
     * @param savedInstanceState previous instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadSounds(S_POPUP, S_CORRECT);

        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        json = new SaveSystemPreferences(this);

        loadHeaderText();
        setupSorting(); // Setup sorting spinner

        // Categories
        categoriesListView = findViewById(R.id.categoriesPlaceholder);
        showScenarioList(); // Setup category and scenario list
    }

    /**
     * Refresh activity when resuming.
     */
    @Override
    protected void onResume() {
        refreshActivity();
        super.onResume();
    }

    /**
     * Loads header text if user has changed it.
     */
    private void loadHeaderText() {
        header = findViewById(R.id.header);
        String savedHeader = GlobalPrefs.loadHeaderText();

        if (!savedHeader.equals("")) {
            header.setText(savedHeader);
        }
    }

    /**
     * Load scenarios and refresh all necessary list values.
     */
    public void refreshActivity() {
        loadScenarios();        // Load scenarios from json
        sortList();             // Sorts loaded list by current sort style
        refreshScenarioList();  // Informs list adapter for changes
    }

    /**
     * Loads scenarios from json and creates ScenarioItem objects from them.
     */
    private void loadScenarios() {
        scenarios.clear();
        resourcesCategories.clear();

        List<String> scenarioNames = json.getScenarioList();

        for (int i = 0; i < scenarioNames.size(); i++) {
            String name = scenarioNames.get(i);
            ScenarioItem scenario = new ScenarioItem(name);

            // If scenario is not user created
            if (!jsonPrefs.checkIfScenarioIsUserCreated(name)) {
                String category = json.getCategory(name);
                resourcesCategories.add(category);
                // If user has not changed the category of the scenario
                if (scenario.getCategory() == null) {
                    loadScenarioCategoryFromResources(scenario);
                }
            }
            scenarios.add(scenario);
        }
    }

    /**
     * Deletes scenario and decreases all scenarios with higher id by one
     * @param scenario scenario to delete
     */
    public void deleteScenario(ScenarioItem scenario) {
        String name = scenario.getName();

        scenarios.remove(scenario);

        // Delete the value, so it does not stay hanging in preferences
        ScenarioItemPrefs.deleteScenarioitem(name);

        // Delete from json
        SaveSystemPreferences json = new SaveSystemPreferences(this);
        json.deleteScenario(name);

        // Reload list
        refreshActivity();
    }

    /**
     * Displays scenario items in a categorized list.
     */
    private void showScenarioList() {
        categoriesListDetail = CategoriesListAdapter.getData(scenarios, resourcesCategories);
        categoriesListTitle = new ArrayList<String>(categoriesListDetail.keySet());
        categoriesListAdapter = new CategoriesListAdapter(this, categoriesListTitle, categoriesListDetail);
        categoriesListView.setAdapter(categoriesListAdapter);

        // Set listener for starting scenario
        categoriesListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                ScenarioItem clickedItem = categoriesListDetail.get(
                        categoriesListTitle.get(groupPosition)).get(
                        childPosition);

                new ScenarioDialog(ChooseScenarioActivity.this, clickedItem).show();

                playSound(S_POPUP);
                return false;
            }
        });

        // Set listener for dragging and moving categories and scenarios
        categoriesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD ||
                        ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        final ScenarioItem selectedItem = categoriesListDetail.get(
                                categoriesListTitle.get(groupPosition)).get(childPosition);
                        setupScenarioDragMovement(selectedItem);
                    } else {
                        final String selectedCategory = categoriesListTitle.get(groupPosition);
                        // Don't allow moving of default and resources categories
                        if (!selectedCategory.equals(getString(R.string.scenarios)) &&
                            !Helper.isCategoryFromResources(selectedCategory)) {
                            setupCategoryDragMovement(selectedCategory);
                        } else {
                            return false;
                        }
                    }

                    // Create shadow where the finger is
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                    view.startDrag(null, shadowBuilder, categoriesListView.getItemAtPosition(position), 0);

                    // Return true as we are handling the event.
                    return true;
                }

                return false;
            }
        });

        // Save opened groups
        categoriesListView.setOnGroupExpandListener((pos) ->  {
            String name = categoriesListTitle.get(pos);
            GlobalPrefs.saveCategoryOpen(name, true);
        });

        // Save closed groups
        categoriesListView.setOnGroupCollapseListener((pos) ->  {
            String name = categoriesListTitle.get(pos);
            GlobalPrefs.saveCategoryOpen(name, false);
        });
    }

    /**
     * Returns name of the drop location.
     *
     * Used in dragging and moving categories and scenarios.
     * @param location dropped location
     * @return name of the drop location
     */
    private String getDropLocationName(Object location) {
        String category = null;

        if (location.getClass().equals(String.class)) {
            // If drop location is on top of category string object
            category = (String) location;
        } else if (location.getClass().equals(ScenarioItem.class)) {
            // Else if drop location is on top of ScenarioItem object
            ScenarioItem item = (ScenarioItem) location;
            category = item.getCategory();
        }

        return category;
    }

    /**
     * Controls dragging and dropping of scenarios.
     * @param selectedItem dragged scenario
     */
    private void setupScenarioDragMovement(final ScenarioItem selectedItem) {
        categoriesListView.setOnDragListener(new AdapterView.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();

                if (action == DragEvent.ACTION_DROP) {
                    int x_cord = (int) event.getX();
                    int y_cord = (int) event.getY();
                    int nPointToPosition = categoriesListView.pointToPosition(x_cord,y_cord);
                    if(categoriesListView.getItemAtPosition(nPointToPosition)!= null) {
                        Object dropped = categoriesListView.getItemAtPosition(nPointToPosition);

                        String category = getDropLocationName(dropped);

                        Debug.print("ChooseScenarioActivity", "onDrag",
                                "Category: " + category + " Scenario: "
                                        + selectedItem.getName(), 1);

                        // If dropped to default category, make value null
                        if (category == null ||
                                category.equals(getString(R.string.scenarios))) {
                            selectedItem.setCategory(null);
                        } else {
                            selectedItem.setCategory(category);
                        }

                        refreshScenarioList();
                    }
                }

                return true;
            }
        });
    }

    /**
     * Controls dragging and dropping of categories.
     * @param selectedCategory dragged category
     */
    private void setupCategoryDragMovement(final String selectedCategory) {
        categoriesListView.setOnDragListener(new AdapterView.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                int action = event.getAction();

                if (action == DragEvent.ACTION_DROP) {
                    int x_cord = (int) event.getX();
                    int y_cord = (int) event.getY();
                    int nPointToPosition = categoriesListView.pointToPosition(x_cord,y_cord);
                    if(categoriesListView.getItemAtPosition(nPointToPosition)!= null) {
                        Object dropped = categoriesListView.getItemAtPosition(nPointToPosition);

                        String categoryNewPosition = getDropLocationName(dropped);

                        Debug.print("ChooseScenarioActivity", "onDrag",
                                "Selected category: " + selectedCategory +
                                        " Dropped on category: " + categoryNewPosition, 1);

                        // If dropped on top of item with default category, value will be null
                        if (categoryNewPosition == null)
                            categoryNewPosition = getString(R.string.scenarios);

                        // If it did not drop on top of itself or it's children
                        if (!categoryNewPosition.equals(selectedCategory)) {
                            GlobalPrefs.moveCategory(selectedCategory, categoryNewPosition);

                            refreshScenarioList();
                        }
                    }
                }

                return true;
            }
        });
    }

    /**
     * Refreshes categories and scenarios.
     */
    private void refreshScenarioList() {
        categoriesListDetail = CategoriesListAdapter.getData(scenarios, resourcesCategories);
        categoriesListTitle = new ArrayList<String>(categoriesListDetail.keySet());
        categoriesListAdapter.setNewItems(categoriesListTitle, categoriesListDetail);

        // Loop through categories and open the ones user has left open
        for (int i = 0; i < categoriesListAdapter.getGroupCount(); i++) {
            String name = (String) categoriesListAdapter.getGroup(i);

            if (GlobalPrefs.loadCategoryOpen(name))
                categoriesListView.expandGroup(i);
        }
    }

    /**
     * Adds sort item selected listener to sort Spinner and sorts list items desirably.
     */
    private void setupSorting() {
        // Load previous sorting style
        sortBy = GlobalPrefs.loadSortType();
        sortAscending = GlobalPrefs.loadSortAscending();

        sortArrow = findViewById(R.id.sortDirection); // Load sort arrow

        Spinner sort = findViewById(R.id.sort);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scenario_sort, R.layout.custom_spinner);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        sort.setAdapter(adapter); // Apply the adapter to the spinner
        sort.setSelection(sortBy); // Set selection to loaded value

        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sort = (String) parent.getItemAtPosition(position);

                if (sort.equals(getResources().getString(R.string.sort_name))) {
                    sortBy = SORT_NAME;
                } else if (sort.equals(getResources().getString(R.string.sort_recent))) {
                    sortBy = SORT_RECENT;
                } else if (sort.equals(getResources().getString(R.string.sort_percentage))) {
                    sortBy = SORT_PERCENTAGE;
                }

                sortList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**
     * Sorts list by current sorting type and order.
     */
    private void sortList() {
        GlobalPrefs.saveSortType(sortBy);
        GlobalPrefs.saveSortAscending(sortAscending);

        switch (sortBy) {
            case SORT_NAME:
                Collections.sort(scenarios, new Comparator<ScenarioItem>() {
                    public int compare(ScenarioItem o1, ScenarioItem o2) {
                        return sortAscending ?
                                o1.getName().compareToIgnoreCase(o2.getName()) :
                                o2.getName().compareToIgnoreCase(o1.getName());
                    }
                });
                break;
            case SORT_RECENT:
                Collections.sort(scenarios, new Comparator<ScenarioItem>() {
                    public int compare(ScenarioItem o1, ScenarioItem o2) {
                        return sortAscending ?
                                o1.getLastTimePlayed().compareTo(o2.getLastTimePlayed()) :
                                o2.getLastTimePlayed().compareTo(o1.getLastTimePlayed());
                    }
                });
                break;
            case SORT_PERCENTAGE:
                Collections.sort(scenarios, new Comparator<ScenarioItem>() {
                    public int compare(ScenarioItem o1, ScenarioItem o2) {
                        return sortAscending ?
                                ((Integer)o1.getPercentageCompleted()).compareTo(o2.getPercentageCompleted()) :
                                ((Integer)o2.getPercentageCompleted()).compareTo(o1.getPercentageCompleted());
                    }
                });
                break;
        }

        float deg = (sortAscending) ? 0f : 180f;
        sortArrow.animate().rotation(deg).setInterpolator(new AccelerateDecelerateInterpolator());

        refreshScenarioList();
    }

    /**
     * Swaps sorting order.
     * @param v arrow button
     */
    public void sortOrderClick(View v) {
        sortAscending = !sortAscending;
        sortList();
    }

    /**
     * Opens HamburgerDialog.
     * @param v hamburger button
     */
    public void hamburgerClicked(View v) {
        HamburgerDialog dialog = new HamburgerDialog(this);
        dialog.show();
        playSound(S_POPUP);
    }

    /**
     * Opens options for given category.
     * @param category desired category
     */
    public void openCategoryOptions(String category) {
        CategoryDialog dialog = new CategoryDialog(this, category);
        dialog.show();
        playSound(S_POPUP);
    }

    /**
     * Adds category.
     *
     * Note: This does not check categories with different languages.
     * @param category name
     */
    public void addCategory(String category) {
        if (!checkIfCategoryNameUnique(category)) {
            return;
        }

        GlobalPrefs.saveCategory(category);

        refreshScenarioList();
    }

    /**
     * Deletes given category.
     * @param category to delete
     */
    public void deleteCategory(String category) {
        if (category.equals(getResources().getString(R.string.scenarios)))
            return;

        for (ScenarioItem item : scenarios) {
            String curCategory = item.getCategory();

            if (curCategory != null && curCategory.equals(category)) {
                // If scenario is user created, move it to default category
                if (jsonPrefs.checkIfScenarioIsUserCreated(item.getName()))
                    item.setCategory(null);
                // Else load the category from resources
                else
                    loadScenarioCategoryFromResources(item);
            }
        }

        GlobalPrefs.deleteCategory(category);

        refreshScenarioList();
    }

    /**
     * Sets category to scenario from resources.
     * @param scenario scenario which is inside resources
     */
    private void loadScenarioCategoryFromResources(ScenarioItem scenario) {
        String name = scenario.getName();

        // Set category to the one defined in resources file
        scenario.setCategory(json.getCategory(name));

        // Save category to prefs as null
        ScenarioItemPrefs.saveCategory(name, null);
    }

    /**
     * Renames given category.
     * @param oldName category's old name
     * @param newName category's new name
     */
    public void renameCategory(String oldName, String newName) {
        if (oldName.equals(getResources().getString(R.string.scenarios)))
            return;

        if (!checkIfCategoryNameUnique(newName)) {
            return;
        }

        for (ScenarioItem item : scenarios) {
            String curCategory = item.getCategory();
            if (curCategory != null && curCategory.equals(oldName)) {
                item.setCategory(newName);
            }
        }

        GlobalPrefs.renameCategory(oldName, newName);

        refreshScenarioList();
    }

    /**
     * Checks if created category name is unigue.
     * @param category name of the category to check
     * @return true if name is unique
     */
    private boolean checkIfCategoryNameUnique(String category) {
        if (GlobalPrefs.loadCategories().contains(category) ||
                category.equals(getResources().getString(R.string.scenarios)) ||
                Helper.isCategoryFromResources(category)) {
            Toast.makeText(this, getResources().getString(R.string.categoryExists),
                    Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    /**
     * Saves header text if it has changed.
     */
    @Override
    protected void onStop() {
        super.onStop();
        // Saves header text if it has changed
        String text = header.getText().toString();
        String savedText = GlobalPrefs.loadHeaderText();
        // If text is the same as resources text
        if (text.equals(getString(R.string.my_tasks))) {
            GlobalPrefs.saveHeaderText(""); // Save header text back to default value
        }
        // If text is not the same as saved text
        else if (!text.equals(savedText)) {
            GlobalPrefs.saveHeaderText(text);
        }
    }

    /**
     * Closes application when user presses back on this Activity.
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
