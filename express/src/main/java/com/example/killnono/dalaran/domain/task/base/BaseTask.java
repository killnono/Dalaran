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
package com.example.killnono.dalaran.domain.task.base;

import android.util.Log;

import com.example.killnono.dalaran.domain.task.strategy.DataFlowStrategy;
import com.example.killnono.dalaran.domain.task.strategy.LocalRemoteStrategy;
import com.example.killnono.dalaran.domain.task.strategy.StrategyType;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/18
 * Time: 下午7:33
 * Version: 1.0
 * V 上层需要的数据类型
 * T 网络返回的
 */
public abstract class BaseTask<V, L, T> implements ITask<V> {


    private StrategyType mStrategyType;

    private String mDataIdentifier;

    public BaseTask() {
        this(null);
    }

    public StrategyType getStrategyType() {
        return mStrategyType;
    }

    public void setStrategyType(StrategyType strategyType) {
        mStrategyType = strategyType;
    }

    public BaseTask(String dataIdentifier) {
        this.mDataIdentifier = dataIdentifier;
    }

    protected boolean isExpired() {
        return true;
    }

    public String getIdentifier() {
        return mDataIdentifier;
    }


    protected void setIdentifier(String identifier) {
        this.mDataIdentifier = identifier;
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
        LocalRemoteStrategy.Builder<V, L, T> builder = new LocalRemoteStrategy.Builder<>();
        switch (getFlowStrategyType()) {
            case LOCAL:
                builder.setLocalObservable(localObservableOrigin());
                builder.setdbDataMapFunc(dbDataMapFunc());
                break;
            case NET_CACHE:
                builder.setremoteObservable(remoteObservableOrigin());
                builder.setcacheAction(cacheAction());
                builder.setnetDataMapFunc(netDataMapFunc());
                break;
            case LOCAL_NET_SAVE:
                builder.setLocalObservable(localObservableOrigin());
                builder.setremoteObservable(remoteObservableOrigin());
                builder.setcacheAction(cacheAction());
                builder.setdbDataMapFunc(dbDataMapFunc());
                builder.setnetDataMapFunc(netDataMapFunc());
                break;
            default:
                builder.setremoteObservable(remoteObservableOrigin());
        }
        return builder.createStrategy();
    }


    protected StrategyType getFlowStrategyType() {
        return StrategyType.NET;
    }

    /**
     * cache disk action
     *
     * @return
     */
    protected Consumer<T> cacheAction() {
        return null;
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
        Log.w("", "");
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
