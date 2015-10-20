package com.renkun.mnpic.ui.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.renkun.mnpic.R;
import com.renkun.mnpic.data.Api;
import com.renkun.mnpic.data.OkHttpClientManager;
import com.renkun.mnpic.module.Picture;
import com.renkun.mnpic.ui.adapter.PhotoViewPagerAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class PhotoDetailsActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private int id;//被点击图片的id,
    private Picture mPicture;
    private TextView mTextView;
    private PhotoViewPagerAdapter mPhotoViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_photo_details);
        mViewPager = (ViewPager) findViewById(R.id.photo_pager);
        mTextView= (TextView) findViewById(R.id.size);
        id = getIntent().getIntExtra("id", 1);
        loadPhoto();
    }
    private void loadPhoto(){
        Request request = new Request.Builder()
                .url(String.format(Api.TNPIC_SHOW, id))
                .get()
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();
        OkHttpClientManager.getInstance().getOkHttpClient()
                .newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                //解析字符串
                String s = response.body().string();
                mPicture = OkHttpClientManager.getJsonBean(s, Picture.class);
                mPhotoViewPagerAdapter=new PhotoViewPagerAdapter(PhotoDetailsActivity.this,
                        mPicture);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewPager.setAdapter(mPhotoViewPagerAdapter);
                        mTextView.setText(mPicture.title+mPicture.list.size());
                    }
                });

            }
        });
    }

}
