package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

public class Debug {
    private static int DEBUG_LEVEL = 5;
    public static boolean showOnUI;

    public static void print(Context callerClass, String methodName, String message, int level) {
        if (BuildConfig.DEBUG && level <= DEBUG_LEVEL) {
            String msg = methodName + ", " + message;
            String className = callerClass.getClass().getSimpleName();

            if (!showOnUI) {
                Log.d(className, msg);
            } else {
                CharSequence text = className + ": " + msg;

                Toast toast = Toast.makeText(callerClass, text, Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }

    public static void print(String callerClass, String methodName, String message, int level) {
        if (BuildConfig.DEBUG && level <= DEBUG_LEVEL) {
            String msg = methodName + ", " + message;
            Log.d(callerClass, msg);
        }
    }

    public static void loadDebug(Context host) {
        Resources res = host.getResources();
        DEBUG_LEVEL = res.getInteger(R.integer.debug_level);
    }
}
