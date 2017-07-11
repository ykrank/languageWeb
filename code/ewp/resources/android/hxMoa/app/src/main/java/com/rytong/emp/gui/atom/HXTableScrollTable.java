package com.rytong.emp.gui.atom;

import org.w3c.dom.Element;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.rytong.emp.dom.css.Layout;
import com.rytong.emp.dom.css.ScrollLayout;
import com.rytong.emp.gui.GUIRealView;
import com.rytong.emp.gui.GUIView;
import com.rytong.emp.gui.atom.atomrela.GUIScrollView;
import com.rytong.emp.tool.Utils;


/**
 * <p>
 * 上拉下拉刷新控件
 * </p>
 * 
 * @author Zhoucj
 *
 */
public class HXTableScrollTable extends LinearLayout implements GUIView, GUIRealView {

	// refresh states
	private static final int PULL_TO_REFRESH = 2;
	private static final int RELEASE_TO_REFRESH = 3;
	private static final int REFRESHING = 4;
	// pull state
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;

	protected ScrollLayout mLayout;
	
	/** 第一次绘制时布局，之后绘制不再布局 */
	private boolean mIsFirstDraw = true;
	/** 变为向下的箭头,改变箭头方向 */
	private RotateAnimation mFlipAnimation;
	/** 变为逆向的箭头,旋转 */
	private RotateAnimation mReverseFlipAnimation;
	/** scrollview */
	private GUIScrollView mScrollView;
	/** 实际盛放内容的table */
	private LinearLayout mDataView;
	/** header view */
	private View mHeaderView;
	/** header view image */
	private ImageView mHeaderImageView;
	/** header progress bar */
	private ProgressBar mHeaderProgressBar;
	/** header view height */
	private int mHeaderViewHeight;
	/** footer view */
	private View mFooterView;
	/** footer view image */
	private ImageView mFooterImageView;
	/** footer progress bar */
	private ProgressBar mFooterProgressBar;
	/** footer view height */
	private int mFooterViewHeight;

	private OnRefreshListener mOnHeaderRefreshListener;
	private OnRefreshListener mOnFooterRefreshListener;
	/** last y */
	private int mLastMotionY;
	/** lock */
	private boolean mLock;
	/** header view current state */
	private int mHeaderState;
	/** footer view current state */
	private int mFooterState;
	/** pull state, pull up or pull down; PULL_UP_STATE or PULL_DOWN_STATE */
	private int mPullState;

	public HXTableScrollTable(Context context) {
		super(context);
		// 设置此LinearLayout的方向
		setOrientation(VERTICAL);

		mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);

		mDataView = new LinearLayout(getContext());
		mDataView.setBackgroundColor(Color.TRANSPARENT);
		mDataView.setOrientation(LinearLayout.VERTICAL);
	}

	public void setOnHeaderRefreshListener(OnRefreshListener headerRefreshListener) {
		mOnHeaderRefreshListener = headerRefreshListener;
	}

	public void setOnFooterRefreshListener(OnRefreshListener footerRefreshListener) {
		mOnFooterRefreshListener = footerRefreshListener;
	}
	
	@Override
	public void onBindElement(Element element) {
	}

	@Override
	public Layout onBuildLayout() {
		mLayout =  new ScrollLayout(Layout.MATCH_PARENT, Layout.MATCH_PARENT);
		return mLayout;
	}
	
	@Override
	public void addView(View view) {
		mDataView.addView(view);
	}
	
	@Override
	public void removeView(View view){
		mDataView.removeView(view);
	}
	
	@Override
	public void removeAllViews(){
		mDataView.removeAllViews();
	}

	@Override
	public void initRealView(Context context) {
		Activity activity =  (Activity) context;
		if (mIsFirstDraw) {
			// header view 在此添加,保证是第一个添加到linearlayout的最上端
			addHeaderView(activity);
			mScrollView = new GUIScrollView(context, null);
			mScrollView.setBackgroundColor(Color.TRANSPARENT);
			mScrollView.addView(mDataView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			super.addView(mScrollView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			addFooterView(activity);
			mIsFirstDraw = false;
		}
		mScrollView.a(mLayout.getScrollYLimit());
	}

	private void addHeaderView(Activity activity) {
		// header view
		LayoutInflater inflater = activity.getLayoutInflater();
		mHeaderView = inflater.inflate(Utils.getResourcesId(activity, "refresh_header", "layout"), this, false);

		mHeaderImageView = (ImageView) mHeaderView.findViewById(
				Utils.getResourcesId(activity, "pull_to_refresh_image", "id"));
		mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(
				Utils.getResourcesId(activity, "pull_to_refresh_progress", "id"));
		// header layout
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
		// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
		params.topMargin = -(mHeaderViewHeight);
		super.addView(mHeaderView, params);
	}

	private void addFooterView(Activity activity) {
		// footer view
		LayoutInflater inflater = activity.getLayoutInflater();
		mFooterView = inflater.inflate(Utils.getResourcesId(activity, "refresh_footer", "layout"), this, false);
		mFooterImageView = (ImageView) mFooterView.findViewById(
				Utils.getResourcesId(activity, "pull_to_load_image", "id"));
		mFooterProgressBar = (ProgressBar) mFooterView.findViewById(
				Utils.getResourcesId(activity, "pull_to_load_progress", "id"));
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
		// 由于是线性布局可以直接添加,只要AdapterView的高度是MATCH_PARENT,那么footerview就会被添加到最后,并隐藏
		super.addView(mFooterView, params);
	}

	/**
	 * <p>
	 * 测量高度
	 * </p>
	 * 
	 * @param child
	 */
	public void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			// 防止外层viewgroup抢走事件
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			// deltaY > 0 是向下运动,< 0是向上运动
			int deltaY = y - mLastMotionY;
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL: {
			// 将事件还给外层viewgroup
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		}
		return false;
	}

	/**
	 * <p>
	 * 是否应该到了父View,即TableScrollTable滑动
	 * </p>
	 * 
	 * @param deltaY > 0 是向下运动, < 0是向上运动
	 * 
	 * @return
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == REFRESHING || mFooterState == REFRESHING) {
			return false;
		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > Utils.getScaledValueY(5) && mScrollView.getScrollY() == 0) {
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < -Utils.getScaledValueY(5) && child.getMeasuredHeight() <= getHeight() + mScrollView.getScrollY()) {
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/*
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return false)则由TableScrollTable
	 * 的子View来处理;否则由下面的方法来处理(即由TableScrollTable自己来处理)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLock) {
			return true;
		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				int deltaY = y - mLastMotionY;
				if (mPullState == PULL_DOWN_STATE) {
					// TableScrollTable执行下拉
					headerPrepareToRefresh(deltaY);
				} else if (mPullState == PULL_UP_STATE) {
					// TableScrollTable执行上拉
					mFooterView.setVisibility(View.VISIBLE);
					footerPrepareToRefresh(deltaY);
				}
				mLastMotionY = y;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				int topMargin = getHeaderTopMargin();
				if (mPullState == PULL_DOWN_STATE) {
					if (topMargin >= 0) {
						// 开始刷新
						headerRefreshing();
					} else {
						// 还没有执行刷新，重新隐藏
						setHeaderTopMargin(-mHeaderViewHeight);
					}
				} else if (mPullState == PULL_UP_STATE) {
					if (Math.abs(topMargin) >= mHeaderViewHeight + mFooterViewHeight/2) {
						// 开始执行footer 刷新
						footerRefreshing();
					} else {
						// 还没有执行刷新，重新隐藏
						setHeaderTopMargin(-mHeaderViewHeight);
					}
					mFooterView.setVisibility(View.GONE);
				}
				break;
		}
        return super.onTouchEvent(event);
	}

	/**
	 * <p>
	 * header 准备刷新,手指移动过程,还没有释放
	 * </p>
	 * 
	 * @param deltaY 手指滑动的距离
	 */
	private void headerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 当header view的topMargin>=0时，说明已经完全显示出来了,修改header view 的提示状态
		if (newTopMargin >= 0 && mHeaderState != RELEASE_TO_REFRESH) {
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderState = RELEASE_TO_REFRESH;
		} else if (newTopMargin < 0 && newTopMargin > -mHeaderViewHeight) {
			// 拖动时没有释放
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderState = PULL_TO_REFRESH;
		}
	}

	/**
	 * <p>
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 * </p>
	 * 
	 * @param deltaY 手指滑动的距离
	 */
	private void footerPrepareToRefresh(int deltaY) {
		int newTopMargin = changingHeaderViewTopMargin(deltaY);
		// 如果header view topMargin 的绝对值大于或等于header + footer 的高度
		// 说明footer view 完全显示出来了，修改footer view 的提示状态
		if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight/2) 
				&& mFooterState != RELEASE_TO_REFRESH) {
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterState = RELEASE_TO_REFRESH;
		} else if (Math.abs(newTopMargin) < (mHeaderViewHeight + mFooterViewHeight/2)) {
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterState = PULL_TO_REFRESH;
		}
	}

	/**
	 * <p>
	 * 修改Header view top margin的值
	 * </p>
	 * 
	 * @param deltaY 手指滑动的距离
	 * 
	 * @return
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.3f;
		// 这里对上拉做一下限制，因为当前上拉后然后不释放手指直接下拉，会把下拉刷新给触发了
		// 表示如果是在上拉后一段距离，然后直接下拉
		if (deltaY > 0 && mPullState == PULL_UP_STATE && Math.abs(params.topMargin) <= mHeaderViewHeight) {
			return params.topMargin;
		}
		// 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		if (deltaY < 0 && mPullState == PULL_DOWN_STATE && Math.abs(params.topMargin) >= mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	/**
	 * <p>
	 * header refreshing
	 * </p>
	 */
	private void headerRefreshing() {
		mHeaderState = REFRESHING;
		setHeaderTopMargin(0);
		mHeaderImageView.setVisibility(View.GONE);
		mHeaderImageView.clearAnimation();
		mHeaderImageView.setImageDrawable(null);
		mHeaderProgressBar.setVisibility(View.VISIBLE);
		if (mOnHeaderRefreshListener != null) {
			mOnHeaderRefreshListener.onRefresh(this);
		}
	}

	/**
	 * <p>
	 * footer refreshing
	 * </p>
	 */
	private void footerRefreshing() {
		mFooterState = REFRESHING;
		int top = mHeaderViewHeight + mFooterViewHeight;
		setHeaderTopMargin(-top);
		mFooterImageView.setVisibility(View.GONE);
		mFooterImageView.clearAnimation();
		mFooterImageView.setImageDrawable(null);
		mFooterProgressBar.setVisibility(View.VISIBLE);
		if (mOnFooterRefreshListener != null) {
			mOnFooterRefreshListener.onRefresh(this);
		}
	}

	/**
	 * <p>
	 * 设置header view 的topMargin的值
	 * </p>
	 * 
	 * @param topMargin 为0时，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * <p>
	 * header view 完成更新后恢复初始状态
	 * </p>
	 */
	public boolean onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mHeaderImageView.setVisibility(View.VISIBLE);
		mHeaderImageView.setImageResource(Utils.getResourcesId(getContext(), "ic_pulltorefresh_arrow", "drawable"));
		mHeaderProgressBar.setVisibility(View.GONE);
		mHeaderState = PULL_TO_REFRESH;
		return true;
	}

	/**
	 * <p>
	 * footer view 完成更新后恢复初始状态
	 * </p>
	 */
	public boolean onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		mFooterImageView.setVisibility(View.VISIBLE);
		mFooterImageView.setImageResource(Utils.getResourcesId(getContext(), "ic_pulltorefresh_arrow_up", "drawable"));
		mFooterProgressBar.setVisibility(View.GONE);
		mFooterState = PULL_TO_REFRESH;
		return true;
	}

	/**
	 * <p>
	 * 获取当前header view 的topMargin
	 * </p>
	 */
	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}

	/**
	 * <p>
	 * Interface definition for a callback to be invoked when list/grid
	 * header/footer view should be refreshed.
	 * </p>
	 */
	public interface OnRefreshListener {
		public void onRefresh(HXTableScrollTable view);
	}
}
