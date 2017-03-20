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
package com.example.killnono.dalaran.domain.task.request;

import android.util.Log;

import com.example.killnono.dalaran.dataprovider.local.store.Course;
import com.example.killnono.dalaran.dataprovider.local.LocalStore;
import com.example.killnono.dalaran.dataprovider.local.store.CourseStore;
import com.example.killnono.dalaran.dataprovider.remote.apiservice.CourseApiService;
import com.example.killnono.dalaran.domain.task.request.strategy.StrategyType;

import org.json.JSONArray;
import org.json.JSONException;

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
public class ChapterRequest extends BaseRequest<JSONArray, Course, JSONArray> {

    private String sub;
    private String sem;
    private String pub;

    public ChapterRequest(String sub, String sem, String pub) {
        this.sub = sub;
        this.sem = sem;
        this.pub = pub;
    }


    @Override
    public Observable<JSONArray> remoteObservableOrigin() {
        return CourseApiService.Factory.create().getCourse(sub, sem, pub);
    }

    @Override
    protected Observable<Course> localObservableOrigin() {
        return CourseStore.getInstance().findDataByIdentifier(getCacheId());
    }


    @Override
    protected String buildCacheId() {
        return CourseApiService.CHAPTERS + sub + sem + pub;
    }

    @Override
    protected StrategyType getFlowStrategyType() {
        if (isExpired()) {
            return StrategyType.LOCAL_NET_SAVE;
        } else {
            return StrategyType.NO_LOCAL_NET_SAVE;
        }
    }

    @Override
    protected Consumer<JSONArray> cacheAction() {
        return new Consumer<JSONArray>() {
            @Override
            public void accept(@NonNull JSONArray jsonArray) throws Exception {
                if (getCacheId() != null) {
                    LocalStore.saveData(getCacheId(), jsonArray.toString()).
                            observeOn(Schedulers.io()).
                            subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(@NonNull Long count) throws Exception {
                                    if (count < 0) {
                                        Log.d("NONO", "缓存失败");
                                    } else {
                                        Log.d("NONO", "缓存成功");

                                    }
                                }
                            });
                }

            }
        };
    }


    @Override
    public Function<Course, JSONArray> dbDataMapFunc() {
        return new Function<Course, JSONArray>() {
            @Override
            public JSONArray apply(@NonNull Course course) throws Exception {
               /*step 1 : 结构化上层需要数据*/
                JSONArray jsonObject = null;
                String content = course.getData();
                try {
                    jsonObject = new JSONArray(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }

        };

    }


    @Override
    public Function<JSONArray, JSONArray> netDataMapFunc() {
        return new Function<JSONArray, JSONArray>() {
            @Override
            public JSONArray apply(JSONArray jsonObject) {
                return jsonObject;
            }
        };
    }


}
