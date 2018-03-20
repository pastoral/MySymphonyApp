package aboutdevice.com.munir.symphony.mysymphony.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.model.NotificationStore;

/**
 * Created by munirul.hoque on 2/7/2017.
 */

public class StoredNewsListAdapter extends RecyclerView.Adapter<StoredNewsListAdapter.StoredNewsListHolder> {
    TextView notification_title, notification_content, id_hiddenid;
    ImageView icon_notification;
    LayoutInflater layoutInflater;
    List<NotificationStore> notificationStoreList;
    Context context;

    public StoredNewsListAdapter(Context context, List<NotificationStore> notificationStoreList){
        this.layoutInflater = LayoutInflater.from(context);
        this.notificationStoreList = notificationStoreList;
        this.context = context;
    }


    class StoredNewsListHolder extends RecyclerView.ViewHolder{

        public StoredNewsListHolder(View view){
            super(view);
            notification_title = (TextView)view.findViewById(R.id.notification_title);
            notification_content = (TextView)view.findViewById(R.id.notification_content);
            icon_notification = (ImageView)view.findViewById(R.id.id_notificationicon);
            id_hiddenid = (TextView)view.findViewById(R.id.id_hiddenid);
        }
    }


    @Override
    public StoredNewsListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //return null;
        View view = layoutInflater.inflate(R.layout.layout_stored_news_list,parent,false);
        return new StoredNewsListHolder(view);
    }

    @Override
    public void onBindViewHolder(StoredNewsListHolder holder, int position) {
        String t = notificationStoreList.get(position).getNotification_title();
        String b = notificationStoreList.get(position).getNotification_content();

        if(b.length() > 75){
            b= b.substring(0,75) + "  ...";
        }

        notification_title.setText(t);
        notification_content.setText(b);
        id_hiddenid.setText ( String.valueOf(notificationStoreList.get(position). getId()));
        if(notificationStoreList.get(position).getImage_url() != null){
            Picasso.with(context).load(notificationStoreList.get(position).getImage_url()).into(icon_notification);
        }
    }

    @Override
    public int getItemCount() {
        return notificationStoreList.size();
    }

    @Override
    public long getItemId(int position) {
       // return super.getItemId(position);
        return notificationStoreList.get(position).getId();
    }

    public int getId(int position){
       return notificationStoreList.get(position).getId();
   }

}
