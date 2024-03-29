package com.sdrzlyz.slidingframelayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by huagai on 14-7-4.
 */
public class MyLinearLayout extends LinearLayout {

    private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

    private boolean isLock = false;// 左右移动锁.

    public OnScrollListener onScrollListener;// 自定义滑动接口

    private boolean b;// 拦截touch标识

    public MyLinearLayout(Context context) {
        super(context);
    }

    public MyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new MySimpleGesture());
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }


    /**
     * 事件分发
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        b = mGestureDetector.onTouchEvent(ev);// 获取手势返回值.
        /***
         * 松开时记得处理缩回...
         */
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            onScrollListener.doLoosen();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 事件拦截处理
     * <p/>
     * 要明白机制，如果返回true的话，那就是进行拦截，处理自己的ontouch. 返回false的话，那么就会向下传递...
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        super.onInterceptTouchEvent(ev);
        return b;
    }

    /**
     * 事件处理
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        isLock = false;
        return super.onTouchEvent(event);
    }

    /**
     * 自定义手势执行
     */
    class MySimpleGesture extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            isLock = true;
            return super.onDown(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (!isLock)
                onScrollListener.doScroll(distanceX);
            // 垂直大于水平
            if (Math.abs(distanceY) > Math.abs(distanceX)) {
                return false;
            } else {
                return true;
            }

        }
    }

    /**
     * 自定义接口 实现滑动...
     */
    public interface OnScrollListener {
        void doScroll(float distanceX);// 滑动...

        void doLoosen();// 手指松开后执行...
    }

}
