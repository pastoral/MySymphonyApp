package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;


import aboutdevice.com.munir.symphony.mysymphony.BaseActivity;
import aboutdevice.com.munir.symphony.mysymphony.Constants;
import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.firebase.FireBaseWorker;
import aboutdevice.com.munir.symphony.mysymphony.model.CCAddress;
import aboutdevice.com.munir.symphony.mysymphony.receiver.ConnectivityReceiver;
import aboutdevice.com.munir.symphony.mysymphony.receiver.MyResultReceiver;
import aboutdevice.com.munir.symphony.mysymphony.services.BackgroundLocationService;
import aboutdevice.com.munir.symphony.mysymphony.services.LocationUpdates;
import aboutdevice.com.munir.symphony.mysymphony.services.NearestCCIntentService;
import aboutdevice.com.munir.symphony.mysymphony.utils.CCAddressViewHolder;
import aboutdevice.com.munir.symphony.mysymphony.utils.DividerItemDecoration;
import aboutdevice.com.munir.symphony.mysymphony.utils.FusedLocationFinder;
import aboutdevice.com.munir.symphony.mysymphony.utils.RecyclerTouchListener;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.REQUEST_CHECK_SETTINGS;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.isFirebaseReady;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permsRequestCode;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class ThreeFragment extends Fragment implements  ResultCallback<LocationSettingsResult>, MyResultReceiver.Receiver {
    public String name;
    public int pos, scrollToPosition = 0;
    public ProgressBar progressBar, progressBarCC;
    public RecyclerView recyclerView;
    private List<CCAddress> ccAddressList;
    private LinearLayoutManager mLinearLayoutManager;
    private FireBaseWorker fireBaseWorker;
    private CCAddressViewHolder ccAddressViewHolder;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<CCAddress,CCAddressViewHolder> firebaseRecyclerAdapter;
    static boolean calledAlready = false;
    private TextView txtNearestCCAddress,txtNearestCCName;
    private static final String TAG = "FusedLocationFinder";

    public LocationSettingsRequest mLocationSettingsRequest;
    public boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    public Location mCurrentlocation,ccLocation,latlanLocation;
    private View view;
    private GoogleApiClient googleApiClient;
    private Context context;
    private int permissionCheck;
    private Bundle bundle;
    private BaseActivity baseActivity;
    public Map<String, Location> ccLocationMap;
    public Map<String, Object> addressMap;
    public Map<String,Float> distanceMap, sortedDistanceMap;
    public Button btnRefresh;
    public CardView nearest_cc_card;
    public Query query;
    // public GpsLocationReceiver gpsLocationReceiver;
    public String nearestCCName, nearestCCAddress;
    public SharedPreferences sharedpreferences;
    public  SharedPreferences.Editor editor;
    BroadcastReceiver gpsLocationReceiver;
    public double nearestCCLat, nearestCCLan;
    private IntentFilter filter;
    private ResponeReceiver receiver;
    public MyResultReceiver resultReceiver;

    private Snackbar snackbar;

    private boolean mapReady;
    public ThreeFragment (){
        ccAddressList = new ArrayList<CCAddress>();
        this.pos = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_three,container,false);

        //getContext().registerReceiver(gpsLocationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bundle = savedInstanceState;
        progressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        progressBarCC = (ProgressBar)view.findViewById(R.id.progressBarCC);
        recyclerView = (RecyclerView)view.findViewById(R.id.cc_recycler);
        txtNearestCCName = (TextView)view.findViewById(R.id.txtNearestCCName);
        txtNearestCCAddress = (TextView)view.findViewById(R.id.txtNearestCCAddress);
        nearest_cc_card = (CardView)view.findViewById(R.id.nearest_cc_header);

        // btnRefresh = (Button)view.findViewById(R.id.buttonRefresh);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(mLinearLayoutManager);

        recyclerView.setHasFixedSize(true);

        filter = new IntentFilter(ResponeReceiver.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        // gpsLocationReceiver = new GpsLocationReceiver();

        ccLocation = new Location("Location of CC");
        latlanLocation = new Location("LAT LAN");
        ccLocationMap = new HashMap<>();
        addressMap = new HashMap<>();

        sharedpreferences = getContext().getSharedPreferences("mysymphonyapp_data", Context.MODE_PRIVATE);
        receiver = new ResponeReceiver();
        resultReceiver = new MyResultReceiver(new Handler());

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            loadSP();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            loadSP();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        ccLocation = new Location("CC Location");

        if (!calledAlready)
        {
            FirebaseDatabase.getInstance();//.setPersistenceEnabled(true)
            calledAlready = true;
        }

        //getContext().registerReceiver(gpsLocationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

        fireBaseWorker = new FireBaseWorker();
        mDatabaseReference = fireBaseWorker.intDatabase(Constants.ADRESS);

        mDatabaseReference.keepSynced(true);


    }

    @Override
    public void onResume() {
        super.onResume();
        //  askForPermission(permisionList[1],REQUEST_CHECK_SETTINGS);
        //ccAddressList.clear();
        getActivity().registerReceiver(receiver,filter);
        resultReceiver.setmReceiver(this);
        final Intent intent = new Intent(getActivity(),MapsActivity.class);
        LoadCC loadCC = new LoadCC();
        loadCC.execute();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CCAddress, CCAddressViewHolder>(
                CCAddress.class,
                R.layout.cc_list,
                CCAddressViewHolder.class,
                // mDatabaseReference.orderByChild("name")
                mDatabaseReference.orderByChild("name")
        ) {
            @Override
            protected void populateViewHolder(CCAddressViewHolder viewHolder, CCAddress model, int position) {
                progressBar.setVisibility(GONE);
                ccLocation.setLatitude(firebaseRecyclerAdapter.getItem(position).getLat());
                ccLocation.setLongitude(firebaseRecyclerAdapter.getItem(position).getLan());
                //Toast.makeText(getActivity(),String.valueOf(mCurrentlocation),Toast.LENGTH_SHORT).show();
                // ccLocationMap.put(firebaseRecyclerAdapter.getItem(position).getName().toString(),ccLocation);

                viewHolder.txtCCName.setText(model.getName());
                viewHolder.txtCCAddress.setText(model.getAddress());
                pos = position;
                // ccLocationMap.put(map.get("name").toString(),ccLocation);


                recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String Key = getRef(position).getKey();
                        intent.putExtra("CCName", firebaseRecyclerAdapter.getItem(position).getName());
                        intent.putExtra("CCAddress", firebaseRecyclerAdapter.getItem(position).getAddress());
                        intent.putExtra("Latitude",String.valueOf(firebaseRecyclerAdapter.getItem(position).getLat()) );
                        intent.putExtra("Longitude" , String.valueOf(firebaseRecyclerAdapter.getItem(position).getLan()));
                        scrollToPosition = position ;
                        if(haveNetworkConnection()) {
                            startActivity(intent);
                        }
                        else{
                            showSnack(false);
                        }
                    }
                }));
                viewHolder.ccIcon.setImageDrawable(drawIcon(alphbetSelect(firebaseRecyclerAdapter.getItem(position).getName().toString())));
                ccLocation = new Location("");
            }
        };
        /*firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver(){
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                itemCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (lastVisiblePosition == -1 || (positionStart >= (itemCount -1) && lastVisiblePosition == (positionStart -1))){
                    recyclerView.scrollToPosition(positionStart);
                }
                //recyclerView.scrollToPosition(positionStart);
            }
        });*/




        // recyclerView.setLayoutManager(mLinearLayoutManager);

        //recyclerView.setAdapter(firebaseRecyclerAdapter);
        startLocationUpdate();
        if(firebaseRecyclerAdapter == null){
            Toast.makeText(getActivity(), "No adapter attached; skipping layout",Toast.LENGTH_SHORT).show();
        }
        else if (mLinearLayoutManager == null){
            Toast.makeText(getActivity(), "No layout manager attached; skipping layout",Toast.LENGTH_SHORT).show();
        }
        else{
            // Toast.makeText(getActivity(), "Got it",Toast.LENGTH_SHORT).show();
            isFirebaseReady = true;

            if(scrollToPosition != 0){
                mLinearLayoutManager.scrollToPositionWithOffset(scrollToPosition,0);
            }
            else{
                mLinearLayoutManager.scrollToPositionWithOffset(0,0);
            }
            recyclerView.setLayoutManager(mLinearLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(firebaseRecyclerAdapter);

            loadSP();
        }


        //  LocationAsyncRunner runner = new LocationAsyncRunner();
        //  runner.execute();

        nearest_cc_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),MapsActivity.class);

                i.putExtra("CCName", nearestCCName);
                i.putExtra("CCAddress", nearestCCAddress);
                i.putExtra("Latitude", String.valueOf(latlanLocation.getLatitude()));
                i.putExtra("Longitude" , String.valueOf(latlanLocation.getLongitude()));
                if(haveNetworkConnection()) {
                    startActivity(i);
                }
                else{
                    showSnack(false);
                }

            }
        });

       /* gpsLocationReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //here you can parse intent and get sms fields.
                boolean anyLocationProv = false;
                LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
                anyLocationProv = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                // anyLocationProv |=  locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                // Log.i("", "Location service status" + anyLocationProv);
                //Toast.makeText(context, "Location service status : " + anyLocationProv, Toast.LENGTH_SHORT).show();
                if(anyLocationProv == false){
                    Toast.makeText(context, "No Location Service " , Toast.LENGTH_SHORT).show();
                    nearest_cc_card.setVisibility(GONE);
                    checkLocationSettings();
                }
                else{
                    LocationAsyncRunner runner = new LocationAsyncRunner();
                    runner.execute();
                    updateUI();
                }
            }
        }; */
        IntentFilter filter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        this.getContext().registerReceiver(gpsLocationReceiver, filter);


    }



    @Override
    public void onPause() {
        super.onPause();
        // this.getContext().unregisterReceiver(gpsLocationReceiver);

        // stopLocationUpdates();

        getActivity().unregisterReceiver(receiver);
        resultReceiver.setmReceiver(this);

    }

    @Override
    public void onStop() {
        super.onStop();

        //  googleApiClient.disconnect();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // firebaseRecyclerAdapter.cleanup();
        stopLocationUpdate();
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_CHECK_SETTINGS :
                switch (resultCode){
                    case Activity.RESULT_OK :
                        //startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED :
                        Log.i("ThreeFragment", "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }




    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    public void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient,mLocationSettingsRequest);
        result.setResultCallback(this);

    }

    /**
     * The callback invoked when
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} is called. Examines the
     * {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch(status.getStatusCode()){
            case LocationSettingsStatusCodes.SUCCESS :
                Log.i(TAG, "All location settings are satisfied.");
                //startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED :
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try{
                    status.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                }
                catch(IntentSender.SendIntentException e){
                    Log.i(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE :
                Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }




    public void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission},permsRequestCode);
            }
            else{
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission},permsRequestCode);
            }
        }
        else{
            Log.v(TAG, " is already granted.");
        }
    }




    private void showSnack(boolean isConnected){
        String message;
        int color;
        if(!isConnected){
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }
        else{
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        }
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    public TextDrawable drawIcon(String str)
    {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        TextDrawable textDrawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(40) /* size in px */
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(str, color);
        return textDrawable;
    }

    public String alphbetSelect(String str)
    {
        String firstAlphabet;
        String ccName = str.trim();
        if(ccName != null)
        {
            firstAlphabet = ccName.substring(0,1);
        }
        else
        {
            firstAlphabet = "A";
        }

        return  firstAlphabet;
    }


    public void loadSP(){
        if(sharedpreferences.getString("NEARESTCC_ADDRESS", null) != null  && sharedpreferences.getString("NEARESTCC_NAME", null) != null){

            nearest_cc_card.setVisibility(GONE);
            txtNearestCCAddress.setText(sharedpreferences.getString("NEARESTCC_ADDRESS", null));
            txtNearestCCName.setText(sharedpreferences.getString("NEARESTCC_NAME", null));

        }
    }


    private void startLocationUpdate(){
        Log.d("STARTLOC upade", "startLocationUpdate fired");
        Intent intent = new Intent(getContext(), BackgroundLocationService.class);
        intent.putExtra("requestId", 101);
        getActivity().startService(intent);

    }

    private void stopLocationUpdate(){
        Intent intent = new Intent(getContext(), BackgroundLocationService.class);

        intent.putExtra("requestId", 101);
        getActivity().stopService(intent);

    }

    @Override
    public void onReceiveResult(int resultCode, Bundle ResultData) {

        nearest_cc_card.setVisibility(VISIBLE);

        switch (resultCode){
            case  NearestCCIntentService.STATUS_RUNNING:
                txtNearestCCName.setText("Updating CC Location .....");
                txtNearestCCAddress.setText("Updating CC Address...");
                break;

            case NearestCCIntentService.STATUS_FINISHED:
                nearestCCName = ResultData.getString("result");
                nearestCCAddress = (String)addressMap.get(nearestCCName);
                latlanLocation = (Location) ccLocationMap.get(nearestCCName);
                txtNearestCCName.setText(nearestCCName);
                txtNearestCCAddress.setText(nearestCCAddress);
        }

    }

    public class ResponeReceiver extends BroadcastReceiver {
        public static final String ACTION_RESP = "MESSAGE_PROCESSED";
        //Log.d("MAP Size " , String.valueOf(locationMap.size()));
        Location lastLocation = new Location("");

        @Override
        public void onReceive(Context context, Intent intent) {
            double[] resultData = new double[2];
            resultData = intent.getDoubleArrayExtra(LocationUpdates.PARAM_OUT_MSG);
            HashMap<String, Object> hashmapobject =new HashMap<>();
            if(resultData[0] != 0.0 || resultData[1] != 0.0) {
               /* editor = sharedpreferences.edit();
                editor.putString("LATITUDE", String.valueOf(resultData[0]));
                editor.putString("LONGITUDE", String.valueOf(resultData[1]));
                editor.commit();*/

                lastLocation.setLatitude(resultData[0]);
                lastLocation.setLongitude(resultData[1]);

                hashmapobject.put("key", ccLocationMap);
                Log.i("LAT LON" , String.valueOf(resultData[0]) + " , " + String.valueOf(resultData[1]));
                Log.i("MAP Size" , String.valueOf(ccLocationMap.size()));
                mCurrentlocation = lastLocation;

                Intent i = new Intent(getContext(), NearestCCIntentService.class);
                i.putExtra("receiver", resultReceiver);
                i.putExtra("requestId", 101);
                i.putExtra("lastlocation",lastLocation);
                i.putExtra("cclocationsmap",hashmapobject);
                getActivity().startService(i);

                //temp.setText(String.valueOf(locationMap.size()));

            }
        }
    }

    public class LoadCC extends AsyncTask<Void,Void,Map<String, Location>> {
        Location location = new Location("");
        @Override
        protected Map<String, Location> doInBackground(Void... params) {
            mDatabaseReference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Map<String, Object> map = (HashMap<String, Object>)dataSnapshot.getValue();


                    location.setLatitude(Double.parseDouble(map.get("lat").toString()));
                    location.setLongitude(Double.parseDouble(map.get("lan").toString()));

                    ccLocationMap.put(map.get("name").toString() , location);

                    addressMap.put(map.get("name").toString(), map.get("address").toString());


                    Log.d("Map Size : ", String.valueOf(ccLocationMap.size()));
                    location = new Location("");
                    publishProgress();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });
            Log.d("Last Map Size : ", String.valueOf(ccLocationMap.size()));
            return ccLocationMap;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // tmptext.setText("Updating... " + String.valueOf(locationMap.size()));

        }

        @Override
        protected void onPostExecute(Map<String, Location> stringObjectMap) {
            super.onPostExecute(stringObjectMap);

        }
    }

}