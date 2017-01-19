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
package com.example.killnono.dalaran.ui.base;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.killnono.dalaran.manager.ToastManager;

import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/21
 * Time: 下午2:56
 * Version: 1.0
 */
public abstract class BaseSubscriber<T> extends Subscriber<T> {

    private static final String TAG = "NONO";

    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted: ");
    }

    @Override
    public void onError(Throwable e) {
        Log.i(TAG, "onError: " + e.getMessage());
        //handle http exception
        if (e instanceof HttpException) {
            Response response = ((HttpException) e).response();
            int code = response.code();
            String errorMsg = "";
            try {
                errorMsg = response.errorBody().string();
                JSONObject jsonObject = new JSONObject(errorMsg);
                if (jsonObject.has("msg")) {
                    errorMsg = jsonObject.getString("msg");
                } else if (jsonObject.has("message")) {
                    errorMsg = jsonObject.getString("message");
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (!TextUtils.isEmpty(errorMsg)) {
                ToastManager.show(errorMsg);
            }
        }

    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
    }
}