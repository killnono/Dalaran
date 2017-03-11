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
public class GetMeTask extends BaseTask<JSONObject, Course, JSONObject> {

    public GetMeTask() {
        super("58be60c585c2ab637abf3cbb");
    }


    @Override
    protected StrategyType getFlowStrategyType() {
        return StrategyType.LOCAL_NET_SAVE;
    }

    @Override
    protected Observable<Course> localObservableOrigin() {
        return LocalStore.findDataByIdentifier(getIdentifier());
    }

    @Override
    public Observable<JSONObject> remoteObservableOrigin() {
        return CourseApiService.Factory.getInstance().getMe();
    }


    @Override
    protected Consumer<JSONObject> cacheAction() {
        return new Consumer<JSONObject>() {

            @Override
            public void accept(JSONObject jsonObject) {
                /* cache*/
                Observable.just(jsonObject)
                        .observeOn(Schedulers.io())
                        .subscribe(new Consumer<JSONObject>() {
                            @Override
                            public void accept(JSONObject jsonObject) {
                                        /* 如果identfier 为null,表示可以将默认id作为 */
                                if (getIdentifier() != null) {
                                    LocalStore.saveData(getIdentifier(), jsonObject.toString());
                                }
                            }
                        });

            }

        };
    }

    @Override
    public Function<Course, JSONObject> dbDataMapFunc() {
        return new Function<Course, JSONObject>() {
            @Override
            public JSONObject apply(@NonNull Course course) throws Exception {
                /*step 1 : 结构化上层需要数据*/
                JSONObject jsonObject = null;
                String content = course.getContent();
                try {
                    jsonObject = new JSONObject(content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonObject;
            }

        };
    }

    @Override
    public Function<JSONObject, JSONObject> netDataMapFunc() {
        return new Function<JSONObject, JSONObject>() {
            @Override
            public JSONObject apply(JSONObject jsonObject) {
                return jsonObject;
            }
        };
    }


}
