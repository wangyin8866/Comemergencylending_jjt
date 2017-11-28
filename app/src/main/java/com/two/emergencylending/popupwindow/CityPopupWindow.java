package com.two.emergencylending.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.PopupWindow;

import com.two.emergencylending.bean.UserInfoManager;
import com.two.emergencylending.widget.OnWheelChangedListener;
import com.two.emergencylending.widget.OnWheelScrollListener;
import com.two.emergencylending.widget.WheelView;
import com.two.emergencylending.widget.adapter.ArrayWheelAdapter;
import com.zyjr.emergencylending.R;

import java.util.HashMap;
import java.util.Map;


public class CityPopupWindow extends PopupWindow implements OnClickListener {
    /**
     * 所有省
     */
    public String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    public Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区
     */
    public Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * 当前省的名称
     */
    public String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    public String mCurrentCityName;
    /**
     * 当前区的名称
     */
    public String mCurrentDistrictName = "";

    private View mMenuView;
    private Button buttonOK;

    private Button buttonCancel;
    private OnCityPopupWindow mCityPopupWindow = null;
    private boolean scrolling = false;   // Scrolling flag
    private Activity context;
    private WheelView country;
    private WheelView city;
    private WheelView district;


    public CityPopupWindow(Activity context, String[] mProvinceDatas, Map<String, String[]> mCitisDatasMap, Map<String, String[]> mDistrictDatasMap) {
        super(context);
        this.context = context;
        this.mProvinceDatas = mProvinceDatas;
        this.mCitisDatasMap = mCitisDatasMap;
        this.mDistrictDatasMap = mDistrictDatasMap;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.city_popupmenu, null);
        buttonOK = (Button) mMenuView.findViewById(R.id.button_ok);
        buttonCancel = (Button) mMenuView.findViewById(R.id.button_cancel);
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

//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				int y=(int) event.getY();
//				if(event.getAction()==MotionEvent.ACTION_UP){
//					if(y<height){
//						selectedCallback(null);
//						dismiss();
//					}
//				}
//				dismiss();
                return true;
            }
        });

        initWheel();
//        setWheel();
    }

    public void setWheel() {
        String current = UserInfoManager.getInstance().getLocation().getmCurrentCity();
        Log.e("CurrentProvince", current);
        if (!TextUtils.isEmpty( UserInfoManager.getInstance().getLocation().getmCurrentCity())) {
            for (int i = 0; i < mProvinceDatas.length; i++) {
                if ( UserInfoManager.getInstance().getLocation().getmCurrentCity().startsWith(mProvinceDatas[i])) {
                    country.setCurrentItem(i);
                    updateCities(city, i);
                    break;
                }
            }
        }
    }


    public void setMyOnClickListener(OnClickListener itemsOnClick) {
        //设置按钮监听
        mMenuView.setOnClickListener(itemsOnClick);
    }


    private void selectedCallback(String privince, int privinceItem, String city, int cityItem, String district, int districtItem) {
        if (mCityPopupWindow != null) {
            mCityPopupWindow.onCityClick(privince, privinceItem, city, cityItem, district, districtItem);
        }
    }

    /**
     * 事件的回调
     *
     * @param listener
     */
    public void setOnCityPopupWindow(final OnCityPopupWindow listener) {
        mCityPopupWindow = listener;
    }

    /**
     * 事件接口
     *
     * @author linc
     */
    public interface OnCityPopupWindow {
        void onCityClick(String province, int privinceItem, String city, int cityItem, String district, int districtItem);
    }

    @Override
    public void onClick(View v) {
        if (buttonOK == v) {
            if (country.getCurrentItem() > mProvinceDatas.length - 1) return;
            mCurrentProviceName = mProvinceDatas[country.getCurrentItem()];
            if (TextUtils.isEmpty(mCurrentProviceName)) return;
            mCurrentCityName = city.getCurrentItem() > mCitisDatasMap.get(mCurrentProviceName).length - 1
                    ? "" : mCitisDatasMap.get(mCurrentProviceName)[city.getCurrentItem()];
            mCurrentDistrictName = district.getCurrentItem() > mDistrictDatasMap.get(mCurrentCityName).length - 1
                    ? "" : mDistrictDatasMap.get(mCurrentCityName)[district.getCurrentItem()];
            selectedCallback(mCurrentProviceName, country.getCurrentItem(), mCurrentCityName, city.getCurrentItem(), mCurrentDistrictName, district.getCurrentItem());
        }
        dismiss();
    }


    private void initWheel() {
        country = (WheelView) mMenuView.findViewById(R.id.country);
        country.setVisibleItems(5);
        if (mProvinceDatas == null || mProvinceDatas.length <= 0) return;
        ArrayWheelAdapter<String> adapter =
                new ArrayWheelAdapter<String>(context, mProvinceDatas);
        adapter.setTextSize(16);
        country.setViewAdapter(adapter);
        country.setCurrentItem(mProvinceDatas.length / 2);

        city = (WheelView) mMenuView.findViewById(R.id.city);
        updateCities(city, country.getCurrentItem());
        district = (WheelView) mMenuView.findViewById(R.id.district);
        updateDistricts(district, city.getCurrentItem());
        country.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateCities(city, newValue);
                }
            }
        });

        country.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                updateCities(city, country.getCurrentItem());
            }
        });


        city.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (!scrolling) {
                    updateDistricts(district, newValue);
                }
            }
        });

        city.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                updateDistricts(district, city.getCurrentItem());
            }
        });


    }

    /**
     * Updates the city wheel
     */
    private void updateCities(WheelView city, int index) {
        mCurrentProviceName = mProvinceDatas[index];
        String[] citys = mCitisDatasMap.get(mCurrentProviceName);
        ArrayWheelAdapter<String> adapter =
                new ArrayWheelAdapter<String>(context, citys);
        adapter.setTextSize(16);
        city.setViewAdapter(adapter);
        city.setCurrentItem(citys.length / 2);
        if (district != null) {
            updateDistricts(district, city.getCurrentItem());
        }
    }


    /**
     * Updates the  wheel
     */
    private void updateDistricts(WheelView district, int index) {
        String[] districts = mDistrictDatasMap.get(mCitisDatasMap.get(mCurrentProviceName)[index]);
        ArrayWheelAdapter<String> adapter =
                new ArrayWheelAdapter<String>(context, districts);
        adapter.setTextSize(16);
        district.setViewAdapter(adapter);
        district.setCurrentItem(districts.length / 2);
    }


}
