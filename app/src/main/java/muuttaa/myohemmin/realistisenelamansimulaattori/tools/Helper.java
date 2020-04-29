package muuttaa.myohemmin.realistisenelamansimulaattori.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public abstract class Helper {

    public static final String HIDE_NEGATIVE_BUTTON = "HIDE_NEGATIVE_BUTTON";

    /**
     * Creates custom alert dialog.
     * @param context application context
     * @param topicText topic text, null = "Alert!"
     * @param messageText message text, null = "Are you sure?"
     * @param positiveText positive button text, null = "Ok"
     * @param negativeText negative button text, null = "Cancel"
     * @param positiveListener listener for positive button
     * @param negativeListener listener for negative button
     * @return created alert dialog
     */
    public static AlertDialog showAlert(Context context, String topicText, String messageText,
                                 String positiveText, String negativeText,
                                 AlertPositiveButtonListener positiveListener,
                                 AlertNegativeButtonListener negativeListener) {

        // Check for null values, if null set to default text
        String topic = topicText != null ? topicText : context.getString(R.string.alertTopic);
        String message = messageText != null ? messageText : context.getString(R.string.alertMessage);
        String posText = positiveText != null ? positiveText : context.getString(R.string.alertPositiveButton);
        String negText = negativeText != null ? negativeText : context.getString(R.string.alertNegativeButton);

        // Initialize alert dialog
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_alert_dialog, null);
        final AlertDialog alertD = new AlertDialog.Builder(context).create();

        // Set topic text
        ((TextView)(view.findViewById(R.id.topic))).setText(topic);
        // Set message text
        ((TextView)(view.findViewById(R.id.text))).setText(message);
        // Set positive button text and listener
        Button positive = view.findViewById(R.id.positiveButton);
        positive.setText(posText);
        positive.setOnClickListener((e) -> {
            if (positiveListener != null)
                positiveListener.onClick();
            alertD.dismiss();
        });
        // Set negative button text and listener
        Button negative = view.findViewById(R.id.negativeButton);
        // Hide negative if you only need one button
        if (negativeText.equals(HIDE_NEGATIVE_BUTTON)) {
            negative.setVisibility(View.INVISIBLE);
        } else {
            negative.setText(negText);
            negative.setOnClickListener((e) -> {
                if (negativeListener != null)
                    negativeListener.onClick();
                alertD.dismiss();
            });
        }

        // Show alert
        alertD.setView(view);
        alertD.show();

        return alertD;
    }
}
