package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import muuttaa.myohemmin.realistisenelamansimulaattori.JsonInterface;
import muuttaa.myohemmin.realistisenelamansimulaattori.R;

/**
 * This class read data from json file in resource and preferences
 * @author Jesse Stenroth
 */
public class SaveSystemPreferences implements JsonInterface {
    private Context context;
    private boolean debuggi = true;
    private List<String> list;
    private String scenarie;
    private String scenarioName;
    private JSONObject rootInScenario;
    private boolean run = false;
    private boolean preferencessa = false;
    private boolean userCreatedScenario = false;

    public SaveSystemPreferences(Context con){
        this.context = con;
        this.list = new LinkedList<>();
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
        this.userCreatedScenario = false;
    }

    @Override
    public void setCurrentScenario(String scenario, boolean user) {
        this.scenarie = scenario;
        this.userCreatedScenario = user;
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

    @Override
    public String getForegroundPicture() {
        createJSONObjectOfScenario();
        String out = null;
        try {
            JSONObject base = this.rootInScenario;
            out = base.getJSONObject(this.scenarioName).getString("fore");
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
                //test
                if(userCreatedScenario){
                    String h1 = getJsonName();
                    String helpp = "";
                    for (int ind = 0; ind < (h1.length() - 5); ind++) {
                        helpp += h1.charAt(ind);
                    }
                    //haku
                    String filet = getStringFromFile(getJsonName());
                    try {
                        this.rootInScenario = new JSONObject(filet);
                    } catch (JSONException e) {
                        if (debuggi) {
                            Log.e("ScenarioFile", "preferencesta ei saatu dataa");
                            e.printStackTrace();
                        }
                    }
                    preferencessa = false;
                } else {
                    if (!preferencessa) {
                        try {
                            this.rootInScenario = new JSONObject(data);
                        } catch (JSONException e) {
                            if (debuggi) {
                                Log.e("ScenarioFile", "tiedostosta ei saati dataa");
                                e.printStackTrace();
                            }
                        }
                    } else {
                        String h1 = getJsonName();
                        String helpp = "";
                        for (int ind = 0; ind < (h1.length() - 5); ind++) {
                            helpp += h1.charAt(ind);
                        }
                        //haku
                        String filet = getStringFromFile(getJsonName());
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
    }

    /**
     * This class help to create list of json and preferences (contains basic information)
     */
    private class scenarioListHelp{
        private String n;
        private String f;

        /**
         * This constructor set name and filename
         * @param nimi name
         * @param filee file name
         */
        public scenarioListHelp(String nimi, String filee){
            n = nimi;
            f = filee;
        }

        /**
         * return name
         * @return name without .json end
         */
        public String getName(){
            return n;
        }

        /**
         * return file name
         * @return file name contains .json end
         */
        public String getFile(){
            return f;
        }
    }

    /**
     * Get data from json file and preferences
     * @return json data in String format
     */
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
        try{
            String data = getStringFromFile("savedata2.json");
            if(data != null) {
                JSONObject b = new JSONObject(data);
                JSONArray array2 = b.getJSONArray("scenarioslist");
                for (int lap = 0; lap < array2.length(); lap++) {
                    JSONObject obj = array2.getJSONObject(lap);
                    scenaariot.add(new scenarioListHelp(obj.getString("name"), obj.getString("file")));
                }
                JSONArray names2 = b.getJSONArray("scenarios");
                for (int lap = 0; lap < names2.length(); lap++) {
                    scenarioNames.add(names2.getString(lap));
                }
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
        System.out.println("out: " + out);
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
     * This method get information from file
     * @param resources resource where get
     * @return String what contains data in file
     */
    private String getStringFromScenariesFile(Resources resources){
        String out = "";
        try {
            InputStream test = resources.openRawResource(R.raw.savedata);
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
    /**
     * Scenario saving
     * @param scenario Scenario class
     * @param edit true if is edit mode
     * @param beforeName name what was in scenario before
     */
    public void saveScenario(Scenario scenario, boolean edit, String beforeName){
        try {
            if(debuggi){
                Log.e("saveScenario", "edit = " + edit);
            }
            //convert data to json
            String file = scenario.getFileName();
            //muunnetaan scenario jsoniksi
            JSONObject base = new JSONObject();
            List<Scene> li = scenario.getListaus();
            for(int lap=0; lap < li.size(); lap++){
                Scene scene = li.get(lap);
                JSONObject help = new JSONObject();
                help.put("question", scene.getQuestion());
                help.put("background", scene.getBackground());
                help.put("person", scene.getPerson());
                help.put("face", scene.getFace());
                help.put("fore", scene.getForeground());
                JSONArray array = new JSONArray();
                String[] l = scene.getAnswers();
                for (int k=0; k < l.length; k++){
                    array.put(l[k]);
                }
                help.put("answers", array);
                List<GeneralKeyAndValue> go = scene.getGoList();
                for(int k=0; k < go.size(); k++){
                    help.put(go.get(k).getKey(), go.get(k).getValue());
                }
                List<GeneralKeyAndValue> color = scene.getColorList();
                for(int k=0; k < color.size(); k++){
                    help.put(color.get(k).getKey(), color.get(k).getValue());
                }
                base.put(scene.getName(), help);
            }
            String scenarioName2 = scenario.getName();
            String out = base.toString();
            //kirjoitus
            write(file, out);
            boolean already = containsAlready(scenarioName2);
            if((!edit && !already) || !already) {
                if(debuggi){
                    Log.e("tallennus", "savedata");
                }
                writeSaveData(file, scenarioName2);
            }
            if(edit){
                //if scenario name was changed
                if(debuggi){
                    Log.e("muokattu nimeaminen", "edit= " + edit + " toka if = " + !beforeName.equals(scenario.getName()) + ". beforeName = " + beforeName + " scenario name = " + scenarioName2);
                }
                if(!beforeName.trim().isEmpty()) {
                    if (!beforeName.equals(scenarioName2)) {
                        if(debuggi){
                            Log.e("ilmoitus", "deletet mukana");
                        }
                        writeSaveData(file, scenarioName2);
                        deleteScenario(beforeName);
                    }
                }
            }
        } catch (Exception e){
            if(debuggi){
                e.printStackTrace();
            }
        }
    }

    private void writeSaveData(String file, String scenarioName2) {
        //yritetään lukea onko savedata2
        String tiedosto = getStringFromFile("savedata2.json");
        //vika täällä
        System.out.println("testaus: " + tiedosto);
        if(tiedosto == null){
            try {
                JSONObject obj = new JSONObject();
                JSONArray array1 = new JSONArray();
                array1.put(scenarioName2);
                obj.put("scenarios", array1);
                JSONObject h = new JSONObject();
                h.put("name", scenarioName2);
                h.put("file", file);
                JSONArray array2 = new JSONArray();
                array2.put(h);
                obj.put("scenarioslist", array2);
                write("savedata2.json", obj.toString());
            } catch (JSONException e){
                if(debuggi){
                    e.printStackTrace();
                }
            }
        } else{
            try {
                JSONObject obj = new JSONObject(tiedosto);
                obj.getJSONArray("scenarios").put(scenarioName2);
                JSONObject h = new JSONObject();
                h.put("name", scenarioName2);
                h.put("file", file);
                obj.getJSONArray("scenarioslist").put(h);
                write("savedata2.json", obj.toString());
            } catch (JSONException e){
                if(debuggi){
                    e.printStackTrace();
                }
            }
        }
    }

    private void write(String file, String con){
        try {
            FileOutputStream fos = context.openFileOutput(file, Context.MODE_PRIVATE);
            DataOutputStream o = new DataOutputStream(fos);
            Writer w = new BufferedWriter(new OutputStreamWriter(o, StandardCharsets.UTF_8));
            w.write(con);
            w.flush();
            w.close();
        }catch (Exception e){
            if (debuggi){
                e.printStackTrace();
            }
        }
    }
    private String getStringFromFile(String file){
        try {
            FileInputStream fis = context.openFileInput(file);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuilder total = new StringBuilder();
            for (String line; (line = br.readLine()) != null; ) {
                total.append(line).append('\n');
            }
            br.close();
            if(debuggi){
                Log.e("ulos", total.toString());
            }
            return total.toString();

        } catch (Exception e){
            if(debuggi){
                e.printStackTrace();
            }
        }
        return null;
    }

    public Scenario saveScenarioFromString(String in, String name){
        String korjattu = "";
        for(int lap=0; lap < name.length(); lap++){
            char m = name.charAt(lap);
            if(m != ' '){
                korjattu += m;
            }
        }
        write(korjattu.toLowerCase() + ".json", in);
        writeSaveData(korjattu.toLowerCase() + ".json", name);
        return null;
    }
    public String convertScenarioToString(Scenario scenario){
        try {
            JSONObject base = new JSONObject();
            List<Scene> li = scenario.getListaus();
            for (int lap = 0; lap < li.size(); lap++) {
                Scene scene = li.get(lap);
                JSONObject help = new JSONObject();
                help.put("question", scene.getQuestion());
                help.put("background", scene.getBackground());
                help.put("person", scene.getPerson());
                help.put("face", scene.getFace());
                help.put("fore", scene.getForeground());
                JSONArray array = new JSONArray();
                String[] l = scene.getAnswers();
                for (int k = 0; k < l.length; k++) {
                    array.put(l[k]);
                }
                help.put("answers", array);
                List<GeneralKeyAndValue> go = scene.getGoList();
                for (int k = 0; k < go.size(); k++) {
                    help.put(go.get(k).getKey(), go.get(k).getValue());
                }
                List<GeneralKeyAndValue> color = scene.getColorList();
                for (int k = 0; k < color.size(); k++) {
                    help.put(color.get(k).getKey(), color.get(k).getValue());
                }
                base.put(scene.getName(), help);
            }
            return base.toString();
        } catch (JSONException e){
            if(debuggi){
                e.printStackTrace();
            }
        }
        return null;
    }
    public boolean containsAlready(String nimi){
        List<scenarioListHelp> scenaariot = new ArrayList<>();
        List<String> scenarioNames = new ArrayList<>();
        String out = "";
        //scenarios from file
        //english
        String g = getStringFromScenariesFile(getLocalizedResources(context, new Locale("en")));
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
        //finnish
        String f = getStringFromScenariesFile(getLocalizedResources(context, new Locale("fi")));
        try {
            JSONObject json = new JSONObject(f);
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
        try{
            String data = getStringFromFile("savedata2.json");
            if(data != null) {
                JSONObject b = new JSONObject(data);
                JSONArray array2 = b.getJSONArray("scenarioslist");
                for (int lap = 0; lap < array2.length(); lap++) {
                    JSONObject obj = array2.getJSONObject(lap);
                    scenaariot.add(new scenarioListHelp(obj.getString("name"), obj.getString("file")));
                }
                JSONArray names2 = b.getJSONArray("scenarios");
                for (int lap = 0; lap < names2.length(); lap++) {
                    scenarioNames.add(names2.getString(lap));
                }
            }
        } catch (JSONException e){
            if(debuggi){
                e.printStackTrace();
            }
        }

        return scenarioNames.contains(nimi);
    }
    public boolean alreadyInResources(String nimi){
        List<scenarioListHelp> scenaariot = new ArrayList<>();
        List<String> scenarioNames = new ArrayList<>();
        String out = "";
        //scenarios from file
        //english
        String g = getStringFromScenariesFile(getLocalizedResources(context, new Locale("en")));
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
        //finnish
        String f = getStringFromScenariesFile(getLocalizedResources(context, new Locale("fi")));
        try {
            JSONObject json = new JSONObject(f);
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
        if(debuggi){
            String tu = "";
            for(int lap=0; lap < scenarioNames.size(); lap++){
                tu += " [ " + scenarioNames.get(lap) + " ]";
            }
            Log.e("testi", "listalla: " + tu);
        }
        return scenarioNames.contains(nimi);
    }

    /**
     * This method you can delete scenario from memory
     * @param nameOfScenario scenario name (not .json name)
     */
    public void deleteScenario(String nameOfScenario){
        try{
            String data = getStringFromFile("savedata2.json");
            if(data != null){
                JSONObject b = new JSONObject(data);
                JSONArray array = b.getJSONArray("scenarioslist");
                String file = "";
                int paikka = 0;
                for(int lap=0; lap < array.length(); lap++){
                    JSONObject vali = array.getJSONObject(lap);
                    if(vali.getString("name").equals(nameOfScenario)){
                        file = vali.getString("file");
                        paikka = lap;
                        break;
                    }
                }
                //deletion
                array.remove(paikka);
                JSONArray names = b.getJSONArray("scenarios");
                int apuPaikka = 0;
                for(int lap=0; lap < names.length(); lap++){
                    if(names.getString(lap).equals(nameOfScenario)){
                        apuPaikka = lap;
                        break;
                    }
                }
                names.remove(apuPaikka);
                write("savedata2.json", b.toString());
                File tiedosto = new File(file);
                tiedosto.delete();
            }
        } catch (JSONException e){
            if(debuggi){
                e.printStackTrace();
            }
        }
    }

    /**
     * work only user's own scenarios
     * @param name name of scenario
     * @return json version of name
     */
    public String getScenarioFileName(String name){
        try{
            String data = getStringFromFile("savedata2.json");
            JSONObject base = new JSONObject(data);
            JSONArray array = base.getJSONArray("scenarioslist");
            String out = "";
            for(int lap=0; lap < array.length(); lap++){
                if(array.getJSONObject(lap).getString("name").equals(name)){
                   out = array.getJSONObject(lap).getString("file");
                   break;
                }
            }
            return out;
        } catch (JSONException e){
            if(debuggi){
                e.printStackTrace();
            }
        }
        return null;
    }
    /**
     * get data in scenario class
     */
    public Scenario getDataOfScenario(String namee, String filu){
        try {
            String data = getStringFromFile(filu);
            JSONObject base = new JSONObject(data);
            Iterator<String> keys = base.keys();
            List<Scene> scenes = new ArrayList<>();
            while( keys.hasNext() ){
                String key = keys.next();
                JSONObject apu = base.getJSONObject(key);
                String question = apu.getString("question");
                String back = apu.getString("background");
                String per = apu.getString("person");
                String fac = apu.getString("face");
                String fore = apu.getString("fore");
                JSONArray array = apu.getJSONArray("answers");
                String[] ans = new String[array.length()];
                List<GeneralKeyAndValue> goList = new LinkedList<>();
                List<GeneralKeyAndValue> colorList = new LinkedList<>();
                for(int lap=0; lap < array.length(); lap++){
                    String arvo = array.get(lap).toString();
                    ans[lap] = arvo;
                    if(debuggi){
                        Log.d("array", arvo);
                    }
                    //color and go info
                    goList.add(new GeneralKeyAndValue(arvo, apu.getString(arvo)));
                    colorList.add(new GeneralKeyAndValue(arvo + "Color", apu.getString(arvo + "Color")));
                }
                Scene aut = new Scene(key, question, back, per, fac, ans, goList, colorList);
                aut.setForeground(fore);
                scenes.add(aut);
            }
            Scenario scenario = new Scenario();
            scenario.setListaus(scenes);
            scenario.setName(namee);
            scenario.setFileName(filu);
            return scenario;
        }catch (JSONException e){
            if (debuggi){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * This method check if scenario is user's created
     * @param name name of scenario
     * @return true if it is user's. false if it is only in res folder in app
     */
    public boolean checkIfScenarioIsUserCreated(String name){
        String g = getStringFromFile("savedata2.json");

        // If user has not created own scenarios yet, g is null
        if (g == null)
            return false;

        boolean oliko = false;
        //scenarios from file
        try {
            JSONObject json = new JSONObject(g);
            JSONArray names = json.getJSONArray("scenarios");
            for(int lap=0; lap < names.length(); lap++){
                String check = names.getString(lap);
                if(check.equals(name)){
                    oliko = true;
                    break;
                }
            }
        } catch (JSONException e){
            if(debuggi){
                e.printStackTrace();
            }
        }
        return oliko;
    }
    private Resources getLocalizedResources(Context context, Locale desiredLocale) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(desiredLocale);
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }
}
