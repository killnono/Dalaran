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
package com.example.killnono.dalaran.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.killnono.dalaran.XApplication;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/23
 * Time: 下午5:56
 * Version: 1.0
 */
public class PerferManager {


    /**
     * 默认偏好文件名
     */
    private static final String DEFAULT_SHARED_PREFERENCES_NAME = "x_shared_prefer";
    /**
     * 访问为私有模式
     */
    private static final int DEFAULT_FILE_MODE = Context.MODE_PRIVATE;


    public static boolean putString(String key, String value) {
        Context context = XApplication.mContext;
        if (context == null) return false;

        SharedPreferences sp = context.getSharedPreferences(
                DEFAULT_SHARED_PREFERENCES_NAME, DEFAULT_FILE_MODE);
        return sp.edit().putString(key, value).commit();
    }


    public static String getStringValue(String key, String defaultValue) {
        Context context = XApplication.mContext;
        if (context == null) return defaultValue;

        SharedPreferences sp = context.getSharedPreferences(
                DEFAULT_SHARED_PREFERENCES_NAME, DEFAULT_FILE_MODE);
        return sp.getString(key, defaultValue);
    }
}
