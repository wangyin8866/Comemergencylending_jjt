package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

/**
 * Created by User on 2016/8/17.
 */
public class GridviewAdapter extends BaseAdapter {
    private Context mContext;
    private String mShowText[];
    private int mPic[];
    private String mState[];
    private int mStatepic[];
    LayoutInflater inflater;

    public GridviewAdapter(Context context, String showText[], int pic[], String state[], int statepic[]) {
        this.mContext = context;
        this.mShowText = showText;
        this.mPic = pic;
        this.mState = state;
        this.mStatepic = statepic;
        inflater = LayoutInflater.from(context);
    }
    
    


    @Override
    public int getCount() {
        return mShowText.length;
    }

    @Override
    public Object getItem(int position) {
        return mShowText[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder viewholder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_griditem, null);
            viewholder = new Viewholder();
            viewholder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
            viewholder.tv_personal = (TextView) convertView.findViewById(R.id.textView3);
            viewholder.iv_state = (ImageView) convertView.findViewById(R.id.iv_states);
            viewholder.tv_state = (TextView) convertView.findViewById(R.id.tv_date);
          
            convertView.setTag(viewholder);
        } else {
            viewholder = (Viewholder) convertView.getTag();
        }
        viewholder.iv_icon.setImageResource(mPic[position]);
        viewholder.tv_personal.setText(mShowText[position]);
//        viewholder.tv_personal.setText(Html.fromHtml("<font color=''>"+mShowText[position]+"</font>"));
        viewholder.tv_state.setText(mState[position]);
//        Drawable topDrawable = mContext.getResources().getDrawable(mPic[position]);
//        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
//        viewholder.tv_personal.setCompoundDrawables(null, topDrawable, null, null);
        viewholder.iv_state.setImageResource(mStatepic[position]);
      
        return convertView;
    }

    class Viewholder {
        ImageView iv_state,iv_icon;
        TextView tv_personal, tv_state;
    }
}
