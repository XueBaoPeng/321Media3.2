package cn.hi321.android.media.widget;
 
import android.widget.ListView;

public class MainListview extends ListView{
	public MainListview(android.content.Context context ){  
        super(context);  
    }  
  
	public MainListview(android.content.Context context,android.util.AttributeSet attrs){  
        super(context, attrs);  
    }  
  
    /** 
     * 设置不滚动 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,  
                MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
  
    }  
}
