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
package com.example.killnono.common.datacore.local;


import com.example.killnono.common.exception.XDBException;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 17/3/9
 * Time: 下午5:13
 * Version: 1.0
 */
public class RealmClient implements IDBEngine<RealmObject, Class<? extends RealmObject>> {


    public RealmClient() {
    }

    @Override
    public void save(final RealmObject entity, Class<? extends RealmObject> aClass) throws XDBException {
//        Realm realm = Realm.getDefaultInstance();
        Realm realm = null;
        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(entity);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(entity);
//        realm.commitTransaction();
    }

    @Override
    public RealmObject delete(String cacheId, Class<? extends RealmObject> aClass) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmObject realmObject = realm.where(aClass).endsWith("cacheId", cacheId).findFirst();
        realmObject.deleteFromRealm();
        realm.commitTransaction();


        return null;
    }

    @Override
    public synchronized void update(final RealmObject entity, Class<? extends RealmObject> aClass) {

        Realm realm = null;
        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(entity);
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        realm.copyToRealmOrUpdate(entity);
//        realm.commitTransaction();
    }

    @Override
    public RealmObject find(final String cacheId, final Class<? extends RealmObject> aClass) {

        final RealmObject[] result = new RealmObject[1];
        Realm realm = null;
        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmObject realmObject = realm.where(aClass).equalTo("cacheId", cacheId).findFirst();
                    if (realmObject != null) {
                        result[0] = realm.copyFromRealm(realmObject);
                    }

                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

//        Realm realm = Realm.getDefaultInstance();
//        Course course = realm.where(Course.class).equalTo("cacheId", cacheId).findFirst();
//        Course result = null;
//        if (course != null) {
//            result = realm.copyFromRealm(course);
//        }
        return result[0];
    }

    @Override
    public long count(final Class<? extends RealmObject> aClass) {
        final long[] count = {0};
        Realm realm = null;
        try { // I could use try-with-resources here
            realm = Realm.getDefaultInstance();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    count[0] = realm.where(aClass).count();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }

        return count[0];

//        Realm realm = Realm.getDefaultInstance();
//        realm.beginTransaction();
//        return realm.where(aClass).count();
    }

}
