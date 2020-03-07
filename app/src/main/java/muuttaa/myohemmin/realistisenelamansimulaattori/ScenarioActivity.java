package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.os.Bundle;
import android.os.Handler;
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
    private List<String> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scenario = extras.getString("scenario");
            saveSystem = new SaveSystem(this);
            saveSystem.setCurrentScenario(scenario);
            questionTextView = findViewById(R.id.question);
            questionTextView.setText(saveSystem.getQuestionFromScenario());
            colors = saveSystem.getColorsInString();
            setupAnswers();
        }
    }

    private void setupAnswers() {
        final ListView list = findViewById(R.id.answers);
        List<String> answersList = saveSystem.getAnswersList();
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,
                R.layout.scenario_answers, answersList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, long id) {
                // Set background-color for button
                view.setBackgroundResource(getResources()
                        .getIdentifier(String.valueOf(colors.get(position)),
                                "drawable", getApplicationContext().
                                        getPackageName()));
                list.setEnabled(false);
                // Pause 1 second, then do the run-method.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Set default background-color for button
                        view.setBackgroundResource(R.drawable.button_default);
                        String clickedItem = (String) list.getItemAtPosition(position);
                        refresh(clickedItem);
                        arrayAdapter.clear();
                        arrayAdapter.addAll(saveSystem.getAnswersList());
                        arrayAdapter.notifyDataSetChanged();
                        list.setEnabled(true);
                    }
                }, 1000);
            }
        });
    }

    private void refresh(String clickedItem) {
        saveSystem.nextScene(clickedItem);
        colors = saveSystem.getColorsInString();
        questionTextView.setText(saveSystem.getQuestionFromScenario());
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
