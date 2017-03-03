package com.demo.ezrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by bixia on 2017/3/1.
 */

public class EzRecyclerView extends RecyclerView {
    protected OnLoadingListener onLoadingListener;
    //全部已经加载出来的item数量
    private int totalItemCount = 0;
    //屏幕上可见的item数量
    private int visibleItemCount = -1;
    private boolean isLoading = false;
    //手指滑动偏移量，大于0则是往上滑，小于0则是往下滑
    private float offset;
    private float downY;

    public EzRecyclerView(Context context) {
        super(context);
    }

    public EzRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public void stopLoading() {
        isLoading = false;
        if (onLoadingListener != null) {
            onLoadingListener.onStopLoading();
        }
    }

    private int findMax(int[] lastPositions) {
        if (lastPositions.length <= 0)
            return -1;
        int max = lastPositions[0];
        for (int lastPosition : lastPositions) {
            if (max < lastPosition) {
                max = lastPosition;
            }
        }
        return max;
    }

    public void setOnLoadingListener(final OnLoadingListener onLoadingListener) {
        this.onLoadingListener = onLoadingListener;
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (offset > 50 && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    LayoutManager layoutManager = getLayoutManager();
                    totalItemCount = layoutManager.getItemCount();
                    if (layoutManager instanceof GridLayoutManager) {
                        visibleItemCount = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof LinearLayoutManager) {
                        visibleItemCount = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] lastPositions = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(lastPositions);
                        visibleItemCount = findMax(lastPositions);
                    }
                    if (!isLoading && totalItemCount > 0 && visibleItemCount >= totalItemCount - 1) {
                        if (onLoadingListener != null) {
                            onLoadingListener.onLoading();
                        }
                        isLoading = true;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //按下的纵坐标比抬起纵坐标大则是上拉加载
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            downY = ev.getY();
        } else if (ev.getAction() == MotionEvent.ACTION_UP) {
            offset = downY - ev.getY();
        }
        return super.dispatchTouchEvent(ev);
    }

    public interface OnLoadingListener {
        void onLoading();

        void onStopLoading();
    }
}
