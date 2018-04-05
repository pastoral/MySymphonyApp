package aboutdevice.com.munir.symphony.mysymphony.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import aboutdevice.com.munir.symphony.mysymphony.R;
public class TopNewsHolder extends RecyclerView.ViewHolder {
   public ImageView offer_img1;
    public TopNewsHolder(View view){
        super(view);
        offer_img1 = view.findViewById(R.id.offer_img1);
    }
}
