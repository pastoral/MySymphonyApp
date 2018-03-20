package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.MySymphonyApp;
import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.adapter.StoredNewsListAdapter;
import aboutdevice.com.munir.symphony.mysymphony.firebase.RemoteConfig;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;
import aboutdevice.com.munir.symphony.mysymphony.utils.DatabaseHandler;
import aboutdevice.com.munir.symphony.mysymphony.utils.DividerItemDecoration;
import aboutdevice.com.munir.symphony.mysymphony.utils.RecyclerTouchListener;

public class StoredNewsList extends AppCompatActivity {
    //DatabaseHandler databaseHandler;
    RecyclerView notificationRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<NotificationStore> notificationStoreList , sortedNotificationStoreList;
    LinearLayoutManager lm;
    //FastItemAdapter notificationStoreFastItemAdapter;
    DatabaseHandler databaseHandler;
    int totalSize, rowId;
    List<Integer> rowsToDelete;
    ArrayList<NotificationStore> notificationStoreKeyList;
    TextView noNitifaication;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private RemoteConfig remoteConfig;
    public ImageView headerImg;
    private AdView adViewStoredNews;

    int maxStoredNews = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_news_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        databaseHandler = new DatabaseHandler(getApplicationContext());

        notificationRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler);
        headerImg = (ImageView)findViewById(R.id.image_header) ;
        adViewStoredNews = (AdView)findViewById(R.id.adViewStoredNews);
        lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        noNitifaication = (TextView)findViewById(R.id.nonotification);
        noNitifaication.setVisibility(View.INVISIBLE);
        notificationRecyclerView.setLayoutManager(lm);
        notificationRecyclerView.setHasFixedSize(true);
        rowsToDelete = new ArrayList<Integer>();

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
        mFirebaseRemoteConfig = remoteConfig.getmFirebaseRemoteConfig();
        fetchRemoteConfig();

    }

    @Override
    protected void onStart() {
        super.onStart();

        // notificationRecyclerView.setAdapter(notificationStoreFastItemAdapter);
        notificationStoreList = databaseHandler.getAllNotifications();
       /*  if( databaseHandler.getNotificationCount() > maxStoredNews) {
           autoDeleteFromList();
        }*/

    }

    @Override
    protected void onResume() {
        super.onResume();

        Collections.reverse(notificationStoreList);
        // Reading all contacts
        Log.d("Reading: ", "Reading all contacts..");
        // List<NotificationStore> notificationStoreList = databaseHandler.getAllNotifications();
        /*for (NotificationStore ns : notificationStoreList)
        {
            String log = "Title : " + ns.getNotification_title() + " , Content:  " + ns.getNotification_content() +
                    " , ctivityToBeOpened: " + ns.getActivityToBeOpened();
            Log.d("Stored Data : ", log);
        }*/
        mAdapter = new StoredNewsListAdapter(getApplicationContext(), notificationStoreList);
        notificationRecyclerView.setLayoutManager(lm);
        notificationRecyclerView.setItemAnimator(new DefaultItemAnimator());
        notificationRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        // notificationStoreFastItemAdapter.add(notificationStoreList);
        notificationRecyclerView.setAdapter(mAdapter);

        int curSize = mAdapter.getItemCount();
        if(curSize < 1){
            noNitifaication.setVisibility(View.VISIBLE);
        }
        mAdapter.notifyDataSetChanged();

        notificationRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                long i = mAdapter.getItemId(position);
                //Toast.makeText(StoredNewsList.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                int id = Integer.valueOf(String.valueOf(i));
                List<NotificationStore> ns = databaseHandler.getRecord(DatabaseHandler.key_id, id);
                String str = null;
                String title = ns.get(0).getNotification_title();
                String content = ns.get(0).getNotification_content();
                String link = ns.get(0).getLink();
                String image_url = ns.get(0).getImage_url();
                String activityToBeOpened = ns.get(0).getActivityToBeOpened();
                String notification_type = ns.get(0).getNotification_type();

                if(notification_type.equals("promo")){
                    Intent intent = new Intent(getApplication(),NewsWebActivity.class);
                    intent.putExtra("targetUrl", link);
                    intent.putExtra("textData", title + "\n" + content);
                    startActivity(intent);
                }

                else if(notification_type.equals("fota")){
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

                else if(notification_type.equals("engage")){
                    Intent intent = new Intent(MySymphonyApp.getContext(), NewsActivity.class);
                    intent.putExtra("title", title);
                    intent.putExtra("body", content);
                    if(image_url != null){
                        intent.putExtra("IMAGEURL", image_url);
                    }
                    startActivity(intent);
                }
            }
        }));

        autoDeleteFromList();

    }

    public void autoDeleteFromList (){
        notificationStoreKeyList = (ArrayList<NotificationStore>) databaseHandler.getAllNotificationKeys();
        // NotificationStore[] notificationStoreKey= (NotificationStore[]) notificationStoreKeyList.toArray();
        int [] notificationStoreKeyArray = new int[notificationStoreKeyList.size()];
        for(int i =0; i <=notificationStoreKeyArray.length; i++ ){
            try {
                notificationStoreKeyArray[i] = notificationStoreKeyList.get(i).getId();
                // notificationStoreKeyArray[i] = Integer.parseInt(notificationStoreKeyList.get(i).toString());
                // String m = null;
            }
            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }
        }

        int j = notificationStoreKeyArray.length;

        for(int i = 0; i <=notificationStoreKeyArray.length - maxStoredNews; i++){
            rowsToDelete.add(notificationStoreKeyArray[i]);
        }
        databaseHandler.deleteRows(rowsToDelete);

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
}
