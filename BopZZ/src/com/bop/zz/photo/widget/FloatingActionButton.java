package com.bop.zz.photo.widget;

import com.bop.zz.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/12 下午3:01
 */
public class FloatingActionButton extends ImageButton {

    int mColorNormal;
    int mColorPressed;
    String mTitle;
    @DrawableRes
    private int mIcon;

    private float mCircleSize;
    private float mShadowRadius;
    private float mShadowOffset;
    private int mDrawableSize;

    public FloatingActionButton(Context context) {
        this(context, null);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.GFFloatingActionButton, 0, 0);
        mColorNormal = attr.getColor(R.styleable.GFFloatingActionButton_fabColorNormal, Color.BLACK);
        mColorPressed = attr.getColor(R.styleable.GFFloatingActionButton_fabColorPressed, Color.BLACK);
        mIcon = attr.getResourceId(R.styleable.GFFloatingActionButton_fabIcon, 0);
        mTitle = attr.getString(R.styleable.GFFloatingActionButton_fabTitle);
        attr.recycle();

        updateCircleSize();
        mShadowRadius = getDimension(R.dimen.fab_shadow_radius);
        mShadowOffset = getDimension(R.dimen.fab_shadow_offset);
        updateDrawableSize();

        updateBackground();
    }

    private void updateDrawableSize() {
        mDrawableSize = (int) (mCircleSize + 2 * mShadowRadius);
    }

    private void updateCircleSize() {
        mCircleSize = getDimension(R.dimen.fab_size_normal);
    }

    public void setIcon(@DrawableRes int icon) {
        if (mIcon != icon) {
            mIcon = icon;
            updateBackground();
        }
    }

    /**
     * @return the current Color for normal state.
     */
    public int getColorNormal() {
        return mColorNormal;
    }

    public void setColorNormalResId(@ColorRes int colorNormal) {
        setColorNormal(getColor(colorNormal));
    }

    public void setColorNormal(int color) {
        if (mColorNormal != color) {
            mColorNormal = color;
            updateBackground();
        }
    }

    /**
     * @return the current color for pressed state.
     */
    public int getColorPressed() {
        return mColorPressed;
    }

    public void setColorPressedResId(@ColorRes int colorPressed) {
        setColorPressed(getColor(colorPressed));
    }

    public void setColorPressed(int color) {
        if (mColorPressed != color) {
            mColorPressed = color;
            updateBackground();
        }
    }

    int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    float getDimension(@DimenRes int id) {
        return getResources().getDimension(id);
    }

    public void setTitle(String title) {
        mTitle = title;
        TextView label = (TextView) getTag(R.id.fab_label);
        if (label != null)
            label.setText(title);
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mDrawableSize, mDrawableSize);
    }

    void updateBackground() {
        float circleLeft = mShadowRadius;
        float circleTop = mShadowRadius - mShadowOffset;

        final RectF circleRect = new RectF(circleLeft, circleTop, circleLeft + mCircleSize, circleTop + mCircleSize);

        LayerDrawable layerDrawable = new LayerDrawable(
                new Drawable[] {
                        new BitmapDrawable(getResources()),
                        createFillDrawable(circleRect),
                        new BitmapDrawable(getResources()),
                        getIconDrawable()
                });

        float iconOffset = (mCircleSize - getDimension(R.dimen.fab_icon_size)) / 2f;

        int iconInsetHorizontal = (int) (mShadowRadius + iconOffset);
        int iconInsetTop = (int) (circleTop + iconOffset);
        int iconInsetBottom = (int) (mShadowRadius + mShadowOffset + iconOffset);

        layerDrawable.setLayerInset(3, iconInsetHorizontal, iconInsetTop, iconInsetHorizontal, iconInsetBottom);

        setBackgroundCompat(layerDrawable);
    }

    Drawable getIconDrawable() {
        if (mIcon != 0) {
            return getResources().getDrawable(mIcon);
        } else {
            return new ColorDrawable(Color.TRANSPARENT);
        }
    }

    private StateListDrawable createFillDrawable(RectF circleRect) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { android.R.attr.state_pressed }, createCircleDrawable(circleRect, mColorPressed));
        drawable.addState(new int[] {}, createCircleDrawable(circleRect, mColorNormal));
        return drawable;
    }

    private Drawable createCircleDrawable(RectF circleRect, int color) {
        final Bitmap bitmap = Bitmap.createBitmap(mDrawableSize, mDrawableSize, Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);

        canvas.drawOval(circleRect, paint);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }
}