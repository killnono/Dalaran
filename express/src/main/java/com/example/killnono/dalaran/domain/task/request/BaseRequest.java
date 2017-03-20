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

import android.util.Log;

import com.example.killnono.common.utils.LogUtils;
import com.example.killnono.dalaran.dataprovider.local.LocalStore;
import com.example.killnono.dalaran.domain.task.base.ITask;
import com.example.killnono.dalaran.domain.task.request.strategy.DataFlowStrategy;
import com.example.killnono.dalaran.domain.task.request.strategy.RequestStrategy;
import com.example.killnono.dalaran.domain.task.request.strategy.StrategyType;

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
 * T 网络返回的
 */
public abstract class BaseRequest<V, L, T> implements ITask<V> {


    private StrategyType mStrategyType;

    private String mCacheId;

    public BaseRequest() {
        mCacheId = buildCacheId();
    }


    public void setStrategyType(StrategyType strategyType) {
        mStrategyType = strategyType;
    }

    public BaseRequest(String cacheId) {
        this.mCacheId = cacheId;
    }

    protected boolean isExpired() {
        return true;
    }

    public String getCacheId() {
        return mCacheId;
    }

    protected void setCacheId(String identifier) {
        this.mCacheId = identifier;
    }

    protected abstract String buildCacheId();


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

    private DataFlowStrategy<V, L, T> buildFlowStrategy() {
        RequestStrategy.Builder<V, L, T> builder = new RequestStrategy.Builder<>();
        switch (getFlowStrategyType()) {
            case LOCAL:
                builder.setLocalObservable(localObservableOrigin());
                builder.setdbDataMapFunc(dbDataMapFunc());
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
                builder.setdbDataMapFunc(dbDataMapFunc());
                builder.setnetDataMapFunc(netDataMapFunc());
                break;
            default:
                builder.setRemoteObservable(remoteObservableOrigin());
        }
        return builder.createStrategy();
    }


    protected StrategyType getFlowStrategyType() {
        return StrategyType.NET;
    }


    protected void onInterceptorNetResponse(T v) {
        LogUtils.i("onInterceptorNetResponse");
    }

    protected Consumer<T> cacheAction() {
        Consumer<T> consumer = null;
        if (getCacheId() != null) {
            consumer = new Consumer<T>() {
                @Override
                public void accept(@NonNull T respones) throws Exception {
                    if (getCacheId() != null) {
                        LocalStore.saveData(getCacheId(), respones.toString()).
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

}
