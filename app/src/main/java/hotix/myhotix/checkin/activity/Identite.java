package hotix.myhotix.checkin.activity;


import android.os.Parcel;
import android.os.Parcelable;

public class Identite implements Parcelable {
    public static final Creator<Identite> CREATOR = new Creator<Identite>() {
        @Override
        public Identite createFromParcel(Parcel in) {
            return new Identite(in);
        }

        @Override
        public Identite[] newArray(int size) {
            return new Identite[size];
        }
    };
    int Id;
    String Name;

    public Identite(int id, String name) {
        super();
        Id = id;
        Name = name;
    }

    public Identite() {

    }

    private Identite(Parcel in) {
        Id = in.readInt();
        Name = in.readString();
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
        return "Identite [Id=" + Id + ", Name=" + Name + "]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Name);
    }
}
