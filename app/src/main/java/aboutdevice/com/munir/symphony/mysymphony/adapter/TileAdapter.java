package aboutdevice.com.munir.symphony.mysymphony.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

import aboutdevice.com.munir.symphony.mysymphony.R;

/**
 * Created by munirul.hoque on 10/31/2016.
 */

public class TileAdapter extends RecyclerView.Adapter<TileAdapter.TileViewHolder> {
    private Context context;
    private Random mRandom = new Random();
    int[] imgList = {R.drawable.toolbar, R.drawable.features, R.drawable.customer_care};
    String[] nameList = {"Hotline", "Features", "Coustomer Care"};
    public TileAdapter(Context context){
        this.context = context;
    }
    public class TileViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        CardView cardView;
        public TileViewHolder(View view){
            super(view);
            imageView = (ImageView)view.findViewById(R.id.tileImage);
            textView = (TextView)view.findViewById(R.id.tileName);
            cardView = (CardView)view.findViewById(R.id.card_view);

        }
    }

    @Override
    public TileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View layoutView = LayoutInflater.from(parent.getContext().inflate(R))
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_home_tile,parent,false);
        return new TileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TileViewHolder holder, int position) {
        holder.cardView.getLayoutParams().height = getRandomIntInRange(290,125);
        holder.imageView.setImageResource(imgList[position]);
        holder.textView.setText(nameList[position]);
    }

    @Override
    public int getItemCount() {
        return nameList.length;
    }

    protected int getRandomIntInRange(int max, int min){
        return mRandom.nextInt((max-min)+min)+min;
    }

}
