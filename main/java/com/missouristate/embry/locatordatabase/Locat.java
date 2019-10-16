package com.missouristate.embry.locatordatabase;

public class Locat{
    private int id;
    private String name;
    private String latitude;
    private String longitude;

    public Locat(int newId, String newName, String lat, String lon) {
        setId(newId);
        setName(newName);
        setLat(lat);
        setLong(lon);
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setLat(String lat) {
            latitude = lat;
    }

    public void setLong(String lon) {
        longitude = lon;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return latitude;
    }

    public String getLong() {
        return longitude;
    }

    public String toString() {
        return id + ", " + name + ", " + latitude + ", " + longitude;
    }
}
