package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains data of one scenario
 * @author Jesse Stenroth
 */
public class Scenario{
    private List<Scene> listaus;
    private String name;
    private String fileName;

    /**
     * basic constructor
     */
    public Scenario(){
        this.listaus = new ArrayList<>();
    }

    /**
     * get list of scenes in scenario
     * @return list of scenes
     */
    public List<Scene> getListaus() {
        return listaus;
    }

    /**
     * This method set list of scenes of scenario
     * @param listaus list of scenes
     */
    public void setListaus(List<Scene> listaus) {
        this.listaus = listaus;
    }

    /**
     * This method return String of scenario name
     * @return scenario name
     */
    public String getName() {
        return name;
    }

    /**
     * This method set name of scenario
     * @param name new scenario name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method return file name of scenario
     * @return scenario file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This method set new scenario file name
     * @param fileName scenario file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String toString(){
        String lis = "[ ";
        for(int lap=0; lap < listaus.size(); lap++){
            lis += listaus.get(lap).toString() + ", ";
        }
        lis += "]";
        return "nimi: " + name + " file: " + fileName + " scenet: " + lis;
    }
}
