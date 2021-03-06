package muuttaa.myohemmin.realistisenelamansimulaattori;

import java.util.List;

public interface JsonInterface {
    public List getScenarioList();
    public List getAnswersList();
    public String getJsonName();
    public void setFirstSceneFromScenario();
    public void nextScene(String selectionAnswer);
    public void setCurrentScenario(String scenario);
    public void setCurrentScenario(String scenario, boolean user);
    public String getQuestionFromScenario();
    public String getCurrentScenario();
    public String getBackgroundPicture();
    public String getPersonPicture();
    public String getFacePicture();
    public boolean endOfScenario();
    public List getColorsInString();
    public String getForegroundPicture();
    public String getCategory(String scenario);
}
