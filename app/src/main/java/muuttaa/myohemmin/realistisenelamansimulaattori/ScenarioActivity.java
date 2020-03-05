package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystem;

public class ScenarioActivity extends AppCompatActivity {
    private String scenario;
    private SaveSystem saveSystem;
    private TextView questionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scenario = extras.getString("scenario");
            saveSystem = new SaveSystem(this);
            saveSystem.setCurrentScenario(scenario);
            Log.d("ScenarioActivity", scenario);
            questionTextView = findViewById(R.id.question);
            questionTextView.setText(saveSystem.getQuestionFromScenario());
            setupAnswers();
        }
    }

    private void setupAnswers() {
        final ListView list = findViewById(R.id.answers);
        List<String> answersList = saveSystem.getAnswersList();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.scenario_item, answersList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedItem = (String) list.getItemAtPosition(position);
                Log.d("ScenarioActivity", clickedItem);
                saveSystem.nextScene(clickedItem);
                Log.d("ScenarioActivity", String.valueOf(saveSystem.getAnswersList()));
                Log.d("ScenarioActivity", saveSystem.getCurrentScenario());
                questionTextView.setText(saveSystem.getQuestionFromScenario());
                arrayAdapter.clear();
                arrayAdapter.addAll(saveSystem.getAnswersList());
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }

        /*
        Testing

        SaveSystem save = new SaveSystem(this);
        save.setCurrentScenario("BMW");
        Log.d("ScenarioActivity", String.valueOf(save.getScenarioList()));  // [Ford, BMW, Fiat]
        Log.d("ScenarioActivity", save.getJsonName());                      // bmw.json
        Log.d("ScenarioActivity", save.getQuestionFromScenario());          // Mikä on kaupunki?
        Log.d("ScenarioActivity", String.valueOf(save.getAnswersList()));   // [eka vastaus, ...]
        Log.d("ScenarioActivity", save.getPictureName());                   // kuva1
         */
}
