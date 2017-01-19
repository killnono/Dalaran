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

import com.example.killnono.dalaran.datastore.remote.CourseApiService;

import org.json.JSONArray;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/18
 * Time: 下午7:32
 * Version: 1.0
 */
public class CourseTask extends BaseTask {

    private String sub;
    private String sem;
    private String pub;

    public CourseTask(String sub, String sem, String pub) {
        this.sub = sub;
        this.sem = sem;
        this.pub = pub;
    }

    public void subscribe(Subscriber<JSONArray> subscriber) {


//        final Observable<JSONObject> localObservable = LocalStore.loginLocal();
//        localObservable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);

        if (isExpired()) {
            CourseApiService service = CourseApiService.Factory.create();
            final Observable<JSONArray> remoteObservable = service.getCourse(sub, sem, pub);
            remoteObservable.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread()).
                    subscribe(subscriber);
        }


    }

}
