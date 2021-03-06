package com.renkun.mnpic.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.renkun.mnpic.R;
import com.renkun.mnpic.data.Api;
import com.renkun.mnpic.util.ColorUtil;
import com.renkun.mnpic.util.Screenutil;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Random;


/**
 * Created by rk on 2015/10/12.
 */
public class PicListCursorAdapter extends CursorAdapter {
    private Resources mResources;
    private LayoutInflater mLayoutInflater;
    private GridView mGridView;
    //屏幕宽高
    private int widthPixels;
    private int heightPixels;
    private SimpleDateFormat format=new SimpleDateFormat("MM-dd");


    public PicListCursorAdapter(Context context, GridView gridView) {
        super(context, null, false);

        mResources=context.getResources();
        mLayoutInflater=LayoutInflater.from(context);
        mGridView=gridView;
        //图片宽高比3：4
        widthPixels= Screenutil.getScreenHeightANDheight(context)[0];
        heightPixels= (int) (widthPixels*1.4);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

//    @Override
//    public Object getItem(int position) {
//        if (mDataValid && mCursor != null) {
//            mCursor.moveToPosition(position);
//            return mCursor;
//        } else {
//            return null;
//        }
//    }

    @Override
    public void changeCursor(Cursor cursor) {
        super.changeCursor(cursor);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return mLayoutInflater.inflate(R.layout.fragment_pic_item,null);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder=getHolder(view);
        holder.mImageView.setBackgroundColor(
                mResources.getColor(ColorUtil.getColor()));
        Picasso.with(context)
                .load(Api.TNPIC_http + cursor.getString(cursor.getColumnIndex("img")))
                .resize(widthPixels, heightPixels)
                .centerInside()
                .into(holder.mImageView);
//设置时间
        String[] strings=format.format(cursor.getLong(cursor.getColumnIndex("time"))).split("-");
        holder.item_time_day
                .setText(strings[1]);
        holder.item_time_month
                .setText(strings[0] + "月");
        holder.pic_size.setText(cursor.getInt(cursor.getColumnIndex("size"))+"P");

    }
    private Holder getHolder(final View view) {
        Holder holder = (Holder) view.getTag();
        if (holder == null) {
            holder = new Holder(view);
            view.setTag(holder);
        }
        return holder;
    }
    static class Holder{

        ImageView mImageView;
        TextView item_time_day;
        TextView item_time_month;
        TextView pic_size;
        public Holder(View view){
            mImageView= (ImageView) view.findViewById(R.id.item_pic);
            item_time_day= (TextView) view.findViewById(R.id.item_time_day);
            item_time_month= (TextView) view.findViewById(R.id.item_time_month);
            pic_size= (TextView) view.findViewById(R.id.pic_size);
        }
    }
}
