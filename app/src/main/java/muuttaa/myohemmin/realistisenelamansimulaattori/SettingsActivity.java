package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.getFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupFontSize();
        setupFontColor();
        setupAudio();
    }

    private void setupFontSize() {
        Spinner fontSize = findViewById(R.id.fontSize);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_sizes, R.layout.custom_spinner);

        // Apply the adapter to the spinner
        fontSize.setAdapter(adapter);
        fontSize.setSelection(GlobalPrefs.getFontStyle().ordinal());

        fontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean started = false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String size = (String) parent.getItemAtPosition(position);

                if (size.equals(getResources().getString(R.string.font_small))) {
                    GlobalPrefs.setFontStyle(FontStyle.Small);
                } else if (size.equals(getResources().getString(R.string.font_normal))) {
                    GlobalPrefs.setFontStyle(FontStyle.Medium);
                } else if (size.equals(getResources().getString(R.string.font_large))) {
                    GlobalPrefs.setFontStyle(FontStyle.Large);
                }

                // This is called automatically at the start of activity
                if (!started)
                    started = true;
                else
                    restartActivity();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupFontColor() {
        Spinner fontColor = findViewById(R.id.fontColor);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_colors, R.layout.custom_spinner);

        // Apply the adapter to the spinner
        fontColor.setAdapter(adapter);

        fontColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String color = (String) parent.getItemAtPosition(position);

                if (color.equals(getResources().getString(R.string.font_black))) {

                } else if (color.equals(getResources().getString(R.string.font_red))) {

                } else if (color.equals(getResources().getString(R.string.font_green))) {

                } else if (color.equals(getResources().getString(R.string.font_blue))) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupAudio() {
        SeekBar musicBar = findViewById(R.id.musicBar);
        SeekBar soundBar = findViewById(R.id.soundBar);
    }

    private void restartActivity() {
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ChooseScenarioActivity.class));
    }
}
