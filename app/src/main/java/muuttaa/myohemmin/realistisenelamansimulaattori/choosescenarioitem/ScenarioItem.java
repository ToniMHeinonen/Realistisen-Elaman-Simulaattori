package muuttaa.myohemmin.realistisenelamansimulaattori.choosescenarioitem;

import java.util.Date;

import muuttaa.myohemmin.realistisenelamansimulaattori.tools.Debug;

public class ScenarioItem {

    private int id;
    private String name;
    private Date lastTimePlayed;
    private int percentageCompleted;
    private String category;

    /**
     * Creates new instance of Scenario Item.
     * @param id id of the scenario
     * @param name name of the scenario
     */
    public ScenarioItem(int id, String name) {
        this.id = id;
        this.name = name;
        this.lastTimePlayed = ScenarioItemPrefs.loadLastTimePlayed(id);
        this.percentageCompleted = ScenarioItemPrefs.loadPercentage(id);
        this.category = ScenarioItemPrefs.loadCategory(id);
        Debug.print("ScenarioItem", "()", this.toString(), 1);
    }

    /**
     * Returns id of the scenario.
     * @return id of the scenario
     */
    public int getId() {
        return id;
    }

    /**
     * Returns name of the scenario.
     * @return name of the scenario
     */
    public String getName() {
        return name;
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
        ScenarioItemPrefs.saveLastTimePlayed(id);
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
        ScenarioItemPrefs.savePercentage(id, percentageCompleted);
    }

    /**
     * Returns the category of the item.
     * @return category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of the item.
     * @param category category to set
     */
    public void setCategory(String category) {
        this.category = category;
        ScenarioItemPrefs.saveCategory(id, category);
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
                ", category='" + category + '\'' +
                '}';
    }
}
