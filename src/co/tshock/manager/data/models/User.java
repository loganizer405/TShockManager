package co.tshock.manager.data.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author koesie10
 */
public class User implements Parcelable {
    private String group;
    private int id;
    private String name;
    private String password;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String group, int id, String name) {
        this(group, id, name, null);
    }

    public User(String group, int id, String name, String password) {
        this.group = group;
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User(String name) {
        this(null, 0, name);
    }

    public User() {
        this(null, 0, null);
    }

    protected User(Parcel in) {
        group = in.readString();
        id = in.readInt();
        name = in.readString();
        password = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group);
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(password);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
