package aboutdevice.com.munir.symphony.mysymphony;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;

import aboutdevice.com.munir.symphony.mysymphony.onesignal.MyNotificationOpenedHandler;
import aboutdevice.com.munir.symphony.mysymphony.onesignal.MyNotificationReceivedHandler;
import aboutdevice.com.munir.symphony.mysymphony.receiver.ConnectivityReceiver;

/**
 * Created by munirul.hoque on 12/21/2016.
 */

public class MySymphonyApp extends Application {
    private static MySymphonyApp mInstance;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        context = getApplicationContext();

        //MyNotificationOpenedHandler : This will be called when a notification is tapped on.
        //MyNotificationReceivedHandler : This will be called when a notification is received while your app is running.
        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new MyNotificationOpenedHandler())
                .setNotificationReceivedHandler( new MyNotificationReceivedHandler() )
                .init();

    }

    public static synchronized MySymphonyApp getmInstance(){
        return mInstance;
    }
    public void setConnectiviyListner(ConnectivityReceiver.ConnectivityReceiverListner listner){
        ConnectivityReceiver.connectivityReceiverListner = listner;
    }
}
