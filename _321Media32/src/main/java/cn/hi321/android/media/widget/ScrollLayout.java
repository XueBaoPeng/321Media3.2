package cn.hi321.android.media.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
 

public class ScrollLayout extends ViewGroup {

		public Scroller mScroller;
		private VelocityTracker mVelocityTracker;
		public int whitchActivity = 0;
		public static int bigestPage = 0;//用来控制是否翻到的最大页数  在单页刷新时不用缓冲加载
		
		public static  int mCurScreen;
		private int mDefaultScreen = 0;
		
		private static final int TOUCH_STATE_REST = 0;
		private static final int TOUCH_STATE_SCROLLING = 1;
		
		private static final int SNAP_VELOCITY = 600;
		
		private int mTouchState = TOUCH_STATE_REST;
		private int mTouchSlop;
		private float mLastMotionX;
		private float mLastMotionY;

		public ScrollLayout(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
			// TODO Auto-generated constructor stub
		}

		public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			// TODO Auto-generated constructor stub
			mScroller = new Scroller(context);
			
			mCurScreen = mDefaultScreen;
			mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		}

		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			// TODO Auto-generated method stub
				int childLeft = 0;
				final int childCount = getChildCount();
				for (int i=0; i<childCount; i++) {
					final View childView = getChildAt(i);
					if (childView.getVisibility() != View.GONE) {
						final int childWidth = childView.getMeasuredWidth();
						childView.layout(childLeft, 0, 
								childLeft+childWidth, childView.getMeasuredHeight());
						childLeft += childWidth;
				}
			}
		}


	    @Override  
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {   
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);   
	  
	        final int width = MeasureSpec.getSize(widthMeasureSpec);   
	        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);   
	        if (widthMode != MeasureSpec.EXACTLY) { 
	        	return ;
//	            throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!"); 
	        }   
	  
	        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);   
	        if (heightMode != MeasureSpec.EXACTLY) {   
	            throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
	        }   
	  
	        // The children are given the same width and height as the scrollLayout   
	        final int count = getChildCount();   
	        for (int i = 0; i < count; i++) {   
	            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);   
	        } 
	        scrollTo(mCurScreen * width, 0);         
	    }  
	    
	    /**
	     *根据目前的位置布局　 滚动到目的地的网页。
	     */
	    public void snapToDestination() {
	    	final int screenWidth = getWidth();
	    	final int destScreen = (int)(getScrollX()+ screenWidth/1.2)/screenWidth;
	    	snapToScreen(destScreen);
	    }
	    
	    public void snapToScreen(int whichScreen) {
	    	// get the valid layout page
	    	whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));
	    	if (getScrollX() != (whichScreen*getWidth())) {
	    		final int delta = whichScreen*getWidth()-getScrollX();
	    		mScroller.startScroll(getScrollX(), 0, 
	    				delta, 0, Math.abs(delta)*2);
	   
	    		mCurScreen = whichScreen; 
	    		invalidate();		// Redraw the layout
	    	}
	    }
	    
	    public void setToScreen(int whichScreen) {
	    	whichScreen = Math.max(0, Math.min(whichScreen, getChildCount()-1));
	    	mCurScreen = whichScreen;
	    	scrollTo(whichScreen*getWidth(), 0);
	    }
	    
	    public int getCurScreen() {
	    	return mCurScreen;
	    }
	    
		@Override//计算滚动
		public void computeScroll() {
			//得到当前的位置
			if (mScroller.computeScrollOffset()) {
	    		scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
				postInvalidate();
			}
		}
		//处理拖动事件
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			// TODO Auto-generated method stub
			
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(event);
			
			final int action = event.getAction();
			final float x = event.getX();
			final float y = event.getY();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				if (!mScroller.isFinished()){
					mScroller.abortAnimation();
				}
				mLastMotionX = x;
				mLastMotionY = y;
				break;
				
			case MotionEvent.ACTION_MOVE:
				
				 
				int deltaX = (int)(mLastMotionX - x);
				int deltaY = (int)(mLastMotionY - y);
				mLastMotionX = x;
				mLastMotionY = y;
	            scrollBy(deltaX, 0);
	           
				break;
				
			case MotionEvent.ACTION_UP:
			 
	            final VelocityTracker velocityTracker = mVelocityTracker;   
	            velocityTracker.computeCurrentVelocity(1000);   
	            int velocityX = (int) velocityTracker.getXVelocity();
	            int velocityY = (int) velocityTracker.getYVelocity();
	            int deltaX1 = (int)(mLastMotionX - x);
	            if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {   
	            		snapToScreen(mCurScreen - 1);  
	            } else if (velocityX < -SNAP_VELOCITY   
	                    && mCurScreen < getChildCount() - 1) {   
	                snapToScreen(mCurScreen + 1);
	            } else {  
	            	//向左
	            	if(velocityX==0&&deltaX1>10){
	            		if(mCurScreen<getChildCount() - 1){
	            			  snapToScreen(mCurScreen +1);
	            		}
	            	//向右
	            	}else if(velocityX==0&&deltaX1<-10){
	            		if(mCurScreen > 0){
	            			 snapToScreen(mCurScreen -1);
	            		}
	            	}else{
	            		snapToDestination(); 
	            	}
	            }   
	            if (mVelocityTracker != null) {   
	                mVelocityTracker.recycle();   
	                mVelocityTracker = null;   
	            }   
	            // } 
	            mTouchState = TOUCH_STATE_REST;   
				break;
			case MotionEvent.ACTION_CANCEL:
				mTouchState = TOUCH_STATE_REST;
				break;
			}
			
			return true;
		}

		@Override//用于拦截和分发事件
		public boolean onInterceptTouchEvent(MotionEvent ev) {
			// TODO Auto-generated method stub
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
			}
			mVelocityTracker.addMovement(ev);
			final int action = ev.getAction();
			if ((action == MotionEvent.ACTION_MOVE) && 
					(mTouchState != TOUCH_STATE_REST)) {
				return true;
			}
			if(whitchActivity==4)
				return false;
			final float x = ev.getX();
			final float y = ev.getY();
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int)Math.abs(mLastMotionX-x);
				final int yDiff = (int)Math.abs(mLastMotionY-y);
				if (xDiff>mTouchSlop||yDiff>mTouchSlop) {
					//scrollBy(xDiff-mTouchSlop, 0);
					mTouchState = TOUCH_STATE_SCROLLING;
					
				}
				break;
				
			case MotionEvent.ACTION_DOWN:
				mLastMotionX = x;
				mLastMotionY = y;
				mTouchState = mScroller.isFinished()? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
				break;
				
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mTouchState = TOUCH_STATE_REST;
				break;
			}
//			return false;
			System.out.println("mTouchState=="+(mTouchState != TOUCH_STATE_REST));
			return mTouchState != TOUCH_STATE_REST;
		}
	}

