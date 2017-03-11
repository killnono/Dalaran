/**
 * Copyright (c) 2014 Guanghe.tv
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 **/
package com.example.killnono.dalaran.dataprovider.remote;

import android.text.TextUtils;

import com.example.killnono.dalaran.manager.PerferManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 17/1/19
 * Time: 下午12:48
 * Version: 1.0
 */
public class CommonInterceptor implements Interceptor {
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
}
