package com.two.emergencylending.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zyjr.emergencylending.R;

/**
 * 项目名称：jijietong1.08
 * 类描述：
 * 创建人：szx
 * 创建时间：2017/1/10 16:16
 * 修改人：szx
 * 修改时间：2017/1/10 16:16
 * 修改备注：
 */
public class UIProcessWidget extends RadioGroup implements OnCheckedChangeListener {
	public static final String WIDGET_PERSONAL = "personal"; //个人资料
	public static final String WIDGET_WORK= "work"; // 单位信息
	public static final String WIDGET_CONTACT = "contact"; //联系人
	public static final String WIDGET_CERTIFICATE = "certificate"; // 信息认证
	public static final String WIDGET_COMPLETE = "complete"; //申请结束
	private LayoutInflater mInflater;
	private OnItemChangedListener mOnItemChangedListener;

	public UIProcessWidget(Context context) {
		this(context, null);
	}

	public UIProcessWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialize(context);
	}

	private void initialize(Context context) {
		setOrientation(HORIZONTAL);
		mInflater = LayoutInflater.from(context);
	}

	public void addTabView(int resId, int textId, int position) {
		RadioButton button = (RadioButton) mInflater.inflate(
				R.layout.main_tab_widget_layout, this, false);
		button.setText(textId);
		button.setTag(position);
		button.setCompoundDrawablesWithIntrinsicBounds(0, resId, 0, 0);
		button.setOnCheckedChangeListener(this);
		addView(button);
	}

	/**
	 * 设置选中选项
	 * 
	 * @param position
	 *            索引
	 * @param checked
	 *            是否选中
	 */
	public void setChecked(int position, boolean checked) {
		RadioButton rdoButton = (RadioButton) getChildAt(position);
		rdoButton.setChecked(checked);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked && mOnItemChangedListener != null) {
			String tag = buttonView.getTag().toString();
			int position = Integer.parseInt(tag);
			mOnItemChangedListener.onChanged(position);
		}
	}

	/**
	 * 设置监听事件
	 */
	public void setOnItemChangedListener(
			OnItemChangedListener onItemChangedListener) {
		this.mOnItemChangedListener = onItemChangedListener;
	}

	public interface OnItemChangedListener {

		void onChanged(int position);
	}
}
