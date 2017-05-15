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

import com.example.killnono.dalaran.utils.Util;

import io.reactivex.disposables.Disposable;

/**
 * Created by Android Studio
 * User: killnono(陈凯)
 * Date: 16/11/21
 * Time: 下午2:56
 * Version: 1.0
 */
public abstract class ProgressSubscriber<T> extends BaseSubscriber<T> {

    private Activity       mCurrentActivity;
    private ProgressDialog mDialog;

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        showDialog();
    }

    public ProgressSubscriber(Activity activity) {
        this.mCurrentActivity = activity;
    }

    @Override
    public void onComplete() {
        super.onComplete();
        dismissDialog();
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        dismissDialog();
    }





    private void showDialog() {
        if (mDialog == null) {
            mDialog = ProgressDialog.show(mCurrentActivity, "登陆中", "请稍等", false, true, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    Util.log("ProgressDialog dialog is canceled");
                    mDisposable.dispose();
                }
            });
        }
    }

    /**
     * dismiss dialog
     */
    private void dismissDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
