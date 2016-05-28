package cz.muni.fi.pv239.reminder.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Marek on 28-May-16.
 */
@Table(name = "reminder")
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

}
