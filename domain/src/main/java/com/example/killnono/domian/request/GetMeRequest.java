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


import com.example.killnono.common.datacore.net.apiservice.CourseApiService;
import com.example.killnono.dataprovider.local.Course;
import com.example.killnono.dataprovider.local.CourseStore;
import com.example.killnono.domian.request.strategy.StrategyType;

import org.json.JSONObject;

import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/18
 * Time: 下午7:32
 * Version: 1.0
 */
public class GetMeRequest extends UserInfoRelatedRequest<JSONObject> {


    @Override
    protected StrategyType getFlowStrategyType() {
        return StrategyType.LOCAL_NET_SAVE;
    }

    @Override
    protected Observable<Course> localObservableOrigin() {
        return CourseStore.getInstance().findDataByIdentifier(getCacheId());
    }

    @Override
    public Observable<JSONObject> remoteObservableOrigin() {
        return CourseApiService.Factory.getInstance().getMe();
    }


    @Override
    public Function<Course, JSONObject> dbDataMapFunc() {
       return BaseRequest.RXHelper.funcCourse2JSONObject();
    }



}
