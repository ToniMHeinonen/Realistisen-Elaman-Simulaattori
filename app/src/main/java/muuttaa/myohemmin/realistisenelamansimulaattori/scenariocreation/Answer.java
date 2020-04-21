package muuttaa.myohemmin.realistisenelamansimulaattori.scenariocreation;

public class Answer {

    private String text;
    private String goesToScene;
    private String color;

    public Answer(String text, String goesToScene, String color) {
        this.text = text;
        this.goesToScene = goesToScene;
        this.color = color;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getGoesToScene() {
        return goesToScene;
    }

    public void setGoesToScene(String goesToScene) {
        this.goesToScene = goesToScene;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "text='" + text + '\'' +
                ", goesToScene='" + goesToScene + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}
