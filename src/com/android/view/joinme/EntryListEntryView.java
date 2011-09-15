package com.android.view.joinme;

import com.android.joinme.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class EntryListEntryView extends LinearLayout {
	
	private Paint paintLine;
	private int viewColor;

	public EntryListEntryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public EntryListEntryView(Context context){
		super(context);
		init();
	}
	
	private void init(){
		Resources myResources = getResources();
		
		paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
		paintLine.setColor(myResources.getColor(R.color.paint_line));
		
		viewColor = myResources.getColor(R.color.view_color);
	}
	
	public void onDraw(Canvas canvas){
		canvas.drawColor(viewColor);
		
		canvas.drawLine(0, 0, getMeasuredHeight(), 0, paintLine);
		canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), paintLine);
		
		canvas.save();
		
		super.onDraw(canvas);
		canvas.restore();
	}

}
