package cz.muni.fi.pv239.reminder.utils;

import cz.muni.fi.pv239.reminder.model.Condition;


public class PrettyPrintUtils {


    public static String getAsFormattedText(Condition condition) {
        if (condition == null || condition.getType() == null) {
            return "";
        }

        switch (condition.getType()) {

            case TIME:
                return "Time: " + printTime(condition.getTimeHours(), condition.getTimeMinutes());

            case WIFI_REACHED:
                return "Wifi reached: " + condition.getWifiSsid();
            case WIFI_LOST:
                return "Wifi lost: " + condition.getWifiSsid();
            case LOCATION_REACHED:
                return "Location reached: " + condition.getLocationName();
            case LOCATION_LEFT:
                return "Location left: " + condition.getLocationName();
            default:
                return "undefined";
        }
    }


    public static String printTime(int hours, int minutes) {
        String hoursString = hours > 9 ? "" + hours : "0" + hours;
        String minutesString = minutes > 9 ? "" + minutes : "0" + minutes;
        return hoursString + ":" + minutesString;
    }


}
