package com.two.emergencylending.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.zyjr.emergencylending.R;


/**
 * 自定义开关控件
 * 
 * @author Administrator
 * 
 */
public class ToggleView extends View {

 private Bitmap mBackground;// 开关的背景
 private Bitmap mSlideRight;// 开
 private Bitmap mSlideLeft;// 关
 private Paint paint = new Paint();
 private float mCurrentX;//点击的位置
 private int mCurrentState = STATE_DOWN;//记录点击的状态
 private static final int STATE_DOWN = 1;
 private static final int STATE_MOVE = 2;
 private static final int STATE_UP = 3;
 private static final int IsOpenChage = 4;

 private boolean isOpen; //是否打开 默认关闭

 /**
  *开关的状态
  * @param open  true is open；false is Close；
  */
 public void setIsOpen(boolean open){
  this.isOpen = open;
  mCurrentState = IsOpenChage;
  //重新绘制界面
  mOnToggleChangeListener.onChange(this, isOpen);
  invalidate();
 }

 public ToggleView(Context context, AttributeSet attrs) {
  super(context, attrs);
  initView();
 }

 private void initView() {
  mBackground = BitmapFactory.decodeResource(getResources(),
          R.drawable.switch_click);
  mSlideRight = BitmapFactory.decodeResource(getResources(),
          R.drawable.switch_on);
  mSlideLeft = BitmapFactory.decodeResource(getResources(),
          R.drawable.switch_off);
 }

 public ToggleView(Context context) {
  this(context, null);
 }

 @Override
 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  Matrix matrix = new Matrix();
  matrix.postScale(1.0f, 1.0f);
  Bitmap temp_1 = Bitmap.createBitmap(mBackground, 0, 0, mBackground.getWidth(),
          mBackground.getHeight(), matrix, true);
  Bitmap temp_2 = Bitmap.createBitmap(mSlideRight, 0, 0, mSlideRight.getWidth(),
          mSlideRight.getHeight(), matrix, true);
  Bitmap temp_3 = Bitmap.createBitmap(mSlideLeft, 0, 0, mSlideLeft.getWidth(),
          mSlideLeft.getHeight(), matrix, true);
  mBackground=temp_1;
  mSlideRight=temp_2;
  mSlideLeft=temp_3;
  // 确定控件的大小
  setMeasuredDimension(mBackground.getWidth(), mBackground.getHeight());
 }

 @Override
 protected void onDraw(Canvas canvas) {
  // 确定控件的样子
  {
   // 开关的背景
   int left = 0;
   int top = 0;
   canvas.drawBitmap(mBackground, left, top, paint);
  }

  {
   switch (mCurrentState) {
   case STATE_DOWN:
   case STATE_MOVE:

    {
     // 开关的滑动控件
     int left = (int) mCurrentX;
     int top = 0;
     if (left > (mBackground.getWidth() - mSlideRight.getWidth())) {
      left = (mBackground.getWidth() - mSlideRight.getWidth());
      canvas.drawBitmap(mSlideRight, left, top, paint);
     } else if (left < 0) {
      left = 0;//不能拖出屏幕
     }
     canvas.drawBitmap(mSlideLeft, left, top, paint);
    }
    break;
   case STATE_UP://松开
    if (isOpen) {
     //开 left 背景的宽度-slide的宽度
     canvas.drawBitmap(mSlideRight, (mBackground.getWidth() - mSlideRight.getWidth()), 0, paint);
    } else {
     //关
     canvas.drawBitmap(mSlideLeft, 0, 0, paint);
    }
    break;
   case IsOpenChage:
    if (isOpen) {
     //开 left 背景的宽度-slide的宽度
     canvas.drawBitmap(mSlideRight, (mBackground.getWidth() - mSlideRight.getWidth()), 0, paint);
    } else {
     //关
     canvas.drawBitmap(mSlideLeft, 0, 0, paint);
    }
    break;
   default:
    break;
   }


  }
  super.onDraw(canvas);
 }

 @Override
 public boolean onTouchEvent(MotionEvent event) {
  switch (event.getAction()) {
  case MotionEvent.ACTION_DOWN:// 按下
   //获取点的位置
   mCurrentX = event.getX();
   mCurrentState = STATE_DOWN;
   break;
  case MotionEvent.ACTION_MOVE:// 移动
   mCurrentX = event.getX();
   mCurrentState = STATE_MOVE;
   break;
  case MotionEvent.ACTION_UP:// 松开
   mCurrentState = STATE_UP;
   mCurrentX = event.getX();//松开的坐标
   if (mCurrentX < mBackground.getWidth()/2 && isOpen){
    //关闭
    isOpen = false;
    //只有状态改变才会触发
    if (mOnToggleChangeListener != null) {
     mOnToggleChangeListener.onChange(this, isOpen);
    }

   } else if (mCurrentX > mBackground.getWidth()/2 && !isOpen) {
    //打开
    isOpen = true;
    if (mOnToggleChangeListener != null) {
     mOnToggleChangeListener.onChange(this, isOpen);
    }
   }
   break;
  default:
   break;
  }
  //重新绘制界面
  invalidate();
  return true;//自己处理事件
 }

 private OnToggleChangeListener mOnToggleChangeListener;

 public void setOnToggleChangeListener(OnToggleChangeListener listener){
  this.mOnToggleChangeListener = listener;
 }

 public interface OnToggleChangeListener{
  void onChange(ToggleView v, boolean isOpen);
 }
}
