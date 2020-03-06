package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystem;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItem;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemAdapter;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooseScenarioActivity extends AppCompatActivity {

    JsonInterface json = new SaveSystem(this);
    ArrayList<ScenarioItem> scenarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScenarioItemPrefs.initialize(this);
        loadScenarios();

        setupSorting();
        showScenarioList();
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
                String sortBy = (String) parent.getItemAtPosition(position);

                if (sortBy.equals(getResources().getString(R.string.sort_name))) {
                    Debug.print("OnItemSelectedListener", "onItemSelected",
                            "sort_name", 2);
                    Collections.sort(scenarios, new Comparator<ScenarioItem>() {
                        public int compare(ScenarioItem o1, ScenarioItem o2) {
                            return o1.getName().compareTo(o2.getName());
                        }
                    });
                } else if (sortBy.equals(getResources().getString(R.string.sort_recent))) {
                    Collections.sort(scenarios, new Comparator<ScenarioItem>() {
                        public int compare(ScenarioItem o1, ScenarioItem o2) {
                            return o2.getLastTimePlayed().compareTo(o1.getLastTimePlayed());
                        }
                    });
                } else if (sortBy.equals(getResources().getString(R.string.sort_percentage))) {
                    Collections.sort(scenarios, new Comparator<ScenarioItem>() {
                        public int compare(ScenarioItem o1, ScenarioItem o2) {
                            return ((Integer)o1.getPercentageCompleted()).compareTo(o2.getPercentageCompleted());
                        }
                    });
                }

                showScenarioList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Debug.print("OnItemSelectedListener", "onNothingSelected",
                        "", 2);
            }
        });
    }

    /**
     * Moves to Settings Activity.
     * @param v settings button
     */
    public void settingsClick(View v) {

    }
}
