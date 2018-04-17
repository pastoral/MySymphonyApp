package aboutdevice.com.munir.symphony.mysymphony.data.remote;

import aboutdevice.com.munir.symphony.mysymphony.data.model.UserDataRemote;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by munirul.hoque on 12/04/2018.
 */

public interface UserDataAPIService {

    @POST("PostMySymAppUsrs_PoweredBy_EDISON_IT_0")
    @FormUrlEncoded
    Call<UserDataRemote> saveUser(@Field("rowid") String rowid,
                                  @Field("uuid") String uuid,
                                  @Field("email") String email,
                                  @Field("imei") String imei,
                                  @Field("mac") String mac,
                                  @Field("msisdn") String msisdn,
                                  @Field("lat") String lat,
                                  @Field("lan") String lan);
    //@POST("User/DoctorLogin")
    //Call<LoginResult> getStringScalar(@Body LoginData body);
}
