package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.CategoriesListAdapter;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystem;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItem;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemAdapter;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class ChooseScenarioActivity extends AppCompatActivity {

    private JsonInterface json = new SaveSystem(this);
    private ArrayList<ScenarioItem> scenarios = new ArrayList<>();
    private final int SORT_NAME = 0, SORT_RECENT = 1, SORT_PERCENTAGE = 2;
    private int sortBy = SORT_NAME;
    private boolean sortAscending = true;

    // Categories
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScenarioItemPrefs.initialize(this);
        loadScenarios();

        setupSorting();
        showScenarioList();

        // Categories
        expandableListView = findViewById(R.id.categoriesPlaceholder);
        expandableListDetail = CategoriesListAdapter.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CategoriesListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " ListView Open.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " ListView Closed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(getApplicationContext(),
                        expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                )
                        .show();
                return false;
            }
        });
    }

    /**
     * Loads scenarios from json and creates ScenarioItem objects from them.
     */
    private void loadScenarios() {
        List<String> scenarioNames = json.getScenarioList();

        for (String name : scenarioNames) {
            scenarios.add(new ScenarioItem(name));
        }
    }

    /**
     * Displays scenario items in a list.
     */
    private void showScenarioList() {
        final ListView list = findViewById(R.id.list);
        list.setAdapter(new ScenarioItemAdapter(this, scenarios));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ScenarioItem clickedItem = (ScenarioItem) list.getItemAtPosition(position);
                String name = clickedItem.getName();
                Intent intent = new Intent(ChooseScenarioActivity.this, ScenarioActivity.class);
                intent.putExtra("scenario", name);
                startActivity(intent);

                // Change date of last time played for this scenario
                ScenarioItemPrefs.saveLastTimePlayed(name);
            }
        });
    }

    /**
     * Adds sort item selected listener to sort Spinner and sorts list items desirably.
     */
    private void setupSorting() {
        Spinner sort = findViewById(R.id.sort);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.scenario_sort, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sort.setAdapter(adapter);

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
        switch (sortBy) {
            case SORT_NAME:
                Collections.sort(scenarios, new Comparator<ScenarioItem>() {
                    public int compare(ScenarioItem o1, ScenarioItem o2) {
                        return sortAscending ?
                                o1.getName().compareTo(o2.getName()) :
                                o2.getName().compareTo(o1.getName());
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

        showScenarioList();
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
     * Moves to Settings Activity.
     * @param v settings button
     */
    public void settingsClick(View v) {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
