package aboutdevice.com.munir.symphony.mysymphony.data.model;

/**
 * Created by munirul.hoque on 12/04/2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDataRemote {

    @SerializedName("rowid")
    @Expose
    private String rowid;
    @SerializedName("uuid")
    @Expose
    private String uuid;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("imei")
    @Expose
    private String imei;
    @SerializedName("mac")
    @Expose
    private String mac;
    @SerializedName("msisdn")
    @Expose
    private String msisdn;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lan")
    @Expose
    private String lan;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserDataRemote() {
    }

    /**
     *
     * @param email
     * @param msisdn
     * @param imei
     * @param lan
     * @param mac
     * @param uuid
     * @param rowid
     * @param lat
     */
    public UserDataRemote(String rowid, String uuid, String email, String imei, String mac, String msisdn, String lat, String lan) {
        super();
        this.rowid = rowid;
        this.uuid = uuid;
        this.email = email;
        this.imei = imei;
        this.mac = mac;
        this.msisdn = msisdn;
        this.lat = lat;
        this.lan = lan;
    }

    public String getRowid() {
        return rowid;
    }

    public void setRowid(String rowid) {
        this.rowid = rowid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }



}