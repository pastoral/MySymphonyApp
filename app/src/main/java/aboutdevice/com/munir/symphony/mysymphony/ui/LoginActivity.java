package aboutdevice.com.munir.symphony.mysymphony.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;

import aboutdevice.com.munir.symphony.mysymphony.BaseActivity;
import aboutdevice.com.munir.symphony.mysymphony.MainActivity;
import aboutdevice.com.munir.symphony.mysymphony.R;

import static aboutdevice.com.munir.symphony.mysymphony.Constants.RC_SIGN_IN;

public class LoginActivity extends BaseActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private RelativeLayout relativeLayout;
    private AuthUI.IdpConfig googleIdp;
    private AuthUI.IdpConfig facebookIdp;
    ArrayList<Object> userData;
    private LinearLayout llayout;
    public ConstraintLayout contraint_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        llayout = findViewById(R.id.login_activity_area);
        contraint_login = findViewById(R.id.contraint_login);

        firebaseAuth = FirebaseAuth.getInstance();
        googleIdp = new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER)
                .setPermissions(Arrays.asList(Scopes.PROFILE)).build();
        //facebookIdp = new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER)
               // .setPermissions(Arrays.asList("user_friends")).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = firebaseAuth.getCurrentUser();
        userData = new ArrayList<>();
        if(firebaseUser != null){
            //Signed in, launch the Sign In Activity
            // Name, email address, and profile photo Url
            userData.add(firebaseUser.getDisplayName());
            userData.add(firebaseUser.getEmail());
            userData.add(firebaseUser.getPhotoUrl());
            //pl = (Parcelable)userData;
            Intent intent = new Intent(this, MainActivity.class);
            //intent.putParcelableArrayListExtra("userdata", pl);
            // startActivity(new Intent(this, ProfileActivity.class));
            startActivity(intent);
            finish();
            return;
        }
        else{
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                    .setAvailableProviders(
                            Arrays.asList(
                                    new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    googleIdp
                                   // new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
                            ))
                    .setLogo(R.mipmap.ic_launcher)
                    .setTheme(R.style.LoginTheme)
                    .build(),RC_SIGN_IN);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if(resultCode== ResultCodes.OK){
                IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
                startActivity(new Intent(this,MainActivity.class)
                        .putExtra("my_token", idpResponse.getIdpToken()));
                finish();
                return;
            }
            else{
                // Sign in failed
                if(response==null){
                    // AppUser pressed back button
                    Snackbar.make(contraint_login,"Signin Cancelled", Snackbar.LENGTH_SHORT);
                    Log.d("Login Info" , "Signin Cancelled");
                }
                if (response!= null && response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Snackbar.make(contraint_login,"No Internet Connection", Snackbar.LENGTH_SHORT);
                    Log.d("Login Info" , "No Internet Connection");
                    new MaterialDialog.Builder(this)
                            .title("Login failed")
                            .content("No Internet Connection")
                            .neutralText("Close")
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .show();


                    return;
                }

                if (response==null || response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Snackbar.make(contraint_login,"Unknown Error", Snackbar.LENGTH_SHORT);
                    Log.d("Login Info" , "Unknown Error");

                    new MaterialDialog.Builder(this)
                            .title("Login failed")
                            .content("Unknown error occurred")
                            .neutralText("Close")
                            .onNeutral(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    finish();
                                }
                            })
                            .show();

                    return;
                }
            }
        }
    }
}
