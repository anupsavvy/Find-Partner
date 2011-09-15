package com.android.joinme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class JoinMeHomeActivity extends JoinMeBaseActivity {
	SharedPreferences joinMeSettings;
	private Button categories;
	private Button replies;
	private Button myProfile;
	private Button help;
	static private final int EXIT = Menu.FIRST;
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        joinMeSettings = getSharedPreferences(JOIN_ME_PREFERENCES, Context.MODE_PRIVATE);
        setButtonColor();
        initCategories();
        initReplies();
        initMyProfile();
        initHelp();
    }
	private void setButtonColor(){
		categories = (Button)findViewById(R.id.Categories);
        categories.getBackground().setColorFilter(0x880000FF, Mode.MULTIPLY);
        replies = (Button)findViewById(R.id.Replies);
        replies.getBackground().setColorFilter(0x880000FF, Mode.MULTIPLY);
        myProfile = (Button)findViewById(R.id.My_Profile);
        myProfile.getBackground().setColorFilter(0x880000FF, Mode.MULTIPLY);
        help = (Button)findViewById(R.id.Help);
        help.getBackground().setColorFilter(0x880000FF, Mode.MULTIPLY);
	}
	private void initCategories(){
		categories.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JoinMeHomeActivity.this,JoinMeCategoriesActivity.class);
				startActivity(intent);
			}
		});
	}
	private void initReplies(){
		
	}
	private void initMyProfile(){
           myProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(JoinMeHomeActivity.this,JoinMeSettingsActivity.class);
				startActivity(intent);
			}
		});
	}
	private void initHelp(){
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuItem exitItem = menu.add(0, EXIT, Menu.NONE, R.string.exit);
		exitItem.setIcon(R.drawable.ic_menu_stop);
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
		case EXIT:
			Editor editor = joinMeSettings.edit();
			editor.remove(JOIN_ME_PREFERENCES_NICKNAME);
			editor.remove(JOIN_ME_PREFERENCES_PASSWORD);
			editor.commit();
			finish();
		}
		
		return true;
	}
}
