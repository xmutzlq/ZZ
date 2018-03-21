package com.bop.zz.refresh.loadingviewfinal.style.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

public class SemiCircleSpinIndicator extends BaseIndicatorController {


    @Override
    public void draw(Canvas canvas, Paint paint) {
        RectF rectF=new RectF(0,0,getWidth(),getHeight());
        canvas.drawArc(rectF,-60,120,false,paint);
    }

    @Override
    public List<Animator> createAnimation() {
        List<Animator> animators=new ArrayList<Animator>();
        ObjectAnimator rotateAnim=ObjectAnimator.ofFloat(getTarget(),"rotation",0,180,360);
        rotateAnim.setDuration(600);
        rotateAnim.setRepeatCount(-1);
        rotateAnim.start();
        animators.add(rotateAnim);
        return animators;
    }


}
