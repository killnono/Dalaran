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
package com.example.killnono.dalaran.domain.task;

import android.util.Log;

import com.example.killnono.dalaran.dataprovider.local.Course;
import com.example.killnono.dalaran.dataprovider.local.LocalStore;
import com.example.killnono.dalaran.dataprovider.remote.apiservice.CourseApiService;
import com.example.killnono.dalaran.domain.task.base.BaseTask;
import com.example.killnono.dalaran.domain.task.strategy.StrategyType;

import org.json.JSONException;
import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/18
 * Time: 下午7:32
 * Version: 1.0
 */
public class LoginTask extends BaseTask<JSONObject, Course, JSONObject> {

    private JSONObject requestData;

    public LoginTask(JSONObject requestData) {
        this.requestData = requestData;
    }

    @Override
    protected StrategyType getFlowStrategyType() {
        return StrategyType.NET_CACHE;
    }


    @Override
    public Observable<JSONObject> remoteObservableOrigin() {
        return CourseApiService.Factory.getInstance().loginOb(requestData);
    }


    @Override
    protected Consumer<JSONObject> cacheAction() {
        return new Consumer<JSONObject>() {
            @Override
            public void accept(@NonNull JSONObject jsonObject) throws Exception {
                String indentifier = getIdentifier();
                if (indentifier == null) {
                    try {
                        indentifier = jsonObject.getString("_id");
                        LocalStore.saveData(getIdentifier(), jsonObject.toString()).
                                observeOn(Schedulers.io()).
                                subscribe(new Consumer<Long>() {
                                    @Override
                                    public void accept(@NonNull Long count) throws Exception {
                                        if (count < 0) {
                                            Log.w("NONO", "缓存失败");
                                        } else {
                                            Log.i("NONO", "缓存成功");

                                        }
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

        };
    }

    @Override
    public Function<JSONObject, JSONObject> netDataMapFunc() {
        return new Function<JSONObject, JSONObject>() {
            @Override
            public JSONObject apply(@NonNull JSONObject jsonObject) throws Exception {
                return jsonObject;
            }
        };
    }


}
