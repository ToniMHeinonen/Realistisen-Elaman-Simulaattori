package muuttaa.myohemmin.realistisenelamansimulaattori;

import androidx.appcompat.app.AppCompatActivity;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scenario;
import muuttaa.myohemmin.realistisenelamansimulaattori.data.Scene;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateScenario extends AppCompatActivity {
    private ArrayList<Scene> list = new ArrayList<>();
    private EditText scenarioName;
    private ListView listaview;

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
                list.add((Scene) data.getSerializableExtra("scene"));
                updateList();
            }
        }
    }
    //update ListView
    private void updateList() {
    }
}
