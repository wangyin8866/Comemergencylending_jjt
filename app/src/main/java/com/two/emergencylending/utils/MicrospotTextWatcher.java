package com.two.emergencylending.utils;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.two.emergencylending.widget.MonitorEditText;


/**
 * Created by User on 2016/8/4.
 */
public class MicrospotTextWatcher implements TextWatcher {

    private MonitorEditText editText;
    private long startTime;//输入起始时间
    private long endTime;//输入结束时间
    private long time;//输入总时间
    private int rollback;//回退次数
    private Activity context;
    private int inputType;
    private String beforeValues;
    private String afterValues;

    public MicrospotTextWatcher(Activity context, MonitorEditText editText, int inputType) {
        this.context = context;
        this.editText = editText;
        this.inputType = inputType;
        this.editText.setOnFocusChangeListener(new FocusListener());
        this.editText.setOnImputCompleteListener(new CompleteListener());
    }






    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if(afterValues == null) return;
             if(charSequence.length() < afterValues.length()) 
                 rollback++;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        afterValues = editable.toString();
    }


    private class FocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean b) {

            if(b) {
                startTime = System.currentTimeMillis();
                rollback = 0;
            }else {

            }
        }
    }

    private class CompleteListener implements MonitorEditText.OnImputCompleteListener {

        @Override
        public void onImputComplete() {
            endTime = System.currentTimeMillis();
            time = endTime - startTime;
        }
    }



}
