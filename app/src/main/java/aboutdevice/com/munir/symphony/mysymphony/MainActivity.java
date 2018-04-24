package aboutdevice.com.munir.symphony.mysymphony;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.io.IOException;
import java.net.NetworkInterface;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import aboutdevice.com.munir.symphony.mysymphony.adapter.SectionAdapter;
import aboutdevice.com.munir.symphony.mysymphony.data.model.UserDataRemote;
import aboutdevice.com.munir.symphony.mysymphony.data.remote.UserDataAPIService;
import aboutdevice.com.munir.symphony.mysymphony.data.remote.UserDataApiUtils;
import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;
import aboutdevice.com.munir.symphony.mysymphony.model.AppUser;
import aboutdevice.com.munir.symphony.mysymphony.ui.FourFrgment;
import aboutdevice.com.munir.symphony.mysymphony.ui.LoginActivity;
import aboutdevice.com.munir.symphony.mysymphony.ui.NewsListActivity;
import aboutdevice.com.munir.symphony.mysymphony.ui.NewsWebActivity;
import aboutdevice.com.munir.symphony.mysymphony.ui.OneFragment;
import aboutdevice.com.munir.symphony.mysymphony.ui.ThreeFragment;
import aboutdevice.com.munir.symphony.mysymphony.ui.TwoFragment;
import aboutdevice.com.munir.symphony.mysymphony.ui.UserProfileActivity;
import aboutdevice.com.munir.symphony.mysymphony.utils.FetchJson;
import aboutdevice.com.munir.symphony.mysymphony.utils.ListTraverse;
import aboutdevice.com.munir.symphony.mysymphony.utils.ModelInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.Query;
import android.view.ViewGroup.LayoutParams;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.REQUEST_CHECK_SETTINGS;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.UPDATE_INTERVAL_IN_MILLISECONDS;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permisionList;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permsRequestCode;
import static aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp.getContext;
import static aboutdevice.com.munir.symphony.mysymphony.ui.FourFrgment.newFacebookIntent;


public class MainActivity extends BaseActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult>, LocationListener {
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public ViewPager mViewPager;

    private String modelName;
    private FetchJson fetchJson;
    public  boolean modelFound;
    private Button newsButton;
    public AdView mAdView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    private LinearLayout featureArea, contactArea;
    private SectionAdapter sectionAdapter;
    public static final  int REQUEST_INVITE = 1;
    private static final String TAG = "MainActivity";
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference databaseReference, dbUserRef;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Map<String, Object> userDataMap = new HashMap<String, Object>();
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    public AppUser appUser;
    public String imei = "";


    private boolean mRequestingLocationUpdates;
    protected LocationRequest mLocationRequest;
    public LocationSettingsRequest mLocationSettingsRequest;
    private Location mLastLocation;

    private static boolean isActivityActive = false;
    public ArrayList<String> existingImeiList = new ArrayList<>();
    public ArrayList<String> existingModelList = new ArrayList<>();
    public ArrayList<String> esitingMacList = new ArrayList<>();
    public String userLocality = "";
    private OneFragment oneFragment = new OneFragment();
    public static String backstackFragTrack ;
    private String macAddress = "00:00:00:00:00:00";
    private String brand = Build.BRAND;
    private BottomNavigationView bottomnavigation;
    public static UserDataRemote userDataRemote;
    private UserDataAPIService userDataAPIService = UserDataApiUtils.getUserDataAPIServices();
    private CoordinatorLayout main_content;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    private ImageView mainimageview;
    private LinearLayout header_area;
    private AppBarLayout appbarmain;




    public MainActivity(){

    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }

    public void setmViewPager(ViewPager mViewPager) {
        this.mViewPager = mViewPager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        if(user == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        MainActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);
        featureArea = (LinearLayout)findViewById(R.id.linear_feature_block) ;
        contactArea = (LinearLayout)findViewById(R.id.linear_contact_us_block) ;
        bottomnavigation = findViewById(R.id.bottomnavigation);
        main_content = findViewById(R.id.main_content);
        header_area = findViewById(R.id.header_area);
        mainimageview = findViewById(R.id.mainimageview);
        appbarmain = findViewById(R.id.appbarmain);
        //logoutText = findViewById(R.id.logout);
        remoteConfig = new RemoteConfig();



        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        invalidateOptionsMenu();
        //supportInvalidateOptionsMenu();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        dbUserRef = databaseReference.child("users");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getInstance().getReference();

        // newsButton = (Button)findViewById(R.id.buttonNews) ;


        modelName = getSystemProperty("ro.product.device");
        fetchJson = new FetchJson(getContext());
        String read = fetchJson.readJSONFromAsset();
        try{
            fetchJson.jsonToMap(read);
            if(!fetchJson.searchModelName(modelName)) {
                modelName = getSystemProperty("ro.build.product");
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }






        // Set up the ViewPager with the sections adapter.
        modelFound = fetchJson.searchModelName(modelName);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        setupViewPager(mViewPager);

//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.maincollapsing);
        collapsingToolbarLayout.setTitleEnabled(false);






        buildGoogleApiClient();
        createLocationRequest();

        // MainActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);
        isGooglePlayServicesAvailable(this);
        MobileAds.initialize(getContext(), "ca-app-pub-4365083222822400~7196026575");
        mAdView = (AdView)findViewById(R.id.adView);
        // mAdView.setAdSize(AdSize.BANNER);
        //mAdView.setAdUnitId("ca-app-pub-4365083222822400/8672759776");


        if(isGooglePlayServicesAvailable(this)){
            ////// -- Need to manage Invitaion code here -------/////

        }
        else{
            return;
        }

        bottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navhome:
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i);
                        break;
                    case R.id.navnews:
                        Intent j = new Intent(getApplicationContext(), NewsListActivity.class);
                        startActivity(j);
                        break;
                    case R.id.navcclocation:
                        if(mViewPager.getAdapter().getCount() ==4 ) {
                            mViewPager.setCurrentItem(2);
                        }
                        else{
                            mViewPager.setCurrentItem(1);
                        }
                        break;
                    case R.id.navlogout:
                        logout();
                        break;
                }
                return true;
            }
        });


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.maincollapsing);
        double heightLinearOfferBlock = getResources().getDisplayMetrics().heightPixels / 4.2;
        AppBarLayout.LayoutParams lpOfferBlock = (AppBarLayout.LayoutParams)collapsingToolbar.getLayoutParams();
        lpOfferBlock.height = (int)heightLinearOfferBlock;


    }

    @Override
    protected void onStart() {
        super.onStart();
        isActivityActive = true;
        mLastLocation = new Location("");

        //receiver = new ResponeReceiver();
        if(!mRequestingLocationUpdates){
            mRequestingLocationUpdates = true;
        }
        if(user == null){
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        else{
            // logoutText.setVisibility(View.VISIBLE);
            mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
            fetchRemoteConfig();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isGooglePlayServicesAvailable(this);
        checkLocationSettings();
        backstackFragTrack = "";

        // WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        //WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = getMACAdress();

        //Toast.makeText(this,macAddress,Toast.LENGTH_LONG).show();
        if(mRequestingLocationUpdates && mGoogleApiClient.isConnected()){
            startLocationUpdate();
        }

        if (user != null) {
            if (!isActivityActive) {
                //showProgressDialog("Loading user data....", MainActivity.this);
            }
            dbUserRef.orderByKey().equalTo(user.getUid()).limitToFirst(1).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        appUser = postSnapshot.getValue(AppUser.class);
                    }
                    if (user != null && appUser != null) {
                        getExistingImei();
                        updateOneSignal(appUser);
                        if (brand.contains("Symphony") || brand.contains("symphony") || brand.contains("SYMPHONY")) {
                            savePost(macAddress,
                                    appUser.getUid(),
                                    appUser.getEmail(),
                                    imei,
                                    macAddress,
                                    appUser.getPhoneNumber(),
                                    String.valueOf(appUser.getLat()),
                                    String.valueOf(appUser.getLan()));


                            if (appUser.getEmail() == null || appUser.getEmail().length()<4
                                    || appUser.getName() == null || appUser.getName().length()<1 ||
                                    appUser.getPhoneNumber() == null || appUser.getPhoneNumber().length()<5||
                                    appUser.getImei() == null || existingImeiList.size()<2) {

                                editPost(
                                        appUser.getUid(),
                                        appUser.getEmail(),
                                        imei,
                                        macAddress,
                                        appUser.getPhoneNumber(),
                                        String.valueOf(appUser.getLat()),
                                        String.valueOf(appUser.getLan()));

                            }
                            else {
                                return;
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }



    /**
     * A placeholder fragment containing a simple view.
     */

    private void setupViewPager(ViewPager viewPager) {
        sectionAdapter = new SectionAdapter(getSupportFragmentManager());
        sectionAdapter.addFrag(new OneFragment(),"Home");
        if(modelFound) {
            sectionAdapter.addFrag(new TwoFragment(), "Features");
            //featureArea.setVisibility(View.VISIBLE);
        }
        sectionAdapter.addFrag(new ThreeFragment(), "Customer Care");
        sectionAdapter.addFrag(new FourFrgment(), "Contact");
        viewPager.setAdapter(sectionAdapter);
    }

  /*  @Override
    public void onPermissionsGranted(int requestCode) {
        Toast.makeText(this, "Permissions Received.", Toast.LENGTH_LONG).show();
    }*/

    public boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                Dialog df = googleApiAvailability.getErrorDialog(activity, status, 2404);
                df.setCancelable(false);
                df.show();
                //googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
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


    public void loadNews(View view){
        //Intent intent = new Intent(getContext(), StoredNewsList.class);
        Intent intent = new Intent(getContext(), NewsListActivity.class);
        startActivity(intent);
    }

    public void loadInvitation(View view){
        //return;
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage(getString(R.string.invitation_message))
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();

        startActivityForResult(intent, REQUEST_INVITE);

    }



    private void fetchRemoteConfig() {
        long cacheExpiration = 3600;
        if(mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()){
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    mFirebaseRemoteConfig.activateFetched();

                } else {

                }
                loadAdvertige();
                loadImageHeader();

            }
        });
    }

    private void loadAdvertige() {
        boolean modelExists = false;
        boolean isAdmobOn = mFirebaseRemoteConfig.getBoolean("is_admob_on");
        String restrictedDevices = mFirebaseRemoteConfig.getString("disable_admob_for");
        List<String> restricted_device_list = Arrays.asList(restrictedDevices.split("\\s*,\\s*"));
        if(isAdmobOn){
            modelExists = restricted_device_list.contains(remoteConfig.getModelName());
            if(modelExists){
                return;
            }
            else{
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }
        }

        else{
            return;
        }

    }




    public void loadFeatureFragment(View v){


        mViewPager.setCurrentItem(1);

    }

    public void loadContactFragment(View v){


        if(mViewPager.getAdapter().getCount() ==4 ) {

            mViewPager.setCurrentItem(3);
        }
        else{
            mViewPager.setCurrentItem(2);
        }
    }

    public void loadCCFragment(View v){


        if(mViewPager.getAdapter().getCount() ==4 ) {
            mViewPager.setCurrentItem(2);
        }
        else{
            mViewPager.setCurrentItem(1);
        }
    }

    @Override
    public void onBackPressed() {

        if(backstackFragTrack.equals("")){

            finish();
        }
        else{
            Intent i  = new Intent(getApplicationContext(),MainActivity.class);
            backstackFragTrack = "";
            startActivity(i);
            // mViewPager.setCurrentItem(0);
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if(requestCode == REQUEST_INVITE){
            if(resultCode == RESULT_OK){
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode,data);
                for(String id : ids){
                    Log.d(TAG, "onActivityResult: sent invitation " + id);
                }
            }
            else{
                // Sending failed or it was canceled, show failure message to the user
                // [START_EXCLUDE]

                //showMessage(getString(R.string.send_failed));
                Toast.makeText(this,getString(R.string.send_failed),Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        }

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        //showMessage(getString(R.string.google_play_services_error));
        Toast.makeText(this,getString(R.string.google_play_services_error),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // mGoogleApiClient.stopAutoManage(this);
        // mGoogleApiClient.disconnect();
        oneFragment.removeListnerFromClient();
        //onBackPressedListener = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdate();
        //mGoogleApiClient.disconnect();
    }

    public void logout() {
        isActivityActive =false;
        //oneFragment.removeListnerFromClient();
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                });
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    public void checkLocationSettings() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,builder.build());

        result.setResultCallback(this);

    }

    protected LocationRequest createLocationRequest() {
        Log.i("Profile Activity : ", "createLocationRequest()");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(10);
        return mLocationRequest;
    }
    private void stopLocationUpdate(){
        /*Intent intent = new Intent(this, BackgroundLocationService.class);
        intent.putExtra("requestId", 101);
        stopService(intent);*/

        if(mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    private String getAddress(double latitude, double longitude) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses.size() > 0) {
                //Address address = addresses.get(0);
                // result.append(address.getLocality()).append("\n");
                // result.append(address.getCountryName());
                result.append(addresses.get(0).getAddressLine(0)+"#");
                result.append(addresses.get(0).getLocality() + "#");
                result.append(addresses.get(0).getCountryName());
                userLocality = addresses.get(0).getLocality();
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }

        return result.toString();
    }



    public void updateOneSignal(AppUser registeredUser){
        String modelName = "";
        String imei ="";
        String swVersion = "";
        String series = "";
        ModelInfo modelInfo = new ModelInfo();
        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            imei = modelInfo.getDeviceImei(mTelephonyManager);
        }
        else{
            imei = "UnAccessable";
        }
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if(modelInfo.getSystemProperty("ro.product.device").length()>0){
                modelName = modelInfo.getSystemProperty("ro.product.device");

            }
            else{
                modelName = modelInfo.getSystemProperty("ro.build.product");
            }
            series = modelName.substring(0, 1);
        }
        else{
            return;
        }
        swVersion = modelInfo.getSystemProperty("ro.build.display.id");
        OneSignal.sendTag("imei",imei);
        OneSignal.sendTag("model",modelName);
        OneSignal.sendTag("userType" , registeredUser.getUserCategoryText());
        OneSignal.sendTag("region" , userLocality);
        OneSignal.sendTag("swVersion" , swVersion);
        OneSignal.sendTag("series", series);
    }

    private void startLocationUpdate(){
        Log.d("STARTLOC update", "startLocationUpdate fired");
        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
       /* Intent intent = new Intent(this, BackgroundLocationService.class);
        intent.putExtra("requestId", 101);
        startService(intent);*/

    }

    public void getExistingImei(){

        String modelName = "";

        ModelInfo modelInfo = new ModelInfo();


        TelephonyManager mTelephonyManager = (TelephonyManager) getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

        if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            if(modelInfo.getSystemProperty("ro.product.device").length()>0){
                modelName = modelInfo.getSystemProperty("ro.product.manufacturer")+ " "+modelInfo.getSystemProperty("ro.product.device");
            }
            else{
                modelName = modelInfo.getSystemProperty("ro.product.manufacturer")+ " "+modelInfo.getSystemProperty("ro.build.product");
            }
            if(appUser!=null){
                for(int i=0; i<appUser.getImei().size(); i++){
                    existingImeiList.add(appUser.imei.get(i).toString());
                }
                for(int i=0; i<appUser.getModel().size(); i++){
                    existingModelList.add(appUser.model.get(i).toString());
                }
                for(int i=0; i<appUser.getMac().size(); i++){
                    esitingMacList.add(appUser.mac.get(i).toString());
                }

            }
            else{
                return;
            }
        }
        else{
            return;
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            imei = modelInfo.getDeviceImei(mTelephonyManager);
        }
        else{
            imei = "UnAccessable";
        }

        if(!existingImeiList.contains(modelInfo.getDeviceImei(mTelephonyManager))) {
            existingImeiList.add(imei);
            dbUserRef.child(user.getUid()).child("imei").setValue(existingImeiList);
            existingModelList.add(modelName);
            dbUserRef.child(user.getUid()).child("model").setValue(existingModelList);
            if (brand.contains("Symphony") || brand.contains("symphony") || brand.contains("SYMPHONY")) {
                //if(ListTraverse.getLast(esitingMacList)==null||ListTraverse.getLast(esitingMacList).isEmpty()) {
                esitingMacList.add(getMACAdress());
                // Toast.makeText(getApplicationContext(),getMACAdress(),Toast.LENGTH_LONG).show();
                dbUserRef.child(user.getUid()).child("mac").setValue(esitingMacList);
//                userDataRemote = new UserDataRemote(appUser.getUid() + "_" + randomString(4),
//                        appUser.getUid(),
//                        appUser.getEmail(),
//                        ListTraverse.getLast(existingImeiList),
//                        ListTraverse.getLast(esitingMacList),
//                        appUser.getPhoneNumber(),
//                        String.valueOf(appUser.getLat()),
//                        String.valueOf(appUser.getLan()));
//            }

            }
        }

       /* if(!userDataMap.get("providerId").toString().equals("phone")){
            if(modelInfo.isSimSupport(mTelephonyManager,getApplicationContext())) {
                String m = modelInfo.getPhoneNumber(getApplicationContext());
                dbUserRef.child(user.getUid()).child("phoneNumber").setValue(m);
            }
        }*/
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if(mRequestingLocationUpdates){
            startLocationUpdate();
        }
    }

    /**
     * Handles suspension of the connection to the Google Play services client.
     */
    @Override
    public void onConnectionSuspended(int cause) {
        mGoogleApiClient.connect();
        Log.d("Profile Activity: " , "Play services connection suspended");
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch(status.getStatusCode()){
            case LocationSettingsStatusCodes.SUCCESS :
                Log.i("Profile Activity : ", "All location settings are satisfied.");
                if(mRequestingLocationUpdates && mGoogleApiClient.isConnected()) {
                    startLocationUpdate();
                }
                //stopLocationUpdate();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED :
                Log.i("Profile Activity : ", "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try{
                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                }
                catch(IntentSender.SendIntentException e){
                    Log.i("Profile Activity : ", "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE :
                Log.i("Profile Activity : ", "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        String address = "";
        double lat = 0.0;
        double lan = 0.0;
        mLastLocation = location;
        if(mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lan = mLastLocation.getLongitude();
            address = getAddress(lat,lan);
        }

        try{
            if(address.length()>3){
                if(dbUserRef.child(user.getUid()).child("location").equals("Gaibandha")) {
                    dbUserRef.child(user.getUid()).child("location").setValue(address);
                }
                if(dbUserRef.child(user.getUid()).child("lat").equals("22.22")||dbUserRef.child(user.getUid()).child("lan").equals("44.44")) {
                    dbUserRef.child(user.getUid()).child("lat").setValue(lat);
                    dbUserRef.child(user.getUid()).child("lan").setValue(lan);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
    }

    private void getLocationData() {
        if (mLastLocation == null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
            }
        }
    }

    public void editProfile(View view){
        Intent i  = new Intent(this, UserProfileActivity.class);
        i.putExtra("USERDATA",appUser);
        startActivity(i);
    }

    public String getMACAdress(){
//        StringBuilder res1 = new StringBuilder();
//        try {
//            // get all the interfaces
//            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
//            //find network interface wlan0
//            for (NetworkInterface networkInterface : all) {
//                if (!networkInterface.getName().equalsIgnoreCase("wlan0")) continue;
//                //get the hardware address (MAC) of the interface
//                byte[] macBytes = networkInterface.getHardwareAddress();
//                if (macBytes == null) {
//                    return "";
//                }
//
//
//
//                for (byte b : macBytes) {
//                    //gets the last byte of b
//                    res1.append(Integer.toHexString(b & 0xFF) + ":");
//                }
//
//                if (res1.length() > 0) {
//                    res1.deleteCharAt(res1.length() - 1);
//                }
//                //return res1.toString();
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        return res1.toString();

        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    // res1.append(Integer.toHexString(b & 0xFF) + ":");
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            //handle exception
        }
        return "";

    }


    public void loadFacebook(View v){
        String url = "https://www.facebook.com/symphonymobile/";
        PackageManager pkm = getContext().getPackageManager();
        Intent i = newFacebookIntent(pkm,url);
        startActivity(i);
    }

    public void loadWeb(View v){
        String url = "https://www.symphony-mobile.com";
        // Intent i = new Intent(Intent.ACTION_VIEW);
        //i.setData(Uri.parse(url));
        //startActivity(i);
        Intent intent = new Intent(MySymphonyApp.getContext(), NewsWebActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("targetUrl", url);
        intent.putExtra("SYSTRAY","systray");

        MySymphonyApp.getContext().startActivity(intent);
    }

    public void loadInstagram(View v){
        Uri uri = Uri.parse("http://instagram.com/_u/symphony_mobile/");
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://instagram.com/symphony_mobile/")));
        }
    }

    private void savePost(String rowid, String uuid,
                          String email, String imei,
                          String mac, String msisdn,
                          String lat, String lan){
        userDataAPIService.storeUserData(rowid, uuid,
                email, imei,
                mac, msisdn,
                lat,  lan)
                .enqueue(new Callback<UserDataRemote>() {
                    @Override
                    public void onResponse(Call<UserDataRemote> call, Response<UserDataRemote> response) {
                        String m =response.raw().request().url().toString();
                        if(response.isSuccessful()){
                            //String m =response.raw().request().url().toString();
                            if (response.body() != null){

                                // Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //  Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Toast.makeText(getApplicationContext(), "Sorry for inconvince server is down", Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onFailure(Call<UserDataRemote> call, Throwable t) {
                        String m = t.getMessage();
                        //showSnack(main_content, t.getMessage());
                        //Toast.makeText(getApplicationContext(),"Userdata is not stored",Toast.LENGTH_LONG).show();
                    }
                });

    }


    private void editPost(String uuid,
                          String email, String imei,
                          String mac, String msisdn,
                          String lat, String lan){
        userDataAPIService.editUserData(uuid,
                email, imei,
                mac, msisdn,
                lat,  lan)
                .enqueue(new Callback<UserDataRemote>() {
                    @Override
                    public void onResponse(Call<UserDataRemote> call, Response<UserDataRemote> response) {
                        String m =response.raw().request().url().toString();
                        if(response.isSuccessful()){
                            String n =response.raw().request().url().toString();
                            if (response.body() != null){

                                // Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                //  Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Toast.makeText(getApplicationContext(), "Sorry for inconvince server is down", Toast.LENGTH_SHORT).show();
                        }

                    }


                    @Override
                    public void onFailure(Call<UserDataRemote> call, Throwable t) {
                        String m = t.getMessage();
                        //showSnack(main_content, t.getMessage());
                        //Toast.makeText(getApplicationContext(),"Userdata is not stored",Toast.LENGTH_LONG).show();
                    }
                });



    }



    String randomString( int len ){
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }

    private void loadImageHeader(){
        String imageHeaderURL = mFirebaseRemoteConfig.getString("get_main_image_header");
        if(imageHeaderURL.equals("none")){
            mainimageview.setImageResource(R.drawable.purple1);
        }
        else{
            Picasso.with(getApplicationContext()).load(imageHeaderURL).into(mainimageview);
        }

    }


}