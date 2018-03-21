package com.bop.zz.listener;

import com.bop.zz.photo.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UILPauseOnScrollListener extends PauseOnScrollListener {

    public UILPauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        super(pauseOnScroll, pauseOnFling);
    }

    @Override
    public void resume() {
        ImageLoader.getInstance().resume();
    }

    @Override
    public void pause() {
        ImageLoader.getInstance().pause();
    }
}
