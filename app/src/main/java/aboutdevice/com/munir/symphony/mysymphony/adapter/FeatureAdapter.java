package aboutdevice.com.munir.symphony.mysymphony.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;

/**
 * Created by munirul.hoque on 11/16/2016.
 */

public class FeatureAdapter extends RecyclerView.Adapter<FeatureAdapter.FeatureHolder> {
    LayoutInflater inflater;
    LinkedHashMap<Integer,String> featureMap;
    //Set entrySet;
    //Iterator iterator;
    //Set<Integer> featureMapkeys;

    public FeatureAdapter(Context context, LinkedHashMap<Integer,String> featureMap){
        this.inflater = LayoutInflater.from(context);
        this.featureMap = featureMap;
        //this.featureMapkeys = featureMap.keySet();

    }

    class FeatureHolder extends RecyclerView.ViewHolder{
        TextView featureText;
        ImageView featureIcon;
        public FeatureHolder(View view){
            super(view);
            featureText = (TextView)view.findViewById(R.id.feature_text);
            featureIcon = (ImageView)view.findViewById(R.id.feature_icon);
        }
    }

    @Override
    public FeatureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.feature_list, parent, false);
        return new FeatureHolder(view);
    }

    @Override
    public void onBindViewHolder(FeatureHolder holder, int position) {
        // holder.featureText.setText(featureMap.values());
        // String temp = featureMap.get(position);
        //  holder.featureIcon.setImageResource(Integer.valueOf(featureMap.get(temp)));
        List<String> featureList = new ArrayList<String>(featureMap.values());
        // String temp = featureList.get(position);
        holder.featureText.setText(featureList.get(position));

        List<Integer> iconList = new ArrayList<Integer>(featureMap.keySet());
        holder.featureIcon.setImageResource(iconList.get(position));

    }

    @Override
    public int getItemCount() {
        return featureMap.size();
    }
}
