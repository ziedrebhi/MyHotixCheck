package hotix.myhotix.checkin.activity;


import android.os.Parcel;
import android.os.Parcelable;

public class ItemSpinner implements Parcelable {
    int Id;
    String Label;
    String Tel;

    public ItemSpinner(int id, String label) {
        Id = id;
        Label = label;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String tel) {
        Tel = tel;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }

    @Override
    public String toString() {
        return "ItemSpinner{" +
                "Id=" + Id +
                ", Label='" + Label + '\'' +
                ", Tel='" + Tel + '\'' +
                '}';
    }


    public static final Creator<ItemSpinner> CREATOR = new Creator<ItemSpinner>() {
        @Override
        public ItemSpinner createFromParcel(Parcel in) {
            return new ItemSpinner(in);
        }

        @Override
        public ItemSpinner[] newArray(int size) {
            return new ItemSpinner[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public ItemSpinner() {

    }

    private ItemSpinner(Parcel in) {
        Id = in.readInt();

        Label = in.readString();
        Tel = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeInt(Id);
        dest.writeString(Label);
        dest.writeString(Tel);

    }

}
