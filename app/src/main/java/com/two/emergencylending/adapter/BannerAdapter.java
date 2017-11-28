package com.two.emergencylending.adapter;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.two.emergencylending.bean.BannerBean;
import com.two.emergencylending.interfaces.EventClick;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：急借通
 * 类描述：轮播图
 * 创建人：szx
 * 创建时间：2016/3/10 9:57
 * 修改人：szx
 * 修改时间：2016/3/10 9:57
 * 修改备注：
 */
public class BannerAdapter extends PagerAdapter {
    private List<BannerBean> array = new ArrayList<BannerBean>();
    private List<ImageView> list = new ArrayList<ImageView>();
    private EventClick eventClick;

    public BannerAdapter(List<BannerBean> arrayList, List<ImageView> list, EventClick eventClick) {
        this.array = arrayList;
        this.list = list;
        this.eventClick = eventClick;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(View arg0, int arg1) {
        ((ViewPager) arg0).addView(list.get(arg1));
        final int index = arg1;
        list.get(arg1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                eventClick.eventClick(array.get(index).getAd_url());
            }
        });
        return list.get(arg1);
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void startUpdate(View arg0) {

    }

    @Override
    public void finishUpdate(View arg0) {

    }

}
