package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.media.MediaPlayer;

public abstract class Music {

    private static MediaPlayer mediaPlayer;
    private static int startCounter = 0;

    public static void onStart() {
        startCounter++;
        if (startCounter == 1) {
            mediaPlayer = MediaPlayer.create(InitializeActivity.getContext(), R.raw.background_music);
            mediaPlayer.setLooping(true);
            updateVolume();
            mediaPlayer.start();
        }
    }

    public static void onStop() {
        startCounter--;
        if (startCounter == 0) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    public static void updateVolume() {
        float volume = GlobalPrefs.loadMusicVolume();
        mediaPlayer.setVolume(volume, volume);
    }
}
