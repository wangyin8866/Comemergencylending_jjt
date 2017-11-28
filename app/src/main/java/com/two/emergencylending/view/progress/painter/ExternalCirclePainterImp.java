package com.two.emergencylending.view.progress.painter;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class ExternalCirclePainterImp implements ExternalCirclePainter {

    private RectF externalCircle;
    private Paint externalCirclePaint;
    private int color;
    private int externalStrokeWidth = 5;
    private int startAngle = 0;
    private int finishAngle = 360;
    private int width;
    private int height;

    public ExternalCirclePainterImp(int externalColor) {
        this.color = externalColor;
        init();
    }

    private void init() {
        initExternalCirclePainter();
    }

    private void initExternalCirclePainter() {
        externalCirclePaint = new Paint();
        externalCirclePaint.setAntiAlias(true);
        externalCirclePaint.setStrokeWidth(externalStrokeWidth);
        externalCirclePaint.setColor(color);
        externalCirclePaint.setStyle(Paint.Style.STROKE);
    }

    private void initExternalCircle() {
        int centre = width/2;
        int radius = (int) (centre - 10/2);
        externalCircle = new RectF();
        externalCircle.set(centre - radius, centre - radius, centre
                + radius, centre + radius);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawArc(externalCircle, startAngle, finishAngle, false, externalCirclePaint);
    }

    @Override
    public void setColor(int color) {
        this.color = color;
        externalCirclePaint.setColor(color);
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
