package com.two.emergencylending.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.two.emergencylending.adapter.CityGridViewAdapter;
import com.two.emergencylending.adapter.SortAdapter;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.City;
import com.two.emergencylending.bean.CityBean;
import com.two.emergencylending.bean.CityModel;
import com.two.emergencylending.constant.CallBackType;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.controller.IControllerCallBack;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.KeyBoard;
import com.two.emergencylending.utils.PinyinComparator;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.two.emergencylending.widget.SideBar;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 项目名称：急借通
 * 类描述：城市选择的页面
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class CityActivity extends BaseActivity implements View.OnClickListener, IControllerCallBack {
    private List<CityBean> cityList;
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog, city;
    private SortAdapter adapter;
    private List<CityBean> hotCity;//热门城市列表
    private CityGridViewAdapter gridViewAdapter;
    private GridView gridView;
    private List<CityModel> SourceDateList;//用于存放排序后的二级城市，最主要的功能
    private ImageButton back, refresh;
    private RelativeLayout rlCity;
//    private boolean locationComplete = false;
//    BaiduLocationController baiduLocation;
    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Override
    public int setContent() {
        return R.layout.activity_city;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    private List<CityBean> getCitys() {
        return City.getInstance().getCitys();
    }

    private void initData() {
//        baiduLocation = new BaiduLocationController(this, this);
        //热门城市的数据初始化
        hotCity = new ArrayList<>();
        //手动设置热城市
        hotCity.add(new CityBean("2", "1", "上海", "2", "S"));
        hotCity.add(new CityBean("2", "1", "北京", "2", "B"));
        hotCity.add(new CityBean("2", "1", "广州", "2", "G"));
        hotCity.add(new CityBean("2", "1", "杭州", "2", "H"));
        hotCity.add(new CityBean("2", "6", "深圳", "2", "S"));
        hotCity.add(new CityBean("2", "6", "武汉", "2", "W"));
        hotCity.add(new CityBean("2", "6", "成都", "2", "C"));
        hotCity.add(new CityBean("2", "1", "重庆", "2", "C"));
        hotCity.add(new CityBean("2", "6", "南京", "2", "N"));
        hotCity.add(new CityBean("2", "1", "西安", "2", "X"));
        //所有城市
        cityList = getCitys();
        pinyinComparator = new PinyinComparator();
        // 根据a-z进行排序源数据
        SourceDateList = filledData(cityList);
        Collections.sort(SourceDateList, pinyinComparator);

    }

    private void initViews() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        View view = View.inflate(this, R.layout.head_city_list, null);
        rlCity = (RelativeLayout) view.findViewById(R.id.rl_city);
        city = (TextView) view.findViewById(R.id.tv_city);
        gridView = (GridView) view.findViewById(R.id.gridview_hot);
        refresh = (ImageButton) view.findViewById(R.id.refresh);
        gridViewAdapter = new CityGridViewAdapter(this, hotCity);
        gridView.setAdapter(gridViewAdapter);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));

        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        sideBar.setTextView(dialog);

        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        sortListView.addHeaderView(view);
        adapter = new SortAdapter(this, SourceDateList);
        sortListView.setAdapter(adapter);
        back = (ImageButton) findViewById(R.id.ib_back);
    }


    @Override
    protected void onStart() {
//        baiduLocation.start(this);
//        baiduLocation.startLocation();
        super.onStart();
    }

    @Override
    protected void onStop() {
//        baiduLocation.stop();
        super.onStop();
    }

    @Override
    public void init() {
        initData();
        initViews();
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        back.setOnClickListener(this);
        refresh.setOnClickListener(this);
        rlCity.setOnClickListener(this);
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                KeyBoard.closeSoftKeyboard(CityActivity.this);
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                String cityName = ((CityModel) adapter.getItem(position - 1)).getName();
                if (cityName != null && cityName.length() > 0) {
                    KeyBoard.closeSoftKeyboard(CityActivity.this);
                    SharedPreferencesUtil.getInstance(CityActivity.this).setString(SPKey.CITY, cityName);
                    Intent intent = new Intent();
                    setResult(100, intent);
                    finish();
                }
            }
        });


        gridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String cityName = hotCity.get(position).getName();
                if (cityName != null && cityName.length() > 0) {
                    KeyBoard.closeSoftKeyboard(CityActivity.this);
                    SharedPreferencesUtil.getInstance(CityActivity.this).setString(SPKey.CITY, cityName);
                    Intent intent = new Intent();
                    setResult(100, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }

    /**
     * 为ListView填充数据
     */
    private List<CityModel> filledData(List<CityBean> date) {
        List<CityModel> mSortList = new ArrayList<>();
        for (int i = 0; i < date.size(); i++) {
            CityModel cityModel = new CityModel();
            String name = date.get(i).getName();
            String firstName = date.get(i).getFirstName();
            String pinyin = date.get(i).getPinyin();
            cityModel.setName(name);
            cityModel.setFirstName(firstName);
            cityModel.setPingyin(pinyin);
            mSortList.add(cityModel);
        }
        return mSortList;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                Intent intent = new Intent();
                setResult(100, intent);
                finish();
                break;
            case R.id.refresh:
//                city.setText("定位中...");
//                baiduLocation.startLocation();
                break;
            case R.id.rl_city:
//                if (locationComplete) {
//                    SharedPreferencesUtil.getInstance(CityActivity.this).setString("city", city.getText().toString());
//                    Intent intent = new Intent();
//                    setResult(100, intent);
//                    finish();
//                }
                break;
            default:
                break;
        }

    }

    @Override
    public void onSuccess(int returnCode, String value) {
        if (returnCode == CallBackType.LOCATION) {
//            locationComplete = true;
//            city.setText(value);
//            baiduLocation.stopLocation();
        }
    }

    @Override
    public void onFail(String errorMessage) {
//        city.setText("定位失败");
    }
//
//    private boolean isGpsEnabled = false;
//    // 为了检测系统是否屏蔽了本应用的定位权限
//    ContentObserver mGpsMonitor = new ContentObserver(null) {
//        @Override
//        public void onChange(boolean selfChange) {
//            super.onChange(selfChange);
//            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            boolean enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//            isGpsEnabled = enabled;
//        }
//    };

    /**
     * 手机上的物理返回按钮
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            setResult(100, intent);
            finish();
            return true;
        }
        return false;
    }
}
