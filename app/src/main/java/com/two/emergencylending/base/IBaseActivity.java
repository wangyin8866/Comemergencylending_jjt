package com.two.emergencylending.base;

/**
 * Created by wangyaping 
 */
public interface IBaseActivity {
    /**
     * 设置xml文件
     */
    int setContent();
    
    int setStatusColor();

    /**
     * view 初始化
     */
    void init();

    /**
     * 数据设置
     */
    void setData();

    /**
     * 设置view监听器
     */
    void setListener();

    void resume();

    void destroy();
}
