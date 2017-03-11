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
package com.example.killnono.dalaran.dataprovider.local;

import android.support.annotation.NonNull;

import com.example.killnono.dalaran.exception.XDBException;
import com.example.killnono.dalaran.utils.Util;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.exceptions.RealmFileException;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/21
 * Time: 上午11:55
 * Version: 1.0
 */
public class LocalStore {


    /**
     *
     * @param identifier
     * @param content
     * @return
     */
    public static Observable<Long> saveData(final String identifier, final String content) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> e) throws Exception {
                long size = 0;
                long time = System.currentTimeMillis();
                Util.logMethodThreadId("saveData");
                Util.log("--> Start cache2Disk:[data]  " + content);
                Course course = new Course();
                course.setIdentifier(identifier);
                course.setContent(content);
                try {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(course);
                    realm.commitTransaction();
                    size = realm.where(Course.class).findAll().size();
                    time = System.currentTimeMillis() - time;
                    Util.log("<-- End cache2Disk(" + time + "):[data] " + content);
                } catch (Exception exception) {
                    e.onError(new XDBException(exception.getMessage()));
                }
                e.onNext(size);
                e.onComplete();
            }
        });
    }



    /**
     * 查找数据
     *
     * @param identifier
     * @return
     */
    public static Observable<Course> findDataByIdentifier(@NonNull final String identifier) {
        Observable<Course> courseObservable = Observable.create(new ObservableOnSubscribe<Course>() {
            @Override
            public void subscribe(ObservableEmitter<Course> e) throws Exception {
                Util.logMethodThreadId("findDataByIdentifier");
                long time = System.currentTimeMillis();
                Util.log("--> Start getCache2Disk: [identifier] = " + identifier);
                Realm realm = Realm.getDefaultInstance();
                Course course = realm.where(Course.class).equalTo("identifier", identifier).findFirst();
                /* log time */
                time = System.currentTimeMillis() - time;
                Course result = null;
                if (course != null) {
                    result = realm.copyFromRealm(course);
                }
                Util.log("<-- End getCache2Disk(" + time + "):" + "[identifier] = " + identifier + " [data] = " + course.getContent());

                e.onNext(result);
                e.onComplete();
            }


        });

        return courseObservable;

    }

    public static void deleteData() {

    }


}
