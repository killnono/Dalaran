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
package com.example.killnono.domian.request;

import android.util.Log;

import com.example.killnono.common.utils.LogUtils;
import com.example.killnono.dataprovider.local.Course;
import com.example.killnono.dataprovider.local.CourseStore;
import com.example.killnono.domian.base.ITask;
import com.example.killnono.domian.request.strategy.DataFlowStrategy;
import com.example.killnono.domian.request.strategy.RequestStrategy;
import com.example.killnono.domian.request.strategy.StrategyType;

import org.json.JSONArray;
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
 * Time: 下午7:33
 * Version: 1.0
 * V 上层需要的数据类型
 * L 本地数据
 * T 网络返回的
 */
public abstract class BaseRequest<V, L, T> implements ITask<V> {


    private StrategyType mStrategy = StrategyType.NET; /* default strategy : <Net/> */

    private String mCacheId;

    public BaseRequest() {
        mStrategy = getFlowStrategyType();
        mCacheId = buildCacheId();
    }

    /**
     * request task cloud config data fetch Strategy,
     * cloud  invoked by client.
     *
     * @param strategy
     * @return request
     */
    public BaseRequest<V, L, T> configStrategy(StrategyType strategy) {
        mStrategy = strategy;
        return this;
    }


    protected boolean isExpired() {
        return true;
    }

    public String getCacheId() {
        return mCacheId;
    }

    protected void setCacheId(String cacheId) {
        this.mCacheId = cacheId;
    }

    protected String buildCacheId() {
        return null;
    }


    /**
     * @param prefix
     * @param objects
     * @return
     */
    private String originCacheKey(String prefix, Object... objects) {
        StringBuilder stringBuilder = new StringBuilder(prefix);
        for (Object o : objects) {
            stringBuilder.append(o);
        }
        return stringBuilder.toString();
    }

    /**
     * 创建上层需要的 观察者
     *
     * @return
     */
    public Observable<V> createFinalFlowObservable() {
        return buildFlowStrategy().getFlowObservable();
    }

    /**
     * build fetch data strategy implement;
     *
     * @return DataFlowStrategy
     */
    private DataFlowStrategy<V, L, T> buildFlowStrategy() {
        RequestStrategy.Builder<V, L, T> builder = new RequestStrategy.Builder<>();
        switch (mStrategy) {
            case LOCAL:
                builder.setLocalObservable(localObservableOrigin());
                builder.setDbDataMapFunc(dbDataMapFunc());
                break;
            case NET_CACHE:
                builder.setRemoteObservable(remoteObservableOrigin());
                builder.setCacheAction(cacheAction());
                builder.setnetDataMapFunc(netDataMapFunc());
                break;
            case LOCAL_NET_SAVE:
                builder.setLocalObservable(localObservableOrigin());
                builder.setRemoteObservable(remoteObservableOrigin());
                builder.setCacheAction(cacheAction());
                builder.setDbDataMapFunc(dbDataMapFunc());
                builder.setnetDataMapFunc(netDataMapFunc());
                break;
            case NET:
                builder.setRemoteObservable(remoteObservableOrigin());
                builder.setnetDataMapFunc(netDataMapFunc());
                break;
            default:
                builder.setRemoteObservable(remoteObservableOrigin());

        }
        return builder.createStrategy();
    }


    /**
     * return strategy
     *
     * @return
     */
    protected StrategyType getFlowStrategyType() {
        return mStrategy;
    }


    /**
     * @param data
     */
    protected void onInterceptorNetResponse(T data) {
        LogUtils.i("onInterceptorNetResponse");
    }


    /**
     * build cache Action
     *
     * @return
     */
    protected Consumer<T> cacheAction() {
        Consumer<T> consumer = null;
        if (getCacheId() != null) {
            consumer = new Consumer<T>() {
                @Override
                public void accept(@NonNull T respones) throws Exception {
                    if (getCacheId() != null) {
                        CourseStore.getInstance().saveData(getCacheId(), respones.toString()).
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

        } else {
            return new Consumer<T>() {
                @Override
                public void accept(@NonNull T t) throws Exception {

                }
            };
        }
        return consumer;
    }


    /**
     * @return local origin Observable
     */
    protected Observable<L> localObservableOrigin() {
        return null;
    }

    /**
     * @return retrofit origin Observable
     */
    public Observable<T> remoteObservableOrigin() {
        return null;
    }

    /**
     * db data Type -> ui data type map
     *
     * @return
     */
    public Function<L, V> dbDataMapFunc() {
        return null;
    }

    /**
     * net data type -> ui data type map
     *
     * @return
     */
    public Function<T, V> netDataMapFunc() {
        return null;
    }

    static class RXHelper {

        static Function<Course, JSONArray> funcCourse2JSONArray() {
            return new Function<Course, JSONArray>() {
                @Override
                public JSONArray apply(@NonNull Course course) throws Exception {
                  /*step 1 : 结构化上层需要数据*/
                    JSONArray jsonObject = null;
                    if (course != null) {
                        String content = course.getData();
                        try {
                            jsonObject = new JSONArray(content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        jsonObject = new JSONArray();
                    }
                    return jsonObject;
                }

            };
        }


        public static Function<Course, JSONObject> funcCourse2JSONObject() {
            return new Function<Course, JSONObject>() {
                @Override
                public JSONObject apply(@NonNull Course course) throws Exception {
                     /*step 1 : 结构化上层需要数据*/
                    JSONObject jsonObject = null;
                    if (course != null) {
                        String content = course.getData();
                        try {
                            jsonObject = new JSONObject(content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        jsonObject = new JSONObject();
                    }
                    return jsonObject;
                }
            };
        }
    }
}
