package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ParentActivity extends AppCompatActivity {

    private Sound sounds;

    /**
     * Calls super onCreate.
     * @param savedInstanceState previous instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Informs music that activity has started.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Music.onStart();
    }

    /**
     * Informs music that activity has stopped.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Music.onStop();
    }

    /**
     * Releases sounds from memory when activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sounds != null)
            sounds.release();
    }

    /**
     * Loads sounds to memory.
     * @param soundFiles files to load
     */
    public void loadSounds(final int... soundFiles) {
        sounds = new Sound(this, soundFiles);
    }

    /**
     * Plays given sound.
     * @param sound file to play
     */
    public void playSound(final int sound) {
        sounds.playSound(sound);
    }
}
