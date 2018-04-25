package aboutdevice.com.munir.symphony.mysymphony.model;

import android.support.annotation.Keep;

import java.sql.Timestamp;

/**
 * Created by munir on 3/28/2018.
 */
@Keep
public class StoredNews {
    private String title;
    private String description;
    private long timestamp;
    private String imageUrl;
    private String series;
    private String type;
    private String top_card;
    private String created_by;
    private String imei;
    private String locality;
    private String activityToBeOpened;
    private long revTimeStamp;
    private String modelSWVersion;
    public String banner;
    public String url;


    public StoredNews(String title, String description, long timestamp, String imageUrl, String series, String type, String top_card, String created_by, String imei, String locality, String activityToBeOpened, long revTimeStamp, String modelSWVersion, String banner, String url) {
        this.title = title;
        this.description = description;
        this.timestamp = timestamp;
        this.imageUrl = imageUrl;
        this.series = series;
        this.type = type;
        this.top_card = top_card;
        this.created_by = created_by;
        this.imei = imei;
        this.locality = locality;
        this.activityToBeOpened = activityToBeOpened;
        this.revTimeStamp = revTimeStamp;
        this.modelSWVersion = modelSWVersion;
        this.banner = banner;
        this.url = url;
    }

    public StoredNews(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTop_card() {
        return top_card;
    }

    public void setTop_card(String top_card) {
        this.top_card = top_card;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getActivityToBeOpened() {
        return activityToBeOpened;
    }

    public void setActivityToBeOpened(String activityToBeOpened) {
        this.activityToBeOpened = activityToBeOpened;
    }

    public long getRevTimeStamp() {
        return revTimeStamp;
    }

    public void setRevTimeStamp(long revTimeStamp) {
        this.revTimeStamp = revTimeStamp;
    }

    public String getModelSWVersion() {
        return modelSWVersion;
    }

    public void setModelSWVersion(String modelSWVersion) {
        this.modelSWVersion = modelSWVersion;
    }
    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
