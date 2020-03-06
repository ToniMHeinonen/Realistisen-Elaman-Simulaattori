package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import java.util.Date;

import muuttaa.myohemmin.realistisenelamansimulaattori.Debug;

public class ScenarioItem {

    private String name;
    private Date lastTimePlayed;
    private int percentageCompleted;

    /**
     * Creates new instance of Scenario Item.
     * @param name name of the scenario
     */
    public ScenarioItem(String name) {
        this.name = name;
        this.lastTimePlayed = ScenarioItemPrefs.loadLastTimePlayed(name);
        this.percentageCompleted = ScenarioItemPrefs.loadPercentage(name);
        Debug.print("ScenarioItem", "()", this.toString(), 1);
    }

    /**
     * Returns name of the scenario.
     * @return name of the scenario
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of the scenario item.
     * @param name of the scenario item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns date of last time played.
     * @return date of last time played
     */
    public Date getLastTimePlayed() {
        return lastTimePlayed;
    }

    /**
     * Sets time of last time played.
     * @param lastTimePlayed last time played
     */
    public void setLastTimePlayed(Date lastTimePlayed) {
        this.lastTimePlayed = lastTimePlayed;
    }

    /**
     * Returns percentage completed of scenario.
     * @return percentage completed of scenario
     */
    public int getPercentageCompleted() {
        return percentageCompleted;
    }

    /**
     * Sets percentage completed of scenario.
     * @param percentageCompleted percentage completed of scenario
     */
    public void setPercentageCompleted(int percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
    }

    /**
     * Returns scenario values.
     * @return scenario values
     */
    @Override
    public String toString() {
        return "ScenarioItem{" +
                "name='" + name + '\'' +
                ", lastTimePlayed=" + lastTimePlayed +
                ", percentageCompleted=" + percentageCompleted +
                '}';
    }
}
