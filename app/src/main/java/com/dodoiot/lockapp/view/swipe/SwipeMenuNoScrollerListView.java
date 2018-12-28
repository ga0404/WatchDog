package com.dodoiot.lockapp.view.swipe;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2016/12/17.
 */
public class SwipeMenuNoScrollerListView extends SwipeMenuListView {


    public SwipeMenuNoScrollerListView(Context context) {
        super(context);

    }

    public SwipeMenuNoScrollerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public SwipeMenuNoScrollerListView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
