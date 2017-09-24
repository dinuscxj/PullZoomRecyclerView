package com.dinuscxj.pullzoom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * @author dinus
 */
public class PullZoomRecyclerView extends PullZoomBaseView<RecyclerView> {

    private int mHeaderContainerHeight;
    private int mZoomViewHeight;

    private Interpolator sSmoothToTopInterpolator;
    private ZoomBackRunnable mZoomBackAnimation;

    public PullZoomRecyclerView(Context context) {
        this(context, null);
    }

    public PullZoomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mHeaderContainerHeight = 0;
        mZoomViewHeight = 0;
        sSmoothToTopInterpolator = createDefaultInterpolator();
        mZoomBackAnimation = new ZoomBackRunnable();
    }

    private Interpolator createDefaultInterpolator() {
        return new DecelerateInterpolator(2.0f);
    }

    @SuppressWarnings("ResourceType")
    @Override
    protected RecyclerView createWrapperView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        //prevent id repeat
        recyclerView.setId(Integer.MIN_VALUE);
        return recyclerView;
    }

    @Override
    protected int createDefaultPullZoomModel() {
        return ZOOM_HEADER;
    }

    @Override
    protected boolean isReadyZoom() {
        if (mMode == ZOOM_HEADER) {
            return isFirstItemCompletelyVisible();
        } else if (mMode == ZOOM_FOOTER) {
            return isLastItemCompletelyVisible();
        }

        return false;
    }

    @Override
    protected void pullZoomEvent(float scrollValue) {
        if (mZoomBackAnimation != null && !mZoomBackAnimation.isFinished()) {
            mZoomBackAnimation.abortAnimation();
        }

        if (mHeaderContainer != null && mZoomView != null) {
            ViewGroup.LayoutParams layoutParams = mHeaderContainer.getLayoutParams();
            layoutParams.height = (int) (Math.abs(scrollValue) + mHeaderContainerHeight);
            mHeaderContainer.setLayoutParams(layoutParams);

            ViewGroup.LayoutParams zoomViewLayoutParams = mZoomView.getLayoutParams();
            zoomViewLayoutParams.height = mZoomViewHeight + layoutParams.height
                    - mHeaderContainerHeight;
            mZoomView.setLayoutParams(zoomViewLayoutParams);
        }

        if (mMode == ZOOM_FOOTER) {
            mWrapperView.scrollToPosition(mWrapperView.getAdapter().getItemCount() - 1);
        }
    }

    @Override
    protected void smoothScrollToTop() {
        mZoomBackAnimation.startAnimation(ZOOM_BACK_DURATION);
    }

    public RecyclerView getRecyclerView() {
        return mWrapperView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mWrapperView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        mWrapperView.setLayoutManager(manager);
    }

    public void setSmoothToTopInterpolator(Interpolator sSmoothToTopInterpolator) {
        this.sSmoothToTopInterpolator = sSmoothToTopInterpolator;
    }

    private boolean isFirstItemCompletelyVisible() {
        if (mWrapperView != null) {
            RecyclerView.Adapter adapter = mWrapperView.getAdapter();
            RecyclerView.LayoutManager mLayoutManager = mWrapperView.getLayoutManager();

            if (null == adapter || adapter.getItemCount() == 0) {
                return false;
            } else if (null == mLayoutManager || mLayoutManager.getItemCount() == 0) {
                return false;
            } else {
                return checkFirstItemCompletelyVisible(mLayoutManager);
            }
        }

        return false;
    }

    private boolean checkFirstItemCompletelyVisible(RecyclerView.LayoutManager mLayoutManager) {
        int firstVisiblePosition = ((RecyclerView.LayoutParams) mLayoutManager.getChildAt(0)
                .getLayoutParams()).getViewAdapterPosition();
        if (firstVisiblePosition == 0) {
            final View firstVisibleChild = mWrapperView.getChildAt(0);
            if (firstVisibleChild != null) {
                return firstVisibleChild.getTop() >= mWrapperView.getTop();
            }
        }
        return false;
    }

    private boolean isLastItemCompletelyVisible() {
        if (mWrapperView != null) {
            RecyclerView.Adapter adapter = mWrapperView.getAdapter();
            RecyclerView.LayoutManager mLayoutManager = mWrapperView.getLayoutManager();

            if (null == adapter || adapter.getItemCount() == 0) {
                return true;
            } else if (null == mLayoutManager || mLayoutManager.getItemCount() == 0) {
                return false;
            } else {
                return checkLastItemCompletelyVisible(mLayoutManager);
            }
        }

        return false;
    }

    private boolean checkLastItemCompletelyVisible(RecyclerView.LayoutManager mLayoutmanager) {
        int lastVisiblePosition = mLayoutmanager.getChildCount() - 1;
        int currentLastVisiblePosition = ((RecyclerView.LayoutParams) mLayoutmanager
                .getChildAt(lastVisiblePosition).getLayoutParams()).getViewAdapterPosition();
        if (currentLastVisiblePosition == mLayoutmanager.getItemCount() - 1) {
            final View lastVisibleChild = mWrapperView.getChildAt(lastVisiblePosition);
            if (lastVisibleChild != null) {
                if (mHeaderContainer != null && mHeaderContainerHeight <= 0) {
                    mHeaderContainerHeight = mHeaderContainer.getMeasuredHeight();
                }
                return lastVisibleChild.getBottom() <= mWrapperView.getBottom();
            }
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        if (mHeaderContainer != null && mHeaderContainerHeight <= 0) {
            mHeaderContainerHeight = mHeaderContainer.getMeasuredHeight();
        }

        if (mZoomView != null && mZoomViewHeight <= 0) {
            mZoomViewHeight = mZoomView.getMeasuredHeight();
        }
    }

    private class ZoomBackRunnable implements Runnable {
        protected long mDuration;
        protected boolean mIsFinished = true;
        protected float mScale;
        protected long mStartTime;

        ZoomBackRunnable() {
        }

        public void abortAnimation() {
            mIsFinished = true;
        }

        public boolean isFinished() {
            return mIsFinished;
        }

        public void run() {
            if (mHeaderContainer != null && mZoomView != null && (!mIsFinished) && (mScale > 1.0f)) {
                // fix PullToZoomView bug  ---dinus
                // should not convert the System.currentTimeMillis() to float
                // otherwise the value of (System.currentTimeMillis() - mStartTime) will still be zero
                float zoomBackProgress = (System.currentTimeMillis() - mStartTime) / (float) mDuration;
                ViewGroup.LayoutParams headerContainerLayoutParams = mHeaderContainer.getLayoutParams();
                ViewGroup.LayoutParams zoomViewLayoutParams = mZoomView.getLayoutParams();

                if (zoomBackProgress > 1.0f) {
                    headerContainerLayoutParams.height = mHeaderContainerHeight;
                    mHeaderContainer.setLayoutParams(headerContainerLayoutParams);

                    zoomViewLayoutParams.height = mZoomViewHeight;
                    mZoomView.setLayoutParams(zoomViewLayoutParams);

                    mIsFinished = true;
                    return;
                }

                float currentScale = mScale - (mScale - 1.0F)
                        * sSmoothToTopInterpolator.getInterpolation(zoomBackProgress);
                headerContainerLayoutParams.height = (int) (currentScale * mHeaderContainerHeight);
                mHeaderContainer.setLayoutParams(headerContainerLayoutParams);

                zoomViewLayoutParams.height = mZoomViewHeight + headerContainerLayoutParams.height
                        - mHeaderContainerHeight;
                mZoomView.setLayoutParams(zoomViewLayoutParams);

                post(this);
            }
        }

        public void startAnimation(long animationDuration) {
            if (mZoomView != null && mHeaderContainer != null) {
                mStartTime = System.currentTimeMillis();
                mDuration = animationDuration;
                mScale = (float) mHeaderContainer.getHeight() / mHeaderContainerHeight;
                mIsFinished = false;
                post(this);
            }
        }
    }
}
