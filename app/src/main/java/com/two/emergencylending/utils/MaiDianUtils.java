package com.two.emergencylending.utils;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import com.two.emergencylending.constant.BuryAction;
import com.two.emergencylending.manager.BuryPointManager;


public class MaiDianUtils {
    MaiDianUtils instance;
    private EditText editText;
    private Activity context;
    private String inputType;

    public MaiDianUtils(Activity context, EditText editText, String inputType) {
        this.context = context;
        this.editText = editText;
        this.inputType = inputType;
        this.editText.setOnFocusChangeListener(new FocusListener("123"));
    }


    private class FocusListener implements View.OnFocusChangeListener {
        String mEventId;

        public FocusListener(String eventId) {

        }


        @Override
        public void onFocusChange(View view, boolean b) {
            if (b) {
            } else {
            }
        }
    }


}
