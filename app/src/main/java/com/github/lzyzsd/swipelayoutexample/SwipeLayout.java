package com.github.lzyzsd.swipelayoutexample;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Bruce on 11/24/14.
 */
public class SwipeLayout extends LinearLayout {

    public static interface OnSwipeListener {
        void onOpen();
        void onClosed();
    }

    private ViewDragHelper viewDragHelper;
    private View contentView;
    private View actionView;
    private int dragDistance;
    private final double AUTO_OPEN_SPEED_LIMIT = 800.0;
    private int draggedX, draggedY;
    private boolean shouldInterceptDragEvent = true;

    public void setShouldInterceptDragEvent(boolean shouldInterceptDragEvent) {
        this.shouldInterceptDragEvent = shouldInterceptDragEvent;
    }

    private OnSwipeListener onSwipeListener;

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.onSwipeListener = onSwipeListener;
    }

    public SwipeLayout(Context context) {
        this(context, null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        contentView = getChildAt(0);
        actionView = getChildAt(1);
//        actionView.setVisibility(GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        dragDistance = actionView.getMeasuredWidth();
        dragDistance = contentView.getMeasuredHeight();
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            return view == contentView || view == actionView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            draggedX = left;
            draggedY = top;
            if (changedView == contentView) {
//                actionView.offsetLeftAndRight(dx);
                actionView.offsetTopAndBottom(dy);
            } else {
//                contentView.offsetLeftAndRight(dx);
                contentView.offsetTopAndBottom(dy);
            }
            if (actionView.getVisibility() == View.GONE) {
                actionView.setVisibility(View.VISIBLE);
            }
            invalidate();
        }

//        @Override
//        public int clampViewPositionHorizontal(View child, int left, int dx) {
//            if (child == contentView) {
//                final int leftBound = getPaddingLeft();
//                final int minLeftBound = -leftBound - dragDistance;
//                final int newLeft = Math.min(Math.max(minLeftBound, left), 0);
//                return newLeft;
//            } else {
//                final int minLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() - dragDistance;
//                final int maxLeftBound = getPaddingLeft() + contentView.getMeasuredWidth() + getPaddingRight();
//                final int newLeft = Math.min(Math.max(left, minLeftBound), maxLeftBound);
//                return newLeft;
//            }
//        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (child == contentView) {
                final int topBound = getPaddingTop();
                final int minLeftBound = -topBound - dragDistance;
                final int newTop = Math.min(Math.max(minLeftBound, top), 0);
                return newTop;
            } else {
                final int minTopBound = getPaddingTop() + contentView.getMeasuredHeight() - dragDistance;
                final int maxTopBound = getPaddingTop() + contentView.getMeasuredHeight() + getPaddingBottom();
                final int newTop = Math.min(Math.max(top, minTopBound), maxTopBound);
                return newTop;
            }
        }

//        @Override
//        public int getViewHorizontalDragRange(View child) {
//            return dragDistance;
//        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return dragDistance;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            boolean settleToOpen = false;
            if (releasedChild == contentView) {
                if (yvel > AUTO_OPEN_SPEED_LIMIT) {
                    settleToOpen = false;
                } else if (yvel < -AUTO_OPEN_SPEED_LIMIT) {
                    settleToOpen = true;
                }else if (draggedY <= -dragDistance / 2) {
                    settleToOpen = true;
                } else if (draggedY > -dragDistance / 2) {
                    settleToOpen = false;
                }
            } else {
                if (yvel > AUTO_OPEN_SPEED_LIMIT) {
                    settleToOpen = true;
                } else if (yvel < -AUTO_OPEN_SPEED_LIMIT) {
                    settleToOpen = false;
                } else if (draggedY >= dragDistance / 2) {
                    settleToOpen = true;
                } else if (draggedY < dragDistance / 2) {
                    settleToOpen = false;
                }
            }
            final int settleDestY;
            if (releasedChild == contentView) {
                settleDestY = settleToOpen ? -dragDistance : 0;
            } else {
                settleDestY = settleToOpen ? dragDistance : 0;
            }
//            viewDragHelper.smoothSlideViewTo(contentView, settleDestX, 0);
            if (releasedChild == contentView) {
                viewDragHelper.smoothSlideViewTo(contentView, 0, settleDestY);
            } else {
                viewDragHelper.smoothSlideViewTo(actionView, 0, settleDestY);
            }
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);

            if (onSwipeListener != null) {
                if (settleToOpen) {
                    onSwipeListener.onOpen();
                } else {
                    onSwipeListener.onClosed();
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(viewDragHelper.shouldInterceptTouchEvent(ev) && shouldInterceptDragEvent) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
