package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Bundle bundle;
    public Intent intent;
    public String ccName , ccAddress;
    public double latitude, langitude;
    private AdView adViewMaps;
    private RemoteConfig remoteConfig;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cc_maps);
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-4365083222822400~5968883777");
        adViewMaps = (AdView)findViewById(R.id.adViewMaps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        intent = getIntent();
        bundle = intent.getExtras();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        remoteConfig = new RemoteConfig();

        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        fetchRemoteConfig();

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    @SuppressWarnings({"MissingPermission"})
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        ccName = intent.getStringExtra("CCName") + " Customer Care";
        ccAddress = intent.getStringExtra("CCAddress");
        latitude = Double.parseDouble(intent.getStringExtra("Latitude"));
        langitude = Double.parseDouble(intent.getStringExtra("Longitude"));
        LatLng sydney = new LatLng(Double.parseDouble(intent.getStringExtra("Latitude")), Double.parseDouble(intent.getStringExtra("Longitude")));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,14));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
        mMap.addMarker(new MarkerOptions()
                .position(sydney)
                .title(intent.getStringExtra("CCName") + " Customer Care")
                .snippet(intent.getStringExtra("CCAddress"))
        ).showInfoWindow();
        mMap.setBuildingsEnabled(true);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        //else{
        //  return;
        //}


    }

    public void locationshare(View v){
        /*Double latitude = Double.parseDouble(intent.getStringExtra("Latitude"));
        Double longitude = Double.parseDouble(intent.getStringExtra("Longitude"));
        String label = intent.getStringExtra("CCName") + " Customer Care" + "\n" + intent.getStringExtra("CCAddress");
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(mapIntent);*/
        List<Intent> shareIntentsLists = new ArrayList<Intent>();
        Intent sendIntent = new Intent();

        sendIntent.setAction(Intent.ACTION_SEND);

        sendIntent.setType("*/*");
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(sendIntent,0);
        if(!resolveInfos.isEmpty()){
            for(ResolveInfo resInfo : resolveInfos){
                String packageName = resInfo.activityInfo.packageName;
                if(!packageName.contains("com.facebook.katana")){
                    Intent intent = new Intent();
                    if(packageName.contains("com.google.android.apps.maps")){
                        String label = ccName + "\n" + ccAddress;
                        String uriBegin = "geo:" + latitude + "," + langitude;
                        String query = latitude + "," + langitude + "(" + label + ")";
                        String encodedQuery = Uri.encode(query);
                        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                        Uri uri = Uri.parse(uriString);
                        intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                    }
                    else {
                        intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_SUBJECT, ccName);
                        intent.putExtra(Intent.EXTRA_TEXT, ccName + "\n" + ccAddress + "\n" + getString(R.string.sent_from) + "   " + getString(R.string.invitation_deep_link));
                        intent.setType("image/*");
                        intent.setPackage(packageName);
                    }
                    shareIntentsLists.add(intent);
                }
            }
            if(!shareIntentsLists.isEmpty()){

                Intent chooserIntent = Intent.createChooser(shareIntentsLists.remove(0), "Choose app to share");

                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentsLists.toArray(new Parcelable[]{}));

                startActivity(chooserIntent);

            }
            else{
                Log.e("Error", "No Apps can perform your task");
            }
        }
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
                adViewMaps.loadAd(adRequest);
            }
        }

        else{
            return;
        }

    }
}
