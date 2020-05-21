package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsCheck {
    private Context context;
    private final int permissionBoth = 22114;
    private Activity activity;
    public PermissionsCheck(Context con, Activity act){
        this.context = con;
        this.activity = act;
    }
    public void BothPermission(Runnable runnable){
        int permissions = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissions2 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if((permissions == PackageManager.PERMISSION_DENIED) || (permissions2 == PackageManager.PERMISSION_DENIED)){
            String[] list = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(activity, list, permissionBoth);
        } else{
            runnable.run();
        }
    }
}
