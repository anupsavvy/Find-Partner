package com.android.joinme;

import java.util.ArrayList;
import java.util.StringTokenizer;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.widget.TextView;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.android.soapconnection.ConnectionParameters;
import com.android.soapconnection.SOAPConnection;
import android.widget.Button;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;


import com.android.adapter.joinme.AdAdapter;
import com.android.joinme.JoinMeBaseActivity;
import com.android.items.joinme.AdItem;
import com.android.view.joinme.EntryListEntryView;
public class JoinMeAdActivity extends ListActivity{

	static final int NEW_ENTRY_DIALOG_ID = 1;
	static private final int ADD_NEW_ENTRY = Menu.FIRST;
	static private final int REFRESH_LIST = Menu.FIRST + 1;
	static private final int HOME = Menu.FIRST + 2;
	
	private String category = null;
	SharedPreferences joinMeSettings;
	private AdAdapter arrayAdapter;
	private ArrayList<AdItem> entries;
	private ListView adListView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.ad_list);
		joinMeSettings = getSharedPreferences(JoinMeBaseActivity.JOIN_ME_PREFERENCES,Context.MODE_PRIVATE);
		Intent categoriesIntent = getIntent();
		category = categoriesIntent.getExtras().getString("category");
		init();
	}
	
	private void init(){
		adListView = getListView();
		entries = new ArrayList<AdItem>();
		int resID = R.layout.ad_list_item;
		arrayAdapter = new AdAdapter(this,resID,entries);
		adListView.setAdapter(arrayAdapter);
		adListView.setHapticFeedbackEnabled(true);
		adListView.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		        int position, long id) {
		      // When clicked, show a toast with the TextView text
		    	Intent intent = new Intent(JoinMeAdActivity.this, JoinMeReplyActivity.class);
		    	TextView username = (TextView) view.findViewById(R.id.Ad_NickName);
		    	TextView request = (TextView) view.findViewById(R.id.Ad_Request);
		    	TextView level = (TextView) view.findViewById(R.id.Level);
		    	TextView date = (TextView) view.findViewById(R.id.date);
		    	intent.putExtra("username",username.getText().toString());
		    	intent.putExtra("request", request.getText().toString());
		    	intent.putExtra("level", level.getText().toString());
		    	intent.putExtra("date",date.getText().toString());
		        startActivity(intent);
		    }
		  });
		loadList();	
	}
	
	private void loadList(){
		SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_GET_REQUESTS,
				 ConnectionParameters.UPLOAD_URL,
				 ConnectionParameters.UPLOAD_GET_REQUESTS_SOAPACTION,
				 ConnectionParameters.UPLOAD_URL_NAMESPACE);
		 conn.init();
		 SoapObject soapObj = conn.getSoapObject();
		 soapObj.addProperty("event",category);
		 soapObj.addProperty("region",joinMeSettings.getString(JoinMeBaseActivity.JOIN_ME_PREFERENCES_CITY, "not set"));
		 try{
			 soapObj = conn.sendSoapObject();
			 int count=0;
			 AdItem newItem = null;
			 
			 while(count < soapObj.getPropertyCount()){
				 SoapObject data = (SoapObject)soapObj.getProperty("return");
				 data = (SoapObject)soapObj.getProperty(count);
				 newItem = new AdItem(data.getProperty("nickName").toString().trim(),
						 data.getProperty("request").toString().trim(),
						 Integer.parseInt(data.getProperty("level").toString().trim()),
						 data.getProperty("date").toString().trim());
				 entries.add(0,newItem);
				 arrayAdapter.notifyDataSetChanged();
				 count++;
			 }
			
		 }catch(SoapFault e){
			 Log.e(JoinMeBaseActivity.DEBUG_TAG,"Download error : " + e.faultactor + " " +
					 e.faultcode + " " + e.faultstring);
		 }catch(ClassCastException e){
			 Log.e(JoinMeBaseActivity.DEBUG_TAG,"Class cast exception : " + e.getMessage());
		 }catch(Exception e){
			 Log.e(JoinMeBaseActivity.DEBUG_TAG,"General exception : " + e.getLocalizedMessage());
			 e.printStackTrace();
		 }
	}

	protected Dialog onCreateDialog(int id){

		switch(id){
		case NEW_ENTRY_DIALOG_ID:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.newentry_dialog, (ViewGroup)findViewById(R.id.new_entry_root));
			final EditText newEntryText = (EditText) layout.findViewById(R.id.new_entry_text);
			final TextView newEntryExampleText = (TextView) layout.findViewById(R.id.example_text);

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.entry);
			
			// code for spinner
			final Spinner level = (Spinner) layout.findViewById(R.id.Spinner_Level);
			
			ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,R.array.level,android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			level.setAdapter(adapter);
			level.setSelection(0);
			
			level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

				public void onItemSelected(AdapterView<?> parent, View itemSelected,
						int selectedItemPosition, long selectedId) {
					Editor edit = joinMeSettings.edit();
					edit.putInt(JoinMeBaseActivity.JOIN_ME_PREFERENCES_LEVEL, selectedItemPosition);
					edit.commit();
				}

				public void onNothingSelected(AdapterView<?> arg0) {
					Editor edit = joinMeSettings.edit();
					edit.putInt(JoinMeBaseActivity.JOIN_ME_PREFERENCES_LEVEL, 0);
					edit.commit();
				}
			});
			
			//spinner code ends
			builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int whichButton) {
					// TODO Auto-generated method stub
					JoinMeAdActivity.this.removeDialog(NEW_ENTRY_DIALOG_ID);

				}
			});
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_NEW_ENTRY,
							ConnectionParameters.UPLOAD_URL,
							ConnectionParameters.UPLOAD_NEW_ENTRY_SOAPACTION,
							ConnectionParameters.UPLOAD_URL_NAMESPACE);
					conn.init();
					SoapObject soapObj = conn.getSoapObject();
					soapObj.addProperty("nickName",joinMeSettings.getString(
							JoinMeBaseActivity.JOIN_ME_PREFERENCES_NICKNAME, "anup"));
					soapObj.addProperty("event", category);
					try{
						soapObj.addProperty("request",newEntryText.getText().toString());
					}
					catch(NullPointerException e){
						removeDialog(NEW_ENTRY_DIALOG_ID);
					}
					soapObj.addProperty("region",joinMeSettings.getString(
							JoinMeBaseActivity.JOIN_ME_PREFERENCES_CITY, "not set"));
					soapObj.addProperty("country",joinMeSettings.getString(
							JoinMeBaseActivity.JOIN_ME_PREFERENCES_COUNTRY, "not set"));
					soapObj.addProperty("level",joinMeSettings.getInt(
							JoinMeBaseActivity.JOIN_ME_PREFERENCES_LEVEL, 0));
					try{
						conn.sendSoapObject();
					}
					catch(Exception e){
						Log.e(JoinMeBaseActivity.DEBUG_TAG,e.getMessage());
					}

					JoinMeAdActivity.this.removeDialog(NEW_ENTRY_DIALOG_ID);
				}

			});
			AlertDialog signUpDialog = builder.create();
			return signUpDialog;
		}

		return null;

	}

	protected void onPrepareDialog(int dialogID, Dialog dialog){
		super.onPrepareDialog(dialogID, dialog);
		switch(dialogID){
		case NEW_ENTRY_DIALOG_ID:
			return;

		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuItem addEntryItem = menu.add(0, ADD_NEW_ENTRY, Menu.NONE, R.string.entry);
		addEntryItem.setIcon(android.R.drawable.ic_menu_add);
		MenuItem refreshItem = menu.add(0, REFRESH_LIST, Menu.NONE, R.string.refresh);
		refreshItem.setIcon(R.drawable.ic_menu_refresh);
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
		case ADD_NEW_ENTRY:
			showDialog(NEW_ENTRY_DIALOG_ID);
			break;
		case REFRESH_LIST:
			init();
			break;
		case HOME:
			startActivity(new Intent(JoinMeAdActivity.this, JoinMeHomeActivity.class));
			finish();
			break;
		}
		
		return true;
	}
	
	
}
