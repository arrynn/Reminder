package cz.muni.fi.pv239.reminder.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import cz.muni.fi.pv239.reminder.utils.PrettyPrintUtils;

@Table(name = "Condition")
public class Condition extends Model implements Parcelable {

    @Column(name = "id_reminder")
    private Long reminderId;

    @Column(name = "fullfiled")
    private boolean fulfilled = false;

    @Column(name = "precondition_fullfiled")
    private boolean preconditionFulfilled = false;

    @Column(name = "type")
    private ConditionType type;

    @Column(name = "time_hours")
    private Integer timeHours = -1;

    @Column(name = "time_minutes")
    private Integer timeMinutes = -1;

    @Column(name = "wifi")
    private String wifiSsid;

    @Column(name = "location")
    private LatLng location;

    @Column(name = "location_name")
    private String locationName;

    public Condition() {
        super();
    }

    public Condition(Parcel parcel) {
        super();
        readFromParcel(parcel);
    }

    public Long getReminderId() {
        return reminderId;
    }

    public void setReminderId(Long reminderId) {
        this.reminderId = reminderId;
    }

    public boolean isFulfilled() {
        return fulfilled;
    }

    public void setFulfilled(boolean fulfilled) {
        this.fulfilled = fulfilled;
    }

    public boolean isPreconditionFulfilled() {
        return preconditionFulfilled;
    }

    public void setPreconditionFulfilled(boolean preconditionFulfilled) {
        this.preconditionFulfilled = preconditionFulfilled;
    }

    public ConditionType getType() {
        return type;
    }

    public void setType(ConditionType type) {
        this.type = type;
    }

    public Integer getTimeHours() {
        return timeHours;
    }

    public void setTimeHours(Integer timeHours) {
        this.timeHours = timeHours;
    }

    public Integer getTimeMinutes() {
        return timeMinutes;
    }

    public void setTimeMinutes(Integer timeMinutes) {
        this.timeMinutes = timeMinutes;
    }

    public String getWifiSsid() {
        return wifiSsid;
    }

    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getTypeAsString() {
        return type == null ? "" : type.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(reminderId == null ? -1 : reminderId);
        dest.writeInt(fulfilled ? 1 : 0);
        dest.writeInt(preconditionFulfilled ? 1 : 0);
        dest.writeString(type == null ? null : type.name());
        dest.writeInt(timeHours);
        dest.writeInt(timeMinutes);
        dest.writeString(wifiSsid);
        dest.writeParcelable(location, 0);
        dest.writeString(locationName);
    }

    /**
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {
        reminderId = in.readLong();
        if (reminderId == -1) {
            reminderId = null;
        }
        fulfilled = in.readInt() == 1;
        preconditionFulfilled = in.readInt() == 1;
        type = ConditionType.valueOf(in.readString());
        timeHours = in.readInt();
        timeMinutes = in.readInt();
        wifiSsid = in.readString();
        location = in.readParcelable(LatLng.class.getClassLoader());
        locationName = in.readString();
    }

    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     * <p/>
     * This also means that you can use use the default
     * constructor to create the object and use another
     * method to hydrate it as necessary.
     * <p/>
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public Condition createFromParcel(Parcel in) {
                    return new Condition(in);
                }

                public Condition[] newArray(int size) {
                    return new Condition[size];
                }
            };


    public static Condition getConditionById(Long id) {
        return new Select().from(Condition.class)
                .where("id = ?", id)
                .executeSingle();
    }

    public static List<Condition> getConditionsByReminderId(Long reminderId) {
        return new Select().from(Condition.class)
                .where("id_reminder = ?", reminderId)
                .execute();
    }


    @Override
    public String toString() {
        return PrettyPrintUtils.getAsFormattedText(this);
    }


    public static List<Condition> getUnfulfilledTimeBasedConditions() {
        return new Select().from(Condition.class)
                .where("fullfiled != TRUE")
                .where("type == ?", ConditionType.TIME)
                .execute();
    }

    public static List<Condition> getUnfulfilledLocationBasedConditions() {
        return new Select().from(Condition.class)
                .where("fullfiled != TRUE")
                .where("type != ?", ConditionType.TIME)
                .execute();
    }

    public static List<Condition> getUnfulfilledWifiBasedConditions() {
        return new Select().from(Condition.class)
                .where("fullfiled != TRUE")
                .where("type IN (?,?)", ConditionType.WIFI_REACHED, ConditionType.WIFI_LOST)
                .execute();
    }

    public static List<Condition> getConditionsByFulfilledAndType(boolean preconditionFulfilled, boolean fulfilled, ConditionType type) {
        return new Select().from(Condition.class)
                .where("precondition_fullfiled = ?", preconditionFulfilled)
                .where("fullfiled = ?", fulfilled)
                .where("type = ?", type)
                .execute();
    }
}
