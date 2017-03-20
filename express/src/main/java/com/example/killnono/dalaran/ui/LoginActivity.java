package com.example.killnono.dalaran.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.killnono.dalaran.R;
import com.example.killnono.dalaran.domain.task.request.ChapterRequest;
import com.example.killnono.dalaran.domain.task.request.GetMeRequest;
import com.example.killnono.dalaran.domain.task.request.LoginRequest;
import com.example.killnono.dalaran.domain.task.request.TestGroupTask;
import com.example.killnono.dalaran.ui.base.BaseActivity;
import com.example.killnono.dalaran.ui.base.BaseSubscriber;
import com.example.killnono.dalaran.ui.base.ProgressSubscriber;
import com.example.killnono.dalaran.utils.Util;

import org.json.JSONArray;
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


    private void pressureTest() {

        BaseSubscriber<JSONObject> baseSubscriber = new BaseSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {

            }
        };
        for (int i = 0; i < 1; i++) {
//            login();
            subscriberBindLife(new TestGroupTask(new GetMeRequest().createFinalFlowObservable(),
                            new ChapterRequest("math", "七年级上", "人教版").createFinalFlowObservable()).createFinalFlowObservable(),
                    baseSubscriber);
//            getMe();
//            getCourse();
        }
    }

    /**
     * 登录
     */
    private void login() {
        BaseSubscriber<JSONObject> loginSubscriber = new BaseSubscriber<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                Util.logMethodThreadId("onNext:");
                try {
                    String name = (String) jsonObject.get("name");
//                    Toast.makeText(LoginActivity.this, name, Toast.LENGTH_LONG).show();
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
            subscriberBindLife(new LoginRequest(requestBodyJO).createFinalFlowObservable(), loginSubscriber);
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
//                getMe(0);
                pressureTest();
                break;
        }
    }

    /**
     * 登录
     */
    private void getMe() {
        ProgressSubscriber<JSONObject> subscriber = new ProgressSubscriber<JSONObject>(this) {
            @Override
            public void onNext(JSONObject jsonObject) {
                Util.logMethodThreadId("onNext:getMe----");
                try {
                    String name = (String) jsonObject.get("name");
//                    Toast.makeText(LoginActivity.this, name, Toast.LENGTH_LONG).show();
                    Util.log("onNext==" + name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        };

        subscriberBindLife(new GetMeRequest().createFinalFlowObservable(), subscriber);
    }


    private void getCourse() {
        BaseSubscriber courseSubscriber = new BaseSubscriber<JSONArray>() {
            @Override
            public void onNext(JSONArray jsonObject) {
                Util.logMethodThreadId("onNext:getCourse----");
            }

        };
        subscriberBindLife(new ChapterRequest("math", "七年级上", "人教版").createFinalFlowObservable(), courseSubscriber);
    }

}

