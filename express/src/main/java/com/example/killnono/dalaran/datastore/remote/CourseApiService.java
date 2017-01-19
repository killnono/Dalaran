package com.example.killnono.dalaran.datastore.remote;

import com.example.killnono.dalaran.UserInfo;
import com.example.killnono.dalaran.datastore.remote.converter.json.JsonConverterFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/10/28
 * Time: 下午4:28
 * Version: 1.0
 */
public interface CourseApiService {

    String BASE_URL = "http://10.8.8.8:9001";

    class Factory {
        private static CourseApiService sCourseApiService;

        public static synchronized CourseApiService getInstance() {
            if (sCourseApiService == null) {
                sCourseApiService = create();
            }
            return sCourseApiService;
        }

        public static final CourseApiService create() {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)//日志拦截器
                    .addNetworkInterceptor(new CommonIntercepter())//网络拦截器,进行重定向等操作
                    .connectTimeout(10, TimeUnit.SECONDS)//设置连接超时
                    .build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(JsonConverterFactory.create())//json数据转换
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//将Callable接口转换成Observable接口
                    .client(client)//网络请求客户端为okhttp
                    .build();
            return retrofit.create(CourseApiService.class);
        }
    }


    /**
     * @param requestJO
     * @return
     */
    @POST("login")
    Observable<JSONObject> loginOb(@Body JSONObject requestJO);


    @GET("chapters")
    Observable<JSONArray> getCourse(@Query("subject") String subject,
                                    @Query("semester") String semester,
                                    @Query("publisher") String publisher);

    @GET("me")
    Observable<JSONObject> getMe();


}
