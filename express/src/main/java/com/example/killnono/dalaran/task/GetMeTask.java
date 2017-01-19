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
package com.example.killnono.dalaran.task;

import com.example.killnono.dalaran.datastore.local.Course;
import com.example.killnono.dalaran.datastore.local.LocalStore;
import com.example.killnono.dalaran.datastore.remote.CourseApiService;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/18
 * Time: 下午7:32
 * Version: 1.0
 */
public class GetMeTask extends BaseTask {

    public void subscribe(Subscriber<JSONObject> subscriber) {

        Observable<JSONObject> localObservable = LocalStore.findDataByIdentifier("586c6a81df22245a76aa2097")
                .observeOn(Schedulers.computation())
                .map(new Func1<Course, JSONObject>() {
                    @Override
                    public JSONObject call(Course course) {
                        JSONObject jsonObject = null;
                        String content = course.getContent();
                        try {
                            jsonObject = new JSONObject(content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return jsonObject;
                    }
                });

        localObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);


        Observable<JSONObject> remoteObservable = CourseApiService.Factory.getInstance().
                getMe();

        remoteObservable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnNext(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject jsonObject) {
                        // TODO: killnono 17/1/19  cache
                        if (getIdentifier() == null) {
                            try {
                                String id = jsonObject.getString("_id");
                                setIdentifier(id);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        LocalStore.insertData(getIdentifier(), jsonObject.toString());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }


}
