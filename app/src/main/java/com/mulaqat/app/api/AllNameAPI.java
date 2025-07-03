package com.mulaqat.app.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface AllNameAPI {
    @POST()
    Call<JsonObject> callApiWithBody(@Url String url, @Body RequestBody body);

    @GET()
    Call<JsonObject> callApiWithParam(@Url String url, @Query("id") String id);
    @GET()
    Call<JsonArray> callApiWithParamArr(@Url String url, @Query("sid") String id);
    @GET("msglist.php")
    Call<JsonObject> getChatList(@Query("sid") String id, @Query("rid") String rId);
    @POST("addmsg.php")
    Call<JsonObject> sendMess(@Body RequestBody rqBody);

    @GET("listing.php")
    Call<JsonArray> getAppList();
}