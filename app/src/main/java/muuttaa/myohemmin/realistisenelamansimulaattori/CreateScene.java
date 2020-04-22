package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AlertDialog;
import muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation.Answer;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.GeneralKeyAndValue;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;
import muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation.CreateAnswerAdapter;
import muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation.CreateAnswerDialogFragment;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Debug;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateScene extends ParentActivity implements CreateAnswerDialogFragment.dialogiFragmentListener,
View.OnClickListener, View.OnLongClickListener{
    private EditText name;
    private EditText question;
    private Spinner background;
    private Spinner person;
    private Spinner face;
    private Spinner foreground;
    private ListView lista;
    private ImageView tausta;
    private ImageView henkilo;
    private ImageView kasvot;
    private List<GeneralKeyAndValue> kysymyksetGo;
    private List<GeneralKeyAndValue> kysymyksetColor;
    private int korvaus = -1;

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
        this.tausta = (ImageView) findViewById(R.id.createBackground);
        this.henkilo = (ImageView) findViewById(R.id.createCharacter);
        this.kasvot = (ImageView) findViewById(R.id.createFace);
        this.foreground = (Spinner) findViewById(R.id.ForegroundSpinner);
        korvaus = getIntent().getIntExtra("korvaus", -1);
        if(getIntent().getBooleanExtra("muokkaus", false)){
            Scene apu = (Scene) getIntent().getParcelableExtra("scene");
            //put values
            this.name.setText(apu.getName());
            kysymyksetGo = apu.getGoList();
            kysymyksetColor = apu.getColorList();
            this.question.setText(apu.getQuestion());
        } else{
            if(getIntent().getBooleanExtra("eka", false)){
                this.name.setText("first");
            }
            kysymyksetGo = new LinkedList<>();
            kysymyksetColor = new LinkedList<>();
        }

        ArrayAdapter<CharSequence> adapterTaustat = ArrayAdapter.createFromResource(this,
                R.array.taustat, R.layout.custom_spinner);
        adapterTaustat.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        this.background.setAdapter(adapterTaustat);

        ArrayAdapter<CharSequence> adapterHenkilot = ArrayAdapter.createFromResource(this,
                R.array.henkilot, R.layout.custom_spinner);
        adapterHenkilot.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        this.person.setAdapter(adapterHenkilot);

        ArrayAdapter<CharSequence> adapterKasvot = ArrayAdapter.createFromResource(this,
                R.array.kasvot, R.layout.custom_spinner);
        adapterKasvot.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        this.face.setAdapter(adapterKasvot);

        ArrayAdapter<CharSequence> adapterForeground = ArrayAdapter.createFromResource(this,
                R.array.kolmasKuva, R.layout.custom_spinner);
        adapterForeground.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        this.foreground.setAdapter(adapterForeground);
        //listeners
        this.background.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.face.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.foreground.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //updates
        updateImages();
        updateListOfAnswers();
    }

    /**
     * This method update images of preview
     */
    private void updateImages() {
        String naama = this.face.getSelectedItem().toString();
        String t = this.background.getSelectedItem().toString();
        String h = this.person.getSelectedItem().toString();
        this.tausta.setImageResource(getResources().getIdentifier(t, "drawable", getPackageName()));
        if(!h.equals("null") && !h.equals("tyhjä") && !h.equals("empty")) {
            this.henkilo.setImageResource(getResources().getIdentifier(h, "drawable", getPackageName()));
        } else{
            this.henkilo.setImageResource(android.R.color.transparent);
            //null value must be last
            int faceNullPosition =  getResources().getStringArray(R.array.kasvot).length - 1;
            this.face.setSelection(faceNullPosition);
        }
        if(!naama.equals("null") && !naama.equals("tyhjä") && !naama.equals("empty")) {
            this.kasvot.setImageResource(getResources().getIdentifier(naama, "drawable", getPackageName()));
        } else{
            this.kasvot.setImageResource(android.R.color.transparent);
        }
        //pakko lisätä foreground koodi myöhemmin tähän
    }

    /**
     * Shows answers in a list.
     */
    public void updateListOfAnswers(){
        ArrayList<Answer> arrayList = new ArrayList<>();
        for(int lap=0; lap < kysymyksetGo.size(); lap++){
            arrayList.add(new Answer(kysymyksetGo.get(lap).getKey(),kysymyksetGo.get(lap).getValue(), kysymyksetColor.get(lap).getValue()));
        }
        CreateAnswerAdapter adapter = new CreateAnswerAdapter(this, arrayList, this);
        lista.setAdapter(adapter);
    }

    public void vastaus(View view) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("muokkaus", false);
        bundle.putInt("korvaa", -1);
        bundle.putInt("koko", kysymyksetColor.size());
        CreateAnswerDialogFragment dia = new CreateAnswerDialogFragment();
        dia.setArguments(bundle);
        dia.show(getSupportFragmentManager(), "vastauksen luonti");
    }

    public void pois(View view) {
        if(allDataGiven()) {
            try {
                String nimi = this.name.getText().toString();
                String kysymys = this.question.getText().toString();
                String tausta = this.background.getSelectedItem().toString();
                String henkilo = this.person.getSelectedItem().toString();
                String kasvo = this.face.getSelectedItem().toString();
                String foree = this.foreground.getSelectedItem().toString();
                if(henkilo.equals("tyhjä") || henkilo.equals("empty")){
                    henkilo = "null";
                }
                if(kasvo.equals("tyhjä") || kasvo.equals("empty")){
                    kasvo = "null";
                }
                if(foree.equals("tyhjä") || foree.equals("empty")){
                    foree = "null";
                }
                String[] ans = new String[kysymyksetGo.size()];
                for (int lap = 0; lap < kysymyksetGo.size(); lap++) {
                    ans[lap] = kysymyksetGo.get(lap).getKey();
                }
                Scene scene = new Scene(nimi, kysymys, tausta, henkilo, kasvo, ans, kysymyksetGo, kysymyksetColor);
                scene.setForeground(foree);
                Intent returnIntent = new Intent();
                returnIntent.putExtra("scene", (Parcelable) scene);
                returnIntent.putExtra("korvaus", korvaus);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, this.getString(R.string.is_all_data_given), Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void applyDataBack(String varia, String meno, String vastaus, int korvaus) {
        if(korvaus >= 0){
            this.kysymyksetGo.set(korvaus, new GeneralKeyAndValue(vastaus, meno));
            this.kysymyksetColor.set(korvaus, new GeneralKeyAndValue(vastaus + "Color", varia));
            updateListOfAnswers();
        } else {
            this.kysymyksetGo.add(new GeneralKeyAndValue(vastaus, meno));
            this.kysymyksetColor.add(new GeneralKeyAndValue(vastaus + "Color", varia));
            updateListOfAnswers();
        }
    }
    private boolean allDataGiven(){
        if(this.name.getText().toString().trim().isEmpty()){
            dataNotGivenAlert(getString(R.string.remember_name), getString(R.string.not_added));
            return false;
        } else if(this.question.getText().toString().trim().isEmpty()){
            dataNotGivenAlert(getString(R.string.remember_question), getString(R.string.not_added));
            return false;
        } else if(this.kysymyksetGo.size() < 1){
            dataNotGivenAlert(getString(R.string.remember_answers), getString(R.string.not_added));
            return false;
        }
        return true;
    }
    private void dataNotGivenAlert(String content, String toastMessage){
        // Show content of what data is missing in alert dialog
        AlertDialog alert = Helper.showAlert(this, getString(R.string.huom), content,
                getString(android.R.string.yes), getString(android.R.string.no),
                null, null);
        // Show toast message after dialog has been dismissed
        alert.setOnDismissListener((d) -> Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show());
    }

    /**
     * Listens to answer button clicks.
     * @param v answer button
     */
    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        Bundle bundle = new Bundle();
        bundle.putBoolean("muokkaus", true);
        GeneralKeyAndValue meno = kysymyksetGo.get(position);
        bundle.putString("menee", meno.getValue());
        bundle.putString("kysymys", meno.getKey());
        bundle.putInt("korvaa", position);
        bundle.putInt("koko", kysymyksetColor.size());
        GeneralKeyAndValue color = kysymyksetColor.get(position);
        bundle.putString("color", color.getValue());
        CreateAnswerDialogFragment dia = new CreateAnswerDialogFragment();
        dia.setArguments(bundle);
        dia.show(getSupportFragmentManager(), "vastauksen luonti");
    }

    /**
     * Listens to answer button long clicks.
     * @param v answer button
     */
    @Override
    public boolean onLongClick(View v) {
        int position = (int) v.getTag();
        final GeneralKeyAndValue varin = kysymyksetColor.get(position);
        final GeneralKeyAndValue menon = kysymyksetGo.get(position);
        Helper.showAlert(this, getString(R.string.huom), getString(R.string.remove_item),
                getString(android.R.string.yes), getString(android.R.string.no),
                () -> {
                    kysymyksetColor.remove(varin);
                    kysymyksetGo.remove(menon);
                    updateListOfAnswers();
                }, null);
        return false;
    }
}
