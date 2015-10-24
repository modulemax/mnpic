package com.renkun.mnpic.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.renkun.mnpic.R;
import com.renkun.mnpic.module.BDpic;
import com.renkun.mnpic.util.ColorUtil;
import com.renkun.mnpic.util.Screenutil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by rk on 2015/10/24.
 */
public class BDArrayAdapter extends ArrayAdapter {
    private Resources mResources;
    private LayoutInflater mLayoutInflater;
    private GridView mGridView;
    private Context mContext;
    //屏幕宽高
    private int widthPixels;
    private int heightPixels;
    //数据
    public ArrayList<BDpic.DATA> data;
    public BDArrayAdapter(Context context, int resource) {
        super(context, resource);
        mContext=context;
        //图片宽高比3：4
        widthPixels= Screenutil.getScreenHeightANDheight(context)[0];
        heightPixels= (int) (widthPixels*1.4);
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResources=mContext.getResources();
        data=new ArrayList<>();
    }
    @Override
    public int getCount() {
        if (data!=null&&data.size()>1)return data.size();
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){
            convertView = mLayoutInflater.inflate(R.layout.fragment_baidu_item, null);
            holder=new Holder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.mImageView.setBackgroundColor(
                mResources.getColor(ColorUtil.getColor()));
        Picasso.with(mContext)
                .load(getUrl(data.get(position)))
                .resize(widthPixels, heightPixels)
                .into(holder.mImageView);
        return convertView;
    }

    static class Holder{

        ImageView mImageView;
        TextView mTextView;

        public Holder(View view){
            mImageView= (ImageView) view.findViewById(R.id.item_pic);
            mTextView= (TextView) view.findViewById(R.id.item_text);
        }
    }
    //优先返回小图的url
    private String getUrl(BDpic.DATA data){
        String url=data.thumbnail_url;
        if (url!=null&&url.startsWith("http://imgt"))
            url=data.thumb_large_url;
        else return url;
        if (url!=null&&url.startsWith("http://imgt"))
            url=data.image_url;
        return url;
    }
}
