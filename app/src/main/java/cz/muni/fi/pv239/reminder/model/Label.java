package cz.muni.fi.pv239.reminder.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Marek on 28-May-16.
 */
@Table(name = "label")
public class Label extends Model {

    @Column(name = "title")
    public String title;

    @Column(name = "color")
    public String color;

}
