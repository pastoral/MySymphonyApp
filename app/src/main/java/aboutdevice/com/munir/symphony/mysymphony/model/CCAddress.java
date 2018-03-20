package aboutdevice.com.munir.symphony.mysymphony.model;

import java.util.Date;

/**
 * Created by munirul.hoque on 11/16/2016.
 */

public class CCAddress {
    private String name;
    private String district;
    private String address;
    private boolean cc ;
    private double lat;
    private double lan;
    private String country;
    private String created;
    private String last_modified;
    private String created_by;


    public CCAddress(){}

    public CCAddress(String name, String district, String address, boolean cc, double lat, double lan, String country, String created, String last_modified, String created_by) {
        this.name = name;
        this.district = district;
        this.address = address;
        this.cc = cc;
        this.lat = lat;
        this.lan = lan;
        this.country = country;
        this.created = created;
        this.last_modified = last_modified;
        this.created_by = created_by;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(String last_modified) {
        this.last_modified = last_modified;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(float lan) {
        this.lan = lan;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public boolean isCc() {
        return cc;
    }

    public void setCc(boolean cc) {
        this.cc = cc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


}
