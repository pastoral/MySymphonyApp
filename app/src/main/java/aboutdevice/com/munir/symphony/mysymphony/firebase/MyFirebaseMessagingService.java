package aboutdevice.com.munir.symphony.mysymphony.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.ui.NewsActivity;

/**
 * Created by munirul.hoque on 1/9/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       // super.onMessageReceived(remoteMessage);

        Log.d(TAG,"From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if(remoteMessage.getData().size() > 0){
            Log.d(TAG, "Message data payload :  " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if(remoteMessage.getNotification()!= null){
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());



        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
      //  Toast.makeText(this, "Message Notification Body: " + remoteMessage.getNotification().getBody() , Toast.LENGTH_SHORT);
        createNotification(remoteMessage.getNotification().getBody());
    }

    private void createNotification( String messageBody){
        Intent intent = new Intent(this, NewsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent newsIntent = PendingIntent.getActivity(this,0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.smiley)
                .setContentTitle("My Symphony Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(notificationSoundURI)
                .setContentIntent(newsIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
