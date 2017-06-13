package com.tqmall.mvp.widget;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.tqmall.adapter.base.BaseRecyclerListAdapter;


public class BaseRecyclerView extends RecyclerView {

    /**
     * 无分页信息，即一次性加载时使用
     */
    public static int PAGELIMIT_NONE = -1;

    /**
     * 是否是向下滑动
     */
    private boolean isScrollDown;

    /**
     * 「更多」加载完成
     */
    private boolean loadBottomDataCompleted = true;

    /**
     * 分别为加载失败、空数据、加载中显示的页面
     */
    private View mFailedView, mEmptyView, mLoadingView;

    /**
     * 滑动到底部时触发的事件
     */
    private OnRecyclerViewScrollBottomListener scrollBottomListener;

    /**
     * 定义滑动到底部时的监听器
     */
    public interface OnRecyclerViewScrollBottomListener {
        void requestNextPage();
    }

    /**
     * RecyclerView的Scroll监听器
     */
    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            // RecyclerView已经停止滑动
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                // 获取到最后一个可见的item
                int lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                // 获取item的总数
                int totalItemCount = layoutManager.getItemCount();
                //是否滑动到底部
                if (lastVisibleItemPosition >= totalItemCount - 1 && isScrollDown) {
                    if (scrollBottomListener != null) {
                        scrollBottomListener.requestNextPage();
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            isScrollDown = dy > 0;
        }
    };

    public BaseRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 设置RecyclerView滑动监听器
     */
    private void init(Context context) {
        setItemAnimator(new DefaultItemAnimator());
        setLayoutManager(new LinearLayoutManager(context));
        //不显示自定义分割线
//        addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));
        setOnScrollListener(onScrollListener);
    }

    /**
     * 底部「更多」加载完成
     */
    private void onLoadNextComplete() {
        this.loadBottomDataCompleted = true;
    }

    /**
     * 无分页值时，列表数据加载完后控制显示的状态
     *
     * @param showFailedView 是否显示失败页面
     */
    public void renderViewByResult(boolean showFailedView) {
        renderViewByResult(showFailedView, PAGELIMIT_NONE, false);
    }

    /**
     * 列表数据加载完之后控制显示的状态
     *
     * @param showFailedView 是否显示失败页面
     * @param pageLimit      分页值
     */
    public void renderViewByResult(boolean showFailedView, int pageLimit, boolean isCurrentListEmpty) {
        onLoadNextComplete();
        if (showFailedView) {
            setVisibility(INVISIBLE);
            setFailedViewVisibility(VISIBLE);
            setEmptyViewVisibility(GONE);
            setLoadingViewVisibility(GONE);
        } else {
            BaseRecyclerListAdapter mAdapter = (BaseRecyclerListAdapter) getAdapter();
            int size = mAdapter.getData().size();
            setLoadingViewVisibility(GONE);
            setFailedViewVisibility(GONE);
            if (size == 0) {
                setVisibility(INVISIBLE);
                setEmptyViewVisibility(VISIBLE);
            } else {
                setVisibility(VISIBLE);
                setEmptyViewVisibility(GONE);
                mAdapter.setHasFooter(pageLimit != PAGELIMIT_NONE && size % pageLimit == 0 && !isCurrentListEmpty);
            }
        }
    }

    /**
     * 设置RecyclerView滑动到底部时的监听器
     *
     * @param onRecyclerViewScrollBottomListener
     */
    public void setOnRecyclerViewScrollBottomListener(OnRecyclerViewScrollBottomListener onRecyclerViewScrollBottomListener) {
        this.scrollBottomListener = onRecyclerViewScrollBottomListener;
    }

    /**
     * 设置首次加载失败时显示的页面
     *
     * @param failedView
     */
    public void setFailedView(View failedView) {
        this.mFailedView = failedView;
    }

    /**
     * 设置数据为空时显示的页面
     *
     * @param emptyView
     */
    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
    }

    /**
     * 设置列表数据加载中时显示的页面
     *
     * @param loadingView
     */
    public void setLoadingView(View loadingView) {
        this.mLoadingView = loadingView;
    }


    /**
     * 设置列表数据是否加载完成
     *
     * @param loadBottomDataCompleted
     */
    public void setLoadBottomDataCompleted(boolean loadBottomDataCompleted){
        this.loadBottomDataCompleted=loadBottomDataCompleted;
    }

    /**
     * 设置失败页面是否显示
     *
     * @param visibility
     */
    private void setFailedViewVisibility(int visibility) {
        if (mFailedView != null) {
            mFailedView.setVisibility(visibility);
        }
    }

    /**
     * 设置空白页面是否显示
     *
     * @param visibility
     */
    private void setEmptyViewVisibility(int visibility) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(visibility);
        }
    }

    /**
     * 设置加载中页面是否显示
     *
     * @param visibility
     */
    private void setLoadingViewVisibility(int visibility) {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(visibility);
        }
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        if (!loadBottomDataCompleted) {
//            return false;
//        } else {
//            return super.onTouchEvent(e);
//        }
//    }
}