/**
 * 
 */
package com.two.emergencylending.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.two.emergencylending.widget.OnWheelChangedListener;
import com.two.emergencylending.widget.WheelView;
import com.two.emergencylending.widget.adapter.NumericWheelAdapter;
import com.zyjr.emergencylending.R;


/**
 * @author wyp 下午4:53:25
 */
public class CyclePopWindow extends PopupWindow implements OnWheelChangedListener {
	private Context context;
	private LayoutInflater layoutInflater;
	private WheelView cycle;
	private String birthday;
	
	public CyclePopWindow(Context context) {
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = layoutInflater.inflate(R.layout.cycle, null);
		this.context = context;
		
		this.setContentView(v);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setAnimationStyle(android.R.style.Animation_InputMethod);
		this.setFocusable(true);
//		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		initPop(v);
	}
	//初始化Pop
	private void initPop(View viewGroup){
		cycle = (WheelView) viewGroup.findViewById(R.id.cycle);
        viewGroup.findViewById(R.id.cycle_complete).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				birthday = (cycle.getCurrentItem()+1)+"";
				pcw.SaveCycle(birthday);
				dismiss();
			}
		});
	viewGroup.findViewById(R.id.cycle_cancle).setOnClickListener(new OnClickListener() {
		@Override
		public void onClick(View v) {
			dismiss();
		}
	});
        cycle.setViewAdapter(new NumericWheelAdapter(context, 1, 31));
        cycle.setCurrentItem(1);
        cycle.setCyclic(true);// 可循环滚动
        cycle.addChangingListener(this);
        
	}
	
	private PopCycleWindow pcw;

	public void setOnCycleListener(PopCycleWindow pcw) {
		this.pcw = pcw;
	}

	public interface PopCycleWindow {
		void SaveCycle(String cycle);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		birthday = (cycle.getCurrentItem())+"";
		pcw.SaveCycle(birthday);
	}
}
