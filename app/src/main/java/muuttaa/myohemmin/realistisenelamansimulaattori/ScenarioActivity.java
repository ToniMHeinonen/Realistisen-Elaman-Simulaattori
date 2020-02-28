package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystem;

public class ScenarioActivity extends AppCompatActivity {
    private String scenario;
    private SaveSystem saveSystem;

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
}
