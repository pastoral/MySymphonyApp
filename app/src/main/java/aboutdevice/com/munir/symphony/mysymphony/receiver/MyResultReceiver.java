package aboutdevice.com.munir.symphony.mysymphony.receiver;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.os.ResultReceiver;

/**
 * Created by munirul.hoque on 3/15/2017.
 */

public class MyResultReceiver extends ResultReceiver {
    private Receiver mReceiver;
    public MyResultReceiver(Handler handler){
        super(handler);
    }
    public void setmReceiver(Receiver mReceiver){
        this.mReceiver = mReceiver;
    }
    public interface Receiver{
        public void onReceiveResult(int resultCode, Bundle ResultData);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        //super.onReceiveResult(resultCode, resultData);
        if(mReceiver!=null){
            mReceiver.onReceiveResult(resultCode,resultData);
        }
    }
}
