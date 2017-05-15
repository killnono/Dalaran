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
package com.example.killnono.dataprovider.local;

import android.support.annotation.NonNull;

import com.example.killnono.common.datacore.local.IDBEngine;
import com.example.killnono.common.datacore.local.RealmClient;
import com.example.killnono.common.exception.XDBException;
import com.example.killnono.common.utils.Util;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.RealmObject;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 17/3/9
 * Time: 下午5:06
 * Version: 1.0
 */
public class CourseStore {

    private IDBEngine<RealmObject, Class<? extends RealmObject>> mIDBEngine;

    private static class SingletonHolder {
        private static final CourseStore INSTANCE = new CourseStore();
    }

    private CourseStore() {
        mIDBEngine = new RealmClient();
    }

    public static final CourseStore getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public Observable<Long> saveData(final String cacheId, final String content) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                long size = 0;
                /*start count time */
                long time = System.currentTimeMillis();
                Util.logMethodThreadId("saveData");
                Util.log("--> Start cache2Disk:[data]  " + content);
                Course course = new Course();
                course.setCacheId(cacheId);
                course.setData(content);
                try {
                    mIDBEngine.save(course, Course.class);
                    mIDBEngine.count(Course.class);
                    /*end count time */
                    time = System.currentTimeMillis() - time;
                    Util.log("<-- End cache2Disk(" + time + "):[data] " + content);
                    e.onNext(size);
                    e.onComplete();
                } catch (XDBException exception) {
                    e.onError(exception);
                }

            }
        });
    }


    /**
     * 查找数据
     *
     * @param cacheId
     * @return
     */
    public Observable<Course> findDataByIdentifier(@NonNull final String cacheId) {
        Observable<Course> courseObservable = Observable.create(new ObservableOnSubscribe<Course>() {
            @Override
            public void subscribe(ObservableEmitter<Course> e) throws Exception {
                Util.logMethodThreadId("findDataByIdentifier");
                long time = System.currentTimeMillis();
                try {
                    Course result = (Course) mIDBEngine.find(cacheId, Course.class);
                    time = System.currentTimeMillis() - time;
                    Util.log("<-- End getCache2Disk(" + time + "):" + "[identifier] = " + cacheId + " [data] = " + (result != null ? result.getData() : "null"));
                    if (result != null) {
                        e.onNext(result);
                    }
                    e.onComplete();
                } catch (XDBException d) {
                    e.onError(d);
                }
            }
        });
        return courseObservable;

    }


}
