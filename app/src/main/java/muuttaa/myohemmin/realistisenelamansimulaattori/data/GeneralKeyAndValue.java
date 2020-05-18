package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class contains values of two information what is in same object
 * @author Jesse Stenroth
 */
public class GeneralKeyAndValue implements Parcelable {
    private String key;
    private String value;

    /**
     * This constructor contains key both values
     * @param key value 1
     * @param value value 2
     */
    public GeneralKeyAndValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Constructor without values
     */
    public GeneralKeyAndValue() {
    }

    /**
     * get value 1
     * @return value in string format
     */
    public String getKey() {
        return key;
    }

    /**
     * Set string value
     * @param key value 1
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * This method return value 2
     * @return value 2 in string format
     */
    public String getValue() {
        return value;
    }

    /**
     * This method set value 2
     * @param value value in String format
     */
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }
    public static final Parcelable.Creator<GeneralKeyAndValue> CREATOR = new Parcelable.Creator<GeneralKeyAndValue>() {
        public GeneralKeyAndValue createFromParcel(Parcel in) {
            return new GeneralKeyAndValue(in);
        }

        public GeneralKeyAndValue[] newArray(int size) {
            return new GeneralKeyAndValue[size];
        }
    };
    private GeneralKeyAndValue(Parcel in){
        key = in.readString();
        value = in.readString();
    }
    public String toString(){
        return "key: " + getKey() + " value: " + getValue();
    }
}
