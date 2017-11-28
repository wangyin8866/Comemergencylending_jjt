package com.two.emergencylending.view.progress.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class InternalCirclePainterImp implements InternalCirclePainter {

    private RectF internalCircle;
    private Paint internalCirclePaint;
    private int color;
    private int width;
    private int height;
    private int internalStrokeWidth = 15;

    public InternalCirclePainterImp(int color) {
        this.color = color;
        init();
    }

    private void init() {
        initExternalCirclePainter();
    }

    private void initExternalCirclePainter() {
        internalCirclePaint = new Paint();
        internalCirclePaint.setAntiAlias(true);
        internalCirclePaint.setStrokeWidth(internalStrokeWidth);
        internalCirclePaint.setColor(color);
        internalCirclePaint.setStyle(Paint.Style.STROKE);
    }

    private void initExternalCircle() {
//        internalCircle = new RectF();
//        internalCircle.set(0, 0, width, height);
//        float padding = internalStrokeWidth * 1.7f;
//        internalCircle.set(padding, padding + marginTop, width - padding, height - padding);
        int centre = width/2; //获取圆心的x坐标
        int radius = (int) (centre - 80/2); //圆环的半径
        internalCircle = new RectF();
        internalCircle.set(centre - radius, centre - radius, centre
                + radius, centre + radius);
    }

    @Override
    public void draw(Canvas canvas) {
//        int centre = width/2; //获取圆心的x坐标
//        int radius = (int) (centre - 80/2); //圆环的半径
//        canvas.drawCircle(centre, centre, radius, internalCirclePaint); //画出圆环
        canvas.drawArc(internalCircle, 0, 360, false, internalCirclePaint);
    }

    public void setColor(int color) {
        this.color = color;
        internalCirclePaint.setColor(color);
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void onSizeChanged(int height, int width) {
        this.width = width;
        this.height = height;
        initExternalCircle();
    }
}