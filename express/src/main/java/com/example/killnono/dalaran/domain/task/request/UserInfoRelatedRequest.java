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

import com.example.killnono.dalaran.dataprovider.local.store.Course;

import org.json.JSONObject;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 17/3/14
 * Time: 下午7:04
 * Version: 1.0
 */
public abstract class UserInfoRelatedRequest<V> extends BaseRequest<V, Course, JSONObject> {


    @Override
    public String getCacheId() {
        return super.getCacheId();
    }

    @Override
    protected void onInterceptorNetResponse(JSONObject v) {
        super.onInterceptorNetResponse(v);
        /* userInfoMannager ...*/
    }

//    @Override
//    protected Consumer<JSONObject> cacheAction() {
//
//        super.cacheAction();
//        return new Consumer<JSONObject>() {
//            @Override
//            public void accept(@NonNull JSONObject jsonObject) throws Exception {
//                try {
//                    String identifier = jsonObject.getString("_id");
//                    if (!TextUtils.isEmpty(identifier)) {
//                        setCacheId(identifier);
//                        /* cache */
//                        LocalStore.saveData(getCacheId(), jsonObject.toString()).
//                                observeOn(Schedulers.io()).
//                                subscribe(new Consumer<Long>() {
//                                    @Override
//                                    public void accept(@NonNull Long count) throws Exception {
//                                        if (count < 0) {
//                                            Log.w("NONO", "缓存失败");
//                                        } else {
//                                            Log.i("NONO", "缓存成功");
//                                        }
//                                    }
//                                });
//                    } else {
//                        LogUtils.w("userInfo d");
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//    }


}
