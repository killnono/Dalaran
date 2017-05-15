package com.example.killnono.dataprovider.remote;

import com.example.killnono.common.datacore.net.XApiServiceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/10/28
 * Time: 下午4:28
 * Version: 1.0
 */
public interface OrderApiService {

    /* test */
    String BASE_URL = "http://10.8.8.8:9430";

    class Factory {
        private static OrderApiService sOrderApiService;

        public static synchronized OrderApiService getInstance() {
            if (sOrderApiService == null) {
                sOrderApiService = create();
            }
            return sOrderApiService;
        }

        private static final OrderApiService create() {
            return XApiServiceHelper.create(OrderApiService.class, BASE_URL);

        }
    }


    /**
     * @return
     */
    @POST("login")
    Observable<JSONObject> postEvent(@Body JSONArray jsonArray);

}
