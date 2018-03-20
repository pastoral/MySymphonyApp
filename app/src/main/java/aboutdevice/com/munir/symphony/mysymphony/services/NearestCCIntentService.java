package aboutdevice.com.munir.symphony.mysymphony.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class NearestCCIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.locationupdates.action.FOO";
    private static final String ACTION_BAZ = "com.locationupdates.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.locationupdates.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.locationupdates.extra.PARAM2";

    private static final String TAG = "NearestCCIntentService";

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;
    public Bundle bundle;


    public Map<String, Object>  sortedMap, distanceMap;
    public Map<String,Location> unsortedMap;

    public Location mLastLocation, location;

    public NearestCCIntentService() {
        super("NearestCCIntentService");


    }



    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "NEAREST Service Started!");
        sortedMap = new LinkedHashMap<>();
        distanceMap = new LinkedHashMap<>();

        if (intent != null) {
            bundle = new Bundle();
            final ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
            location = new Location("");

            HashMap<String, Object> hashMapObject = (HashMap<String, Object>) intent.getSerializableExtra("cclocationsmap");
            unsortedMap = (HashMap<String, Location>) hashMapObject.get("key");
            mLastLocation = intent.getParcelableExtra("lastlocation");

            resultReceiver.send(STATUS_RUNNING, Bundle.EMPTY);

            if (mLastLocation != null) {
                for (Map.Entry<String, Location> entry : unsortedMap.entrySet()) {
                    String key = entry.getKey();
                    Location value = entry.getValue();
                    distanceMap.put(key, mLastLocation.distanceTo(value));
                }

                if (distanceMap.size() > 0) {
                    sortedMap = sortByValue(distanceMap);
                    if(sortedMap.size() > 0) {
                        Map.Entry<String, Object> entry = sortedMap.entrySet().iterator().next();
                        bundle.putString("result" , entry.getKey());

                    }

                }

                resultReceiver.send(STATUS_FINISHED, bundle);

                Log.d(TAG, "Service Stopping!");
                this.stopSelf();

            }
        }
    }

    public Map sortByValue(Map unSortedMap){
        sortedMap = new TreeMap(new ValueComparator(unSortedMap));
        sortedMap.putAll(unSortedMap);
        return sortedMap;
    }




    class ValueComparator implements Comparator {
        Map map;

        public ValueComparator(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return valueA.compareTo(valueB);
        }
    }




}
