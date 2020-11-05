package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;

public class GameOverActivity extends ParentActivity {
    private ArrayList<Integer> userAnswers;
    private String scenario;
    private TextView completedScenarioTextView;
    private TextView completedPercentageTextView;
    private TextView feedbackTextView;
    private ImageView percentageBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            loadSounds(R.raw.correct);
            scenario = extras.getString("scenario");
            userAnswers = (ArrayList) extras.getSerializable("userAnswers");
            completedScenarioTextView = findViewById(R.id.resultScenario);
            completedScenarioTextView.setText(getString(R.string.completed_scenario) + scenario);
            completedPercentageTextView = findViewById(R.id.resultPercentage);
            feedbackTextView = findViewById(R.id.feedback);
            percentageBg = findViewById(R.id.resultPercentageBackground);
            int completedPercentage = getPercentage(userAnswers);
            feedbackTextView.setText(giveFeedback(completedPercentage));
            completedPercentageTextView.setText(getString(R.string.completed_percentage) + completedPercentage + "%");
            giveFeedback(completedPercentage);
            if (ScenarioItemPrefs.loadPercentage(scenario) < completedPercentage) {
                ScenarioItemPrefs.savePercentage(scenario, completedPercentage);
            }

            // Change date of last time played for this scenario
            ScenarioItemPrefs.saveLastTimePlayed(scenario);
        }
    }

    private Integer getPercentage(ArrayList<Integer> list) {
        double result = 0;
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }
        result = result / (double) list.size();
        return (int) Math.round(result);
    }

    public void goToMenu(View v) {
        if (v.getId() == R.id.backToMenu) {
            playSound(R.raw.correct);
            onBackPressed();
        }
    }

    private String giveFeedback(int completedPercentage) {
        String result = "";
        if (completedPercentage < 50) {
            result = getString(R.string.completed_50);
            percentageBg.setImageResource(R.drawable.red);
        } else if (completedPercentage <= 75) {
            result = getString(R.string.completed_75);
            percentageBg.setImageResource(R.drawable.yellow);
        } else if (completedPercentage <= 90) {
            result = getString(R.string.completed_90);
            percentageBg.setImageResource(R.drawable.yellow);
        } else if (completedPercentage <= 99) {
            result = getString(R.string.completed_99);
            percentageBg.setImageResource(R.drawable.yellow);
        } else {
            result = getString(R.string.completed_100);
            percentageBg.setImageResource(R.drawable.green);
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        // Start new intent just in case if previous one has been closed by the memory management
        startActivity(new Intent(this, ChooseScenarioActivity.class));
        finish();
    }
}
