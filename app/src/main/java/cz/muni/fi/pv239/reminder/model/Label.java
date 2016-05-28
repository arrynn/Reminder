package cz.muni.fi.pv239.reminder.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Marek on 28-May-16.
 */
@Table(name = "Label")
public class Label extends Model {

    @Column(name = "title")
    public String title;

    @Column(name = "color")
    public String color;

    public Label() {
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
