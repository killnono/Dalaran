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

import com.example.killnono.common.exception.XDBException;
import com.example.killnono.common.utils.LogUtils;
import com.example.killnono.dalaran.manager.ToastManager;
import com.example.killnono.dalaran.utils.Util;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/21
 * Time: 下午2:56
 * Version: 1.0
 */
public abstract class BaseSubscriber<T> implements Observer<T> {

    Disposable mDisposable;

    @Override
    public void onSubscribe(Disposable d) {
        Util.logMethodThreadId("onSubscribe");

        this.mDisposable = d;
    }

    @Override
    public void onComplete() {
        LogUtils.d("onCompleted");
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.d("onError: " + e.getMessage());
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

            /* handle some code*/
            if (403 == code || 401 == code) {
                /* 去登录界面 */
            }

        } else if (e instanceof XDBException) { /*handle db exception*/
            ToastManager.show(e.getMessage());
        }
    }

}
