package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.SaveSystemPreferences;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateScenario extends AppCompatActivity {
    private List<Scene> list = new ArrayList<>();
    private EditText scenarioName;
    private ListView listaview;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load font theme
        getTheme().applyStyle(GlobalPrefs.getFontStyle().getResId(), true);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_scenario);
        this.scenarioName = (EditText) findViewById(R.id.scenenNimi);
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
        }
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
            SharedPreferences test = getSharedPreferences("scenarios", MODE_PRIVATE);
            SaveSystemPreferences json = new SaveSystemPreferences(this, test);
            json.saveScenario(scenario);
        } catch (Exception e){
            Toast.makeText(this, "Varmista, että olet antanut kaikki tiedot", Toast.LENGTH_LONG).show();
        }
    }
}
