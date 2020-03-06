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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSorting();
        setupScenarioList();
    }

    /**
     * Loads scenario list from json and displays values as list.
     * Sets current scenario to the clicked value.
     */
    private void setupScenarioList() {
        final ListView list = findViewById(R.id.list);
        List<String> scenarios = json.getScenarioList();
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
                System.out.println("Sort item selected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing selected");
            }
        });
    }

    public void settingsClick(View v) {

    }
}
