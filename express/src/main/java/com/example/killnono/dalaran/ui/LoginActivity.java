package com.example.killnono.dalaran.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.killnono.dalaran.*;
import com.example.killnono.dalaran.task.GetMeTask;
import com.example.killnono.dalaran.task.LoginTask;
import com.example.killnono.dalaran.ui.base.BaseActivity;
import com.example.killnono.dalaran.ui.base.ProgressSubscriber;
import com.example.killnono.dalaran.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText             mPasswordView;
    private View                 mProgressView;
    private View                 mLoginFormView;

    private ProgressSubscriber<JSONObject> loginSubscriber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.name);
        mPasswordView = (EditText) findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.get_me_button).setOnClickListener(this);


    }


    /**
     * 登录
     */
    private void login() {
        loginSubscriber = new ProgressSubscriber<JSONObject>(this) {
            @Override
            public void onNext(JSONObject jsonObject) {
                Util.testLogThreadId("onNext");
                try {
                    String name = (String) jsonObject.get("name");
                    Toast.makeText(LoginActivity.this, name, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        };

        JSONObject requestBodyJO = new JSONObject();
        try {
            requestBodyJO.put("name", mEmailView.getText().toString()); // case insensitive
            requestBodyJO.put("password", mPasswordView.getText().toString());
            new LoginTask(requestBodyJO).subscribe(loginSubscriber);
        } catch (Exception e) {
            Log.e("NONO", "loginTest: ", e);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                login();
                break;
            case R.id.get_me_button:
                getMe();
                break;
        }
    }

    /**
     * 登录
     */
    private void getMe() {
        ProgressSubscriber subscriber = new ProgressSubscriber<JSONObject>(this) {
            @Override
            public void onNext(JSONObject jsonObject) {
                Util.testLogThreadId("onNext");
                try {
                    String name = (String) jsonObject.get("name");
                    Toast.makeText(LoginActivity.this, name, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        };

        new GetMeTask().subscribe(subscriber);


    }
}

