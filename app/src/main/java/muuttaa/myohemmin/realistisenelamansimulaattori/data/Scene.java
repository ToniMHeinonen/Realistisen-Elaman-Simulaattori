package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.LinkedList;
import java.util.List;

public class Scene implements Parcelable {
    private String name;
    private String question;
    private String background;
    private String person;
    private String face;
    private String[] answers;
    private List<GeneralKeyAndValue> goList;
    private List<GeneralKeyAndValue> colorList;

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
    public Scene(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

    public List<GeneralKeyAndValue> getGoList() {
        return goList;
    }
    public List<GeneralKeyAndValue> getColorList() {
        return colorList;
    }
    public void addToGoList(String key, String value){
        if(this.goList == null){
            this.goList = new LinkedList<>();
        }
        this.goList.add(new GeneralKeyAndValue(key, value));
    }
    public void addToColorList(String key, String value){
        if(this.colorList == null){
            this.colorList = new LinkedList<>();
        }
        this.colorList.add(new GeneralKeyAndValue(key, value));
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
        dest.writeStringArray(answers);
        dest.writeList(goList);
        dest.writeList(colorList);
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
        in.readStringArray(answers);
        in.readList(goList, GeneralKeyAndValue.class.getClassLoader());
        in.readList(colorList, GeneralKeyAndValue.class.getClassLoader());
    }
}
