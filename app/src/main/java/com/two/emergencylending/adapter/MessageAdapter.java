package com.two.emergencylending.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.two.emergencylending.bean.CenterMessage;
import com.two.emergencylending.widget.SlidingButtonView;

import java.util.List;


/**
 * 消息中心
 *
 * @author wangwx
 */
public class MessageAdapter extends BaseAdapter implements SlidingButtonView.IonSlidingButtonListener {
    private Context mContext;
    private List<CenterMessage> messages;
    private LayoutInflater inflater;
    private SlidingButtonView mMenu = null;
    private CenterMessageAdapter.IonSlidingViewClickListener mIDeleteBtnClickListener;

    public MessageAdapter(Context context,
                          List<CenterMessage> list) {
        mContext = context;
        this.messages = list;
        inflater = LayoutInflater.from(context);
        mIDeleteBtnClickListener = (CenterMessageAdapter.IonSlidingViewClickListener) context;
    }

    public void refresh(List<CenterMessage> list) {
        this.messages = list;
        notifyDataSetInvalidated();
    }

    public List<CenterMessage> getList() {
        return messages;
    }

    @Override
    public int getCount() {
        return messages == null ? 0 : messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
//            convertView = inflater.inflate(R.layout.activity_message_center_add_delete_item, parent,
//                    false);
//            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CenterMessage message = messages.get(position);
        int left = 20;
        int top = 10;
//        int right = 20;
//        int bottom = 10;
//        viewHolder.item.setPadding(left, top, right, bottom);
//        viewHolder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext) - left - right;
//
//        viewHolder.statue.setText(message.getLoanState());
//        viewHolder.message.setText(message.getLoanMessage());
//        viewHolder.time.setText(message.getLoanTime());
//        viewHolder.layout_content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //判断是否有删除菜单打开
//                if (menuIsOpen()) {
//                    closeMenu();//关闭菜单
//                } else {
//                    int n = viewHolder.getLayoutPosition();
//                    mIDeleteBtnClickListener.onItemClick(v, n);
//                }
//
//            }
//        });
//        viewHolder.btn_Delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int n = viewHolder.getLayoutPosition();
//                mIDeleteBtnClickListener.onDeleteBtnCilck(v, n);
//            }
//        });
        return convertView;
    }

    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (SlidingButtonView) view;
    }

    @Override
    public void onDownOrMove(SlidingButtonView slidingButtonView) {
        if (menuIsOpen()) {
            if (mMenu != slidingButtonView) {
                closeMenu();
            }
        }
    }

    private static class ViewHolder {
//        @Bind(R.id.message_status)
//        TextView statue;
//        @Bind(R.id.message_context)
//        TextView message;
//        @Bind(R.id.message_time)
//        TextView time;
//        @Bind(R.id.btn_delete)
//        ImageView btn_Delete;
//        @Bind(R.id.layout_content)
//        View layout_content;
//        @Bind(R.id.rl_item)
//        View item;

//        public ViewHolder(View view) {
//            super();
//            ButterKnife.bind(this, view);
////            ((SlidingButtonView) view).setSlidingButtonListener(MessageAdapter.this);
//        }
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
        void onItemClick(View view, int position);

        void onDeleteBtnCilck(View view, int position);
    }
}
