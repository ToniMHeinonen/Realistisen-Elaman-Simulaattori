package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.PermissionsCheck;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Helper;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.HTMLDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateScenario extends ParentActivity {
    private List<Scene> list = new ArrayList<>();
    private EditText scenarioName;
    private ListView listaview;
    private ArrayAdapter<String> adapter;
    private boolean debuggi = false;
    private String outName = "";
    private boolean editMode = false;
    private SaveSystemPreferences saveSystem;
    private String beforeName = "";
    private boolean writePermissionGiven = false;
    private boolean readPermissionGiven = false;
    private boolean nowEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scenario);
        saveSystem = new SaveSystemPreferences(this);
        this.scenarioName = (EditText) findViewById(R.id.ScenarioCreateName);
        this.listaview = (ListView) findViewById(R.id.listaScene);
        //check if is edit scenario
        Intent i = getIntent();
        if(i.getBooleanExtra("edit", false)){
            editMode = true;
            String name = i.getStringExtra("name");
            SaveSystemPreferences j = new SaveSystemPreferences(this);
            String filu = j.getScenarioFileName(name);
            Scenario oletus = j.getDataOfScenario(name, filu);
            scenarioName.setText(oletus.getName());
            list = oletus.getListaus();
            beforeName = oletus.getName();
            nowEdit = true;
        }
        updateList();

        // Check if to show tutorial dialog
        if (GlobalPrefs.loadTutorialScenario()) {
            showInfo(null);
        }
    }

    public void luo(View view) {
        moveToCreateScene(true, -1);
    }

    /**
     * Moves to Create Scene Activity.
     * @param createNew whether to create new scene or modify existing
     * @param position position of the existing scene in list
     */
    private void moveToCreateScene(boolean createNew, int position) {
        Intent i = new Intent(CreateScenario.this, CreateScene.class);
        boolean onko = list.size() == 0;
        i.putExtra("eka", onko);

        // Create ArrayList of scenes List
        ArrayList<String> sceneNames = new ArrayList<>();

        for (Scene scene : list) {
            sceneNames.add(scene.getName());
        }

        if (createNew) {
            i.putExtra("muokkaus", false);
            i.putExtra("korvaus", -1);
        } else {
            i.putExtra("muokkaus", true);
            i.putExtra("scene", list.get(position));
            // Remove selected scene and add it later in CreateScene,
            // in case user changes the name of scene
            sceneNames.remove(list.get(position).getName());
            i.putExtra("korvaus", position);
        }
        i.putStringArrayListExtra("createdScenes", sceneNames);

        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //after scene creation
            if (resultCode == RESULT_OK) {
                int k = data.getIntExtra("korvaus", -1);
                if(k == -1) {
                    list.add((Scene) data.getParcelableExtra("scene"));
                    updateList();
                } else{
                    list.set(k, (Scene) data.getParcelableExtra("scene"));
                    updateList();
                }
                if(scenarioName.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, getString(R.string.autosave_not), Toast.LENGTH_LONG).show();
                } else {
                    nowEdit = true;
                    basicSave(false);
                }
            }
        } else if(requestCode == 22){
            //after import
            if(resultCode == RESULT_OK){
                try {
                    Uri uri = data.getData();
                    String path = uri.toString();
                    if(debuggi){
                        System.out.println("path: " + path);
                    }
                    SaveSystemPreferences js = new SaveSystemPreferences(this);
                    js.saveScenarioFromString(getStringFromFile(uri), outName);
                    Toast.makeText(this, this.getString(R.string.import_done), Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e){
                    if(debuggi){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResult){
        boolean allOK = true;
        for(int lap=0; lap < grantResult.length; lap++){
            if(grantResult[lap] == PackageManager.PERMISSION_DENIED){
                allOK = false;
                break;
            }
        }
        if(requestCode == 22114){
            //after permission check
            if(grantResult.length > 0 && allOK){
                readPermissionGiven = true;
                writePermissionGiven = true;
            } else{
                readPermissionGiven = false;
                writePermissionGiven = false;
            }
        }
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        // http://www.java2s.com/Code/Java/File-Input-Output/ConvertInputStreamtoString.htm
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        Boolean firstLine = true;
        while ((line = reader.readLine()) != null) {
            if(firstLine){
                sb.append(line);
                firstLine = false;
            } else {
                sb.append("\n").append(line);
            }
        }
        reader.close();
        return sb.toString();
    }

    public String getStringFromFile (Uri filePath) throws IOException {
        InputStream fin = this.getContentResolver().openInputStream(filePath);
        String ret = convertStreamToString(fin);
        fin.close();
        return ret;
    }

    //update ListView
    private void updateList() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for(int lap=0; lap < list.size(); lap++){
            arrayList.add(list.get(lap).getName());
        }
        adapter = new ArrayAdapter<String>(this, R.layout.scene_item, arrayList);
        listaview.setAdapter(adapter);
        final Context con = this;
        //remove function
        listaview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Scene scene = list.get(position);
                Helper.showAlert(con, getString(R.string.huom), getString(R.string.remove_item),
                        getString(android.R.string.yes), getString(android.R.string.no),
                        () -> {
                            list.remove(scene);
                            updateList();
                        }, null);
                return true;
            }
        });
        //modify scene
        listaview.setOnItemClickListener((parent, view, position, id) -> {
            moveToCreateScene(false, position);
        });
    }

    public void showInfo(View v) {
        new HTMLDialog(this, HTMLDialog.HTMLText.SCENARIO).show();
    }

    public void lisaa(View view) {
        basicSave(true);
    }

    /**
     * save scenario
     * @param end if true then close scenario creation
     */
    private void basicSave(boolean end){
        if(allDataGiven()) {
            String name = scenarioName.getText().toString();
            if (StringContainsNumber(name)) {
                Toast.makeText(this, getString(R.string.not_number), Toast.LENGTH_SHORT).show();
            } else if(name.equals("null")){
                Toast.makeText(this, getString(R.string.not_null), Toast.LENGTH_SHORT).show();
            } else {
                SaveSystemPreferences json = new SaveSystemPreferences(this);
                final Context con = this;
                //if scenario contains already that name
                if (json.containsAlready(name)) {
                    if (editMode) {
                        Scenario scenario = new Scenario();
                        scenario.setListaus(list);
                        scenario.setName(name);
                        String korjattu = "";
                        for (int lap = 0; lap < name.length(); lap++) {
                            char m = name.charAt(lap);
                            if (m != ' ') {
                                korjattu += m;
                            }
                        }
                        scenario.setFileName(korjattu.toLowerCase() + ".json");
                        json.saveScenario(scenario, editMode, beforeName);
                        if(end) {
                            finish();
                        }
                    } else {
                        Toast.makeText(this, getString(R.string.duplicate_warning), Toast.LENGTH_LONG).show();
                    }
                } else {
                    try {
                        editMode = true;
                        Scenario scenario = new Scenario();
                        scenario.setListaus(list);
                        scenario.setName(name);
                        String korjattu = "";
                        for (int lap = 0; lap < name.length(); lap++) {
                            char m = name.charAt(lap);
                            if (m != ' ') {
                                korjattu += m;
                            }
                        }
                        scenario.setFileName(korjattu.toLowerCase() + ".json");
                        json.saveScenario(scenario, editMode, beforeName);
                        Toast.makeText(this, getString(R.string.saved_scenario), Toast.LENGTH_LONG).show();
                        if(end) {
                        startActivity(new Intent(this, ChooseScenarioActivity.class));
                            finish();
                        }
                    } catch (Exception e) {
                        Toast.makeText(this, this.getString(R.string.is_all_data_given), Toast.LENGTH_LONG).show();
                        if (debuggi) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void laheta(View view) throws IOException {
        if(allDataGiven()) {
            new PermissionsCheck(this, this).BothPermission(() ->{
                Scenario scenario = new Scenario();
                scenario.setListaus(list);
                String name = scenarioName.getText().toString();
                scenario.setName(name);
                String korjattu = "";
                for (int lap = 0; lap < name.length(); lap++) {
                    char m = name.charAt(lap);
                    if (m != ' ') {
                        korjattu += m;
                    }
                }
                String file = korjattu.toLowerCase() + ".json";
                scenario.setFileName(file);
                //check if name already used
                SaveSystemPreferences json = new SaveSystemPreferences(this);
                if (json.containsAlready(name)) {
                    if (editMode) {
                        json.saveScenario(scenario, editMode, beforeName);
                    } else {
                        Toast.makeText(this, getString(R.string.duplicate_warning), Toast.LENGTH_LONG).show();
                    }
                } else {
                    json.saveScenario(scenario, editMode, beforeName);
                }
                Toast.makeText(this, this.getString(R.string.file_saved), Toast.LENGTH_LONG).show();
                //send option
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                String myFilePath = getFilesDir() + "/" + file;
                File fileWithinMyDir = new File(myFilePath);
                Uri path = FileProvider.getUriForFile(this, "muuttaa.myohemmin.realistisenelamansimulaattori", fileWithinMyDir);
                if (fileWithinMyDir.exists()) {
                    intentShareFile.setType("application/json");
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, path);
                    } else {
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, path);
                    }
                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            file);
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, file);

                    startActivity(Intent.createChooser(intentShareFile, this.getString(R.string.send_scenario)));
                }
            });
        }
    }

    public void importtaus(View view) {
            new PermissionsCheck(this, this).BothPermission(() ->{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(this.getString(R.string.give_new_scenario_name));

                // Set up the input
                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                final Context con = this;
                // Set up the buttons
                builder.setPositiveButton(this.getString(R.string.done), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        outName = input.getText().toString();
                        if (outName.equals("null")) {
                            Helper.showAlert(con, con.getString(R.string.huom),
                                    con.getString(R.string.not_null),
                                    getString(android.R.string.no), Helper.HIDE_NEGATIVE_BUTTON,
                                    () -> Toast.makeText(con, con.getString(R.string.not_added), Toast.LENGTH_LONG).show(),
                                    null);
                        } else {
                            if ((new SaveSystemPreferences(con).containsAlready(outName) || StringContainsNumber(outName))) {
                                Helper.showAlert(con, con.getString(R.string.huom),
                                        con.getString(R.string.duplicate_warning) + " " +
                                                con.getString(R.string.or) + " " +
                                                con.getString(R.string.not_number),
                                        getString(android.R.string.no), Helper.HIDE_NEGATIVE_BUTTON,
                                        () -> Toast.makeText(con, con.getString(R.string.not_added), Toast.LENGTH_LONG).show(),
                                        null);
                            } else {
                                if (outName == null || outName.isEmpty()) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                                    outName = sdf.format(new Date());
                                }
                                Helper.showAlert(con, con.getString(R.string.huom),
                                        con.getString(R.string.file_types),
                                        getString(android.R.string.yes), Helper.HIDE_NEGATIVE_BUTTON,
                                        () -> {
                                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                            i.setType("*/*");
                                            startActivityForResult(i, 22);
                                        },
                                        null);
                            }
                        }
                    }
                });
                builder.setNegativeButton(this.getString(R.string.back_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            });
    }

    /**
     * This method check user was given all data
     * @return true if all data given else false
     */
    private boolean allDataGiven(){
        String nimi = this.scenarioName.getText().toString();
        int montakoScenea = this.list.size();
        boolean onkoFirst = false;
        int montakoFirst = 0;
        for(int lap=0; lap < this.list.size(); lap++){
            if(this.list.get(lap).getName().equals("first")){
                onkoFirst = true;
                montakoFirst++;
            }
        }
        if(montakoScenea < 1){
            dataNotGivenAlert(getString(R.string.not_enough_scene), getString(R.string.not_added));
            return false;
        } else if(nimi.trim().isEmpty()){
            dataNotGivenAlert(getString(R.string.remember_name), getString(R.string.not_added));
            return false;
        } else if(nimi.trim().equals("null")){
            dataNotGivenAlert(getString(R.string.not_null), getString(R.string.not_added));
            return false;
        } else if(!onkoFirst){
            dataNotGivenAlert(getString(R.string.need_first), getString(R.string.not_added));
            return false;
        } else if(montakoFirst > 1){
            dataNotGivenAlert(getString(R.string.too_many_first), getString(R.string.not_added));
            return false;
        } else if((saveSystem.containsAlready(nimi) && !editMode) || saveSystem.alreadyInResources(nimi)){
            dataNotGivenAlert(getString(R.string.unique_name), getString(R.string.not_added));
            return false;
        }
        return true;
    }

    /**
     * This method check if String contains number value
     * @param input String what user want to check
     * @return true if contains number else false
     */
    private boolean StringContainsNumber(String input){
        String numbers = "1234567890";
        boolean oliko = false;
        for(int lap=0; lap < numbers.length(); lap++){
            if(input.contains("" +numbers.charAt(lap))){
                oliko = true;
                break;
            }
        }
        return oliko;
    }
    private void dataNotGivenAlert(String content, String toastMessage){
        Context co = this;
        Helper.showAlert(this, getString(R.string.huom), content,
                getString(android.R.string.yes), Helper.HIDE_NEGATIVE_BUTTON,
                () -> Toast.makeText(co, toastMessage, Toast.LENGTH_LONG).show(),
                null);
    }
}
