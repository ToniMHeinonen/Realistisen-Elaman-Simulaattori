package muuttaa.myohemmin.realistisenelamansimulaattori;

import java.util.Date;

public class ScenarioItem {

    private String name;
    private Date lastTimePlayed;
    private int percentageCompleted;

    public ScenarioItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getLastTimePlayed() {
        return lastTimePlayed;
    }

    public void setLastTimePlayed(Date lastTimePlayed) {
        this.lastTimePlayed = lastTimePlayed;
    }

    public int getPercentageCompleted() {
        return percentageCompleted;
    }

    public void setPercentageCompleted(int percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
    }

    @Override
    public String toString() {
        return "ScenarioItem{" +
                "name='" + name + '\'' +
                ", lastTimePlayed=" + lastTimePlayed +
                ", percentageCompleted=" + percentageCompleted +
                '}';
    }
}
