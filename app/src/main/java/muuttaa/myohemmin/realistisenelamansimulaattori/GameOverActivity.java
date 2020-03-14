package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.os.Bundle;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class GameOverActivity extends AppCompatActivity {
    private ArrayList<Integer> userAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userAnswers = (ArrayList) extras.getSerializable("userAnswers");
        }
        Debug.print("GameOverActivity",
                "onCreate()",
                "Answer percentages: " + userAnswers.toString(),
                1);
    }
}
