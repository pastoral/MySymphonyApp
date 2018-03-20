package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aboutdevice.com.munir.symphony.mysymphony.BaseActivity;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.model.AppUser;
import aboutdevice.com.munir.symphony.mysymphony.utils.ProfileAlertBuilder;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.permisionList;
import static aboutdevice.com.munir.symphony.mysymphony.Constants.permsRequestCode;

public class UserProfileActivity extends BaseActivity {
     private Toolbar toolbar;
    private String name, email, uid, editedEmail, userProvider;

    private TextView userName, userEmail, changeEmail, userLocation, phoneNumber;
    private ImageView userPhoto;
    private ProfileAlertBuilder profileAlertBuilder;
    private CoordinatorLayout coordinate_profile;
    private Button btnChangePic, button_change_password;
    private DatabaseReference databaseReference, dbUserRef;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Map<String, Object> userDataMap = new HashMap<String, Object>();
    public final int REQUEST_CODE_PICKER = 123;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference, profilepicReference;
    public AppUser appUser;


    private boolean mRequestingLocationUpdates;
    protected LocationRequest mLocationRequest;
    private SupportMapFragment mapFragment;
    public LocationSettingsRequest mLocationSettingsRequest;
    public PlaceAutocompleteFragment autocompleteFragment;
    //private ResponeReceiver receiver;
    private IntentFilter filter;

    private Location mLastLocation;




    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private GoogleApiClient mGoogleApiClient;
    private String placeName,vicinity = null;
    private static boolean isActivityActive = false;
    public ArrayList<String> existingImeiList = new ArrayList<>();
    public ArrayList<String> existingModelList = new ArrayList<>();
    public String userLocality = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //toolbar =  findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        UserProfileActivity.super.requestAppPermissions(permisionList, R.string.runtime_permissions_txt, permsRequestCode);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        dbUserRef = databaseReference.child("users");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getInstance().getReference();
    }
}
