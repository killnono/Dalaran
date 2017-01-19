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
package com.example.killnono.dalaran.datastore.local;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/21
 * Time: 上午11:55
 * Version: 1.0
 */
public class LocalStore {

    public static Observable<JSONObject> loginLocal() {
        final Observable<JSONObject> observable = Observable.create(new Observable.OnSubscribe<JSONObject>() {
            @Override
            public void call(Subscriber<? super JSONObject> subscriber) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("name", "chenkai");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(jsonObject);
            }
        });
        return observable;
    }

    public static void insertCourse(String type, String content) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Course course = realm.createObject(Course.class); // Create a new object
        course.setType(type);
        course.setContent(content);
        realm.commitTransaction();
    }

    public static Observable<JSONArray> findListByType(final String type) {
        Observable<JSONArray> observable = Observable.create(new Observable.OnSubscribe<JSONArray>() {
            @Override
            public void call(Subscriber<? super JSONArray> subscriber) {
                Realm realm = Realm.getDefaultInstance();
                RealmResults<Course> courses = realm.where(Course.class).equalTo("type",type).findAll();
                for (Course course :courses) {
                    try {
                        JSONObject jo = new JSONObject(course.getContent());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        subscriber.onError(e);
                    }

                }
            }
        });
        return observable;
    }


}
