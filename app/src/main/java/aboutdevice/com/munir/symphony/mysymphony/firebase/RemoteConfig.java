package aboutdevice.com.munir.symphony.mysymphony.firebase;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.ui.NewsActivity;

/**
 * Created by munirul.hoque on 2/13/2017.
 */

public class RemoteConfig {

    public String modelName;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public RemoteConfig (){

    }

    public FirebaseRemoteConfig getmFirebaseRemoteConfig(){
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(remoteConfigSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config);

        return  mFirebaseRemoteConfig;
    }




    public String getModelName(){
      String modelName = getSystemProperty("ro.product.device") ;
        return modelName;
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


}
