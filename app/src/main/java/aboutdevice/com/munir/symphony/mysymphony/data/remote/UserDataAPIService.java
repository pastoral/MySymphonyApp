package aboutdevice.com.munir.symphony.mysymphony.data.remote;



import javax.security.auth.callback.Callback;

import aboutdevice.com.munir.symphony.mysymphony.data.model.UserDataRemote;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

/**
 * Created by munirul.hoque on 12/04/2018.
 */

public interface UserDataAPIService {

    @POST("PostMySymAppUsrs_PoweredBy_EDISON_IT_0?")
    @FormUrlEncoded
    void saveUser(@Field("rowid") String rowid,
                                              @Field("uuid") String uuid,
                                              @Field("email") String email,
                                              @Field("imei") String imei,
                                              @Field("mac") String mac,
                                              @Field("msisdn") String msisdn,
                                              @Field("lat") String lat,
                                              @Field("lan") String lan);

    @POST("PostMySymAppUsrs_PoweredBy_EDISON_IT_0?")
    Call<UserDataRemote> storeUser(@Body UserDataRemote userDataRemote);

    @PUT("PostMySymAppUsrs_PoweredBy_EDISON_IT_0")
    Call<UserDataRemote> storeUserInfo(@Body UserDataRemote userDataRemote);

    @Headers({
            "Content-Type: application/json"
    })
    @GET("PostMySymAppUsrs_PoweredBy_EDISON_IT_0")
     Call<UserDataRemote> storeUserData(@Query("rowid")String rowid, @Query("uuid")String uuid,
                       @Query("email")String email, @Query("imei")String imei,
                       @Query("mac")String mac, @Query("msisdn")String msisdn,
                       @Query("lat")String lat, @Query("lan")String lan);

    @GET("PutMySymAppUsrs_PoweredBy_EDISON_IT_2")
    Call<UserDataRemote> editUserData( @Query("uuid")String uuid,
                                       @Query("email")String email, @Query("imei")String imei,
                                       @Query("mac")String mac, @Query("msisdn")String msisdn,
                                       @Query("lat")String lat, @Query("lan")String lan);
}
