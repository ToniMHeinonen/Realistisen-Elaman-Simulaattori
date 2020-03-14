package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystem;

public class ScenarioActivity extends AppCompatActivity {
    private String scenario;
    private SaveSystem saveSystem;
    private TextView questionTextView;
    private List<String> colors;
    private ArrayList<Integer> userAnswers;
    private final int CORRECT = 100;
    private final int WRONG = 0;
    private final int SEMICORRECT = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        userAnswers = new ArrayList<>();
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
                // Set background-color for button depending user's answer.
                view.setBackgroundResource(getResources()
                        .getIdentifier(String.valueOf(colors.get(position)),
                                "drawable", getApplicationContext().
                                        getPackageName()));

                // Add user's answer to list and set arrayadapter non-clickable.
                addUserAnswerToList(colors.get(position));
                list.setEnabled(false);

                // Pause 1 second, then do the run-method.
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Set default background-color for button.
                        view.setBackgroundResource(R.drawable.button_default);
                        String clickedItem = (String) list.getItemAtPosition(position);
                        saveSystem.nextScene(clickedItem);

                        // If the question was last from the scenario, go to GameOverActivity
                        // and send user's answers. Else update the questions in arrayadapter.
                        if (saveSystem.endOfScenario()) {
                            Intent intent = new Intent(getApplicationContext(), GameOverActivity.class);
                            intent.putExtra("userAnswers", userAnswers);
                            startActivity(intent);
                        } else {
                            colors = saveSystem.getColorsInString();
                            questionTextView.setText(saveSystem.getQuestionFromScenario());
                            arrayAdapter.clear();
                            arrayAdapter.addAll(saveSystem.getAnswersList());
                            arrayAdapter.notifyDataSetChanged();
                            list.setEnabled(true);
                        }
                    }
                }, 1000);
            }
        });
    }

    /**
     * Add 100% if player answered correctly, 50% if answer was "acceptable" and
     * 0% if answer was wrong.
     * @param color color of the answer
     */
    private void addUserAnswerToList(String color) {
        switch (color) {
            case "green":
                userAnswers.add(CORRECT);
                break;
            case "yellow":
                userAnswers.add(SEMICORRECT);
                break;
            case "red":
                userAnswers.add(WRONG);
                break;
        }
    }
}
