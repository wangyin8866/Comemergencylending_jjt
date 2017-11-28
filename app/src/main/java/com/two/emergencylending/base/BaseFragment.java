package com.two.emergencylending.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.two.emergencylending.utils.SharedPreferencesUtil;

import butterknife.ButterKnife;

/**
 * Created by wangyaping
 */
public abstract class BaseFragment extends Fragment implements IBaseActivity {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;
    protected SharedPreferencesUtil mSharedPreferencesUtil;
    protected View root;
    protected Context mContext;
    protected Bundle savedInstanceState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ActivityManage:", this.getClass().getName());// 打印出每个activity的类名
        mSharedPreferencesUtil = SharedPreferencesUtil.getInstance(getActivity());
        mContext = getActivity();
       
    }


    @Override
    public int setStatusColor() {
        return 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            root = LayoutInflater.from(mContext).inflate(setContent(), null);
            ButterKnife.bind(this, root);
            init();
            setData();
            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        resume();
    }

    @Override
    public void onDestroy() {
        destroy();
        super.onDestroy();
    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected void onInvisible() {


    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
    
   
}
