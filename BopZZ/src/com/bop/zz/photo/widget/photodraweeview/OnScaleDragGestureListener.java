package com.bop.zz.photo.widget.photodraweeview;

public interface OnScaleDragGestureListener {
    void onDrag(float dx, float dy);

    void onFling(float startX, float startY, float velocityX, float velocityY);

    void onScale(float scaleFactor, float focusX, float focusY);

    void onScaleEnd();
}
