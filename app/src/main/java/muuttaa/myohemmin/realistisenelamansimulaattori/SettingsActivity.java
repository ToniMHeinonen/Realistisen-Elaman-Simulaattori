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

    private String fontSize = "", fontColor = "";

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
                String size = (String) parent.getItemAtPosition(position);
                fontSize = size;
                GlobalPrefs.setFontSizePos(position);

                // This is called automatically at the start of activity
                if (!started) {
                    started = true;
                } else {
                    updateFontStyle();
                    restartActivity();
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
                String color = (String) parent.getItemAtPosition(position);
                fontColor = color;
                GlobalPrefs.setFontColorPos(position);

                // This is called automatically at the start of activity
                if (!started) {
                    started = true;
                } else {
                    updateFontStyle();
                    restartActivity();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateFontStyle() {
        if (fontSize.equals(getResources().getString(R.string.font_small))) {
            if (fontColor.equals(getResources().getString(R.string.font_black))) {
                GlobalPrefs.setFontStyle(FontStyle.SmallBlack);
            } else if (fontColor.equals(getResources().getString(R.string.font_red))) {
                GlobalPrefs.setFontStyle(FontStyle.SmallRed);
            } else if (fontColor.equals(getResources().getString(R.string.font_green))) {
                GlobalPrefs.setFontStyle(FontStyle.SmallGreen);
            } else if (fontColor.equals(getResources().getString(R.string.font_blue))) {
                GlobalPrefs.setFontStyle(FontStyle.SmallBlue);
            }
        } else if (fontSize.equals(getResources().getString(R.string.font_normal))) {
            if (fontColor.equals(getResources().getString(R.string.font_black))) {
                GlobalPrefs.setFontStyle(FontStyle.MediumBlack);
            } else if (fontColor.equals(getResources().getString(R.string.font_red))) {
                GlobalPrefs.setFontStyle(FontStyle.MediumRed);
            } else if (fontColor.equals(getResources().getString(R.string.font_green))) {
                GlobalPrefs.setFontStyle(FontStyle.MediumGreen);
            } else if (fontColor.equals(getResources().getString(R.string.font_blue))) {
                GlobalPrefs.setFontStyle(FontStyle.MediumBlue);
            }
        } else if (fontSize.equals(getResources().getString(R.string.font_large))) {
            if (fontColor.equals(getResources().getString(R.string.font_black))) {
                GlobalPrefs.setFontStyle(FontStyle.LargeBlack);
            } else if (fontColor.equals(getResources().getString(R.string.font_red))) {
                GlobalPrefs.setFontStyle(FontStyle.LargeRed);
            } else if (fontColor.equals(getResources().getString(R.string.font_green))) {
                GlobalPrefs.setFontStyle(FontStyle.LargeGreen);
            } else if (fontColor.equals(getResources().getString(R.string.font_blue))) {
                GlobalPrefs.setFontStyle(FontStyle.LargeBlue);
            }
        }
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
