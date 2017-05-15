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
package com.example.killnono.domian.request.strategy;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 17/3/8
 * Time: 下午3:47
 * Version: 1.0
 */
public class RequestStrategy<V, L, T> implements DataFlowStrategy<V, L, T> {

    private Observable<L>  localObservableOrigin;
    private Observable<T>  remoteObservableOrigin;
    private Consumer<T>    cacheAction;
    private Function<L, V> dbDataMapFunc;
    private Function<T, V> netDataMapFunc;

    public static class Builder<V, L, T> {
        private Observable<L>  localObservable;
        private Observable<T>  remoteObservable;
        private Consumer<T>    cacheAction;
        private Function<L, V> dbDataMapFunc;
        private Function<T, V> netDataMapFunc;

        public Builder setLocalObservable(Observable<L> localObservable) {
            this.localObservable = localObservable;
            return this;
        }

        public Builder setRemoteObservable(Observable<T> remoteObservable) {
            this.remoteObservable = remoteObservable;
            return this;
        }

        public Builder setCacheAction(Consumer<T> cacheAction) {
            this.cacheAction = cacheAction;
            return this;
        }

        public Builder setDbDataMapFunc(Function<L, V> dbDataMapFunc) {
            this.dbDataMapFunc = dbDataMapFunc;
            return this;
        }

        public Builder setnetDataMapFunc(Function<T, V> netDataMapFunc) {
            this.netDataMapFunc = netDataMapFunc;
            return this;
        }

        public DataFlowStrategy<V, L, T> createStrategy() {
            return new RequestStrategy<>(this);
        }

    }


    private RequestStrategy(RequestStrategy.Builder<V, L, T> builder) {
        this.localObservableOrigin = builder.localObservable;
        this.remoteObservableOrigin = builder.remoteObservable;
        this.cacheAction = builder.cacheAction;
        this.dbDataMapFunc = builder.dbDataMapFunc;
        this.netDataMapFunc = builder.netDataMapFunc;
    }

    @Override
    public Observable<V> getFlowObservable() {
        Observable<V> result;
        Observable<V> localFlowObservable = getLocalFlow();
        Observable<V> remoteFlowObservable = getRemoteFlow();
        if (localFlowObservable != null && remoteFlowObservable != null) {
            result = Observable.concatArrayDelayError(getLocalFlow(), getRemoteFlow())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread(), true);
        } else {
            result = (localFlowObservable != null) ? localFlowObservable : remoteFlowObservable;
        }
        return result;
    }

    /**
     * build local data flow（origin，action，map...）
     *
     * @return local flow
     */
    private Observable<V> getLocalFlow() {
        if (localObservableOrigin == null)
            return null;
        Observable result = localObservableOrigin;
        if (dbDataMapFunc != null) {
            result = localObservableOrigin.map(dbDataMapFunc);
        }
        return result;
    }


    /**
     * build remote data flow（origin，action，map...）
     *
     * @return remote flow
     */
    private Observable<V> getRemoteFlow() {
        if (remoteObservableOrigin == null)
            return null;
        Observable result = remoteObservableOrigin;
        if (cacheAction != null) {
            result = remoteObservableOrigin.doOnNext(cacheAction);
        }
        if (netDataMapFunc != null) {
            result = result.map(netDataMapFunc);
        }
        return result;
    }


}
