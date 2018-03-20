package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import aboutdevice.com.munir.symphony.mysymphony.BaseActivity;
import aboutdevice.com.munir.symphony.mysymphony.R;

public class SelfCareActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_self_care);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
