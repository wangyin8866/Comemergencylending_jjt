package com.two.emergencylending.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zyjr.emergencylending.R;

/**
 * Created by wangyaping
 */
public class Topbar extends RelativeLayout {
    private LayoutParams rightIcoParams;
    private LayoutParams leftIcoParams;
    private ImageView iv_left, iv_right;
    private Button leftButton, rightButton;
    private TextView tvTitle;
    //定义左边Button对应的属性
    private int leftTextColor;
    private Drawable leftBackground, leftIco, rightIco;
    private String leftText;
    private float leftTextSize;
    //定义右边Button对应的属性
    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;
    private float rightTextSize;
    //定义要显示标题文字的属性
    private float titleTextSize;
    private int titleTextColor;
    private String title;
    //定义布局方式
    private LayoutParams leftParams, rightParams, titleParams;
    //接口回调机制
    private topbarClickListener listener;

    public interface topbarClickListener {
        public void leftClick();

        public void rightClick();
    }

    public void setOnTopbarClickListener(topbarClickListener listener) {
        this.listener = listener;
    }

    /**
     * 说明：@SuppressLint("NewApi"）屏蔽一切新api中才能使用的方法报的android lint错误
     *
     * @param context
     * @param attrs
     * @TargetApi() 只屏蔽某一新api中才能使用的方法报的android lint错误
     */
    @TargetApi(value = Build.VERSION_CODES.JELLY_BEAN)
    public Topbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        //步骤一：获取属性
        //获取自定义属性值的映射,存储在TypeArray中
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Topbar);
        leftTextColor = typedArray.getColor(R.styleable.Topbar_leftTextColor, 0);
        leftIco = typedArray.getDrawable(R.styleable.Topbar_iv_left);
        rightIco = typedArray.getDrawable(R.styleable.Topbar_iv_right);
        leftBackground = typedArray.getDrawable(R.styleable.Topbar_leftBackground);
        leftText = typedArray.getString(R.styleable.Topbar_leftText);
        leftTextSize = typedArray.getDimension(R.styleable.Topbar_leftTextSize, 0);
        rightTextColor = typedArray.getColor(R.styleable.Topbar_rightTextColor, 0);
        rightBackground = typedArray.getDrawable(R.styleable.Topbar_rightBackground);
        rightText = typedArray.getString(R.styleable.Topbar_rightText);
        rightTextSize = typedArray.getDimension(R.styleable.Topbar_rightTextSize, 0);
        titleTextColor = typedArray.getColor(R.styleable.Topbar_mytitleTextColor, 0);
        titleTextSize = typedArray.getDimension(R.styleable.Topbar_mytitleTextSize, 0);
        title = typedArray.getString(R.styleable.Topbar_mytitle);
        //TypeArray使用完后要记得回收，避免浪费资源和缓存可能出现的错误
        typedArray.recycle();
        //步骤二：获取控件（自定义View中的组合模式，让已有的控件与新的控件进行组合）
        leftButton = new Button(context);
        rightButton = new Button(context);
        tvTitle = new TextView(context);
        iv_left = new ImageView(context);
        iv_right = new ImageView(context);
        //步骤三：设置控件属性
        iv_left.setImageDrawable(leftIco);
        iv_right.setImageDrawable(rightIco);
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);
        leftButton.setTextSize(leftTextSize);
        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);
        rightButton.setTextSize(rightTextSize);
        tvTitle.setTextColor(titleTextColor);
        tvTitle.setTextSize(titleTextSize);
        tvTitle.setText(title);
        //设置隐藏控件
        iv_left.setVisibility(INVISIBLE);
        iv_right.setVisibility(INVISIBLE);
        leftButton.setVisibility(INVISIBLE);
        rightButton.setVisibility(INVISIBLE);
        //为了美观，另外设置文字的居中显示
        tvTitle.setGravity(Gravity.CENTER);
        //步骤四：添加控件到ViewGroup中
        leftIcoParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftIcoParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE); //这里的TRUE不是布尔值，而是常量
        leftIcoParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
//        leftIcoParams.leftMargin = 20;
        iv_left.setPadding(20, 10, 30, 10);
        addView(iv_left, leftIcoParams);

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE); //这里的TRUE不是布尔值，而是常量
        leftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        iv_left.setPadding(20, 10, 30, 10);
        addView(leftButton, leftParams); //添加设置好的控件

        rightIcoParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightIcoParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE); //这里的TRUE不是布尔值，而是常量
        rightIcoParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        iv_right.setPadding(30, 0, 20, 0);
        addView(iv_right, rightIcoParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE); //这里的TRUE不是布尔值，而是常量
        rightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        rightButton.setPadding(30, 0, 20, 0);
        addView(rightButton, rightParams);

        titleParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(tvTitle, titleParams);


        //步骤五：实现自定义接口回调机制
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.leftClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.rightClick();
            }
        });
        iv_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.leftClick();
            }
        });
        iv_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.rightClick();
            }
        });
    }

    /**
     * 功能：设置是否显示左按钮
     *
     * @param flag
     */
    public void setLeftButtonIsVisiable(boolean flag) {
        if (flag) {
            leftButton.setVisibility(View.VISIBLE); //显示左按钮
        } else {
            leftButton.setVisibility(View.GONE); //隐藏左按钮
        }
    }

    /**
     * 功能：设置是否显示右按钮
     *
     * @param flag
     */
    public void setRightButtonIsVisiable(boolean flag) {
        if (flag) {
            rightButton.setVisibility(View.VISIBLE); //显示左按钮
        } else {
            rightButton.setVisibility(View.GONE); //隐藏左按钮
        }
    }

    /**
     * 功能：设置是否显示左Ico
     *
     * @param flag
     */
    public void setLeftIcoIsVisiable(boolean flag) {
        if (flag) {
            iv_left.setVisibility(View.VISIBLE); //显示左按钮
        } else {
            iv_left.setVisibility(View.GONE); //隐藏左按钮
        }
    }

    /**
     * 功能：设置是否显示右Ico
     *
     * @param flag
     */
    public void setRightIcoIsVisiable(boolean flag) {
        if (flag) {
            iv_right.setVisibility(View.VISIBLE); //显示左按钮
        } else {
            iv_right.setVisibility(View.GONE); //隐藏左按钮
        }
    }

    /**
     * 获取左边Ico
     *
     * @return
     */
    public ImageView getLeftIco() {
        return iv_left;
    }

    /**
     * 获取右边Ico
     *
     * @return
     */
    public ImageView getRightIco() {
        return iv_right;
    }

    /**
     * 获取左边btn
     *
     * @return
     */
    public Button getLeftButton() {
        return leftButton;
    }

    /**
     * 获取右边btn
     *
     * @return
     */
    public Button getRightButton() {
        return rightButton;
    }

    public TextView getTvTitle() {
        return tvTitle;
    }
}
