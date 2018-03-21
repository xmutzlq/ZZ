package com.bop.zz.listener;

import android.widget.AbsListView;

import com.lidroid.xutils.task.TaskHandler;

public class XUtils2PauseOnScrollListener extends com.lidroid.xutils.bitmap.PauseOnScrollListener {
    public XUtils2PauseOnScrollListener(TaskHandler taskHandler, boolean pauseOnScroll, boolean pauseOnFling) {
        super(taskHandler, pauseOnScroll, pauseOnFling);
    }

    public XUtils2PauseOnScrollListener(TaskHandler taskHandler, boolean pauseOnScroll, boolean pauseOnFling, AbsListView.OnScrollListener customListener) {
        super(taskHandler, pauseOnScroll, pauseOnFling, customListener);
    }
}
