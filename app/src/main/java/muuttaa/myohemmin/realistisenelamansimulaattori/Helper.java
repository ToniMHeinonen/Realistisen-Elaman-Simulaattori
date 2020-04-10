package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

public abstract class Helper {

    public static void showAlert(Activity activity, int alertID, String topic, String message, AlertButtonListener listener) {
        // Initialize alert dialog
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = layoutInflater.inflate(R.layout.custom_alert_dialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(activity).create();

        // Set topic text
        ((TextView)(view.findViewById(R.id.topic))).setText(topic);
        // Set message text
        ((TextView)(view.findViewById(R.id.text))).setText(message);
        // Set positive button text and listener
        Button positive = view.findViewById(R.id.positiveButton);
        positive.setText(activity.getString(R.string.scenarioReset));
        positive.setOnClickListener((e) -> {
            listener.positiveButtonClicked(alertID);
            alertD.dismiss();
        });
        // Set negative button text and listener
        Button negative = view.findViewById(R.id.negativeButton);
        negative.setText(activity.getString(R.string.scenarioCancel));
        negative.setOnClickListener((e) -> {
            listener.negativeButtonClicked(alertID);
            alertD.dismiss();
        });

        // Show alert
        alertD.setView(view);
        alertD.show();
    }
}
