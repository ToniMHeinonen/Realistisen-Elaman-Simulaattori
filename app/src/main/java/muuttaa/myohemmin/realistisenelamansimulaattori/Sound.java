package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.util.SparseIntArray;

public class Sound {

    private SoundPool soundPool;
    private SparseIntArray sounds = new SparseIntArray();

    /**
     * Initializes sounds.
     *
     * Loads sounds from memory and adds them to a list when they are loaded.
     * @param activity activity where this class is used
     * @param soundFiles files to be played on current activity
     */
    public Sound(Activity activity, final int... soundFiles) {
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

        // Load all given files
        for (int file : soundFiles) {
            soundPool.load(activity, file, 1);
        }

        // Add sound to array when load is complete
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                // SampleId seems to be loading order, starting from 1
                sounds.put(soundFiles[sampleId - 1], sampleId);
            }
        });
    }

    /**
     * Plays given sound.
     * @param fileID id of the sound to play
     */
    public void playSound(int fileID) {
        if(sounds.indexOfKey(fileID) >= 0) {
            float volume = GlobalPrefs.loadSoundVolume();
            soundPool.play(sounds.get(fileID), volume, volume, 0, 0, 1);
        }
    }

    /**
     * Releases audio files from memory.
     *
     * This must be called on every onDestroy method of Activity where this class is used.
     */
    public void release() {
        soundPool.release();
        soundPool = null;
    }
}
