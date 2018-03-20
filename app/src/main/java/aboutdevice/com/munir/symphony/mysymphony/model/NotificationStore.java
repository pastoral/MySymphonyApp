package aboutdevice.com.munir.symphony.mysymphony.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;

/**
 * Created by admin on 1/31/2017.
 */

public class NotificationStore {
    private int id;
    public String notification_title;
    public String notification_content;
    private String activityToBeOpened;
    private String model_sw_version;
    private String t;
    private String b;
    private String link;
    public String image_url;
    private String insertion_date;
    private String notification_id;
    private String notification_type;

   // public static TextView txtnotification_title, txtnotification_content;

    public NotificationStore(){}


    public NotificationStore( int id, String notification_title, String notification_content, String activityToBeOpened, String model_sw_version, String t, String b, String link, String image_url, String insertion_date, String notification_id, String notification_type) {
        this.id = id;
        this.notification_content = notification_content;
        this.notification_title = notification_title;
        this.activityToBeOpened = activityToBeOpened;
        this.model_sw_version = model_sw_version;
        this.t = t;
        this.b = b;
        this.link = link;
        this.image_url = image_url;
        this.insertion_date = insertion_date;
        this.notification_id = notification_id;
        this.notification_type = notification_type;
    }

    public NotificationStore( String notification_title, String notification_content, String activityToBeOpened, String model_sw_version, String t, String b, String link, String image_url, String insertion_date, String notification_id, String notification_type) {

        this.notification_title = notification_title;
        this.notification_content = notification_content;
        this.activityToBeOpened = activityToBeOpened;
        this.model_sw_version = model_sw_version;
        this.t = t;
        this.b = b;
        this.link = link;
        this.image_url = image_url;
        this.insertion_date = insertion_date;
        this.notification_id = notification_id;
        this.notification_type = notification_type;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotification_content() {
        return notification_content;
    }

    public void setNotification_content(String notification_content) {
        this.notification_content = notification_content;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public String getActivityToBeOpened() {
        return activityToBeOpened;
    }


    public void setActivityToBeOpened(String activityToBeOpened) {
        this.activityToBeOpened = activityToBeOpened;
    }

    public String getModel_sw_version() {
        return model_sw_version;
    }


    public void setModel_sw_version(String model_sw_version) {
        this.model_sw_version = model_sw_version;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getInsertion_date() {
        return insertion_date;
    }

    public void setInsertion_date(String insertion_date) {
        this.insertion_date = insertion_date;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public void setNotification_type(String notification_type) {
        this.notification_type = notification_type;
    }




}
