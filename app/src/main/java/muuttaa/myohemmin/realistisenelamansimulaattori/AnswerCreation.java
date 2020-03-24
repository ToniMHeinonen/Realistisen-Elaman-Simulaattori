package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.GeneralKeyAndValue;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class AnswerCreation extends AppCompatActivity {
    private Spinner varit;
    private EditText menee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_creation);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);

        this.varit = (Spinner) findViewById(R.id.vastausVari);
        this.menee = (EditText) findViewById(R.id.menee);

        ArrayAdapter<CharSequence> adapterVarit = ArrayAdapter.createFromResource(this,
                R.array.varit, android.R.layout.simple_spinner_item);
        adapterVarit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.varit.setAdapter(adapterVarit);
    }

    public void valmis(View view) {
        String meno = this.menee.getText().toString();
        String vari = this.varit.getSelectedItem().toString();
        finish();
    }
}
