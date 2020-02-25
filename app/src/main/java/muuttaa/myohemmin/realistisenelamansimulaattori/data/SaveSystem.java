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
    private String FILENAME = "savedata.json";
    private Context context;
    private boolean debuggi = true;
    private List<String> list;

    /**
     * This constructor get Context information
     * example:
     * SaveSystem save = new SaveSystem(this);
     * @param con activity
     */
    public SaveSystem(Context con){
        this.context = con;
        this.list = new LinkedList<>();
    }
    @Override
    public List getScenarioList() {
        list.clear();
        String data = getStringFromFile();
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
        return null;
    }

    @Override
    public String getPictureName() {
        return null;
    }

    @Override
    public String getJsonName() {
        return null;
    }

    @Override
    public void setFirstSceneFromScenario() {

    }

    @Override
    public void nextScene(String selectionAnswer) {

    }

    @Override
    public void setCurrentScenario(String scenario) {

    }
    private String getStringFromFile(){
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
}
