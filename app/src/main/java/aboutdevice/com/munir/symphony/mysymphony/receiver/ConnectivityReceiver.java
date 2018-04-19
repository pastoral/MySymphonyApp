package aboutdevice.com.munir.symphony.mysymphony.receiver;



/**
 * Created by munirul.hoque on 12/21/2016.
 */

public class ConnectivityReceiver { //extends BroadcastReceiver {

//    public static ConnectivityReceiverListner connectivityReceiverListner;
//
//    public ConnectivityReceiver(){
//        super();
//    }
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//
//        if(connectivityReceiverListner != null){
//            connectivityReceiverListner.onNetworkConnectionChanged(isConnected);
//        }
//    }
//
//    public static boolean isConnected(){
//        ConnectivityManager cm = (ConnectivityManager) MySymphonyApp.getmInstance().getApplicationContext()
//                                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        return activeNetwork != null
//                && activeNetwork.isConnectedOrConnecting();
//    }
//
//    public interface ConnectivityReceiverListner{
//        void onNetworkConnectionChanged(boolean isConnected);
//    }
}
