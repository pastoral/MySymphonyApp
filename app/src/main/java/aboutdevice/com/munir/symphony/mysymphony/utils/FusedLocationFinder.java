package aboutdevice.com.munir.symphony.mysymphony.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import java.text.DateFormat;
import java.util.Date;

import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.ui.ThreeFragment;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.REQUEST_CHECK_SETTINGS;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permsRequestCode;


/**
 * Created by munirul.hoque on 11/21/2016.
 */

public class FusedLocationFinder implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {
    private static final String TAG = "FusedLocationFinder";
    public LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    public boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;
    public Location mCurrentlocation;
    private View view;
    private GoogleApiClient googleApiClient;
    private Context context;
    private Activity hostActivity;
    private ThreeFragment threeFragment;

    String permisionList[] = {"android.Manifest.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};

    public FusedLocationFinder(Context context, Activity hostActivity){
        this.context = context;
        this.hostActivity = hostActivity;
        //temporary... it might be called from onCreate
        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        //
    }

    public synchronized GoogleApiClient buildGoogleApiClient(){
        if(googleApiClient == null){
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        return googleApiClient;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public  LocationRequest createLocationRequest(){
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }


    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    public void buildLocationSettingRequest(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
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
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED :
                Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");
                try{
                    status.startResolutionForResult(hostActivity, REQUEST_CHECK_SETTINGS);
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


    /**
     * Requests location updates from the FusedLocationApi.
     */
    public void startLocationUpdates(){
        if(ContextCompat.checkSelfPermission(hostActivity,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(@NonNull Status status) {
                            mRequestingLocationUpdates = true;
                        }
                    });
        }
        else{
            Log.v(TAG,"Start Location update gets hampered :((");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentlocation = location;
        mLastUpdateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        ///////////////////////////////////////////Update your UI now ////////////////////////////////
        //threeFragment.updateUI();
        Log.v(TAG,"Last updated on " + mLastUpdateTime);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to GoogleApiClient");
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if(mCurrentlocation == null){
                mCurrentlocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                mLastUpdateTime = DateFormat.getDateTimeInstance().format(new Date());
            }
            threeFragment.mCurrentlocation = mCurrentlocation;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    public void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(hostActivity, permission)){
                ActivityCompat.requestPermissions(hostActivity, new String[]{permission},permsRequestCode);
            }
            else{
                ActivityCompat.requestPermissions(hostActivity, new String[]{permission},permsRequestCode);
            }
        }
        else{
            Log.v(TAG, " is already granted.");
        }
    }

    public void stopLocationUpdates(){
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        mRequestingLocationUpdates = false;
                    }
                }
        );
    }




}
