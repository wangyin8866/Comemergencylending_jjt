package com.two.emergencylending.view.progress.painter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;

public class ProgressPainterImp implements ProgressPainter {
    private RectF point;
    private RectF progressCircle;
    private Paint progressPaint;
    private Paint pointPaint;
    private int color = Color.RED;
    private float startAngle = 90f;
    private float plusAngle = 0;
    private int internalStrokeWidth = 40;
    private float min;
    private float max;
    private int width;
    private int height;
    private float mThumbPosX;
    private float mThumbPosY;
    private boolean showPoint = false;

    public ProgressPainterImp(int color, float min, float max, int progressStrokeWidth) {
        this.color = color;
        this.min = min;
        this.max = max;
        this.internalStrokeWidth = progressStrokeWidth;
        init();
    }

    private void init() {
        initInternalCirclePainter();

    }

    private void initInternalCirclePainter() {
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(internalStrokeWidth);
        progressPaint.setColor(color);
        progressPaint.setStyle(Paint.Style.STROKE);
        pointPaint = new Paint();
        pointPaint.setAntiAlias(true);
        pointPaint.setStrokeWidth(5);
        pointPaint.setColor(color);
        pointPaint.setStyle(Paint.Style.FILL);
    }

    private void initInternalCircle() {
        int centre = width / 2; //获取圆心的x坐标
        int radius = (int) (centre - 80 / 2); //圆环的半径
        progressCircle = new RectF();
        progressCircle.set(centre - radius, centre - radius, centre
                + radius, centre + radius);
        point = new RectF();
//        point.set(centre - radius - 10, centre - radius - 10, centre
//                + radius, centre + radius);
        point.set(centre - radius, centre - radius, centre
                + radius, centre + radius);
        mThumbPosX = (float) (radius * Math.cos(0));
        mThumbPosY = (float) (radius * Math.sin(0));
        pos = new float[2];
        tan = new float[2];
//        RectF oval = new RectF(centre - radius, centre - radius, centre
//                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限
    }

    private float[] pos;                // 当前点的实际位置
    private float[] tan;                // 当前点的tangent值,用于计算图片所需旋转的角度

    //    private Matrix mMatrix;             // 矩阵,用于对图片进行一些操作
    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(progressCircle, startAngle, plusAngle, false, progressPaint);  //根据进度画圆弧
//        canvas.drawArc(point, startAngle, plusAngle, false, progressPaint);  //根据进度画圆弧
        if (plusAngle>0){
            //绘制白色小星星
            Path orbit = new Path();
            //通过Path类画一个90度（180—270）的内切圆弧路径
            orbit.addArc(
                    point
                    , startAngle, plusAngle);
            // 创建 PathMeasure
            PathMeasure measure = new PathMeasure(orbit, false);
            measure.getPosTan(measure.getLength() * 1, pos, tan);
            //绘制实心小圆圈
            canvas.drawCircle(pos[0], pos[1], 15, pointPaint);
        }
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public void setValue(float value) {
        if (value == 500) {
            showPoint = true;
        } else {
            showPoint = false;
        }
        this.plusAngle = (359.8f * value) / max;
//        this.startAngle = (359.8f * value-1) / max;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.width = width;
        this.height = height;
        initInternalCircle();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        progressPaint.setColor(color);
    }
}
