package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

public abstract class Sound {

    private static SoundPool soundPool;

    // Sound effects
    public static final int CORRECT, WRONG, POPUP;

    /**
     * Loads sound files.
     */
    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(6)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
        }

        Context context = ChooseScenarioActivity.getContext();

        CORRECT = soundPool.load(context, R.raw.correct, 1);
        WRONG = soundPool.load(context, R.raw.wrong, 1);
        POPUP = soundPool.load(context, R.raw.popup, 1);
    }

    /**
     * Calls static code block.
     */
    public static void initialize() {
        // This calls the static code block and loads necessary sound files
    }

    /**
     * Plays given sound.
     * @param soundID id of the sound to play
     */
    public static void playSound(int soundID) {
        float volume = GlobalPrefs.loadSoundVolume();
        soundPool.play(soundID, volume, volume, 0, 0, 1);
    }
}
