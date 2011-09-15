package com.android.adapter.joinme;

import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.items.joinme.AdItem;
import com.android.joinme.R;

public class AdAdapter extends ArrayAdapter<AdItem> {
	
	int resource;

	public AdAdapter(Context context, int _resource, List<AdItem> _items) {
		super(context, _resource, _items);
		resource = _resource;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		LinearLayout adView = null;
		
		AdItem item = getItem(position);
		
		String nickName = item.getNickName();
		String request = item.getRequest();
		int level = item.getLevel();
		String dateString = item.getDateString();
		
		if (convertView == null){
			adView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
			vi.inflate(resource, adView, true);
		}else{
			adView = (LinearLayout) convertView;
		}
		
		TextView nickNameTextView = (TextView)adView.findViewById(R.id.Ad_NickName);
		TextView requestTextView = (TextView)adView.findViewById(R.id.Ad_Request);
		TextView levelTextView = (TextView)adView.findViewById(R.id.Level);
		TextView dateStringTextView = (TextView)adView.findViewById(R.id.date);
		
		nickNameTextView.setText(nickName);
		requestTextView.setText(request);
		
		switch(level){
		case 0:
			levelTextView.setText("Beginner");
			break;
		case 1:
			levelTextView.setText("Intermediate");
			break;
		case 2:
			levelTextView.setText("Pro");
			break;
		}
		
		dateStringTextView.setText(dateString);
		
		return adView;
	}
}
