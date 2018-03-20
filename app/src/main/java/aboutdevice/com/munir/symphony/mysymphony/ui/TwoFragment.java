package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.R;
import aboutdevice.com.munir.symphony.mysymphony.adapter.FeatureAdapter;
import aboutdevice.com.munir.symphony.mysymphony.utils.FetchJson;
import aboutdevice.com.munir.symphony.mysymphony.utils.SpecException;


/**
 * Created by munirul.hoque on 5/16/2016.
 */
public class TwoFragment extends Fragment  {
    public String modelName;
    TextView txtLat, txtLan, txtModelName ;
    RecyclerView recyclerView;
    private View view;
    private RecyclerView.Adapter mAdapter;
    public FetchJson fetchJson;
    private List<String> featureList = new ArrayList<String>();
    private LinkedHashMap<Integer,String> featureMap = new LinkedHashMap<Integer,String>();
    public TwoFragment (){
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_two,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        txtModelName = (TextView)view.findViewById(R.id.txtmodelname);
        recyclerView = (RecyclerView)view.findViewById(R.id.feature_recycler);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        modelName = getSystemProperty("ro.product.device");
        fetchJson = new FetchJson(getContext());

       /* if(!fetchJson.searchModelName(modelName)) {
            modelName = getSystemProperty("ro.product.name");
        }*/

        String read = fetchJson.readJSONFromAsset();
        try{
            fetchJson.jsonToMap(read);
            if(!fetchJson.searchModelName(modelName)) {
                modelName = getSystemProperty("ro.build.product");
            }
            featureList = fetchJson.getMapData(modelName);
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(fetchJson.searchModelName(modelName)){
            if(modelName.equals("P9_Plus")){
                modelName = "P9+";
            }
            txtModelName.setText(modelName);
            featureMap = fetchJson.createFeatureMap(featureList);
        }

        else{
            txtModelName.setText("This model is not enlisted");
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        if(featureMap.size() > 0){
            mAdapter = new FeatureAdapter(getContext(),featureMap);
            recyclerView.setAdapter(mAdapter);
            //Toast.makeText(getActivity(),se.exceptionSpec[2],Toast.LENGTH_SHORT).show();
        }

        else{
            txtModelName.setText("No feature found");
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

}
