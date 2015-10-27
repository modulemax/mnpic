package com.renkun.mnpic.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.renkun.mnpic.R;
import com.renkun.mnpic.util.Screenutil;

/**
 * Created by rk on 2015/10/13.
 */
public class TgClassifyAdapter extends ArrayAdapter {
    private Resources mResources;
    private LayoutInflater mLayoutInflater;
    private GridView mGridView;
    private Context mContext;
    //屏幕宽高
    private int widthPixels;
    private int heightPixels;
    //分类图片
    private static final int pic[]={R.drawable.pic_1,R.drawable.pic_2,R.drawable.pic_3,R.drawable.pic_4,
            R.drawable.pic_5,R.drawable.pic_6,R.drawable.pic_7};
    //分类标题
    private static final String title[]={"性感美女","韩日美女","丝袜美腿","美女照片",
                                            "美女写真","清纯美女","性感车模"};
    public TgClassifyAdapter(Context context, int resource) {
        super(context, resource);
        mContext=context;
        //图片宽高比3：4
        widthPixels= Screenutil.getScreenHeightANDheight(context)[0];
        heightPixels= (int) (widthPixels*1.4);
        mLayoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 7;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){

            convertView = mLayoutInflater.inflate(R.layout.fragment_tg_classify_item, null);
            holder=new Holder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (Holder) convertView.getTag();
        }
        holder.mTextView.setText(title[position]);
        holder.mImageView.setImageResource(pic[position]);
//        Picasso.with(mContext).load(pic[position])
//                .resize(widthPixels,heightPixels)
//                .centerCrop()
//                .into(holder.mImageView);
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
}
