package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.List;

public class ChooseScenarioActivity extends AppCompatActivity {

    JsonInterface json = new SaveSystem(this);
    List<String> scenarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSorting();
        scenarios = json.getScenarioList();
        showScenarioList();
    }

    /**
     * Loads scenario list from json and displays values as list.
     */
    private void showScenarioList() {
        final ListView list = findViewById(R.id.list);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.scenario_item, scenarios);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem=(String) list.getItemAtPosition(position);
                Intent intent = new Intent(ChooseScenarioActivity.this, ScenarioActivity.class);
                intent.putExtra("scenario", clickedItem);
                startActivity(intent);
            }
        });
    }

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
                    java.util.Collections.sort(scenarios);
                    showScenarioList();
                } else if (sortBy.equals(getResources().getString(R.string.sort_recent))) {

                } else if (sortBy.equals(getResources().getString(R.string.sort_percentage))) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Debug.print("OnItemSelectedListener", "onNothingSelected",
                        "", 2);
            }
        });
    }

    public void settingsClick(View v) {

    }
}
