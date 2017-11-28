package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.two.emergencylending.bean.CenterMessage;
import com.two.emergencylending.constant.DBCode;
import com.two.emergencylending.utils.DateUtil;
import com.two.emergencylending.utils.Utils;
import com.two.emergencylending.widget.SlidingButtonView;
import com.zyjr.emergencylending.R;

import java.util.List;

public class CenterMessageAdapter extends BaseAdapter implements SlidingButtonView.IonSlidingButtonListener {
    private Context mContext;
    private List<CenterMessage> message;
    private IonSlidingViewClickListener mIDeleteBtnClickListener;
    private SlidingButtonView mMenu = null;
    private LayoutInflater inflater;

    public CenterMessageAdapter(Context context, List<CenterMessage> message) {
        this.message = message;
        mContext = context;
        inflater = LayoutInflater.from(context);
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
    }

    @Override
    public int getCount() {
        return message.size();
    }

    @Override
    public Object getItem(int position) {
        return message.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_message_center_add_delete_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.btn_delete = (ImageView) convertView.findViewById(R.id.btn_delete);
            viewHolder.layout_content = (LinearLayout) convertView.findViewById(R.id.layout_content);
            viewHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            viewHolder.unread = (ImageView) convertView.findViewById(R.id.unread);
            viewHolder.message_context = (TextView) convertView.findViewById(R.id.message_context);
            viewHolder.message_status = (TextView) convertView.findViewById(R.id.message_status);
            viewHolder.message_time = (TextView) convertView.findViewById(R.id.message_time);
            viewHolder.sbv_item = (SlidingButtonView) convertView.findViewById(R.id.sbv_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CenterMessage centerMessage = message.get(position);
        int left = 20;
        int top = 10;
        int right = 20;
        int bottom = 10;
        viewHolder.rl_item.setPadding(left, top, right, bottom);
        viewHolder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext) - left - right;
        if (message.get(position).getStatus().equals(DBCode.IS_READ)) {
            viewHolder.unread.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.unread.setVisibility(View.VISIBLE);
        }
        viewHolder.message_status.setText(centerMessage.getNewsTitle());
        viewHolder.message_context.setText(centerMessage.getNewsDetail());
        viewHolder.message_time.setText(DateUtil.stringToDate(centerMessage.getCreateDate()));
        viewHolder.layout_content.setTag(centerMessage.getId());
        viewHolder.btn_delete.setTag(centerMessage.getId());
        viewHolder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    CenterMessage compare = null;
                    for (CenterMessage bean : message) {
                        if (bean.getId().equals(v.getTag())) {
                            compare = bean;
                        }
                    }
                    mIDeleteBtnClickListener.onItemClick(v, compare);
                }
            }
        });
        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CenterMessage compare = null;
                for (CenterMessage bean : message) {
                    if (bean.getId().equals(v.getTag())) {
                        compare = bean;
                    }
                }
                mIDeleteBtnClickListener.onDeleteBtnCilck(v, compare);
            }
        });
        viewHolder.sbv_item.setSlidingButtonListener(this);
        return convertView;
    }

    private static class ViewHolder {
        ImageView btn_delete;
        LinearLayout layout_content;
        RelativeLayout rl_item;
        ImageView unread;
        TextView message_status;
        TextView message_time;
        TextView message_context;
        SlidingButtonView sbv_item;
    }

    public void removeData(int position) {
        message.remove(position);
        notifyDataSetChanged();
    }

    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    /**
     * 滑动或者点击了Item监听
     *
     * @param slidingButtonView
     */
    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断是否有菜单打开
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }


    public interface IonSlidingViewClickListener {
        void onItemClick(View view, CenterMessage message);

        void onDeleteBtnCilck(View view, CenterMessage message);
    }

}
