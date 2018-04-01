package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import aboutdevice.com.munir.symphony.mysymphony.R;

import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;
import aboutdevice.com.munir.symphony.mysymphony.model.AppUser;
import aboutdevice.com.munir.symphony.mysymphony.utils.FetchJson;


import static aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp.getContext;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class OneFragment extends Fragment {
   // private RecyclerView mRecyclerView;
   // private TileAdapter mTileAdapter;
   // private RecyclerView.LayoutManager mLayoutManager;
   // private TileSpacesItemDecoration tileSpacesItemDecoration;
   public LinearLayout contactline1, contactline2;
    public AdView mAdView;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    public  View view;
    private LinearLayout featureArea, contactArea;
    private String modelName;
    private FetchJson fetchJson;
    public  boolean modelFound;
    public LinearLayout linear_offerarea;
    public TextView main_user_name, main_user_phone,main_user_email;
    public ImageView userPhoto;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private AppUser appUser;
    public Map<String,Object> userDataMap = new HashMap<String, Object>();
    private DatabaseReference dbRef,dbUserRef;
    public static ProgressDialog mProgressDialog;
    public static boolean isActivityActive = false;
    //public PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);

    public ValueEventListener profileListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                appUser = postSnapshot.getValue(AppUser.class);
                if(appUser!=null) {
                    UpdateUI(appUser);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public OneFragment (){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_one,container,false);

        contactline1 = (LinearLayout)view.findViewById(R.id.hotline1);
        contactline2 = (LinearLayout)view.findViewById(R.id.hotline2);

        featureArea = (LinearLayout)view.findViewById(R.id.linear_feature_block) ;
        contactArea = (LinearLayout)view.findViewById(R.id.linear_contact_us_block) ;

        linear_offerarea = view.findViewById(R.id.linear_offerarea);

        main_user_email = view.findViewById(R.id.main_user_email);
        main_user_phone = view.findViewById(R.id.main_user_phone);
        main_user_name = view.findViewById(R.id.main_user_name);
        userPhoto = view.findViewById(R.id.userPhoto);



        contactline1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:16272"));
                startActivity(intent);
            }
        });

        contactline2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:09666700666"));
                startActivity(intent);
            }
        });

        //Set Height for the offer block
        double heightLinearOfferBlock = getResources().getDisplayMetrics().heightPixels / 4.3;
        LinearLayout.LayoutParams lpOfferBlock = (LinearLayout.LayoutParams)linear_offerarea.getLayoutParams();
        lpOfferBlock.height = (int)heightLinearOfferBlock;

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        modelFound = fetchJson.searchModelName(modelName);
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbUserRef = dbRef.child("users");
        dbUserRef.keepSynced(true);
        //setHasOptionsMenu(true);
       /* contactline1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:16272"));
                startActivity(intent);
            }
        });

        contactline2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0966670066"));
                startActivity(intent);
            }
        });*/

    }

    @Override
    public void onStart() {
        super.onStart();
        isActivityActive = true;

    }

    @Override
    public void onResume() {
        super.onResume();
        if(modelFound) {
            featureArea.setVisibility(View.VISIBLE);
        }

        if(user!=null){
           // Toast.makeText(getActivity(),user.getUid().toString(),Toast.LENGTH_LONG).show();

            if(!isActivityActive){
                showProgressDialog("Loading User Data ... ", getActivity());
            }

            dbUserRef.orderByKey().equalTo(user.getUid()).limitToFirst(1).addValueEventListener(profileListener);
//                    (new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                        appUser = postSnapshot.getValue(AppUser.class);
//                        UpdateUI(appUser);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });

        }

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

    public static void showProgressDialog(String message, Context context){
        if(mProgressDialog==null){
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage(message);
            mProgressDialog.setIndeterminate(true);
        }
        if(context != null) {
            mProgressDialog.show();
        }
    }

    public void hideProgressDialog(){
        if(mProgressDialog != null  && mProgressDialog.isShowing()){
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isActivityActive = false;
        hideProgressDialog();
    }

    public void UpdateUI(AppUser appUser){
        //String m = null;
        if(appUser!=null) {
            main_user_name.setText(appUser.getName());
            main_user_phone.setText(appUser.getPhoneNumber());
            main_user_email.setText(appUser.getEmail());
            if (appUser.getPhotoURL() != null) {
                Glide.with(getActivity()).load(appUser.getPhotoURL()).asBitmap().into(userPhoto);

//            Glide.with(getActivity()).load(appUser.getPhotoURL()).asBitmap().into(new SimpleTarget<Bitmap>() {
//                @Override
//                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                    //bitmap = resource;
//                    userPhoto.setImageBitmap(resource);
//                   // mAttacher = new PhotoViewAttacher(ivGirlDetail);
//                }
//            });
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isActivityActive = false;
        dbUserRef.removeEventListener(profileListener);
    }

    public void removeListnerFromClient(){

    }


}
