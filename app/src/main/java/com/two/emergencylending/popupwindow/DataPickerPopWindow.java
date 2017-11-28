/**
 *
 */
package com.two.emergencylending.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.two.emergencylending.widget.OnWheelChangedListener;
import com.two.emergencylending.widget.WheelView;
import com.two.emergencylending.widget.adapter.NumericWheelAdapter;
import com.zyjr.emergencylending.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static com.zyjr.emergencylending.R.id.day;
import static com.zyjr.emergencylending.R.id.month;


/**
 * @author wyp 下午4:53:25
 */
public class DataPickerPopWindow extends PopupWindow implements OnWheelChangedListener {
    private Context context;
    private LayoutInflater layoutInflater;
    private WheelView yearView;
    private WheelView monthView;
    private WheelView dayView;
    private int thisyear;
    private int thismonth;
    private int thisday;

    public DataPickerPopWindow(Context context, int thisyear, int thismonth, int thisday) {
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.data_picker, null);
        this.context = context;
        this.thisyear = thisyear;
        this.thismonth = thismonth + 1;
        this.thisday = thisday;
        this.setContentView(v);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setAnimationStyle(android.R.style.Animation_InputMethod);
        this.setFocusable(true);
//		this.setOutsideTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initPop(v);
    }

    //初始化Pop
    private void initPop(View viewGroup) {
        yearView = (WheelView) viewGroup.findViewById(R.id.year);
        monthView = (WheelView) viewGroup.findViewById(month);
        dayView = (WheelView) viewGroup.findViewById(day);

        viewGroup.findViewById(R.id.data_picker_complete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String birthday = (yearView.getCurrentItem() + 1991) + "-" + (monthView.getCurrentItem() + 1) + "-" + (dayView.getCurrentItem() + 1);
                pdpw.SaveData(birthday);
                dismiss();
            }
        });
        viewGroup.findViewById(R.id.data_picker_cancle).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        System.out.println((thisyear - 1991) + "----" + thismonth + "----" + thisday);
        yearView.setViewAdapter(new NumericWheelAdapter(context, 1991, getcurrTime()[0], "年"));
        yearView.setCurrentItem(thisyear - 1991);
        int month = getMonth(thisyear);
        monthView.setViewAdapter(new NumericWheelAdapter(context, 1, month, "月"));
        monthView.setCurrentItem(thismonth - 1);
        int day = getDay(thisyear, thismonth);
        dayView.setViewAdapter(new NumericWheelAdapter(context, 1, day, "日"));
        dayView.setCurrentItem(thisday - 1);
        yearView.setCyclic(true);// 可循环滚动
        monthView.setCyclic(true);// 可循环滚动
        dayView.setCyclic(true);// 可循环滚动
        yearView.addChangingListener(this);
        monthView.addChangingListener(this);
        dayView.addChangingListener(this);
    }

    private int[] getcurrTime() {
        String[] dates = new SimpleDateFormat("yyyy-MM-dd").format(new Date()).split("-");
        int[] date = new int[dates.length];
        date[0] = Integer.valueOf(dates[0]);
        date[1] = Integer.valueOf(dates[1]);
        date[2] = Integer.valueOf(dates[2]);
        Log.e("date", Arrays.toString(new int[]{thisyear, thismonth, thisday}));
        Log.e("date", Arrays.toString(date));
        return date;
    }

    private int getMonth(int thisyear) {
        int[] date = getcurrTime();
        if (thisyear == date[0]) {
            return date[1];
        }
        return 12;
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int[] date = getcurrTime();
        if (year == date[0] && month == date[1]) {
            return date[2];
        }
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /* (non-Javadoc)
     * @see com.wheelviewcity.OnWheelChangedListener#onChanged(com.wheelviewcity.WheelView, int, int)
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel != dayView) {
            int year = yearView.getCurrentItem();
            int month = monthView.getCurrentItem();
            monthView.setViewAdapter(new NumericWheelAdapter(context, 1, getMonth(year + 1991), "月"));
            dayView.setViewAdapter(new NumericWheelAdapter(context, 1, getDay(year + 1991, month + 1), "日"));
        }
    }


    private PopDataPickerWindow pdpw;

    public void setOnBirthdayListener(PopDataPickerWindow pdpw) {
        this.pdpw = pdpw;
    }

    public interface PopDataPickerWindow {
        void SaveData(String birthday);
    }
}
