package com.example.killnono.dataprovider.remote;


import com.example.killnono.common.datacore.net.XApiServiceHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/10/28
 * Time: 下午4:28
 * Version: 1.0
 */
public interface CourseApiService {

    String CHAPTERS = "chapters";
    String CVS      = "config/cvs";

    /* test */
//    String BASE_URL = "http://10.8.8.8:9430";
    String BASE_URL = "https://android-api-v4-0.yangcong345.com";

    class Factory {
        private static CourseApiService sCourseApiService;

        public static synchronized CourseApiService getInstance() {
            if (sCourseApiService == null) {
                sCourseApiService = create();
            }
            return sCourseApiService;
        }

        public static final CourseApiService create() {
            return XApiServiceHelper.create(CourseApiService.class, BASE_URL);
        }
    }


    /**
     * @param requestJO
     * @return
     */
    @POST("login")
    Observable<JSONObject> loginOb(@Body JSONObject requestJO);

    /**
     * @param requestJO
     * @return
     */
    @POST("login")
    Observable<Map<String, Object>> loginOb1(@Body Map<String, Object> requestJO);


    @GET(CHAPTERS)
    Observable<JSONArray> getCourse(@Query("subject") String subject,
                                    @Query("semester") String semester,
                                    @Query("publisher") String publisher);

    @GET(CVS)
    Observable<JSONArray> getCVS();


    @GET("chapters")
    Observable<List<Map<String, Object>>> getCourse1(@Query("subject") String subject,
                                                     @Query("semester") String semester,
                                                     @Query("publisher") String publisher);

    @GET("me")
    Observable<JSONObject> getMe();

    @GET("me")
    Observable<Map<String, Object>> getMe1();


}
