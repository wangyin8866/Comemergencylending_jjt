package com.two.emergencylending.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
* Created by wangyaping
*/
public abstract class LazyBaseLoadFragment extends Fragment {
    /**
     * 控件是否初始化
     */
    protected boolean isViewCreated;
    /**
     * 数据是否加载完毕
     */
    private boolean isLoadDateCompleted;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);
        initViews();
        setListener();
        setData();
        isViewCreated = true;
        return view;
    }

    public abstract int getLayout();

    public abstract void initViews();

    public abstract void setListener();

    public abstract void setData();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isViewCreated && !isLoadDateCompleted) {
            loaData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {
            loaData();
        }
    }

    public void loaData() {
        isLoadDateCompleted = true;
    }
}
