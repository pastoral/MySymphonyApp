package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.app.ProgressDialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import android.support.design.widget.Snackbar;

import android.support.v4.app.Fragment;


import android.support.v4.view.ViewPager;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;


import aboutdevice.com.munir.symphony.mysymphony.adapter.TopNewsHolder;

import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;
import aboutdevice.com.munir.symphony.mysymphony.model.AppUser;
import aboutdevice.com.munir.symphony.mysymphony.model.StoredNews;

import aboutdevice.com.munir.symphony.mysymphony.utils.FetchJson;



/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class OneFragment extends Fragment {

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
   // public SimpleDraweeView userPhoto;
    public ImageView userPhoto;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private AppUser appUser;
    public Map<String,Object> userDataMap = new HashMap<String, Object>();
    private DatabaseReference dbRef,dbUserRef,dpTopNewsRef, dbOfferBannerRef;
    public static ProgressDialog mProgressDialog;
    public static boolean isActivityActive = false;

    //public PhotoViewAttacher mAttacher = new PhotoViewAttacher(imageView);
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter<StoredNews,TopNewsHolder> topNewsRecyclerAdapter;
    private Snackbar snackbar;
    private LinearLayout linear_content_holder,linear_cc_block;
    private StoredNews[] storedNews = new StoredNews[2];
    private StoredNews topBannerNews = new StoredNews();
    private Query query;
    //private SimpleDraweeView offer_img1, offer_img2;
    private ImageView offer_img1, offer_img2;
    public static SharedPreferences topSharedPreferences;
    public static SharedPreferences.Editor topEditor ;

    private ViewPager oneViewPager;
    private Button btnEditProfile;
    private LinearLayout linear_offer_banner;



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

    public ValueEventListener topCardListner = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onDataChange(DataSnapshot dataSnapshot) {
            int i = 0;
            for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                storedNews[i] = postSnapshot.getValue(StoredNews.class);
                //Glide.with(getActivity()).load(storedNews.getImageUrl()).asBitmap().into(offer_img1);
                i++;
            }
            linear_offerarea.setVisibility(View.VISIBLE);
            if(storedNews.length>1){

                topEditor = getContext().getSharedPreferences("mysymphonyapp_top", Context.MODE_PRIVATE).edit();
                topEditor.putString("title1",storedNews[0].getTitle());
                topEditor.putString("title2",storedNews[1].getTitle());
                topEditor.putString("content1",storedNews[0].getDescription());
                topEditor.putString("content2",storedNews[1].getDescription());
                topEditor.putString("image_url1",storedNews[0].getImageUrl());
                topEditor.putString("image_url2",storedNews[1].getImageUrl());
                topEditor.putString("activityToBeOpened1",storedNews[0].getActivityToBeOpened());
                topEditor.putString("activityToBeOpened2",storedNews[1].getActivityToBeOpened());
                topEditor.putString("notification_type1",storedNews[0].getType());
                topEditor.putString("notification_type2",storedNews[1].getType());

                topEditor.apply();
            }



        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public ValueEventListener offerBannerListner = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onDataChange(DataSnapshot dataSnapshot) {
            int i = 0;
            for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                topBannerNews = postSnapshot.getValue(StoredNews.class);
            }
            linear_offer_banner.setVisibility(View.VISIBLE);
            if(topBannerNews != null){

                topEditor = getContext().getSharedPreferences("mysymphonyapp_top", Context.MODE_PRIVATE).edit();
                topEditor.putString("title1",storedNews[0].getTitle());
                topEditor.putString("title2",storedNews[1].getTitle());
                topEditor.putString("content1",storedNews[0].getDescription());
                topEditor.putString("content2",storedNews[1].getDescription());
                topEditor.putString("image_url1",storedNews[0].getImageUrl());
                topEditor.putString("image_url2",storedNews[1].getImageUrl());
                topEditor.putString("activityToBeOpened1",storedNews[0].getActivityToBeOpened());
                topEditor.putString("activityToBeOpened2",storedNews[1].getActivityToBeOpened());
                topEditor.putString("notification_type1",storedNews[0].getType());
                topEditor.putString("notification_type2",storedNews[1].getType());

                topEditor.apply();
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

        //MainActivity.backstackFragTrack = "";
        contactline1 = (LinearLayout)view.findViewById(R.id.hotline1);
        contactline2 = (LinearLayout)view.findViewById(R.id.hotline2);

        featureArea = (LinearLayout)view.findViewById(R.id.linear_feature_block) ;
        contactArea = (LinearLayout)view.findViewById(R.id.linear_contact_us_block) ;

        linear_offerarea = view.findViewById(R.id.linear_offerarea);

        linear_offer_banner = view.findViewById(R.id.linear_offer_banner);
        main_user_email = view.findViewById(R.id.main_user_email);
        main_user_phone = view.findViewById(R.id.main_user_phone);
        main_user_name = view.findViewById(R.id.main_user_name);
        userPhoto = view.findViewById(R.id.userPhoto);
        gridLayoutManager = new GridLayoutManager(getContext(),2);
        gridLayoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linear_content_holder = view.findViewById(R.id.linear_content_holder);
        offer_img1 = view.findViewById(R.id.offer_img1);
        offer_img2 = view.findViewById(R.id.offer_img2);
        linear_cc_block = view.findViewById(R.id.linear_cc_block);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        // recyclerTopCard = view.findViewById(R.id.recyclerTopCard);
        dbRef = FirebaseDatabase.getInstance().getReference();
        dbUserRef = dbRef.child("users");
        dbUserRef.keepSynced(true);
        dpTopNewsRef = dbRef.child("news");
        dpTopNewsRef.keepSynced(true);
        dbOfferBannerRef = dbRef.child("news");
        dbOfferBannerRef.keepSynced(true);



        topSharedPreferences = getContext().getSharedPreferences("mysymphonyapp_top", Context.MODE_PRIVATE);


        dpTopNewsRef.orderByChild("top_card").equalTo("yes").limitToFirst(2).addValueEventListener(topCardListner);

        dbUserRef.orderByKey().equalTo(user.getUid()).limitToFirst(1).addValueEventListener(profileListener);



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

        if(topSharedPreferences.getString("image_url1", "null") != null && topSharedPreferences.getString("image_url2", "null") != null ){

            String image_url1 = topSharedPreferences.getString("image_url1", "https://firebasestorage.googleapis.com/v0/b/about-device.appspot.com/o/creatives%2Fslider_one.jpg?alt=media&token=64b9d22d-a435-45b0-9578-b7c10936c7bc");
            String image_url2 = topSharedPreferences.getString("image_url2", "https://firebasestorage.googleapis.com/v0/b/about-device.appspot.com/o/creatives%2Fslider_one.jpg?alt=media&token=64b9d22d-a435-45b0-9578-b7c10936c7bc");
            //offer_img1.setImageURI(Uri.parse(image_url1));
           // offer_img2.setImageURI(Uri.parse(image_url2));
            Picasso.with(getActivity())
                    .load(image_url1)
                    .fit()
                    .into(offer_img1);

            Picasso.with(getActivity())
                    .load(image_url2)
                    .fit()
                    .into(offer_img2);


        }
        offer_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offer1();
            }
        });

        offer_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offer2();
            }
        });

        if(user!=null){
            // Toast.makeText(getActivity(),user.getUid().toString(),Toast.LENGTH_LONG).show();

            if(!isActivityActive){
                showProgressDialog("Loading User Data ... ", getActivity());
            }




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
            btnEditProfile.setVisibility(View.VISIBLE);

            if (appUser.getPhotoURL() != null) {
                //Glide.with(getActivity()).load(appUser.getPhotoURL()).into(userPhoto);
                //userPhoto.setImageURI(Uri.parse(appUser.getPhotoURL()));
                Picasso.with(getActivity())
                        .load(appUser.getPhotoURL())
                        .fit()
                        .into(userPhoto);

            }

        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isActivityActive = false;
        if(dbUserRef != null){
            dbUserRef.removeEventListener(profileListener);
        }
        if(dpTopNewsRef != null) {
            dpTopNewsRef.removeEventListener(topCardListner);
        }
    }

    public void removeListnerFromClient(){

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
        snackbar = Snackbar.make(linear_content_holder, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

    public void offer1(){
        // String restoredText = topSharedPreferences.getString("mysymphonyapp_top", null);
        if (topSharedPreferences.getString("title1", null) != null) {
            String title = topSharedPreferences.getString("title1", "No Title");//"No Title" is the default value.
            String content = topSharedPreferences.getString("content1", "No Description");
            String image_url = topSharedPreferences.getString("image_url1", "https://firebasestorage.googleapis.com/v0/b/about-device.appspot.com/o/creatives%2Fslider_one.jpg?alt=media&token=64b9d22d-a435-45b0-9578-b7c10936c7bc");
            String activityToBeOpened = topSharedPreferences.getString("activityToBeOpened1", "NewsListActivity");
            String notification_type = topSharedPreferences.getString("notification_type1", "news");

            String link = "";

            if(haveNetworkConnection()) {
                if(notification_type.equals("news")||notification_type.equals("offer")){
                    Intent intent = new Intent(MySymphonyApp.getContext(), NewsActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("body", content);
                    if(image_url != null){
                        intent.putExtra("IMAGEURL", image_url);
                    }
                    startActivity(intent);
                }
                //startActivity(intent);
                if(notification_type.equals("fota")){
                    if(activityToBeOpened.equals("MediaTekFOTA")){
                        Intent LaunchIntent = MySymphonyApp.getContext().getPackageManager().getLaunchIntentForPackage("com.mediatek.systemupdate");
                        startActivity(LaunchIntent);
                    }
                    else if(activityToBeOpened.equals("SpedturmFOTA")){
                        Intent LaunchIntent = MySymphonyApp.getContext().getPackageManager().getLaunchIntentForPackage("com.megafone.systemupdate");
                        startActivity(LaunchIntent);
                    }
                    else if(activityToBeOpened.equals("UniversalFOTA")){
                        Intent LaunchIntent = MySymphonyApp.getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.gms");
                        startActivity(LaunchIntent);
                    }
                }
            }
            else{
                showSnack(false);
            }
        }
    }

    public void offer2(){
        // String restoredText2 = topSharedPreferences.getString("mysymphonyapp_top", null);
        if (topSharedPreferences.getString("title1", null) != null) {
            String title = topSharedPreferences.getString("title2", "No Title");//"No Title" is the default value.
            String content = topSharedPreferences.getString("content2", "No Description");
            String image_url = topSharedPreferences.getString("image_url2", "https://firebasestorage.googleapis.com/v0/b/about-device.appspot.com/o/creatives%2Fslider_one.jpg?alt=media&token=64b9d22d-a435-45b0-9578-b7c10936c7bc");
            String activityToBeOpened = topSharedPreferences.getString("activityToBeOpened2", "NewsListActivity");
            String notification_type = topSharedPreferences.getString("notification_type2", "news");

            String link = "";

            if(haveNetworkConnection()) {
                if(notification_type.equals("news")||notification_type.equals("offer")){
                    Intent intent = new Intent(MySymphonyApp.getContext(), NewsActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("body", content);
                    if(image_url != null){
                        intent.putExtra("IMAGEURL", image_url);
                    }
                    startActivity(intent);
                }
                //startActivity(intent);
                if(notification_type.equals("fota")){
                    if(activityToBeOpened.equals("MediaTekFOTA")){
                        Intent LaunchIntent = MySymphonyApp.getContext().getPackageManager().getLaunchIntentForPackage("com.mediatek.systemupdate");
                        startActivity(LaunchIntent);
                    }
                    else if(activityToBeOpened.equals("SpedturmFOTA")){
                        Intent LaunchIntent = MySymphonyApp.getContext().getPackageManager().getLaunchIntentForPackage("com.megafone.systemupdate");
                        startActivity(LaunchIntent);
                    }
                    else if(activityToBeOpened.equals("UniversalFOTA")){
                        Intent LaunchIntent = MySymphonyApp.getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.gms");
                        startActivity(LaunchIntent);
                    }
                }
            }
            else{
                showSnack(false);
            }
        }
    }

    public void loadTopCard(){

    }





}