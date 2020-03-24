package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.GeneralKeyAndValue;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.LinkedList;
import java.util.List;

public class CreateScene extends AppCompatActivity {
    private EditText name;
    private EditText question;
    private Spinner background;
    private Spinner person;
    private Spinner face;
    private ListView lista;
    private List<GeneralKeyAndValue> kysymyksetGo;
    private  List<GeneralKeyAndValue> kysymyksetColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.getFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scene);
        this.name = (EditText) findViewById(R.id.scenenNimi);
        this.question = (EditText) findViewById(R.id.kysymys);
        this.background = (Spinner) findViewById(R.id.taustaSpinner);
        this.person = (Spinner) findViewById(R.id.personSpinner);
        this.face = (Spinner) findViewById(R.id.KasvoSpinner);
        this.lista = (ListView) findViewById(R.id.listaVastaukset);

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
    }

    public void vastaus(View view) {
        Intent i = new Intent(getApplicationContext(), AnswerCreation.class);
        startActivity(i);
    }

    public void pois(View view) {
        String nimi = this.name.getText().toString();
        String kysymys = this.question.getText().toString();
        String tausta = this.background.getSelectedItem().toString();
        String henkilo = this.person.getSelectedItem().toString();
        String kasvo = this.face.getSelectedItem().toString();
        String[] ans = new String[kysymyksetGo.size()];
        for(int lap=0; lap < kysymyksetGo.size(); lap++){
            ans[lap] = kysymyksetGo.get(lap).getKey();
        }
        Scene scene = new Scene(nimi,kysymys,tausta,henkilo,kasvo,ans,kysymyksetGo,kysymyksetColor);
    }
}
