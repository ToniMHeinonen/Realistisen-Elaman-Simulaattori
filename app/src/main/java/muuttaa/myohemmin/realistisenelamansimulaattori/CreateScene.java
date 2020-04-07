package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.GeneralKeyAndValue;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateScene extends ParentActivity implements dialogiFragmentti.dialogiFragmentListener{
    private EditText name;
    private EditText question;
    private Spinner background;
    private Spinner person;
    private Spinner face;
    private ListView lista;
    private List<GeneralKeyAndValue> kysymyksetGo;
    private  List<GeneralKeyAndValue> kysymyksetColor;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scene);
        this.name = (EditText) findViewById(R.id.scenenNimi);
        this.question = (EditText) findViewById(R.id.kysymys);
        this.background = (Spinner) findViewById(R.id.taustaSpinner);
        this.person = (Spinner) findViewById(R.id.personSpinner);
        this.face = (Spinner) findViewById(R.id.KasvoSpinner);
        this.lista = (ListView) findViewById(R.id.listaVastaukset);

        if(getIntent().getBooleanExtra("eka", false)){
            this.name.setText("first");
        }

        ArrayAdapter<CharSequence> adapterTaustat = ArrayAdapter.createFromResource(this,
                R.array.taustat, android.R.layout.simple_spinner_item);
        adapterTaustat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.background.setAdapter(adapterTaustat);

        ArrayAdapter<CharSequence> adapterHenkilot = ArrayAdapter.createFromResource(this,
                R.array.henkilot, android.R.layout.simple_spinner_item);
        adapterHenkilot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.person.setAdapter(adapterHenkilot);

        ArrayAdapter<CharSequence> adapterKasvot = ArrayAdapter.createFromResource(this,
                R.array.kasvot, android.R.layout.simple_spinner_item);
        adapterKasvot.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.face.setAdapter(adapterKasvot);

        kysymyksetGo = new LinkedList<>();
        kysymyksetColor = new LinkedList<>();
        updateListOfAnswers();
    }

    public void updateListOfAnswers(){
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int lap=0; lap < kysymyksetGo.size(); lap++){
            arrayList.add(this.getString(R.string.name_file) + kysymyksetGo.get(lap).getKey() + this.getString(R.string.go) + kysymyksetGo.get(lap).getValue() + this.getString(R.string.color_set) + kysymyksetColor.get(lap).getValue());
        }
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        lista.setAdapter(adapter);
        final Context con = this;
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final GeneralKeyAndValue varin = kysymyksetColor.get(position);
                final GeneralKeyAndValue menon = kysymyksetGo.get(position);
                new AlertDialog.Builder(con)
                        .setTitle(con.getString(R.string.huom))
                        .setMessage(con.getString(R.string.remove_item))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                kysymyksetColor.remove(varin);
                                kysymyksetGo.remove(menon);
                                updateListOfAnswers();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });
    }

    public void vastaus(View view) {
        dialogiFragmentti dia = new dialogiFragmentti();
        dia.show(getSupportFragmentManager(), "vastauksen luonti");
    }

    public void pois(View view) {
        try {
            String nimi = this.name.getText().toString();
            String kysymys = this.question.getText().toString();
            String tausta = this.background.getSelectedItem().toString();
            String henkilo = this.person.getSelectedItem().toString();
            String kasvo = this.face.getSelectedItem().toString();
            String[] ans = new String[kysymyksetGo.size()];
            for (int lap = 0; lap < kysymyksetGo.size(); lap++) {
                ans[lap] = kysymyksetGo.get(lap).getKey();
            }
            Scene scene = new Scene(nimi, kysymys, tausta, henkilo, kasvo, ans, kysymyksetGo, kysymyksetColor);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("scene", (Parcelable) scene);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        } catch (Exception e){
            Toast.makeText(this, this.getString(R.string.is_all_data_given), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void applyDataBack(String varia, String meno, String vastaus) {
        this.kysymyksetGo.add(new GeneralKeyAndValue(vastaus, meno));
        this.kysymyksetColor.add(new GeneralKeyAndValue(vastaus + "Color", varia));
        updateListOfAnswers();
    }
}
