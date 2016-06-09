package cz.muni.fi.pv239.reminder.model;

import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Marek on 28-May-16.
 */
@Table(name = "Reminder")
public class Reminder extends Model {

    /**
     * Title of reminder
     */
    @Column(name = "title")
    public String title;

    /**
     * Description or note for this reminder
     */
    @Column(name = "description")
    public String description;


    public List<Condition> conditions = new ArrayList<>();

    @Deprecated
    @Column(name = "identifier")
    public String identifier;

    @Column(name = "displayed")
    public boolean displayed;

    @Column(name = "reminded_on")
    public Date remindedOn;

    public Reminder() {
        super();
    }

    public static List<Reminder> getAllReminders() {
        return new Select().from(Reminder.class).execute();
    }

    public static Reminder getReminderById(Long id) {
        return new Select().from(Reminder.class)
                .where("id = ?", id)
                .executeSingle();
    }

}
