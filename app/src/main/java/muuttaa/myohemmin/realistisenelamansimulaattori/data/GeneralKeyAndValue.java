package muuttaa.myohemmin.realistisenelamansimulaattori.data;

public class GeneralKeyAndValue {
    private String key;
    private String value;

    public GeneralKeyAndValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public GeneralKeyAndValue() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
