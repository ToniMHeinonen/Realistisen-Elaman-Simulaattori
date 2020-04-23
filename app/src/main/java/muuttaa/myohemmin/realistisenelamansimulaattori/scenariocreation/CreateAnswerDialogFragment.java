package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Debug;

public class CreateAnswerDialogFragment extends AppCompatDialogFragment {
    private View view;
    private EditText menee;
    private EditText vastaus;
    private dialogiFragmentListener listener;
    private int korvaus = -1;
    private int koko = 0;
    private ArrayList<Scene> scenes;


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

        menee = (EditText) view.findViewById(R.id.menee);
        vastaus = (EditText) view.findViewById(R.id.vastauksenTeksti);
        if(getArguments().getBoolean("muokkaus", false)) {
            korvaus = getArguments().getInt("korvaa", -1);
            menee.setText(getArguments().getString("menee", " "));
            vastaus.setText(getArguments().getString("kysymys", " "));

            // Load color
            String color = getArguments().getString("color");
            int colorPos = colorMap.get(color);
            colorButtons[colorPos].setSelected(true);
            selectedButton = colorButtons[colorPos];
        }
        scenes = getArguments().getParcelableArrayList("createdScenes");
        Debug.print("Create", "", scenes.toString(),1);
        koko = getArguments().getInt("koko", 0);
        builder.setView(view)
                .setNegativeButton(getContext().getString(R.string.back_button), null)
                .setPositiveButton(getContext().getString(R.string.done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            try {
                                String v = (String) selectedButton.getTag();
                                String m = menee.getText().toString();
                                //if go is empty
                                if(m.trim().isEmpty() || m.trim().equals("end") || m.trim().equals("lopetus")){
                                    m = "null";
                                }
                                String vas = vastaus.getText().toString();
                                //if answer is empty then replace default
                                if(vas.trim().isEmpty()){
                                    vas = getString(R.string.default_answer) + " " + (koko + 1);
                                }
                                listener.applyDataBack(v, m, vas, korvaus);
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), getContext().getString(R.string.give_all_data), Toast.LENGTH_LONG).show();
                            }
                    }
                });

        return builder.create();
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
