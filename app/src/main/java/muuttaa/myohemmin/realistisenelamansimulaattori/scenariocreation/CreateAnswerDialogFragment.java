package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.HTMLDialog;

public class CreateAnswerDialogFragment extends AppCompatDialogFragment {
    private View view;
    private Spinner sceneSpinner;
    private EditText vastaus;
    private dialogiFragmentListener listener;
    private String goesTo;
    private int korvaus = -1;
    private int koko = 0;
    private ArrayList<String> sceneNames;
    private int END_POSITION = 0;

    private Map<String, Integer> colorMap = new HashMap<String, Integer>() {{
        put("green", 0);
        put("yellow", 1);
        put("red", 2);
    }};
    private Button[] colorButtons = new Button[3];
    private Button selectedButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.answer_dialog, null);

        setupColorButtons();

        vastaus = (EditText) view.findViewById(R.id.vastauksenTeksti);
        if(getArguments().getBoolean("muokkaus", false)) {
            korvaus = getArguments().getInt("korvaa", -1);
            goesTo = getArguments().getString("menee", " ");
            vastaus.setText(getArguments().getString("kysymys", " "));

            // Load color
            String color = getArguments().getString("color");
            int colorPos = colorMap.get(color);
            colorButtons[colorPos].setSelected(true);
            selectedButton = colorButtons[colorPos];
        }

        // Set listener for info button since onClick does not work on DialogFragment
        view.findViewById(R.id.infoButton).setOnClickListener((v) -> showInfo());

        sceneNames = getArguments().getStringArrayList("createdScenes");
        setupSceneSpinner();

        koko = getArguments().getInt("koko", 0);
        builder.setView(view)
                .setNegativeButton(getContext().getString(R.string.back_button), null)
                .setPositiveButton(getContext().getString(R.string.done), null);

        final AlertDialog mAlertDialog = listenForCancelAndConfirm(builder);

        return mAlertDialog;
    }

    @Override
    public void onResume() {
        // Check if to show tutorial dialog
        if (GlobalPrefs.loadTutorialAnswer()) {
            showInfo();
        }
        super.onResume();
    }

    /**
     * Listens for confirm and cancel button presses.
     *
     * Creates and AlertDialog, so the dismiss call can be handled manually.
     * This prevents the AlertDialog from closing when it catches an error.
     * @param builder builder which is used to make the dialog
     * @return created AlertDialog object
     */
    private AlertDialog listenForCancelAndConfirm(AlertDialog.Builder builder) {
        final AlertDialog mAlertDialog = builder.create();
        mAlertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = mAlertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        try {
                            String v = (String) selectedButton.getTag();
                            String m;

                            // If spinner is at end position, set value as null
                            if (sceneSpinner.getSelectedItemPosition() == END_POSITION) {
                                m = "null";
                            } else {
                                m = (String) sceneSpinner.getSelectedItem();
                            }

                            String vas = vastaus.getText().toString();
                            //if answer is empty then replace default
                            if(vas.trim().isEmpty()){
                                vas = getString(R.string.default_answer) + " " + (koko + 1);
                            }
                            listener.applyDataBack(v, m, vas, korvaus);
                            dismiss();
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), getContext().getString(R.string.give_all_data), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return mAlertDialog;
    }

    /**
     * Sets up color button listeners.
     */
    private void setupColorButtons() {
        colorButtons[0] = view.findViewById(R.id.greenButton);
        colorButtons[1] = view.findViewById(R.id.yellowButton);
        colorButtons[2] = view.findViewById(R.id.redButton);

        // Set button listener to all of the buttons
        for (int i = 0; i < colorButtons.length; i++) {
            colorButtons[i].setOnClickListener((v) -> {
                Button clickedBtn = (Button) v;
                selectedButton = clickedBtn;

                // Loop through all of the buttons and set them selected and unselected
                for (int index = 0; index < colorButtons.length; index++) {
                    Button button = colorButtons[index];
                    button.setSelected(clickedBtn == button);
                }
            });
        }
    }

    /**
     * Sets up scene spinner for selecting where this answer leads to.
     */
    private void setupSceneSpinner() {
        sceneSpinner = view.findViewById(R.id.sceneSpinner);

        // Add end to the top of the list
        sceneNames.add(END_POSITION, getString(R.string.null_value));

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.custom_spinner, sceneNames);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        sceneSpinner.setAdapter(adapter);

        // Set spinner selection to saved value
        if (goesTo != null) {
            int index = sceneNames.indexOf(goesTo);
            if (index != -1)
                sceneSpinner.setSelection(index);
        }
    }

    public void showInfo() {
        new HTMLDialog(getActivity(), HTMLDialog.HTMLText.ANSWER).show();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (dialogiFragmentListener) context;
    }
    public interface dialogiFragmentListener{
        //if korvaus is -1 then not replace
        void applyDataBack(String varia, String meno, String vastaus, int korvaus);
    }
}
