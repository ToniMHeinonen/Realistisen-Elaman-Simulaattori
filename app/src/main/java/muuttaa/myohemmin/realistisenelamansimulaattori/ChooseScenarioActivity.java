package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ChooseScenarioActivity extends AppCompatActivity {

    JsonInterface json = new SaveSystem(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                json.setCurrentScenario(clickedItem);
            }
        });
    }

    /*
    For testing ScenarioActivity-class. Works only with BMW for now.
     */
    public void clicked(View v) {
        Intent intent = new Intent(this, ScenarioActivity.class);
        intent.putExtra("scenario", json.getCurrentScenario());
        startActivity(intent);
    }
}
