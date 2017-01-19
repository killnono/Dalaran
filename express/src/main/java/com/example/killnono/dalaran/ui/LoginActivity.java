package com.example.killnono.dalaran.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.example.killnono.dalaran.*;
import com.example.killnono.dalaran.task.LoginTask;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

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
        findViewById(R.id.email_sign_in_button).setOnClickListener(this);


    }


    /**
     *
     */
    private void login() {
        loginSubscriber = new ProgressSubscriber<JSONObject>(this) {
            @Override
            public void onNext(JSONObject jsonObject) {
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
            case R.id.email_sign_in_button:
                login();
                break;
        }
    }
}

