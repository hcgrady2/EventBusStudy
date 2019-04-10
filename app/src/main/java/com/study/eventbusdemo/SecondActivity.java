package com.study.eventbusdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.study.eventbusdemo.core.EventBus;

/**
 * Created by hcw on 2019/4/9.
 * Copyright©hcw.All rights reserved.
 */

public class SecondActivity extends AppCompatActivity {

    Button second_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);

        second_btn = findViewById(R.id.second_btn);

        second_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventBean("one","twooo"));

                Log.i("EventBusDemo", "send: " + Thread.currentThread().getName());

            }


        });

    }
}
