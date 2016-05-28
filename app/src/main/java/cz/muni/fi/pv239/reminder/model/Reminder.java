package cz.muni.fi.pv239.reminder.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by Marek on 28-May-16.
 */
@Table(name = "Reminder")
public class Reminder extends Model {

    @Column(name = "title")
    public String title;

    @Column(name = "description")
    public String description;

    @Column(name = "type")
    public int type;

    @Column(name = "identifier")
    public String identifier;

    @Column(name = "id_label", index = true)
    public Label label;

    @Column(name = "displayed")
    public boolean displayed;

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

    public static List<Reminder> getReminderByTypeAndIdentifier(int type, String name) {
        return new Select().from(Reminder.class)
                .where("displayed =?", false)
                .where("type =?", type)
                .where("identifier = ?", name)
                .execute();
    }

}
