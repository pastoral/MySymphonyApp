package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Bundle;

import android.support.annotation.Keep;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

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

import com.google.android.gms.location.LocationSettingsRequest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import java.util.HashMap;

import java.util.List;
import java.util.Map;


import aboutdevice.com.munir.symphony.mysymphony.BaseActivity;
import aboutdevice.com.munir.symphony.mysymphony.Constants;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.firebase.FireBaseWorker;
import aboutdevice.com.munir.symphony.mysymphony.model.CCAddress;

import aboutdevice.com.munir.symphony.mysymphony.services.BackgroundLocationService;

import aboutdevice.com.munir.symphony.mysymphony.utils.CCAddressViewHolder;
import aboutdevice.com.munir.symphony.mysymphony.utils.DividerItemDecoration;

import aboutdevice.com.munir.symphony.mysymphony.utils.RecyclerTouchListener;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.REQUEST_CHECK_SETTINGS;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.isFirebaseReady;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permsRequestCode;
import static android.view.View.GONE;



/**
 * Created by munirul.hoque on 5/16/2016.
 */
@Keep
public class ThreeFragment extends Fragment {
    public String name;
    public int pos, scrollToPosition = 0;
    public ProgressBar progressBar, progressBarCC;
    public RecyclerView recyclerView;
    private List<CCAddress> ccAddressList;
    private LinearLayoutManager mLinearLayoutManager;
    private FireBaseWorker fireBaseWorker;
    private CCAddressViewHolder ccAddressViewHolder;
    private DatabaseReference mDatabaseReference;
    public FirebaseRecyclerAdapter<CCAddress,CCAddressViewHolder> firebaseRecyclerAdapter;
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
   // public CardView nearest_cc_card;
    public Query query;
    // public GpsLocationReceiver gpsLocationReceiver;
    //public String nearestCCName, nearestCCAddress;
   // public SharedPreferences sharedpreferences;
    //public  SharedPreferences.Editor editor;
    //BroadcastReceiver gpsLocationReceiver;
    public double nearestCCLat, nearestCCLan;
    private IntentFilter filter;
    //private ResponeReceiver receiver;
    //public MyResultReceiver resultReceiver;

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
       // nearest_cc_card = (CardView)view.findViewById(R.id.nearest_cc_header);

        // btnRefresh = (Button)view.findViewById(R.id.buttonRefresh);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //recyclerView.setLayoutManager(mLinearLayoutManager);

        recyclerView.setHasFixedSize(true);

        //filter = new IntentFilter(ResponeReceiver.ACTION_RESP);
        //filter.addCategory(Intent.CATEGORY_DEFAULT);

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

        //sharedpreferences = getContext().getSharedPreferences("mysymphonyapp_data", Context.MODE_PRIVATE);
//        receiver = new ResponeReceiver();
//        resultReceiver = new MyResultReceiver(new Handler());

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            //loadSP();
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //loadSP();
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
//        getActivity().registerReceiver(receiver,filter);
//        resultReceiver.setmReceiver(this);
        final Intent intent = new Intent(getActivity(),MapsActivity.class);
        //LoadCC loadCC = new LoadCC();
        //loadCC.execute();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CCAddress, CCAddressViewHolder>(
                CCAddress.class,
                R.layout.cc_list,
                CCAddressViewHolder.class,
                // mDatabaseReference.orderByChild("name")
                mDatabaseReference.orderByChild("name")
        ) {
            @Override
            public void populateViewHolder(CCAddressViewHolder viewHolder, CCAddress model, int position) {
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
            firebaseRecyclerAdapter.startListening();
            recyclerView.setAdapter(firebaseRecyclerAdapter);

            //loadSP();
        }



        IntentFilter filter = new IntentFilter("android.location.PROVIDERS_CHANGED");
        //this.getContext().registerReceiver(gpsLocationReceiver, filter);


    }



    @Override
    public void onPause() {
        super.onPause();


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




    private void startLocationUpdate(){
        Log.d("STARTLOC upade", "startLocationUpdate fired");


    }

    private void stopLocationUpdate(){
        Intent intent = new Intent(getContext(), BackgroundLocationService.class);

        intent.putExtra("requestId", 101);
        getActivity().stopService(intent);

    }



}