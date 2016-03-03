package cn.hi321.android.media.widget; 
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;

public class GalleryView extends Gallery
{
	public static boolean ISAUTOFLING = true;
	
	public GalleryView(Context context)
	{
		super(context);
	}
	public GalleryView(Context context, AttributeSet attributeset)
	{
		super(context, attributeset);
	}
	public GalleryView(Context context, AttributeSet attributeset, int i)
	{
		super(context, attributeset, i);	
	}
	public void scrollToLeft()
	{
		onScroll(null, null, -1, 0);
		super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, null);
	}
	
	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}
	
	public void scrollToRight()
	{
		onScroll(null, null, 1, 0);
		onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, null);
	}
	public boolean onDown(MotionEvent motionevent)
	{
		return super.onDown(motionevent);
	}

	public boolean onFling(MotionEvent motionevent, MotionEvent motionevent1, float f, float f1)
	{
		if (isScrollingLeft(motionevent,motionevent1)) 
		{
			ISAUTOFLING  = false;
			scrollToLeft();
		}
		else
		{
			ISAUTOFLING  = false;
			scrollToRight();
		}
		return false;
	}

}