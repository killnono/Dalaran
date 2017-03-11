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

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 17/3/9
 * Time: 下午5:13
 * Version: 1.0
 */
public  abstract class SqlLocalStore implements ILocalStore<String,JSONObject> {
//    @Override
//    public Observable<Long> saveData(String s,JSONObject o) {
//        return null;
//    }
//
//    @Override
//    public Observable<String> findDataByIdentifier(String s ,@NonNull String identifier) {
//        return null;
//    }


//    @Override
//    public Observable<Long> saveData(String identifier, String content) {
//        Course course = new Course();
//        Realm realm = Realm.getDefaultInstance();
//            realm.beginTransaction();
//            realm.copyToRealmOrUpdate(t);
//            realm.commitTransaction();
//            size = realm.where(course.getClass()).findAll().size();//
//        return null;
//    }
//
//    @Override
//    public Observable<T> findDataByIdentifier(@NonNull String identifier) {
//        return null;
//    }


//    @Override
//    public Observable<Long> saveData() {
//        int size = 0;
//        try {
//            Realm realm = Realm.getDefaultInstance();
//            realm.beginTransaction();
//            realm.copyToRealmOrUpdate(t);
//            realm.commitTransaction();
//            size = realm.where(tClass).findAll().size();//            time = System.currentTimeMillis() - time;
//        } catch (Exception exception) {
////            e.onError(new XDBException(exception.getMessage()));
//        }
//
//        return null;
//    }
}
