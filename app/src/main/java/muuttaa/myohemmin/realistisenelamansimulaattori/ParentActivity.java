package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ParentActivity extends AppCompatActivity {

    private Sound sounds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Music.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sounds != null)
            sounds.release();
    }

    public void loadSounds(final int... soundFiles) {
        sounds = new Sound(this, soundFiles);
    }

    public void playSound(final int sound) {
        sounds.playSound(sound);
    }
}
