package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                R.array.font_sizes, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        fontSize.setAdapter(adapter);

        fontSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String size = (String) parent.getItemAtPosition(position);

                if (size.equals(getResources().getString(R.string.font_small))) {

                } else if (size.equals(getResources().getString(R.string.font_normal))) {

                } else if (size.equals(getResources().getString(R.string.font_large))) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupFontColor() {
        Spinner fontColor = findViewById(R.id.fontColor);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.font_colors, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
}
