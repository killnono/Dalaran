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

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.example.killnono.dalaran.XApplication;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/23
 * Time: 下午2:05
 * Version: 1.0
 */
public class ToastManager {

    private static Context sContext = XApplication.mContext;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static Toast sToast;

    public static void show(final String message) {
        show(message, Toast.LENGTH_SHORT);
    }

    public static void show(final String message, final int duration) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                if (sToast != null) {
                    sToast.cancel();
                }

                sToast = Toast.makeText(sContext, message, duration);
                sToast.setGravity(Gravity.CENTER, 0, 0);
                sToast.show();
            }
        });
    }

    public static void show(int resId) {
        show(sContext.getString(resId));
    }

    public static void show(int resId, int duration) {
        show(sContext.getString(resId), duration);
    }
}
