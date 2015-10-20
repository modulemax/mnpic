package com.renkun.mnpic.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.renkun.mnpic.R;
import com.renkun.mnpic.dao.DataProvider;
import com.renkun.mnpic.data.Api;
import com.renkun.mnpic.data.OkHttpClientManager;
import com.renkun.mnpic.module.Gallery;
import com.renkun.mnpic.ui.activity.PhotoDetailsActivity;
import com.renkun.mnpic.ui.adapter.PicListCursorAdapter;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class FeedFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private GridView mGridView;
    private PullToRefreshGridView mPullRefreshGridView;
    private PicListCursorAdapter mPicListCursorAdapter;

    private int NumColumns=1;
    private int PIC_CLASSIFY = 1;//图片类别
    private int PIC_ROWS = 6;//图片条数
    private int PIC_PAGE = 1;//图片分页
    private String MAX_ID;//当前数据里保存的最大该类目的图片id,所对应的Preference索引
    //MAX_ID所对应的Preference索引的值
    private int MAX_ID_NUM;
    private Cursor mCursor;
    //页面图片集，数据库地址
    private Uri mUri;

    public FeedFragment(int classify,int NumColumns) {
        // Required empty public constructor
        PIC_CLASSIFY=classify;
        MAX_ID="MAX_ID_"+PIC_CLASSIFY;
        this.NumColumns=NumColumns;
        mUri=Uri.parse(DataProvider.SCHEME + DataProvider.AUTHORITY + String.valueOf(PIC_CLASSIFY));

    }
    public FeedFragment(){
        mUri=Uri.parse(DataProvider.SCHEME + DataProvider.AUTHORITY + String.valueOf(PIC_CLASSIFY));

    }

    @Override
    public void onResume() {
        super.onResume();
        MAX_ID_NUM=getActivity().getPreferences(Context.MODE_APPEND).getInt(MAX_ID, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mPullRefreshGridView = (PullToRefreshGridView) inflater.inflate(R.layout.fragment_feed, container, false);
        mPullRefreshGridView.setOnRefreshListener(new OnGrideRefreshListener());
        mGridView = mPullRefreshGridView.getRefreshableView();

        mPicListCursorAdapter=new PicListCursorAdapter(getActivity(),mGridView);
        mGridView.setNumColumns(NumColumns);
        mGridView.setAdapter(mPicListCursorAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCursor.moveToPosition(position);
                Intent intent=new Intent(getActivity(), PhotoDetailsActivity.class);
                intent.setPackage(getActivity().getPackageName());
                intent.putExtra("id", mCursor.getInt(mCursor.getColumnIndex("id")));
                startActivity(intent);

            }
        });


        getLoaderManager().initLoader(0, null, this);
        //loadFirst();
        return mPullRefreshGridView;
    }


    private void loadFirst() {
        loadData();
    }

    private void loadNext() {
        loadData();
    }

    private void loadData() {
        final Request request = new Request.Builder()
                .url(String.format(Api.TNPIC_NEWS,MAX_ID_NUM,
                        PIC_ROWS, PIC_CLASSIFY))
                .get()
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build();
        Log.d("http",request.urlString());
        OkHttpClientManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }
            @Override
            public void onResponse(final Response response) throws IOException {
                //解析字符串
                String s=response.body().string();
                final Gallery jsonBean = OkHttpClientManager
                        .getJsonBean(s, Gallery.class);
                Log.d("http", jsonBean.tngou.size() + "///" + s);
                getActivity().getContentResolver()
                        .bulkInsert(mUri,
                                Gallery.getContentValues(jsonBean));
                MAX_ID_NUM=jsonBean.tngou.get(jsonBean.tngou.size()-1).id;
            }
        });

    }
    //Loader的3个方法
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),mUri,null, "galleryclass=?", new String[]{String.valueOf(PIC_CLASSIFY)}, "id desc");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursor=data;
        mPicListCursorAdapter.changeCursor(data);
        mPullRefreshGridView.onRefreshComplete();
        Snackbar.make(getView(),data.getCount()+"",Snackbar.LENGTH_LONG)
                .setAction("yes", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPicListCursorAdapter.changeCursor(null);
    }

    //Gride的刷新监听类
    private class OnGrideRefreshListener implements PullToRefreshBase.OnRefreshListener2<GridView> {
        @Override
        public void onPullDownToRefresh(final PullToRefreshBase<GridView> refreshView) {
            loadNext();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
            loadNext();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        ////MAX_ID所对应的Preference索引的值
        getActivity().getPreferences(Context.MODE_APPEND)
                .edit().putInt(MAX_ID,MAX_ID_NUM).commit();
    }
}
