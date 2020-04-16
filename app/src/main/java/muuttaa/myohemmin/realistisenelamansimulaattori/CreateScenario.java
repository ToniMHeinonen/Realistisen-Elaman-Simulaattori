package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;
import muuttaa.myohemmin.realistisenelamansimulaattori.tools.GlobalPrefs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private boolean debuggi = true;
    private String outName = "";
    private boolean editMode = false;
    private SaveSystemPreferences saveSystem;

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
        }
        updateList();
    }

    public void luo(View view) {
        Intent i = new Intent(CreateScenario.this, CreateScene.class);
        boolean onko = list.size() == 0;
        i.putExtra("eka", onko);
        i.putExtra("muokkaus", false);
        i.putExtra("korvaus", -1);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int k = data.getIntExtra("korvaus", -1);
                if(k == -1) {
                    list.add((Scene) data.getParcelableExtra("scene"));
                    updateList();
                } else{
                    list.set(k, (Scene) data.getParcelableExtra("scene"));
                    updateList();
                }
            }
        } else if(requestCode == 22){
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
        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList);
        listaview.setAdapter(adapter);
        final Context con = this;
        listaview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Scene scene = list.get(position);
                new AlertDialog.Builder(con)
                        .setTitle(con.getString(R.string.huom))
                        .setMessage(con.getString(R.string.remove_item))
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                list.remove(scene);
                                updateList();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return false;
            }
        });
        listaview.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(CreateScenario.this, CreateScene.class);
            boolean onko = list.size() == 0;
            i.putExtra("eka", onko);
            i.putExtra("muokkaus", true);
            i.putExtra("scene", list.get(position));
            i.putExtra("korvaus", position);
            startActivityForResult(i, 1);
        });
    }

    public void lisaa(View view) {
        if(allDataGiven()) {
            String name = scenarioName.getText().toString().toLowerCase();
            SaveSystemPreferences json = new SaveSystemPreferences(this);
            final Context con = this;
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
                    scenario.setFileName(korjattu + ".json");
                    json.saveScenario(scenario, true);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.duplicate_warning), Toast.LENGTH_LONG).show();
                }
            } else {
                try {
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
                    scenario.setFileName(korjattu + ".json");
                    json.saveScenario(scenario, false);
                    Toast.makeText(this, getString(R.string.saved_scenario), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, ChooseScenarioActivity.class));
                    finish();
                } catch (Exception e) {
                    Toast.makeText(this, this.getString(R.string.is_all_data_given), Toast.LENGTH_LONG).show();
                    if (debuggi) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void laheta(View view) throws IOException {
        if(allDataGiven()) {
            Scenario scenario = new Scenario();
            scenario.setListaus(list);
            String name = scenarioName.getText().toString().toLowerCase();
            scenario.setName(name);
            String korjattu = "";
            for (int lap = 0; lap < name.length(); lap++) {
                char m = name.charAt(lap);
                if (m != ' ') {
                    korjattu += m;
                }
            }
            String file = korjattu + ".json";
            scenario.setFileName(file);
            SaveSystemPreferences json = new SaveSystemPreferences(this);
            if (json.containsAlready(name)) {
                if (editMode) {
                    json.saveScenario(scenario, true);
                } else {
                    Toast.makeText(this, getString(R.string.duplicate_warning), Toast.LENGTH_LONG).show();
                }
            } else {
                json.saveScenario(scenario, false);
            }
            Toast.makeText(this, this.getString(R.string.file_saved), Toast.LENGTH_LONG).show();

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
        }
    }

    public void importtaus(View view) {
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
                outName = input.getText().toString().toLowerCase();
                if(new SaveSystemPreferences(con).containsAlready(outName)) {
                    new AlertDialog.Builder(con)
                            .setTitle(con.getString(R.string.huom))
                            .setMessage(con.getString(R.string.duplicate_warning))
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(con, con.getString(R.string.not_added), Toast.LENGTH_LONG).show();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else{
                    if (outName == null || outName.isEmpty()) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                        outName = sdf.format(new Date());
                    }
                    new AlertDialog.Builder(con)
                            .setTitle(con.getString(R.string.huom))
                            .setMessage(con.getString(R.string.file_types))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                    i.setType("*/*");
                                    startActivityForResult(i, 22);
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
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
    }
    private boolean allDataGiven(){
        String nimi = this.scenarioName.getText().toString();
        int montakoScenea = this.list.size();
        if(montakoScenea < 1){
            dataNotGivenAlert(getString(R.string.not_enough_scene), getString(R.string.not_added));
            return false;
        } else if(nimi.isEmpty()){
            dataNotGivenAlert(getString(R.string.remember_name), getString(R.string.not_added));
            return false;
        } else if(saveSystem.containsAlready(nimi)){
            dataNotGivenAlert(getString(R.string.unique_name), getString(R.string.not_added));
            return false;
        }
        return true;
    }

    private void dataNotGivenAlert(String content, String toastMessage){
        Context co = this;
        new AlertDialog.Builder(this)
                .setTitle(this.getString(R.string.huom))
                .setMessage(content)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(co, toastMessage, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
