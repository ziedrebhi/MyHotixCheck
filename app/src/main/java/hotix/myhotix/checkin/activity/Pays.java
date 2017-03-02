package hotix.myhotix.checkin.activity;

import android.os.Parcel;
import android.os.Parcelable;

public class Pays implements Parcelable {
    public static final String PAYS_ID = "Id";
    public static final String PAYS_NAME = "Name";
    public static final String PAYS_CODE = "Code";

    public static final Parcelable.Creator<Pays> CREATOR = new Parcelable.Creator<Pays>() {
        @Override
        public Pays createFromParcel(Parcel in) {
            return new Pays(in);
        }

        @Override
        public Pays[] newArray(int size) {
            return new Pays[size];
        }
    };
    int Id;
    String Name;
    String Code;

    public Pays(int id, String name, String Code) {
        super();
        Id = id;
        Name = name;
        Code = Code;

    }

    public Pays() {

    }


    private Pays(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
        Code = in.readString();
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return "Pays [Id=" + Id + ", Name=" + Name + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
        dest.writeString(Code);
    }
}
