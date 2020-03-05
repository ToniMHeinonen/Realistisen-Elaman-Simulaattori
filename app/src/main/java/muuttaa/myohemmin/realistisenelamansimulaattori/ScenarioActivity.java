package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
                R.layout.scenario_answers, answersList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                view.setBackgroundResource(R.drawable.answer_correct);
                // Pause 1 second, then do the run-method.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        view.setBackgroundResource(R.drawable.answer_button);
                        String clickedItem = (String) list.getItemAtPosition(position);
                        saveSystem.nextScene(clickedItem);
                        questionTextView.setText(saveSystem.getQuestionFromScenario());
                        arrayAdapter.clear();
                        arrayAdapter.addAll(saveSystem.getAnswersList());
                        arrayAdapter.notifyDataSetChanged();
                    }
                }, 1000);
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
