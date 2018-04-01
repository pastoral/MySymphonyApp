package aboutdevice.com.munir.symphony.mysymphony.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import aboutdevice.com.munir.symphony.mysymphony.model.AppUser;

public class ProfileDataListner {
    public static AppUser appsUser;
    public ProfileDataListner(){}
    public ValueEventListener profileListener = new ValueEventListener() {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void onDataChange(DataSnapshot dataSnapshot) {
            for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                appsUser = postSnapshot.getValue(AppUser.class);

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

}
