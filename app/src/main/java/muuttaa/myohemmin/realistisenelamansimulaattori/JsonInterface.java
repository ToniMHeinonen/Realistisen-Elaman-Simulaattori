package muuttaa.myohemmin.realistisenelamansimulaattori;

import java.util.List;

public interface JsonInterface {
    public List getScenarioList();
    public List getAnswersList();
    public String getPictureName();
    public String getJsonName();
    public void setFirstSceneFromScenario();
    public void nextScene(String selectionAnswer);
    public void setCurrentScenario(String scenario);
}
