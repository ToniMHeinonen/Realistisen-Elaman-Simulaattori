package muuttaa.myohemmin.realistisenelamansimulaattori.data;

import android.os.Parcel;
import android.os.Parcelable;

public class GeneralKeyAndValue implements Parcelable {
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
}
