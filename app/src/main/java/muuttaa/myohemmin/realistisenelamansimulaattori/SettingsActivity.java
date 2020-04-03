package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

public class SettingsActivity extends ParentActivity {

    private FontStyle[][] styles = new FontStyle[][]{
            {FontStyle.SmallBlack, FontStyle.SmallRed, FontStyle.SmallGreen, FontStyle.SmallBlue},
            {FontStyle.MediumBlack, FontStyle.MediumRed, FontStyle.MediumGreen, FontStyle.MediumBlue},
            {FontStyle.LargeBlack, FontStyle.LargeRed, FontStyle.LargeGreen, FontStyle.LargeBlue}
    };

    // Sound
    private Sound sound;
    private int S_CORRECT = R.raw.correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Load sounds
        sound = new Sound(this, S_CORRECT);

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
        sizeSpinner.setSelection(GlobalPrefs.loadFontSizePos());

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean started = false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // This is called automatically at the start of activity
                if (!started) {
                    started = true;
                } else {
                    GlobalPrefs.saveFontSizePos(position);
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
        colorSpinner.setSelection(GlobalPrefs.loadFontColorPos());

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            boolean started = false;

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // This is called automatically at the start of activity
                if (!started) {
                    started = true;
                } else {
                    GlobalPrefs.saveFontColorPos(position);
                    updateFontStyle();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateFontStyle() {
        int size = GlobalPrefs.loadFontSizePos();
        int color = GlobalPrefs.loadFontColorPos();
        GlobalPrefs.saveFontStyle(styles[size][color]);

        // Restart activity to apply changes to xml
        finish();
        startActivity(getIntent());
    }

    private void setupAudio() {
        // Music
        final SeekBar musicBar = findViewById(R.id.musicBar);
        musicBar.setProgress((int) (GlobalPrefs.loadMusicVolume() * 10));
        musicBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                progressChangedValue = musicBar.getProgress();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                GlobalPrefs.saveMusicVolume((float) progressChangedValue / 10);
                Music.updateVolume();
            }
        });

        // Sound
        final SeekBar soundBar = findViewById(R.id.soundBar);
        soundBar.setProgress((int) (GlobalPrefs.loadSoundVolume() * 10));
        soundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                progressChangedValue = soundBar.getProgress();
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                GlobalPrefs.saveSoundVolume((float) progressChangedValue / 10);
                sound.playSound(S_CORRECT);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ChooseScenarioActivity.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sound.release();
    }
}
