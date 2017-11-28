package com.two.emergencylending.activity.GuideModule;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.two.emergencylending.activity.MainActivity;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.constant.SPKey;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.utils.SettingManager;
import com.two.emergencylending.utils.SharedPreferencesUtil;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;

/**
 * 项目名称：急借通
 * 类描述：引导页
 * 创建人：szx
 * 创建时间：2016/5/26 14:49
 * 修改人：szx
 * 修改时间：2016/5/26 14:49
 * 修改备注：
 */
public class GuideActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {


    private ViewPager viewPager;
    private ArrayList<View> views;
    private Button skip;
    private ImageView mImageView = null;

    private ImageView[] mImageViews = null;
    private ViewGroup mGroup;

    private static final int[] pics = {R.drawable.guide1, R.drawable.guide2, R.drawable.guide3};
    private static final int[] dots = {R.drawable.icon_dot, R.drawable.icon_doth};
    private ImageAdapter vpAdapter;
    private int mImageIndex = 0;

    @Override
    public int setContent() {
        return R.layout.activity_splash;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    protected boolean getStick() {
        return true;
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        skip = (Button) findViewById(R.id.skip);
        skip.setVisibility(View.INVISIBLE);
        mGroup = (ViewGroup) findViewById(R.id.viewGroup);
    }

    @Override
    public void setData() {
        vpAdapter = new ImageAdapter(GuideActivity.this, pics);
        final int imageCount = pics.length;
        if (imageCount > 1) {
            mImageViews = new ImageView[imageCount];
            for (int i = 0; i < imageCount; i++) {
                mImageView = new ImageView(GuideActivity.this);
                int imageParams = (int) (SettingManager.getInstance().getDensity() * 10 + 0.5f);
                int imagePadding = (int) (SettingManager.getInstance().getDensity() * 3 + 0.5f);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imageParams, imageParams);
                params.bottomMargin = imagePadding;
                params.topMargin = imagePadding;
                params.leftMargin = imagePadding;
                params.rightMargin = imagePadding;
                mImageView.setLayoutParams(params);
                mImageViews[i] = mImageView;
                if (i == 0) {
                    mImageViews[i].setBackgroundResource(dots[1]);
                } else {
                    mImageViews[i].setBackgroundResource(dots[0]);
                }
                mGroup.addView(mImageViews[i]);
            }
        }


        // 设置数据
        viewPager.setAdapter(vpAdapter);
        viewPager.setOnPageChangeListener(this);
    }

    @Override
    public void setListener() {
        skip.setOnClickListener(this);
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skip:
                SharedPreferencesUtil.getInstance(GuideActivity.this).setBoolean(SPKey.IS_FIRST_INSTAN, false);
                CommonUtils.goToActivity(getContext(), MainActivity.class);
                finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mImageIndex = position;
        mImageViews[position].setBackgroundResource(dots[1]);
        for (int i = 0; i < mImageViews.length; i++) {
            if (position != i) {
                mImageViews[i].setBackgroundResource(dots[0]);
            }
        }
        if (position == pics.length - 1) {
            skip.setVisibility(View.VISIBLE);
        } else {
            skip.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ImageAdapter extends PagerAdapter {

        private ArrayList<ImageView> mImageViewCacheList;

        private int[] mGuideList = new int[0];

        private Context mContext;

        public ImageAdapter(Context context, int[] guideList) {
            mContext = context;
            mGuideList = guideList;
            mImageViewCacheList = new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            return mGuideList.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            int imageid = mGuideList[position];
            ImageView imageView = null;
            if (mImageViewCacheList.isEmpty()) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            } else {
                imageView = mImageViewCacheList.remove(0);
            }
            imageView.setTag(imageid);
            container.addView(imageView);
            imageView.setImageResource(imageid);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ImageView view = (ImageView) object;
            container.removeView(view);
            mImageViewCacheList.add(view);
        }

    }
}
