package com.example.killnono.dalaran.datastore.remote;

import android.text.TextUtils;

import com.example.killnono.dalaran.UserInfo;
import com.example.killnono.dalaran.converter.JsonConverterFactory;
import com.example.killnono.dalaran.manager.PerferManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

    public static String Token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6IjU2ZmE0NDBlMzVjMTk4ZGU2NTkyN2E5MSIsInJvbGUiOiJzdHVkZW50IiwiaWF0IjoxNDc5NzA0MTQ4LCJleHAiOjE0ODIyOTYxNDh9.1XLm-z-JkNvvN8pwNCkVt8SGm5qKXAvSPN3ZnwPT3NQ";

    public static final String LOGIN = "login";

    public static final String COURSE = "chapters";

    String BASE_URL = "http://10.8.8.8:9000";

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
                    .addNetworkInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {

                            /* add request header*/
                            Request.Builder builder = chain.request().newBuilder();
                            String authorization = PerferManager.getStringValue("Authorization", null);
                            if (!TextUtils.isEmpty(authorization)) {
                                builder.addHeader("Authorization", authorization);
                            }
                            Request request = builder.addHeader("device", "test deviceId")
                                    .addHeader("eventTime", System.currentTimeMillis() + "")
                                    .addHeader("client-type", "android-app")
                                    .addHeader("client-version", "3.1.1").build();


                            /*save header Authorization */
                            Response originalResponse = chain.proceed(request);
                            authorization = originalResponse.header("Authorization");
                            if (!TextUtils.isEmpty(authorization)) {
                                PerferManager.putString("Authorization", authorization);//if Authorization is note empty,save prefer
                            }
                            return originalResponse;
                        }
                    })//网络拦截器,进行重定向等操作
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
    @POST(LOGIN)
    Observable<JSONObject> loginOb(@Body JSONObject requestJO);


    @GET(COURSE)
    Observable<JSONArray> getCourse(@Query("subject") String subject,
                                    @Query("semester") String semester,
                                    @Query("publisher") String publisher);


    //test done
    @POST(LOGIN)
    Call<JSONObject> loginJO(@Body JSONObject requestJO);


    @POST(LOGIN)
    Call<JSONObject> login1(@Body UserInfo requestJO);
}
