package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.LinkedList;
import java.util.List;

/**
 *This class contains data of one scene
 * @author Jesse Stenroth
 */
public class Scene implements Parcelable {
    private String name;
    private String question;
    private String background;
    private String person;
    private String face;
    private String foreground = "null";
    private String[] answers;
    private List<GeneralKeyAndValue> goList;
    private List<GeneralKeyAndValue> colorList;

    public Scene(){}

    /**
     * This constructor contains all data what scene need
     * @param name name of scene
     * @param question question of scene
     * @param background background picture name
     * @param person person picture name
     * @param face face picture name
     * @param answers String[] list of answers
     * @param goList list of answer go data
     * @param colorList list of answer color data
     */
    public Scene(String name, String question, String background, String person, String face, String[] answers, List<GeneralKeyAndValue> goList, List<GeneralKeyAndValue> colorList) {
        this.name = name;
        this.question = question;
        this.background = background;
        this.person = person;
        this.face = face;
        this.answers = answers;
        if(goList != null){
            for (int lap=0; lap < goList.size(); lap++){
                addToGoList(goList.get(lap).getKey(), goList.get(lap).getValue());
            }
        }
        if(colorList != null){
            for(int lap=0; lap < colorList.size(); lap++){
                addToColorList(colorList.get(lap).getKey(), colorList.get(lap).getValue());
            }
        }
    }

    /**
     * Copy values from other Scene object.
     * @param otherScene other Scene object
     */
    public Scene(Scene otherScene, String copy) {
        this.name = otherScene.name + copy;
        this.question = otherScene.question;
        this.background = otherScene.background;
        this.person = otherScene.person;
        this.face = otherScene.face;
        this.answers = otherScene.answers;
        this.goList = otherScene.goList;
        this.colorList = otherScene.colorList;
    }

    /**
     * get foregroung picture name
     * @return name of foreground picture
     */
    public String getForeground() {
        if( foreground == null){
            return "null";
        }
        return foreground;
    }

    /**
     * This method set foreground picture name
     * @param foreground picture name
     */
    public void setForeground(String foreground) {
        this.foreground = foreground;
    }

    /**
     * This method return name of scenario
     * @return scenario name
     */
    public String getName() {
        return name;
    }

    /**
     * This method set new scenario name
     * @param name new name of scenario
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method return question of scenario
     * @return scenario question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * This method set new question of scenario
     * @param question new scenario question
     */
    public void setQuestion(String question) {
        this.question = question;
    }
    /**
     * get background picture name
     * @return name of background picture
     */
    public String getBackground() {
        return background;
    }
    /**
     * This method set background picture name
     * @param background picture name
     */
    public void setBackground(String background) {
        this.background = background;
    }
    /**
     * get person picture name
     * @return name of person picture
     */
    public String getPerson() {
        return person;
    }
    /**
     * This method set person picture name
     * @param person picture name
     */
    public void setPerson(String person) {
        this.person = person;
    }
    /**
     * get face picture name
     * @return name of face picture
     */
    public String getFace() {
        return face;
    }
    /**
     * This method set face picture name
     * @param face picture name
     */
    public void setFace(String face) {
        this.face = face;
    }

    /**
     * This method return String[] list of answers
     * @return list of answers
     */
    public String[] getAnswers() {
        return answers;
    }

    /**
     * This method set new answers list
     * @param answers new answers list
     */
    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    /**
     * This method return list of where answers go
     * @return answer go list
     */
    public List<GeneralKeyAndValue> getGoList() {
        return goList;
    }

    /**
     * This method return list of colors of answers
     * @return list of colors and answers
     */
    public List<GeneralKeyAndValue> getColorList() {
        return colorList;
    }

    /**
     * This method add data where answer go
     * @param key answer
     * @param value scene name
     */
    public void addToGoList(String key, String value){
        if(this.goList == null){
            this.goList = new LinkedList<>();
        }
        this.goList.add(new GeneralKeyAndValue(key, value));
    }

    /**
     * This method add new color data of answer
     * @param key answer
     * @param value color data of answer
     */
    public void addToColorList(String key, String value){
        if(this.colorList == null){
            this.colorList = new LinkedList<>();
        }
        this.colorList.add(new GeneralKeyAndValue(key, value));
    }

    public String toString(){
        String vastaukset = "[ ";
        for(int lap=0; lap < answers.length; lap++){
            vastaukset += answers[lap] + ", ";
        }
        vastaukset += "]";
        String go = "[ ";
        for(int lap=0; lap < goList.size(); lap++){
            go += "(" + goList.get(lap).toString() + "), ";
        }
        go += "]";
        String color = "[ ";
        for(int lap=0; lap < colorList.size(); lap++){
            color += "(" + colorList.get(lap).toString() + "), ";
        }
        color += "]";

        return "{ name: " + name + " question: " + question + " back: " + background + " person: " + person + " face: " + face + " foreground: " + foreground + " vastaukset: " + vastaukset + " goList: " + go + " colorList: " + color + "}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(question);
        dest.writeString(background);
        dest.writeString(person);
        dest.writeString(face);
        dest.writeString(foreground);
        dest.writeStringArray(answers);
        dest.writeTypedList(goList);
        dest.writeTypedList(colorList);
    }
    public static final Parcelable.Creator<Scene> CREATOR = new Parcelable.Creator<Scene>() {
        public Scene createFromParcel(Parcel in) {
            return new Scene(in);
        }

        public Scene[] newArray(int size) {
            return new Scene[size];
        }
    };
    private Scene(Parcel in){
        name = in.readString();
        question = in.readString();
        background = in.readString();
        person = in.readString();
        face = in.readString();
        foreground = in.readString();
        answers = in.createStringArray();
        goList = in.createTypedArrayList(GeneralKeyAndValue.CREATOR);
        colorList = in.createTypedArrayList(GeneralKeyAndValue.CREATOR);
    }
}
