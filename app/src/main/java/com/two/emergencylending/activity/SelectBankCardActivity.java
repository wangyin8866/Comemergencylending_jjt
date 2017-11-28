package com.two.emergencylending.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.two.emergencylending.adapter.BankAdapter;
import com.two.emergencylending.adapter.OnItemClickListener;
import com.two.emergencylending.application.IApplication;
import com.two.emergencylending.base.BaseActivity;
import com.two.emergencylending.bean.BankBean;
import com.two.emergencylending.utils.CommonUtils;
import com.two.emergencylending.view.Topbar;
import com.zyjr.emergencylending.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 项目名称：急借通
 * 类描述：选择银行卡
 * 创建人：wyp
 * 创建时间：2016/8/10 10:11
 * 修改人：wyp
 * 修改时间：2016/8/10 10:11
 * 修改备注：
 */
public class SelectBankCardActivity extends BaseActivity implements Topbar.topbarClickListener, OnItemClickListener {
    @Bind(R.id.topbar)
    Topbar topbar;
    @Bind(R.id.recycler_Bank)
    RecyclerView recycler_Bank;
    BankAdapter adapter;
    private List<BankBean> banks;

    @Override
    public int setContent() {
        return R.layout.activity_bank_seclet;
    }

    @Override
    public int setStatusColor() {
        return CommonUtils.getStatusBarColorNormal();
    }

    @Override
    public void init() {
        CommonUtils.setMiuiStatusBarDarkMode(this, true);
        topbar.getLeftIco().setVisibility(View.VISIBLE);
        topbar.getLeftIco().setImageResource(R.drawable.icon_back);
        initData();
        adapter = new BankAdapter(getContext(), banks);
        // 给RecyclerView绑定适配器
        recycler_Bank.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        // 布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler_Bank.setLayoutManager(linearLayoutManager);

    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        topbar.setOnTopbarClickListener(this);
    }

    @Override
    public void resume() {
        IApplication.currClass = this.getClass();
    }

    @Override
    public void destroy() {

    }


    @OnClick(R.id.recycler_Bank)
    public void onClick() {
    }

    @Override
    public void leftClick() {
        finish();
    }

    @Override
    public void rightClick() {

    }

    public void onItemClick(View view, int postion) {
//        Intent intent = new Intent();
//        intent.putExtra(UserInfoManager.BANKDATA, bank);
//        setResult(RESULT_OK, intent);
//        finish();
    }

    public void initData() {
        banks = new ArrayList<BankBean>();
        BankBean bankBean = new BankBean();
        for (int i = 0; i < 30; i++) {
            bankBean.setBankName("招商");
            bankBean.setChecked(true);
            bankBean.setTheBank("aaaa");
            banks.add(bankBean);
        }
    }

}
