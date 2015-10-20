package com.renkun.mnpic.ui.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.renkun.mnpic.R;
import com.renkun.mnpic.ui.fragment.FeedFragment;

public class DetailsClassifyActivity extends AppCompatActivity {
    private  int CLASSIFY;
    private TextView title;
    private static final String titles[]={"性感美女","韩日美女","丝袜美腿","美女照片",
            "美女写真","清纯美女","性感车模"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_details_classify);
        title= (TextView) findViewById(R.id.title);
        CLASSIFY=getIntent().getIntExtra("position",1);
        title.setText(titles[CLASSIFY-1]);
        setDefaultFragment();

    }
    private void setDefaultFragment()
    {
        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction transaction=fm.beginTransaction();
        FeedFragment feedFragment=new FeedFragment(CLASSIFY,2);
        transaction.replace(R.id.fragment_details,feedFragment);
        transaction.commit();
    }

}
