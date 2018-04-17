package aboutdevice.com.munir.symphony.mysymphony.data.remote;

/**
 * Created by munirul.hoque on 12/04/2018.
 */

public class UserDataApiUtils {
    private UserDataApiUtils(){}
    public static final String USER_DATA_BASE_URL = "http://202.0.94.14:7890/api/erms/";
    public static UserDataAPIService getUserDataAPIServices(){
        return  UserDataRetrofitClient.geClient(USER_DATA_BASE_URL).create(UserDataAPIService.class);
    }
}
