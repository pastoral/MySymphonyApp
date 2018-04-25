package aboutdevice.com.munir.symphony.mysymphony.model;

import android.support.annotation.Keep;

import java.io.Serializable;
import java.util.List;

/**
 * Created by AppUser on 8/18/2017.
 */
@SuppressWarnings("serial")

public class AppUser implements Serializable {
    private String uid;
    private String name;
    private String email;
    private String phoneNumber;
    private String photoURL;
    private String providerId;
    private String location;
    private double lat;
    private double lan;
    public List<Object> model;
    public List<Object> imei;
    private String userCategoryText;
    private String userCategoryImage;
    public List<Object> mac;


    public AppUser() {
    }

    public AppUser(String uid, String name, String email, String phoneNumber, String photoURL, String providerId, String location, double lat, double lan, List<Object> imei, List<Object> model, String userCategoryText, String userCategoryImage, List<Object> mac) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.photoURL = photoURL;
        this.providerId = providerId;
        this.location = location;
        this.lat = lat;
        this.lan = lan;
        this.imei = imei;
        this.model = model;
        this.userCategoryText = userCategoryText;
        this.userCategoryImage = userCategoryImage;
        this.mac = mac;

    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Object> getModel() {
        return model;
    }

    public void setModel(List<Object> model) {
        this.model = model;
    }

    public List<Object> getImei() {
        return imei;
    }

    public void setImei(List<Object> imei) {
        this.imei = imei;
    }

    public String getUserCategoryText() {
        return userCategoryText;
    }

    public void setUserCategoryText(String userCategoryText) {
        this.userCategoryText = userCategoryText;
    }

    public String getUserCategoryImage() {
        return userCategoryImage;
    }

    public void setUserCategoryImage(String userCategoryImage) {
        this.userCategoryImage = userCategoryImage;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public List<Object> getMac() {
        return mac;
    }

    public void setMac(List<Object> mac) {
        this.mac = mac;
    }
}