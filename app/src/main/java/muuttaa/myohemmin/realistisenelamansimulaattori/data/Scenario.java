package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import java.util.LinkedList;
import java.util.List;

public class Scenario {
    private List<Scene> listaus;
    private String name;
    private String fileName;
    public Scenario(){
        this.listaus = new LinkedList<>();
    }

    public List<Scene> getListaus() {
        return listaus;
    }

    public void setListaus(List<Scene> listaus) {
        this.listaus = listaus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
