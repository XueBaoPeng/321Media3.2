package cn.hi321.android.media.widget;
 
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.hi321.android.media.ui.SearchActivity;
import cn.hi321.android.media.utils.UIUtils;

import com.android.china.R;

public class MainTitlebar extends LinearLayout implements OnClickListener{

	  private Button btnLogo;
	  private ImageButton btnSearch;
	  private Context context;
	  private TextView tvTitle;

	  public MainTitlebar(Context paramContext)
	  {
	    this(paramContext, null);
	  }

	  public MainTitlebar(Context paramContext, AttributeSet paramAttributeSet)
	  {
	    super(paramContext, paramAttributeSet);
	    View localView = UIUtils.getLayoutInflater(paramContext).inflate(R.layout.view_main_title_bar, this);
	    this.context = paramContext;
	    initView();
	  }
	  private void initView()
	  {
	    Button localButton1 = (Button)findViewById(R.id.btn_logo);
	    this.btnLogo = localButton1;
	    ImageButton localImageButton1 = (ImageButton)findViewById(R.id.btn_search);
	    this.btnSearch = localImageButton1;
//	    btnSearch.setVisibility(View.GONE);
	    TextView localTextView = (TextView)findViewById(R.id.tv_title);
	    this.tvTitle = localTextView;
	    Button localButton2 = this.btnLogo;
	    btnSearch.setOnClickListener(this);
	  }

	  public void show(String paramString)
	  {
	    this.tvTitle.setText(paramString);
	  }

	@Override
	public void onClick(View v) {
		 if(v.getId() == R.id.btn_logo){
//			 ((MainActivity)MainTitlebar.access$0(this.this$0)).showTab(2131296395);
		 }else if(v.getId() == R.id.btn_search){ 
		    Intent localIntent = new Intent(context, SearchActivity.class);
		    context.startActivity(localIntent);
		    ((Activity)context).overridePendingTransition(R.anim.fade, R.anim.hold);
		 }
	}
}
