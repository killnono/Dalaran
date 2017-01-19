package com.example.killnono.dalaran.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.killnono.dalaran.ui.base.BaseSubscriber;
import com.example.killnono.dalaran.R;
import com.example.killnono.dalaran.task.ChapterTask;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Context context;
    private static final String TAG = "NONO";
    String testImageURL = "http://7sbkoa.com2.z0.glb.qiniucdn.com/02_%E4%B8%89%E8%A7%92%E5%BD%A2%E7%9A%84%E4%B8%89%E8%BE%B9%E5%85%B3%E7%B3%BB_New-02.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);

        initViews();

    }


    private void initViews() {
        ImageView imageView = (ImageView) findViewById(R.id.img);
        findViewById(R.id.btn_login_page).setOnClickListener(this);
        Glide.with(this).load(R.drawable.feedback_mascot_continue_video).into(imageView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCourse();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private BaseSubscriber<JSONArray> courseSubscriber;

    private void getCourse() {
        courseSubscriber = new BaseSubscriber<JSONArray>() {

            @Override
            public void onNext(JSONArray jsonObject) {
                Log.i(TAG, "onNext: ");
                Toast.makeText(MainActivity.this, "getCourse", Toast.LENGTH_LONG).show();
            }

        };

        new ChapterTask("math", "七年级上", "人教版").subscribe(courseSubscriber);

//        courseSubscriber.unsubscribe();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_page:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
