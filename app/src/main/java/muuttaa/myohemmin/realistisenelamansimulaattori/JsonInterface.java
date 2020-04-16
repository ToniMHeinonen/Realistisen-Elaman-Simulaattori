package muuttaa.myohemmin.realistisenelamansimulaattori;

import java.util.List;

public interface JsonInterface {
    public List getScenarioList();
    public List getAnswersList();
    public String getJsonName();
    public void setFirstSceneFromScenario();
    public void nextScene(String selectionAnswer);
    public void setCurrentScenario(String scenario);
    public String getQuestionFromScenario();
    public String getCurrentScenario();
    public String getBackgroundPicture();
    public String getPersonPicture();
    public String getFacePicture();
    public boolean endOfScenario();
    public List getColorsInString();
    public String getForegroundPicture();
}
