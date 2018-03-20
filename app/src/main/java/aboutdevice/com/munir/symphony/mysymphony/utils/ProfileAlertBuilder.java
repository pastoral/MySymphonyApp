package aboutdevice.com.munir.symphony.mysymphony.utils;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * Created by munirul.hoque on 8/20/2017.
 */

public class ProfileAlertBuilder extends AlertDialog.Builder {
    private Context mContext;
    private AlertDialog mAlertDialog;
    private String mEditedText;



    public ProfileAlertBuilder(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        //this.mEditedText = mEditedText;
    }
    public AlertDialog.Builder alertForEditText(String setText, String setTitle, String setMessage, EditText edittext){
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        edittext.setText(setText);
        alert.setMessage(setMessage);
        alert.setTitle(setTitle);
        alert.setView(edittext);
        return alert;
    }



}