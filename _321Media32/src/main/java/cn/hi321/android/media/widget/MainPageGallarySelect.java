package cn.hi321.android.media.widget;

import android.view.View;
import android.widget.AdapterView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainPageGallarySelect implements OnItemSelectedListener {

    int maxPage = 0;
    int minPage = 0;
    boolean mIsFirstTouch = false;
    private int pageSize = 6;
    private RadioGroup mRadioGroupGallery ;
    
    

    public MainPageGallarySelect(int pageSize, RadioGroup mRadioGroupGallery) {
		this.pageSize = pageSize;
		this.mRadioGroupGallery = mRadioGroupGallery;
	}

	@Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {
    	RadioButton radioButton = (RadioButton) mRadioGroupGallery.getChildAt(position
                % pageSize);
    	if(radioButton!=null)
    		radioButton.setChecked(true);
        if (!mIsFirstTouch) {
            maxPage = position;
            minPage = position;
            mIsFirstTouch = true;
        } else {
            maxPage = (maxPage > position) ? maxPage : position;
            minPage = (minPage < position) ? minPage : position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

}
