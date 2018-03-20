package aboutdevice.com.munir.symphony.mysymphony.firebase;

import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import aboutdevice.com.munir.symphony.mysymphony.model.CCAddress;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.ADRESS;

/**
 * Created by munirul.hoque on 11/16/2016.
 */

public class FireBaseWorker {
    //public static FirebaseDatabase mDatabase;
    public  DatabaseReference mDatabaseReference;



    public DatabaseReference intDatabase(String path){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(ADRESS);

        return mDatabaseReference;
    }



}
