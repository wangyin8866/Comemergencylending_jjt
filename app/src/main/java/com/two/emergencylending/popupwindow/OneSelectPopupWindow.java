package com.two.emergencylending.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.two.emergencylending.bean.CodeBean;
import com.two.emergencylending.widget.OnWheelScrollListener;
import com.two.emergencylending.widget.WheelView;
import com.two.emergencylending.widget.adapter.ArrayWheelAdapter;
import com.zyjr.emergencylending.R;

import java.util.List;


public class OneSelectPopupWindow extends PopupWindow implements OnClickListener {
    /**
     * 所有数据
     */
    public List<CodeBean> mAllDatas;

    /**
     * 当前选择
     */
    public CodeBean mSelectDatasName;

    private View mMenuView;
    private Button buttonOK;
    private Button buttonCancel;
    private onSelectPopupWindow mSelectPopupWindow = null;
    private boolean scrolling = false;   // Scrolling flag
    private Activity context;
    private WheelView selector;


    public OneSelectPopupWindow(Activity context, List<CodeBean> Datas) {
        super(context);
        this.context = context;
        this.mAllDatas = Datas;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.education_popupmenu, null);
        buttonOK = (Button) mMenuView.findViewById(R.id.btn_ok);
        buttonCancel = (Button) mMenuView.findViewById(R.id.btn_cancel);

        mMenuView.setOnClickListener(this);
        buttonOK.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);

        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00000000);
        this.setBackgroundDrawable(dw);

        mMenuView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                //dismiss();
                return true;
            }
        });

        initWheel();
    }

    private void initWheel() {
        selector = (WheelView) mMenuView.findViewById(R.id.education);
        selector.setVisibleItems(5);
        if (mAllDatas == null || mAllDatas.size() <= 0) return;
        String[] array = new String[mAllDatas.size()];
        for (int i = 0; i < mAllDatas.size(); i++) {
            array[i] = mAllDatas.get(i).getName();
        }

        ArrayWheelAdapter<String> adapter =
                new ArrayWheelAdapter<String>(context, array);
        adapter.setTextSize(16);
        selector.setViewAdapter(adapter);
        selector.setCurrentItem(mAllDatas.size() / 2);

        selector.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (buttonOK == v) {
            if (selector.getCurrentItem() > mAllDatas.size() - 1) return;
            mSelectDatasName = mAllDatas.get(selector.getCurrentItem());
            if (mSelectDatasName == null) return;
            selectedCallback(selector.getCurrentItem(), mAllDatas.get(selector.getCurrentItem()));
        }
        dismiss();
    }


    public void setMyOnClickListener(OnClickListener itemsOnClick) {
        //设置按钮监听
        mMenuView.setOnClickListener(itemsOnClick);
    }


    private void selectedCallback(int index, CodeBean data) {
        if (mSelectPopupWindow != null) {
            mSelectPopupWindow.onSelectClick(index, data);
        }
    }

    /**
     * 事件的回调
     *
     * @param listener
     */
    public void setOnSelectPopupWindow(final onSelectPopupWindow listener) {
        mSelectPopupWindow = listener;
    }


    /**
     * 事件接口
     *
     * @author linc
     */
    public interface onSelectPopupWindow {
        public void onSelectClick(int index, CodeBean select);
    }
}
