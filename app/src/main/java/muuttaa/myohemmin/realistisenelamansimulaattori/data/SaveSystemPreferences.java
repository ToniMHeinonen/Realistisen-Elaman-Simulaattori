package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import muuttaa.myohemmin.realistisenelamansimulaattori.JsonInterface;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

public class SaveSystemPreferences implements JsonInterface {
    private Context context;
    private boolean debuggi = false;
    private List<String> list;
    private String scenarie;
    private String scenarioName;
    private JSONObject rootInScenario;
    private boolean run = false;
    private SharedPreferences tiedot;
    private boolean preferencessa = false;

    public SaveSystemPreferences(Context con, SharedPreferences pref){
        this.context = con;
        this.list = new LinkedList<>();
        tiedot = pref;
        setFirstSceneFromScenario();
    }
    @Override
    public List getScenarioList() {
        list.clear();
        this.rootInScenario = null;
        String data = getStringFromScenariesFileAndPreferences();
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
        String data = getStringFromScenariesFileAndPreferences();
        try {
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("scenarioslist");
            for(int lap=0; lap < array.length(); lap++){
                String test = array.getString(lap);
                JSONObject obj = new JSONObject(test);
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
        this.run = false;
    }

    @Override
    public void nextScene(String selectionAnswer) {
        createJSONObjectOfScenario();
        try{
            JSONObject base = this.rootInScenario;
            this.scenarioName = base.getJSONObject(this.scenarioName).getString(selectionAnswer);
            if(this.scenarioName.equals("null")){
                this.run = true;
            }
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
            System.out.println("base: " + base);
            out = base.getJSONObject(this.scenarioName).getString("question");
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
        return out;
    }

    @Override
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

    @Override
    public boolean endOfScenario() {
        return run;
    }

    @Override
    public List getColorsInString() {
        List listaus = getAnswersList();
        //convert object list to String list
        List<String> strings = new ArrayList<>(listaus.size());
        for (Object object : listaus) {
            strings.add(object != null ? object.toString() : null);
        }
        createJSONObjectOfScenario();
        List<String> list = new LinkedList<>();
        try{
            JSONObject base = this.rootInScenario.getJSONObject(this.scenarioName);
            for(int lap=0; lap < listaus.size(); lap++){
                String sana = strings.get(lap);
                list.add(base.getString(sana + "Color"));
            }
        } catch (JSONException e){
            if(debuggi){
                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                e.printStackTrace();
            }
        }
        return list;
    }
    /**
     * This method create jsonObject if it is null (not contains already)
     */
    private void createJSONObjectOfScenario(){
        if(rootInScenario == null){
                String data = getStringFromSceneFile();
            if(!preferencessa) {
                try {
                    this.rootInScenario = new JSONObject(data);
                } catch (JSONException e) {
                    if (debuggi) {
                        Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                        e.printStackTrace();
                    }
                }
            } else{
                String h1 = getJsonName();
                String helpp = "";
                for(int ind = 0; ind < (h1.length() - 5); ind++){
                    helpp += h1.charAt(ind);
                }
                int montako = getInteger(helpp + "Size");
                String filet = "";
                for(int lap=0; lap < montako; lap++){
                    filet += getString(helpp + lap);
                }
                try {
                    this.rootInScenario = new JSONObject(filet);
                } catch (JSONException e) {
                    if (debuggi) {
                        Log.e("ScenarioFile", "preferencesta ei saatu dataa");
                        e.printStackTrace();
                    }
                }
                preferencessa = false;
            }
        }
    }
    private class scenarioListHelp{
        private String n;
        private String f;
        public scenarioListHelp(String nimi, String filee){
            n = nimi;
            f = filee;
        }
        public String getName(){
            return n;
        }
        public String getFile(){
            return f;
        }
    }

    private String getStringFromScenariesFileAndPreferences(){
        String g = getStringFromScenariesFile();
        List<scenarioListHelp> scenaariot = new ArrayList<>();
        List<String> scenarioNames = new ArrayList<>();
        String out = "";
        //scenarios from file
        try {
            JSONObject json = new JSONObject(g);
            JSONArray array = json.getJSONArray("scenarioslist");
            for(int lap=0; lap < array.length(); lap++){
                JSONObject obj = array.getJSONObject(lap);
                scenaariot.add(new scenarioListHelp(obj.getString("name"), obj.getString("file")));
            }
            JSONArray names = json.getJSONArray("scenarios");
            for(int lap=0; lap < names.length(); lap++){
                scenarioNames.add(names.getString(lap));
            }
        } catch (JSONException e){
            if(debuggi){
                e.printStackTrace();
            }
        }
        //scenarios from preferences
        try {
            int montako = getInteger("scenaarioita");
            String file = "";
            for (int kk = 0; kk < montako; kk++){
                file += getString("scenario" + kk);
            }
            JSONObject json = new JSONObject(file);
            JSONArray array = json.getJSONArray("scenarioslist");
            for(int lap=0; lap < array.length(); lap++){
                JSONObject obj = array.getJSONObject(lap);
                scenaariot.add(new scenarioListHelp(obj.getString("name"), obj.getString("file")));
            }
            JSONArray names = json.getJSONArray("scenarios");
            for(int lap=0; lap < names.length(); lap++){
                scenarioNames.add(names.getString(lap));
            }
        } catch (JSONException e){
            if(debuggi){
                e.printStackTrace();
            }
        }
        //collect data
        try {
            JSONArray ekaLista = new JSONArray();
            for (int lap = 0; lap < scenarioNames.size(); lap++) {
                ekaLista.put(scenarioNames.get(lap));
            }
            JSONArray tokaLista = new JSONArray();
            for (int lap = 0; lap < scenaariot.size(); lap++) {
                JSONObject object = new JSONObject();
                scenarioListHelp hh = scenaariot.get(lap);
                object.put("name", hh.getName());
                object.put("file", hh.getFile());
                tokaLista.put(object.toString());
            }
            JSONObject apu = new JSONObject();
            apu.put("scenarios", ekaLista);
            apu.put("scenarioslist", tokaLista);
            out = apu.toString();
        } catch (JSONException e){
            if(debuggi){
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
            int apu = context.getResources().getIdentifier(helpp, "raw", context.getPackageName());
            if(apu == 0){
                preferencessa = true;
                return "null";
            }
            InputStream test = context.getResources().openRawResource(apu);
            BufferedReader br = new BufferedReader(new InputStreamReader(test));
            String nextLine;
            while ((nextLine = br.readLine()) != null) {
                out += nextLine;
            }
        } catch (FileNotFoundException e){
            preferencessa = true;
            if(debuggi){
                Log.e("FILE/SaveSystem/get", "Tiedostoa ei löytynyt");
                e.printStackTrace();
            }
            return "null";
        } catch (IOException e){
            preferencessa = true;
            if(debuggi){
                Log.e("FILE/SaveSystem/get", "kirjoituksessa ongelmaa");
                e.printStackTrace();
            }
            return "null";
        }
        return out;
    }
    //put data
    private void sendString(String key, String data){
        SharedPreferences.Editor editor = tiedot.edit();
        editor.putString(key, data);
        editor.commit();
    }
    private String getString(String key){
        return tiedot.getString(key, "{}");
    }
    private void sendInteger(String key, int luku){
        SharedPreferences.Editor editor = tiedot.edit();
        editor.putInt(key, luku);
        editor.commit();
    }
    private int getInteger(String key){
        return tiedot.getInt(key, 0);
    }
}