package com.two.emergencylending.view.progress.painter;

public interface ProgressPainter extends Painter {

    void setMax(float max);

    void setMin(float min);

    void setValue(float value);

}
