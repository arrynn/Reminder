package cz.muni.fi.pv239.reminder.utils;

import com.activeandroid.serializer.TypeSerializer;
import com.google.android.gms.maps.model.LatLng;

public class LatLngSerializer extends TypeSerializer {

    @Override
    public Class<?> getDeserializedType() {
        return LatLng.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if (data == null) {
            return null;
        }

        return ((LatLng) data).latitude + ":" + ((LatLng) data).longitude;
    }

    @Override
    public LatLng deserialize(Object data) {
        if (data == null) {
            return null;
        }

        String[] s = ((String) data).split(":");

        return new LatLng(Double.valueOf(s[0]), Double.valueOf(s[1]));
    }
}