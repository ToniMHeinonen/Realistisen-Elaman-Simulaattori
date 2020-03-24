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

    private FontStyle[][] styles = new FontStyle[][]{
            {FontStyle.SmallBlack, FontStyle.SmallRed, FontStyle.SmallGreen, FontStyle.SmallBlue},
            {FontStyle.MediumBlack, FontStyle.MediumRed, FontStyle.MediumGreen, FontStyle.MediumBlue},
            {FontStyle.LargeBlack, FontStyle.LargeRed, FontStyle.LargeGreen, FontStyle.LargeBlue}
    };

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
        final Spinner sizeSpinner = findViewById(R.id.fontSize);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_sizes, R.layout.custom_spinner);

        // Apply the adapter to the spinner
        sizeSpinner.setAdapter(adapter);
        sizeSpinner.setSelection(GlobalPrefs.getFontSizePos());

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean started = false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // This is called automatically at the start of activity
                if (!started) {
                    started = true;
                } else {
                    GlobalPrefs.setFontSizePos(position);
                    updateFontStyle();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupFontColor() {
        final Spinner colorSpinner = findViewById(R.id.fontColor);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_colors, R.layout.custom_spinner);

        // Apply the adapter to the spinner
        colorSpinner.setAdapter(adapter);
        colorSpinner.setSelection(GlobalPrefs.getFontColorPos());

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean started = false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // This is called automatically at the start of activity
                if (!started) {
                    started = true;
                } else {
                    GlobalPrefs.setFontColorPos(position);
                    updateFontStyle();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateFontStyle() {
        int size = GlobalPrefs.getFontSizePos();
        int color = GlobalPrefs.getFontColorPos();
        GlobalPrefs.setFontStyle(styles[size][color]);

        // Restart activity to apply changes to xml
        finish();
        startActivity(getIntent());
    }

    private void setupAudio() {
        SeekBar musicBar = findViewById(R.id.musicBar);
        SeekBar soundBar = findViewById(R.id.soundBar);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ChooseScenarioActivity.class));
    }
}
