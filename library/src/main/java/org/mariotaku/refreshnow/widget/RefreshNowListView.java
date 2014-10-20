package org.mariotaku.refreshnow.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import org.mariotaku.refreshnow.widget.iface.IRefreshNowView;

public class RefreshNowListView extends ListView implements IRefreshNowView {

    private final Helper mHelper;

    public RefreshNowListView(final Context context) {
        this(context, null);
    }

    public RefreshNowListView(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public RefreshNowListView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        mHelper = new Helper(this, context, attrs, defStyle);
    }

    /**
     * Check if this view can be scrolled vertically in a certain direction.
     *
     * @param direction Negative to check scrolling up, positive to check scrolling down.
     * @return true if this view can be scrolled in the specified direction, false otherwise.
     */
    public boolean canScrollVertically(int direction) {
        final int offset = computeVerticalScrollOffset(direction);
        final int range = computeVerticalScrollRange() - computeVerticalScrollExtent();
        if (range == 0) return false;
        if (direction < 0) {
            return offset > 0;
        } else {
            return offset < range - 1;
        }
    }

    private int computeVerticalScrollOffset(int direction) {
        final int firstPosition = getFirstVisiblePosition();
        final int childCount = getChildCount();
        if (firstPosition >= 0 && childCount > 0) {
            if (isSmoothScrollbarEnabled()) {
                final View view = getChildAt(0);
                final int top = view.getTop() + (direction > 0 ? getListPaddingBottom() : -getListPaddingTop());
                int height = view.getHeight();
                if (height > 0) {
                    return Math.max(firstPosition * 100 - (top * 100) / height +
                            (int) ((float) getScrollY() / getHeight() * getCount() * 100), 0);
                }
            } else {
                int index;
                final int count = getCount();
                if (firstPosition == 0) {
                    index = 0;
                } else if (firstPosition + childCount == count) {
                    index = count;
                } else {
                    index = firstPosition + childCount / 2;
                }
                return (int) (firstPosition + childCount * (index / (float) count));
            }
        }
        return 0;
    }

    @Override
    public RefreshMode getRefreshMode() {
        return mHelper.getRefreshMode();
    }

    @Override
    public boolean isRefreshing() {
        return mHelper.isRefreshing();
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent ev) {
        return super.dispatchTouchEvent(mHelper.processOnTouchEvent(ev));
    }

    @Override
    public void setConfig(final RefreshNowConfig config) {
        mHelper.setConfig(config);
    }

    @Override
    public void setFriction(final float friction) {
        super.setFriction(friction);
        mHelper.setFriction(friction);
    }

    @Override
    public void setOnRefreshListener(final OnRefreshListener listener) {
        mHelper.setOnRefreshListener(listener);
    }

    @Override
    public void setRefreshComplete() {
        mHelper.setRefreshComplete();
    }

    @Override
    public void setRefreshIndicatorView(final View view) {
        mHelper.setRefreshIndicatorView(view);
    }

    @Override
    public View getRefreshIndicatorView() {
        return mHelper.getRefreshIndicatorView();
    }

    @Override
    public void setRefreshing(final boolean refreshing) {
        mHelper.setRefreshing(refreshing);
    }

    @Override
    public void setRefreshMode(final RefreshMode mode) {
        mHelper.setRefreshMode(mode);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl, final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mHelper.dispatchOnScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected boolean overScrollBy(final int deltaX, final int deltaY, final int scrollX, final int scrollY,
                                   final int scrollRangeX, final int scrollRangeY, final int maxOverScrollX, final int maxOverScrollY,
                                   final boolean isTouchEvent) {
        return true;
    }

}