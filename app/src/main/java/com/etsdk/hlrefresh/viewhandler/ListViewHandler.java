package com.etsdk.hlrefresh.viewhandler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.etsdk.hlrefresh.BaseRefreshLayout;
import com.etsdk.hlrefresh.ILoadViewFactory;


public class ListViewHandler implements ViewHandler {
	private Integer autoLastLoadMorePosition;
	@Override
	public boolean handleSetAdapter(View contentView, Object adapter, ILoadViewFactory.ILoadMoreView loadMoreView, OnClickListener onClickLoadMoreListener) {
		final ListView listView = (ListView) contentView;
		boolean hasInit = false;
		if (loadMoreView != null) {
			loadMoreView.init(new ListViewFootViewAdder(listView), onClickLoadMoreListener);
			hasInit = true;
		}
		listView.setAdapter((ListAdapter) adapter);
		return hasInit;
	}

	@Override
	public void setOnScrollBottomListener(View contentView, Integer autoLastLoadMorePosition, BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener) {
		ListView listView = (ListView) contentView;
		this.autoLastLoadMorePosition=autoLastLoadMorePosition;
		listView.setOnScrollListener(new ListViewOnScrollListener(onScrollBottomListener));
		listView.setOnItemSelectedListener(new ListViewOnItemSelectedListener(onScrollBottomListener));
	}

	/**
	 * 针对于电视 选择到了底部项的时候自动加载更多数据
	 */
	private class ListViewOnItemSelectedListener implements OnItemSelectedListener {
		private BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener;

		public ListViewOnItemSelectedListener(BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener) {
			super();
			this.onScrollBottomListener = onScrollBottomListener;
		}

		@Override
		public void onItemSelected(AdapterView<?> listView, View view, int position, long id) {
			if (listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScorllBootom();
				}
			}

			if (autoLastLoadMorePosition!=null&&listView.getLastVisiblePosition() + autoLastLoadMorePosition >= listView.getCount()) {// 如果滚动到倒数autoLastLoadMorePosition
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScorllBootom();
				}
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}
	};

	/**
	 * 滚动到底部自动加载更多数据
	 */
	private class ListViewOnScrollListener implements OnScrollListener {
		private BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener;

		public ListViewOnScrollListener(BaseRefreshLayout.OnScrollBottomListener onScrollBottomListener) {
			super();
			this.onScrollBottomListener = onScrollBottomListener;
		}

		@Override
		public void onScrollStateChanged(AbsListView listView, int scrollState) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() + 1 == listView.getCount()) {// 如果滚动到最后一行
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScorllBootom();
				}
			}
			if (autoLastLoadMorePosition!=null&&scrollState == OnScrollListener.SCROLL_STATE_IDLE && listView.getLastVisiblePosition() + autoLastLoadMorePosition >= listView.getCount()) {// 如果滚动到倒数autoLastLoadMorePosition
				if (onScrollBottomListener != null) {
					onScrollBottomListener.onScorllBootom();
				}
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		}
	};

	private class ListViewFootViewAdder implements ILoadViewFactory.FootViewAdder {
		private ListView listView;

		public ListViewFootViewAdder(ListView listView) {
			super();
			this.listView = listView;
		}

		@Override
		public View addFootView(int layoutId) {
			View view = LayoutInflater.from(listView.getContext()).inflate(layoutId, listView, false);
			return addFootView(view);
		}

		@Override
		public View addFootView(View view) {
			listView.addFooterView(view);
			return view;
		}

		@Override
		public View getContentView() {
			return listView;
		}

	}
}
