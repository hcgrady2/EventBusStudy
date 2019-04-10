package com.study.eventbusdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.study.eventbusdemo.core.EventBus;
import com.study.eventbusdemo.core.Subscribe;
import com.study.eventbusdemo.core.ThreadMode;

public class MainActivity extends AppCompatActivity {

    Button main_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        main_btn = findViewById(R.id.main_btn);

        main_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.startActivity(new Intent(MainActivity.this,SecondActivity.class));
            }
        });
    }


    @Subscribe(threadMode = ThreadMode.BACKROUND)
    public void getEvent(EventBean bean){
        Log.i("EventBusDemo", "getEvent: " + Thread.currentThread().getName());
        Log.i("EventBusDemo", "getEvent: " + bean.toString());
    }
}
