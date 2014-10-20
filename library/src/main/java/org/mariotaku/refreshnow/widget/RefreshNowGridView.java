package org.mariotaku.refreshnow.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;

import org.mariotaku.refreshnow.widget.iface.IRefreshNowView;

public class RefreshNowGridView extends GridView implements IRefreshNowView {

    private final Helper mHelper;

    public RefreshNowGridView(final Context context) {
        this(context, null);
    }

    public RefreshNowGridView(final Context context, final AttributeSet attrs) {
        this(context, attrs, android.R.attr.listViewStyle);
    }

    public RefreshNowGridView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        mHelper = new Helper(this, context, attrs, defStyle);
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


    protected int computeVerticalScrollOffset(int direction) {
        final int firstPosition = getFirstVisiblePosition();
        final int itemCount = getCount();
        if (firstPosition >= 0 && getChildCount() > 0) {
            final View view = getChildAt(0);
            // Dirty hack for getting correct result. Don't ask me why it works!
            final int top = view.getTop() + (direction > 0 ? getListPaddingBottom() : -getListPaddingTop());
            int height = view.getHeight();
            if (height > 0) {
                final int numColumns = getNumColumns();
                final int rowCount = (itemCount + numColumns - 1) / numColumns;
                // In case of stackFromBottom the calculation of whichRow needs
                // to take into account that counting from the top the first row
                // might not be entirely filled.
                final int oddItemsOnFirstRow = isStackFromBottom() ? ((rowCount * numColumns) -
                        itemCount) : 0;
                final int whichRow = (firstPosition + oddItemsOnFirstRow) / numColumns;
                return Math.max(whichRow * 100 - (top * 100) / height +
                        (int) ((float) getScrollY() / getHeight() * rowCount * 100), 0);
            }
        }
        return 0;
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