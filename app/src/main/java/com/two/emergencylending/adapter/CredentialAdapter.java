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
 * Created by User on 2016/8/20.
 */
public class CredentialAdapter extends BaseAdapter {
    private Context mContext;
    private int[] pic;
    private String[] text;
    private String[] mCredentState;
    private boolean[] verifty;
    LayoutInflater inflater;

    public CredentialAdapter(Context credentials, int[] credentialspic, String[] credentialText, boolean[] credentialverifty, String[] credentState) {
        this.mContext = credentials;
        this.pic = credentialspic;
        this.text = credentialText;
        this.verifty = credentialverifty;
        this.mCredentState = credentState;
        inflater = LayoutInflater.from(credentials);
    }

    @Override
    public int getCount() {
        return text.length;
    }

    @Override
    public Object getItem(int position) {
        return text[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.credential_item, null);
            holder.iv_bt = (ImageView) convertView.findViewById(R.id.iv_bt);
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.iv_picture = (ImageView) convertView.findViewById(R.id.iv_pic);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_credential);
            holder.tv_state = (TextView) convertView.findViewById(R.id.tv_state);
            holder.iv_state = (ImageView) convertView.findViewById(R.id.iv_state);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_picture.setImageResource(pic[position]);
        holder.tv_name.setText(text[position]);
        if (text[position].equals("手机运营商认证") && !verifty[position]) {
            holder.iv_bt.setImageResource(R.drawable.icon_compulsory);
            holder.iv_bt.setVisibility(View.VISIBLE);
        } else {
            holder.iv_bt.setVisibility(View.GONE);
        }
        holder.tv_state.setText(mCredentState[position]);
        if (mCredentState[position].equals("未认证")) {
            holder.tv_state.setBackgroundColor(mContext.getResources().getColor(R.color.register_hint));
            holder.iv_state.setImageResource(R.drawable.icon_statedefault);
        } else if (mCredentState[position].equals("认证中")) {
            holder.tv_state.setBackgroundColor(mContext.getResources().getColor(R.color.credenting));
            holder.iv_state.setImageResource(R.drawable.icon_stateing);
        } else if (mCredentState[position].equals("认证成功")) {
            holder.tv_state.setBackgroundColor(mContext.getResources().getColor(R.color.credent_success));
            holder.iv_state.setImageResource(R.drawable.icon_statesuccess);
        } else if (mCredentState[position].equals("认证失败")) {
            holder.tv_state.setBackgroundColor(mContext.getResources().getColor(R.color.credent_fail));
            holder.iv_state.setImageResource(R.drawable.icon_statefailed);
        }
//        if (verifty[position]){
//            holder.iv_select.setVisibility(View.VISIBLE);
//        }else {
//            holder.iv_select.setVisibility(View.INVISIBLE);
//        }
        return convertView;
    }

    class ViewHolder {
        ImageView iv_picture, iv_bt, iv_select, iv_state;
        TextView tv_name, tv_state;
    }

}
