package com.two.emergencylending.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.two.emergencylending.bean.Icon;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MineLoanItemAdapter extends BaseAdapter {

    private List<Icon> objects = new ArrayList<Icon>();

    private Context context;
    private LayoutInflater layoutInflater;

    public MineLoanItemAdapter(Context context, List list) {
        this.context = context;
        this.objects = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Icon getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.activity_mine_loan_item, null);
            convertView.setTag(new ViewHolder(convertView));
        }
        initializeViews((Icon) getItem(position), (ViewHolder) convertView.getTag());

        return convertView;
    }

    private void initializeViews(Icon object, ViewHolder holder) {
        holder.tvText.setText(object.txt_name);
        holder.ivIcon.setImageResource(object.icon);
        holder.tvUse.setText(object.use);
        if (object.num > 0) {
            holder.tvCount.setVisibility(View.VISIBLE);
            holder.tvCount.setText(Html.fromHtml("<font color='" + object.useColor + "'>" + "" + "</font>"));
        } else {
            holder.tvCount.setVisibility(View.INVISIBLE);
        }

    }

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_text)
        TextView tvText;
        @Bind(R.id.tv_use)
        TextView tvUse;
        @Bind(R.id.tv_count)
        TextView tvCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
