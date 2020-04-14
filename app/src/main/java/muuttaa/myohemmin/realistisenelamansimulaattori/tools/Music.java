package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

import android.media.MediaPlayer;

import muuttaa.myohemmin.realistisenelamansimulaattori.InitializeActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public abstract class Music {

    private static MediaPlayer mediaPlayer;
    private static int startCounter = 0;

    /**
     * Checks if startCounter is at 1.
     *
     * This is called every time a new Activity starts. If it's the first Activity,
     * start playing music.
     */
    public static void onStart() {
        startCounter++;
        if (startCounter == 1) {
            mediaPlayer = MediaPlayer.create(InitializeActivity.getContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            updateVolume();
            mediaPlayer.start();
        }
    }

    /**
     * Checks if startCounter is at 0.
     *
     * This is called everytime an Activity stops. If there are no more activities,
     * stop playing music.
     */
    public static void onStop() {
        startCounter--;
        if (startCounter == 0) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    /**
     * Tells music player that volume has changed.
     */
    public static void updateVolume() {
        float volume = GlobalPrefs.loadMusicVolume();
        mediaPlayer.setVolume(volume, volume);
    }
}
