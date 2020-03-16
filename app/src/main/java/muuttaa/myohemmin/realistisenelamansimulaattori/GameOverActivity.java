package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    private ArrayList<Integer> userAnswers;
    private String scenario;
    private TextView completedScenario;
    private TextView completedPercentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            scenario = extras.getString("scenario");
            userAnswers = (ArrayList) extras.getSerializable("userAnswers");
            completedScenario = findViewById(R.id.resultScenario);
            completedScenario.setText("Suoritettu scenario: " + scenario);
            completedPercentage = findViewById(R.id.resultPercentage);
            completedPercentage.setText("Onnistumisprosentti: " + getPercentage(userAnswers) + "%");
        }
    }

    private String getPercentage(ArrayList<Integer> list) {
        double result = 0;
        for (int i = 0; i < list.size(); i++) {
            result += list.get(i);
        }
        result = result / (double) list.size();
        return new DecimalFormat("#.##").format(result);
    }
}
