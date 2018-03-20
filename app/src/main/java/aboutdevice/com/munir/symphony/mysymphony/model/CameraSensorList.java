package aboutdevice.com.munir.symphony.mysymphony.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by munirul.hoque on 1/30/2017.
 */

public class CameraSensorList {
   public Map<String, String> sensorList = new HashMap<>();
    public CameraSensorList(){
        sensorList.put("imx258","13MP");
        sensorList.put("s5k3l8", "5MP");
        sensorList.put("ar1335", "13MP");
        sensorList.put("s5k5e8", "5MP");
        sensorList.put("sp8407", "8MP");
        sensorList.put("sp5506", "5MP");
    }
}
