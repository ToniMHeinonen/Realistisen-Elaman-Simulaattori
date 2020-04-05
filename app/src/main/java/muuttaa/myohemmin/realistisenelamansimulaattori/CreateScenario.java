package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.loadFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scenario);
        this.scenarioName = (EditText) findViewById(R.id.ScenarioCreateName);
        this.listaview = (ListView) findViewById(R.id.listaScene);
        updateList();
    }

    public void luo(View view) {
        Intent i = new Intent(CreateScenario.this, CreateScene.class);
        startActivityForResult(i, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                list.add((Scene) data.getParcelableExtra("scene"));
                updateList();
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
                    Toast.makeText(this, "Import valmis", Toast.LENGTH_LONG).show();
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
    }

    public void lisaa(View view) {
        try {
            Scenario scenario = new Scenario();
            scenario.setListaus(list);
            String name = scenarioName.getText().toString().toLowerCase();
            scenario.setName(name);
            scenario.setFileName(name + ".json");
            SaveSystemPreferences json = new SaveSystemPreferences(this);
            json.saveScenario(scenario);
            finish();
        } catch (Exception e){
            Toast.makeText(this, "Varmista, että olet antanut kaikki tiedot", Toast.LENGTH_LONG).show();
            if(debuggi){
                e.printStackTrace();
            }
        }
    }

    public void laheta(View view) throws IOException {
        Scenario scenario = new Scenario();
        scenario.setListaus(list);
        String name = scenarioName.getText().toString().toLowerCase();
        scenario.setName(name);
        String file = name + ".json";
        scenario.setFileName(file);
        SaveSystemPreferences json = new SaveSystemPreferences(this);
        json.saveScenario(scenario);
        Toast.makeText(this, "Tiedosto tallennettu", Toast.LENGTH_LONG).show();

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

            startActivity(Intent.createChooser(intentShareFile, "Lähetä Scenario"));
        }
    }

    public void importtaus(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        final Context con = this;

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                outName = input.getText().toString();
                if(outName == null || outName.isEmpty()){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
                    outName = sdf.format(new Date());
                }
                new AlertDialog.Builder(con)
                        .setTitle("Huom!")
                        .setMessage("Valitse vain .json päätteisiä scenaario tiedostoja.")
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
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }




}
