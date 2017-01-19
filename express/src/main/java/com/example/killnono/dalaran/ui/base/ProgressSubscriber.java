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
package com.example.killnono.dalaran.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.util.Log;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/21
 * Time: 下午2:56
 * Version: 1.0
 */
public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> {

    private static final String TAG = "NONO";
    private Activity mCurrentActivity;

    private ProgressDialog dialog;

    public ProgressSubscriber(Activity activity) {
        this.mCurrentActivity = activity;
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        dismissDialog();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        dismissDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        showDialog();
    }

    private void showDialog() {
        Log.i(TAG, "showDialog: ");
        if (dialog == null) {
            dialog = ProgressDialog.show(mCurrentActivity, "登陆中", "请稍等", false, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Log.i(TAG, "pre onCancel: ");
                    unsubscribe();
                }
            });
        }
    }

    /**/
    private void dismissDialog() {
        Log.i(TAG, "dismissDialog: ");
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
