package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import muuttaa.myohemmin.realistisenelamansimulaattori.JsonInterface;

public class SaveSystem implements JsonInterface {
    private String FILENAME = "saveData.json";
    private Context context;
    private boolean debuggi = true;

    public SaveSystem(Context con){
        this.context = con;
    }
    @Override
    public List getScenarioList() {
        return null;
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
    public void nextScene() {

    }

    @Override
    public void setCurrentScenario() {

    }

    private void writeStringToFile(String in){
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            fos.write(in.getBytes());
            fos.close();
        } catch (FileNotFoundException e){
            if(debuggi){
                Log.e("FILE/SaveSystem/write", "Tiedostoa ei löytynyt");
                e.printStackTrace();
            }
        } catch (IOException e){
            if(debuggi){
                Log.e("FILE/SaveSystem/write", "kirjoituksessa ongelmaa");
                e.printStackTrace();
            }
        }
    }
    private String getStringFromFile(){
        String out = "";
        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            int byt;
            while ((byt = fis.read()) != -1) {
                out += (char) byt;
            }
            fis.close();
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
