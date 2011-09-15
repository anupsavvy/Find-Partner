package com.android.joinme;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import com.android.joinme.JoinMeBaseActivity;

public class JoinMeCategoriesActivity extends ListActivity {
	static private final int HOME = Menu.FIRST;
    private static final int ADACTIVITY = 1;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		initList();
	}
	private void initList(){
		String[] categories = getResources().getStringArray(R.array.categories);
		View listItem = (View) findViewById(android.R.layout.simple_list_item_1);
	
		setListAdapter(new ArrayAdapter<String>(JoinMeCategoriesActivity.this,
				android.R.layout.simple_list_item_1,categories));
		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				JoinMeCategoriesActivity.this.startActivityForResult((new Intent(JoinMeCategoriesActivity.this,JoinMeAdActivity.class
						).putExtra("category",((TextView)view).getText())),ADACTIVITY);
			}  
		});
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		
		switch(requestCode){
		case ADACTIVITY:
			if(resultCode == Activity.RESULT_CANCELED){
				finish();
			}
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		
		MenuItem settingsItem = menu.add(0, HOME, Menu.NONE,"HOME");
		settingsItem.setIcon(R.drawable.ic_menu_home);
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		super.onPrepareOptionsMenu(menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem){
		super.onOptionsItemSelected(menuItem);
		switch(menuItem.getItemId()){
		case HOME:
			startActivity(new Intent(JoinMeCategoriesActivity.this, JoinMeHomeActivity.class));
			finish();
			break;
		}
		
		return true;
	}

}
