package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;

public class GameOverActivity extends AppCompatActivity {
    private ArrayList<Integer> userAnswers;
    private String scenario;
    private TextView completedScenarioTextView;
    private TextView completedPercentageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.getFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scenario = extras.getString("scenario");
            userAnswers = (ArrayList) extras.getSerializable("userAnswers");
            completedScenarioTextView = findViewById(R.id.resultScenario);
            completedScenarioTextView.setText("Suoritettu skenaario: " + scenario);
            completedPercentageTextView = findViewById(R.id.resultPercentage);
            int completedPercentage = getPercentage(userAnswers);
            completedPercentageTextView.setText("Onnistumisprosentti: " + completedPercentage + "%");
            if (ScenarioItemPrefs.loadPercentage(scenario) < completedPercentage) {
                ScenarioItemPrefs.savePercentage(scenario, completedPercentage);
            }
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
            Intent intent = new Intent(this, ChooseScenarioActivity.class);
            startActivity(intent);
        }
    }
}
