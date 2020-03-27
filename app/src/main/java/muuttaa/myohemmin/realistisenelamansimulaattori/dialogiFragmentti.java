package muuttaa.myohemmin.realistisenelamansimulaattori;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class dialogiFragmentti extends AppCompatDialogFragment {
    private Spinner vari;
    private EditText menee;
    private EditText vastaus;
    private dialogiFragmentListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialogi, null);
        vari = (Spinner) view.findViewById(R.id.vastausVari);
        menee = (EditText) view.findViewById(R.id.menee);
        vastaus = (EditText) view.findViewById(R.id.vastauksenTeksti);
        builder.setView(view)
                .setTitle("Luo vastaus")
                .setNegativeButton("peruuta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Valmis", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String v = vari.getSelectedItem().toString();
                        String m = menee.getText().toString();
                        String vas = vastaus.getText().toString();
                        listener.applyDataBack(v, m, vas);
                    }
                });

        return builder.create();
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        listener = (dialogiFragmentListener) context;
    }
    public interface dialogiFragmentListener{
        void applyDataBack(String varia, String meno, String vastaus);
    }
}