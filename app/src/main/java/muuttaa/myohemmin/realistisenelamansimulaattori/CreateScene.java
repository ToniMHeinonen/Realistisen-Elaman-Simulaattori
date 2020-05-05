package muuttaa.myohemmin.realistisenelamansimulaattori;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
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
    private ImageView foreGroundView;
    private List<GeneralKeyAndValue> kysymyksetGo;
    private List<GeneralKeyAndValue> kysymyksetColor;

    // Scene name values
    private HashSet<String> retrievedSceneNames;

    private int korvaus = -1;
    private boolean debuggi = true;

    // Using custom names in image files
    private String[] backgroundFiles;
    private String selectedBackground;
    private String[] foregroundFiles;
    private String selectedForeground;
    private String[] personFiles;
    private String selectedPerson;
    private String[] faceFiles;
    private String selectedFace;

    // Loading icon for image
    private RelativeLayout loadingPanel, imageLayout;
    private boolean imageVisible;

    private final int BACKGROUND = 1, FOREGROUND = 2, PERSON = 3, FACE = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scene);

        // Load drawable file names for images
        backgroundFiles = getResources().getStringArray(R.array.taustat);
        foregroundFiles = getResources().getStringArray(R.array.kolmasKuva);
        personFiles = getResources().getStringArray(R.array.henkilot);
        faceFiles = getResources().getStringArray(R.array.kasvot);

        this.name = (EditText) findViewById(R.id.scenenNimi);
        this.question = (EditText) findViewById(R.id.kysymys);
        this.background = (Spinner) findViewById(R.id.taustaSpinner);
        this.person = (Spinner) findViewById(R.id.personSpinner);
        this.face = (Spinner) findViewById(R.id.KasvoSpinner);
        this.lista = (ListView) findViewById(R.id.listaVastaukset);
        this.tausta = (ImageView) findViewById(R.id.createBackground);
        this.henkilo = (ImageView) findViewById(R.id.createCharacter);
        this.kasvot = (ImageView) findViewById(R.id.createFace);
        this.foreGroundView = (ImageView) findViewById(R.id.createForeground);
        this.foreground = (Spinner) findViewById(R.id.ForegroundSpinner);

        // Show loading icon
        this.loadingPanel = findViewById(R.id.loadingPanel);
        this.imageLayout = findViewById(R.id.imageLayout);
        loadingPanel.setVisibility(View.VISIBLE);
        imageLayout.setVisibility(View.GONE);

        korvaus = getIntent().getIntExtra("korvaus", -1);
        retrievedSceneNames = new HashSet<>(getIntent().getStringArrayListExtra("createdScenes"));
        boolean paivitaSpinnerit = false;
        Scene apu = null;
        if(getIntent().getBooleanExtra("muokkaus", false)){
            apu = (Scene) getIntent().getParcelableExtra("scene");
            //put values
            this.name.setText(apu.getName());
            kysymyksetGo = apu.getGoList();
            kysymyksetColor = apu.getColorList();
            this.question.setText(apu.getQuestion());
            paivitaSpinnerit = true;
        } else{
            if(getIntent().getBooleanExtra("eka", false)){
                this.name.setText("first");
            }
            kysymyksetGo = new LinkedList<>();
            kysymyksetColor = new LinkedList<>();
        }

        ArrayAdapter<CharSequence> adapterTaustat = ArrayAdapter.createFromResource(this,
                R.array.taustatName, R.layout.custom_spinner);
        adapterTaustat.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        this.background.setAdapter(adapterTaustat);

        ArrayAdapter<CharSequence> adapterHenkilot = ArrayAdapter.createFromResource(this,
                R.array.henkilotName, R.layout.custom_spinner);
        adapterHenkilot.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        this.person.setAdapter(adapterHenkilot);

        ArrayAdapter<CharSequence> adapterKasvot = ArrayAdapter.createFromResource(this,
                R.array.kasvotName, R.layout.custom_spinner);
        adapterKasvot.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        this.face.setAdapter(adapterKasvot);

        ArrayAdapter<CharSequence> adapterForeground = ArrayAdapter.createFromResource(this,
                R.array.kolmasKuvaName, R.layout.custom_spinner);
        adapterForeground.setDropDownViewResource(R.layout.custom_spinner_dropdown);

        this.foreground.setAdapter(adapterForeground);

        // Set listeners for image spinners
        this.background.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages(BACKGROUND);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        this.foreground.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages(FOREGROUND);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        this.face.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages(FACE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        this.person.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateImages(PERSON);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //updates
        updateListOfAnswers();
        if(paivitaSpinnerit){
            updateSpinners(apu);
        } else {
            // User is creating new scene, load spinner positions
            this.background.setSelection(GlobalPrefs.loadBackgroundPos());
            this.foreground.setSelection(GlobalPrefs.loadForegroundPos());
            this.person.setSelection(GlobalPrefs.loadPersonPos());
            this.face.setSelection(GlobalPrefs.loadFacePos());
        }
    }

    private void updateSpinners(Scene apu) {
        if(apu != null) {
            if(debuggi){
                Log.e("scene", apu.toString());
            }
            String faceKuva = apu.getFace();
            String backKuva = apu.getBackground();
            String foreKuva = apu.getForeground();
            String hahmoKuva = apu.getPerson();
            if(debuggi){
                Log.e("scene", "face: " + faceKuva + " back: " + backKuva + " fore: " + foreKuva + " hahmo: " + hahmoKuva);
            }

            // Background
            int paikka = 0;
            for(int lap=0; lap < this.backgroundFiles.length; lap++){
                if(this.backgroundFiles[lap].equals(backKuva)){
                    paikka = lap;
                    break;
                }
            }
            this.background.setSelection(paikka);

            // Foreground
            paikka = 0;
            for(int lap=0; lap < this.foregroundFiles.length; lap++){
                if(this.foregroundFiles[lap].equals(foreKuva)){
                    paikka = lap;
                    break;
                } else if(this.foregroundFiles[lap].equals("empty") || this.foregroundFiles[lap].equals("tyhjä")){
                    paikka = lap;
                    break;
                }
            }
            this.foreground.setSelection(paikka);

            // Person
            paikka = 0;
            for(int lap=0; lap < this.personFiles.length; lap++){
                if(this.personFiles[lap].equals(hahmoKuva)){
                    paikka = lap;
                    break;
                } else if(this.personFiles[lap].equals("empty") || this.personFiles[lap].equals("tyhjä")){
                    paikka = lap;
                    break;
                }
            }
            this.person.setSelection(paikka);

            // Face
            paikka = 0;
            for(int lap=0; lap < this.faceFiles.length; lap++){
                if(this.faceFiles[lap].equals(faceKuva)){
                    paikka = lap;
                    break;
                } else if(this.faceFiles[lap].equals("empty") || this.faceFiles[lap].equals("tyhjä")){
                    paikka = lap;
                    break;
                }
            }
            this.face.setSelection(paikka);
        }
    }

    /**
     * This method update scene preview images.
     */
    private void updateImages(int image) {
        switch (image) {
            case BACKGROUND:
                // Get correct file from the spinner position
                selectedBackground = backgroundFiles[this.background.getSelectedItemPosition()];
                // Set image as the file
                this.tausta.setImageResource(getResources().getIdentifier(selectedBackground,
                        "drawable", getPackageName()));
                // Save spinner position
                GlobalPrefs.saveBackgroundPos(this.background.getSelectedItemPosition());
                break;
            case FOREGROUND:
                // Get correct file from the spinner position
                selectedForeground = foregroundFiles[this.foreground.getSelectedItemPosition()];
                // Set image as the file (hide if value is null)
                if(!selectedForeground.equals("null") && !selectedForeground.equals("tyhjä") &&
                        !selectedForeground.equals("empty")) {
                    this.foreGroundView.setImageResource(getResources().getIdentifier(
                            selectedForeground, "drawable", getPackageName()));
                } else {
                    this.foreGroundView.setImageResource(android.R.color.transparent);
                }
                // Save spinner position
                GlobalPrefs.saveForegroundPos(this.foreground.getSelectedItemPosition());
                break;
            case PERSON:
                // Get correct file from the spinner position
                selectedPerson = personFiles[this.person.getSelectedItemPosition()];
                // Set image as the file (hide if value is null)
                if(!selectedPerson.equals("null") && !selectedPerson.equals("tyhjä") &&
                        !selectedPerson.equals("empty")) {
                    this.henkilo.setImageResource(getResources().getIdentifier(selectedPerson,
                            "drawable", getPackageName()));
                } else{
                    this.henkilo.setImageResource(android.R.color.transparent);
                    //null value must be last
                    int faceNullPosition = getResources().getStringArray(R.array.kasvot).length - 1;
                    this.face.setSelection(faceNullPosition);
                }
                // Save spinner position
                GlobalPrefs.savePersonPos(this.person.getSelectedItemPosition());
                break;
            case FACE:
                // Get correct file from the spinner position
                selectedFace = faceFiles[this.face.getSelectedItemPosition()];
                // Set image as the file (hide if value is null)
                if(!selectedFace.equals("null") && !selectedFace.equals("tyhjä") &&
                        !selectedFace.equals("empty")) {
                    this.kasvot.setImageResource(getResources().getIdentifier(selectedFace,
                            "drawable", getPackageName()));
                } else {
                    this.kasvot.setImageResource(android.R.color.transparent);
                }
                // Save spinner position
                GlobalPrefs.saveFacePos(this.face.getSelectedItemPosition());
                break;
        }

        // Show loaded images if not already shown
        if (!imageVisible) {
            imageVisible = true;
            loadingPanel.setVisibility(View.GONE);
            imageLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Shows answers in a list.
     */
    public void updateListOfAnswers(){
        ArrayList<Answer> arrayList = new ArrayList<>();
        for(int lap=0; lap < kysymyksetGo.size(); lap++){
            arrayList.add(new Answer(this, kysymyksetGo.get(lap).getKey(),kysymyksetGo.get(lap).getValue(), kysymyksetColor.get(lap).getValue()));
        }
        CreateAnswerAdapter adapter = new CreateAnswerAdapter(this, arrayList, this);
        lista.setAdapter(adapter);
    }

    public void vastaus(View view) {
        openAnswerDialog(false, -1);
    }

    public void pois(View view) {
        if(allDataGiven()) {
            try {
                String nimi = this.name.getText().toString();
                String kysymys = this.question.getText().toString();
                String tausta = selectedBackground;
                String henkilo = selectedPerson;
                String kasvo = selectedFace;
                String foree = selectedForeground;
                if(henkilo.equals("tyhjä") || henkilo.equals("empty")){
                    henkilo = "null";
                }
                if(kasvo.equals("tyhjä") || kasvo.equals("empty")){
                    kasvo = "null";
                }
                if(foree.equals("tyhjä") || foree.equals("empty")){
                    foree = "null";
                }
                if(nimi.trim().isEmpty()){
                    nimi = "first";
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
        if(this.question.getText().toString().trim().isEmpty()){
            dataNotGivenAlert(getString(R.string.remember_question), getString(R.string.not_added));
            return false;
        } else if(this.name.getText().toString().trim().equals("null")){
            dataNotGivenAlert(getString(R.string.not_null), getString(R.string.not_added));
            return false;
        } else if(this.kysymyksetGo.size() < 1){
            dataNotGivenAlert(getString(R.string.remember_answers), getString(R.string.not_added));
            return false;
        }
        return true;
    }
    private void dataNotGivenAlert(String content, String toastMessage){
        // Show content of what data is missing in alert dialog
        Helper.showAlert(this, getString(R.string.huom), content,
                getString(android.R.string.yes), Helper.HIDE_NEGATIVE_BUTTON,
                () -> Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show(),
                null);
    }

    /**
     * Opens answer dialog.
     * @param editOldAnswer true if editing old answer, false if creating new answer
     * @param position positions of the answer to edit
     */
    private void openAnswerDialog(boolean editOldAnswer, int position) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("muokkaus", editOldAnswer);
        bundle.putInt("koko", kysymyksetColor.size());
        bundle.putInt("korvaa", position); // If not editing, position is -1

        // If editing old answer
        if (editOldAnswer) {
            GeneralKeyAndValue meno = kysymyksetGo.get(position);
            String me = meno.getValue();
            if(me.equals("null")){
                me = "";
            }
            bundle.putString("menee", me);
            bundle.putString("kysymys", meno.getKey());

            GeneralKeyAndValue color = kysymyksetColor.get(position);
            bundle.putString("color", color.getValue());
        }

        // For some reason sceneNames gets duplicated if I pass the class variable
        // ArrayList of sceneNames, DialogFragment is probably the reason why it happens
        ArrayList<String> sceneNames = new ArrayList<>(retrievedSceneNames);
        sceneNames.add(name.getText().toString());

        // Pass all the created scenes to AnswerDialog
        bundle.putStringArrayList("createdScenes", sceneNames);

        CreateAnswerDialogFragment dia = new CreateAnswerDialogFragment();
        dia.setArguments(bundle);
        dia.show(getSupportFragmentManager(), "vastauksen luonti");
    }

    /**
     * Listens to answer button clicks.
     * @param v answer button
     */
    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        openAnswerDialog(true, position);
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
        return true;
    }
}
