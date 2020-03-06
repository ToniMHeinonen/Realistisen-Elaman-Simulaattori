package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import muuttaa.myohemmin.realistisenelamansimulaattori.JsonInterface;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class SaveSystem implements JsonInterface {
    private Context context;
    private boolean debuggi = true;
    private List<String> list;
    private String scenarie;
    private String scenarioName;
    private JSONObject rootInScenario;

    /**
     * This constructor get Context information
     * example:
     * SaveSystem save = new SaveSystem(this);
     * @param con activity
     */
    public SaveSystem(Context con){
        this.context = con;
        this.list = new LinkedList<>();
        setFirstSceneFromScenario();
    }

    public String getCurrentScenario() {
        return scenarie;
    }

    @Override
    public String getBackgroundPicture() {
        createJSONObjectOfScenario();
        String out = null;
        try {
            JSONObject base = this.rootInScenario;
            out = base.getJSONObject(this.scenarioName).getString("background");
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
        return out;
    }

    @Override
    public String getPersonPicture() {
        createJSONObjectOfScenario();
        String out = null;
        try {
            JSONObject base = this.rootInScenario;
            out = base.getJSONObject(this.scenarioName).getString("person");
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
        return out;
    }

    @Override
    public String getFacePicture() {
        createJSONObjectOfScenario();
        String out = null;
        try {
            JSONObject base = this.rootInScenario;
            out = base.getJSONObject(this.scenarioName).getString("face");
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
        return out;
    }

    /**
     * This method create jsonObject if it is null (not contains already)
     */
    private void createJSONObjectOfScenario(){
        if(rootInScenario == null){
            String data = getStringFromSceneFile();
            try {
                this.rootInScenario = new JSONObject(data);
            } catch (JSONException e){
                if(debuggi){
                    Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public List getScenarioList() {
        list.clear();
        this.rootInScenario = null;
        String data = getStringFromScenariesFile();
        try {
            JSONObject base = new JSONObject(data);
            JSONArray help = base.getJSONArray("scenarios");
            for(int index = 0; index < help.length(); index++){
                String adding = help.getString(index);
                list.add(adding);
            }
        }catch (JSONException e){
            if(debuggi){
                Log.e("JSON/SaveSystem", "Virhe muodostaessa json objektia");
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public List getAnswersList() {
        createJSONObjectOfScenario();
        List<String> list = new LinkedList<>();
        try{
            JSONObject base = this.rootInScenario;
            JSONArray array = base.getJSONObject(this.scenarioName).getJSONArray("answers");
            for(int lap=0; lap < array.length(); lap++){
                list.add(array.getString(lap));
            }
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public String getJsonName() {
        String out = "";
        String data = getStringFromScenariesFile();
        try {
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("scenarioslist");
            for(int lap=0; lap < array.length(); lap++){
                JSONObject obj = array.getJSONObject(lap);
                if(scenarie.equals(obj.getString("name"))){
                    out = obj.getString("file");
                    break;
                }
            }
        } catch (JSONException e){
            if(debuggi){
                Log.e("JSON/SaveSystem", "Virhe muodostaessa json objektia");
                e.printStackTrace();
            }
        }
        return out;
    }

    @Override
    public void setFirstSceneFromScenario() {
        this.scenarioName = "first";
    }

    @Override
    public void nextScene(String selectionAnswer) {
        createJSONObjectOfScenario();
        try{
            JSONObject base = this.rootInScenario;
            this.scenarioName = base.getJSONObject(this.scenarioName).getString(selectionAnswer);
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setCurrentScenario(String scenario) {
        this.scenarie = scenario;
    }

    @Override
    public String getQuestionFromScenario() {
        createJSONObjectOfScenario();
        String out = "";
        try {
            JSONObject base = this.rootInScenario;
            out = base.getJSONObject(this.scenarioName).getString("question");
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
        return out;
    }

    /**
     * This method get information from file
     * @return String what contains data in file
     */
    private String getStringFromScenariesFile(){
        String out = "";
        try {
            InputStream test = context.getResources().openRawResource(R.raw.savedata);
            BufferedReader br = new BufferedReader(new InputStreamReader(test));
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                out += nextLine;
            }
        } catch (FileNotFoundException e){
            if(debuggi){
                Log.e("FILE/SaveSystem/get", "Tiedostoa ei löytynyt");
                e.printStackTrace();
            }
        } catch (IOException e){
            if(debuggi){
                Log.e("FILE/SaveSystem/get", "kirjoituksessa ongelmaa");
                e.printStackTrace();
            }
        }
        return out;
    }

    /**
     * This method get information from scenario file
     * @return string contains data from file
     */
    private String getStringFromSceneFile(){
        String out = "";
        String h1 = getJsonName();
        String helpp = "";
        for(int ind = 0; ind < (h1.length() - 5); ind++){
            helpp += h1.charAt(ind);
        }
        try {
            InputStream test = context.getResources().openRawResource(
                    context.getResources().getIdentifier(helpp, "raw", context.getPackageName()));
            BufferedReader br = new BufferedReader(new InputStreamReader(test));
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                out += nextLine;
            }
        } catch (FileNotFoundException e){
            if(debuggi){
                Log.e("FILE/SaveSystem/get", "Tiedostoa ei löytynyt");
                e.printStackTrace();
            }
        } catch (IOException e){
            if(debuggi){
                Log.e("FILE/SaveSystem/get", "kirjoituksessa ongelmaa");
                e.printStackTrace();
            }
        }
        return out;
    }
}
