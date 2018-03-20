package aboutdevice.com.munir.symphony.mysymphony.onesignal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationDisplayedResult;
import com.onesignal.OSNotificationReceivedResult;

import org.json.JSONObject;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;
import aboutdevice.com.munir.symphony.mysymphony.utils.DatabaseHandler;

/**
 * Created by munirul.hoque on 1/12/2017.
 */

public class MyNotificationExtenderService  extends NotificationExtenderService{
    String bigPicture;
    String activityToBeOpened;
    String link;
    String modelSWVersion;
    String title,body, t,b, notificationID, notificationType;
    boolean returnVal;
    JSONObject data;
    DatabaseHandler databaseHandler;
    int totalSize, rowId;
    List<Integer> rowsToDelete;
    ArrayList<NotificationStore> notificationStoreKeyList;

    int maxStoredNews = 7;

    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        data = notification.payload.additionalData;
        link = notification.payload.launchURL;
        bigPicture = notification.payload.bigPicture;
        modelSWVersion = data.optString("modelSWVersion", null);
        title = notification.payload.title;
        body = notification.payload.body;
        activityToBeOpened = data.optString("activityToBeOpened", null);
        t = data.optString("t", null); // useless
        b = data.optString("b", null); // useless
        notificationID = notification.payload.notificationID.toString();


        if(link != null && modelSWVersion.equals("any")){
            notificationType = "promo";
        }
        else if(!modelSWVersion.equals("any")){
            notificationType = "fota";
        }
        else{
            notificationType = "engage";
        }


        OverrideSettings overrideSettings = new OverrideSettings();

        databaseHandler = new DatabaseHandler(getApplicationContext());

        //int minKey = databaseHandler.getMinRowId();
        rowsToDelete = null;
       // if(databaseHandler.getNotificationCount() > maxStoredNews) {
         //   autoDeleteFromList();
        //}


        if (modelSWVersion != null && (modelSWVersion.equals(getSystemProperty("ro.custom.build.version"))) || (modelSWVersion.equals(getSystemProperty("ro.build.display.id")))) {
            if(!databaseHandler.CheckIsDataAlreadyInDBorNot(DatabaseHandler.notification_id, notificationID)){
                insertDB();
            }
            overrideSettings.extender = new NotificationCompat.Extender() {
                @Override
                public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                    // Sets the background notification color to Red on Android 5.0+ devices.
                    Bitmap icon = BitmapFactory.decodeResource(MySymphonyApp.getContext().getResources(),
                            R.drawable.ic_launcher);
                    builder.setLargeIcon(icon);
                    return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
                }
            };

            OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
            Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);

        }

        else if(modelSWVersion != null && modelSWVersion.equals("any")){
           // long count = databaseHandler.getNotificationCount();
            if(!databaseHandler.CheckIsDataAlreadyInDBorNot(DatabaseHandler.notification_id, notificationID)){
                insertDB();
            }
          //  insertDB();
            overrideSettings.extender = new NotificationCompat.Extender() {
                @Override
                public NotificationCompat.Builder extend(NotificationCompat.Builder builder) {
                    // Sets the background notification color to Red on Android 5.0+ devices.
                    Bitmap icon = BitmapFactory.decodeResource(MySymphonyApp.getContext().getResources(),
                            R.drawable.ic_launcher);
                    builder.setLargeIcon(icon);
                    return builder.setColor(new BigInteger("FF0000FF", 16).intValue());
                }
            };

            OSNotificationDisplayedResult displayedResult = displayNotification(overrideSettings);
            Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId);
        }
       // databaseHandler.deleteAll(new NotificationStore(title,body,activityToBeOpened,modelSWVersion,t,b,link,bigPicture,new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),notificationID, notificationType));
        totalSize = (int)databaseHandler.getNotificationCount();
        databaseHandler.close();
        return true;
    }

    public String getSystemProperty(String key) {
        String value = null;

        try {
            value = (String) Class.forName("android.os.SystemProperties")
                    .getMethod("get", String.class).invoke(null, key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public void insertDB(){

        Log.d("Insert: ", "Inserting ..");
        databaseHandler.addNotification(new NotificationStore(title,body,activityToBeOpened,modelSWVersion,t,b,link,bigPicture,new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime()),notificationID,notificationType));
        String temp = null;
    }


}
