package cz.muni.fi.pv239.reminder.model;


import cz.muni.fi.pv239.reminder.R;
import xdroid.enumformat.EnumFormat;
import xdroid.enumformat.EnumString;

public enum ConditionType {

    @EnumString(R.string.enum_condition_type_time)
    TIME,

    @EnumString(R.string.enum_condition_type_wifi_reached)
    WIFI_REACHED,

    @EnumString(R.string.enum_condition_type_wifi_lost)
    WIFI_LOST,

    @EnumString(R.string.enum_condition_type_location_reached)
    LOCATION_REACHED,

    @EnumString(R.string.enum_condition_type_location_left)
    LOCATION_LEFT;


    @Override
    public String toString() {
        EnumFormat enumFormat = EnumFormat.getInstance();
        return enumFormat.format(this);
    }

}
