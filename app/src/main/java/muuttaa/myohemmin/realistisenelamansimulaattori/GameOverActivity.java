package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem.ScenarioItemPrefs;

public class GameOverActivity extends ParentActivity {
    private ArrayList<Integer> userAnswers;
    private String scenario;
    private TextView completedScenarioTextView;
    private TextView completedPercentageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

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
            completedPercentageTextView.setText("Onnistumisprosentti: " + completedPercentage + "%" +
                    "\n\n" + giveFeedback(completedPercentage));
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
            Intent intent = new Intent(this, ChooseScenarioActivity.class);
            startActivity(intent);
        }
    }

    private String giveFeedback(int completedPercentage) {
        String result = "";
        if (completedPercentage <= 50) {
            result = "Voi harmi, nyt ei kyllä mennyt aivan putkeen... Yritä uudelleen paremmalla " +
                    "onnella ja harkitse tarkkaan vastauksiasi.";
        } else if (completedPercentage < 100) {
            result = "Hienoa! Suoriuduit tästä skenaariosta varsin mallikkaasti, " +
                    "mutta vielä on parantamisen varaa.";
        } else {
            result = "Loistavaa! Sait kaikki oikein tässä skenaariossa.";
        }
        return result;
    }
}
