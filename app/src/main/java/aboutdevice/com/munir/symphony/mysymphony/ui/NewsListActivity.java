package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.BaseActivity;
import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.adapter.NewsHolder;
import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;
import aboutdevice.com.munir.symphony.mysymphony.model.StoredNews;
import aboutdevice.com.munir.symphony.mysymphony.utils.DividerItemDecoration;
import aboutdevice.com.munir.symphony.mysymphony.utils.RecyclerTouchListener;

import static aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp.getContext;
import static android.view.View.GONE;

/**
 * Created by munir on 3/28/2018.
 */

public class NewsListActivity extends BaseActivity {
    RecyclerView notificationRecyclerView;
    private RecyclerView.Adapter mAdapter;
    LinearLayoutManager lm;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    public ImageView headerImg;
    private AdView adViewStoredNews;
    TextView noNitifaication;
    static boolean newsCalledAlready = false;
    private NewsHolder newsHolder;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerAdapter<StoredNews,NewsHolder> newsRecyclerAdapter;
    public TextView notification_title, notification_content, id_hiddenid;
    public  ImageView icon_notification;
    public List<StoredNews> storedNewsList;
    private ProgressBar newsprogressbar;
    private Intent intent;
    private Snackbar snackbar;
    private CoordinatorLayout newscoordinator;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_news_list);
        notificationRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler);
        headerImg = (ImageView)findViewById(R.id.image_header) ;
        newsprogressbar = findViewById(R.id.newsprogressbar);
        adViewStoredNews = (AdView)findViewById(R.id.adViewStoredNews);
        newscoordinator = findViewById(R.id.newscoordinator);
        lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        //noNitifaication = (TextView)findViewById(R.id.nonotification);
        //noNitifaication.setVisibility(View.VISIBLE);

        //notificationRecyclerView.setHasFixedSize(true);

        remoteConfig = new RemoteConfig();
        //  notificationStoreFastItemAdapter = new FastItemAdapter();
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        // NotificationStore nsi = new NotificationStore();
        // databaseHandler.deleteAll(nsi);

        if (!newsCalledAlready)
        {
            FirebaseDatabase.getInstance();//.setPersistenceEnabled(true);
            newsCalledAlready = true;
        }


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //getContext().registerReceiver(gpsLocationReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("news");

        mDatabaseReference.keepSynced(true);


        //noNitifaication.setVisibility(View.INVISIBLE);
        newsRecyclerAdapter = new FirebaseRecyclerAdapter<StoredNews,NewsHolder>(
                StoredNews.class,
                R.layout.layout_stored_news_list,
                NewsHolder.class,
                mDatabaseReference.orderByChild("revTimeStamp")
        ){
            @Override
            protected void populateViewHolder(NewsHolder viewHolder, StoredNews model, int position) {
                String description = model.getDescription();
                //String title = model.getTitle();

                if(description.length() > 90){
                    description= description.substring(0,90) + "  ...";
                }
                viewHolder.notification_title.setText(model.getTitle());
                viewHolder.notification_content.setText(description);
                // Picasso.with(getApplicationContext()).load(viewHolder.icon_notification);
                viewHolder.icon_notification.setImageURI(Uri.parse(model.getImageUrl()));
                Glide.with(NewsListActivity.this).load(Uri.parse(model.getImageUrl())).into(viewHolder.icon_notification);
                newsprogressbar.setVisibility(GONE);
                notificationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), new RecyclerTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String title = newsRecyclerAdapter.getItem(position).getTitle();
                        String content = newsRecyclerAdapter.getItem(position).getDescription();
                        String link = "";
                        String image_url = newsRecyclerAdapter.getItem(position).getImageUrl();
                        String activityToBeOpened = newsRecyclerAdapter.getItem(position).getActivityToBeOpened();
                        String notification_type = newsRecyclerAdapter.getItem(position).getType();
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
                            showNoNetworkSnack(false);
                        }
                    }
                }));

            }
        };

        notificationRecyclerView.setLayoutManager(lm);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        newsRecyclerAdapter.notifyDataSetChanged();
        notificationRecyclerView.setAdapter(newsRecyclerAdapter);


        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        fetchRemoteConfig();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
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
                loadImageHeader();
                loadAdvertige();
            }
        });
    }

    private void loadImageHeader(){
        String imageHeaderURL = mFirebaseRemoteConfig.getString("get_news_image_header");
        if(imageHeaderURL.equals("none")){
            headerImg.setImageResource(R.drawable.stored_news);
        }
        else{
            Picasso.with(getApplicationContext()).load(imageHeaderURL).into(headerImg);
        }

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
                adViewStoredNews.loadAd(adRequest);
            }
        }

        else{
            return;
        }

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

    private void showNoNetworkSnack(boolean isConnected){
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
        snackbar = Snackbar.make(newscoordinator, message, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
        TextView textView = (TextView)sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();
    }

}
