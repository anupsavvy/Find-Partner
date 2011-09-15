package com.android.joinme;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import com.android.adapter.joinme.AdAdapter;
import com.android.items.joinme.AdItem;
import com.android.soapconnection.ConnectionParameters;
import com.android.soapconnection.SOAPConnection;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class JoinMeShowRepliesActivity extends ListActivity {
 
	static final private int HOME = Menu.FIRST;
	SharedPreferences joinMeSettings;
	private ListView adListView;
	private AdAdapter arrayAdapter;
	private ArrayList<AdItem> entries;
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		joinMeSettings = getSharedPreferences(JoinMeBaseActivity.JOIN_ME_PREFERENCES,
				Context.MODE_PRIVATE);
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
		    	/*Intent intent = new Intent(JoinMeShowRepliesActivity.this, JoinMeReplyActivity.class);
		    	TextView username = (TextView) view.findViewById(R.id.Ad_NickName);
		    	TextView request = (TextView) view.findViewById(R.id.Ad_Request);
		    	TextView level = (TextView) view.findViewById(R.id.Level);
		    	TextView date = (TextView) view.findViewById(R.id.date);
		    	intent.putExtra("username",username.getText().toString());
		    	intent.putExtra("request", request.getText().toString());
		    	intent.putExtra("level", level.getText().toString());
		    	intent.putExtra("date",date.getText().toString());
		        startActivity(intent);*/
		    }
		  });
		loadList();	
	}
	
	private void loadList(){
		SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_GET_REPLIES,
				 ConnectionParameters.UPLOAD_URL,
				 ConnectionParameters.UPLOAD_GET_REPLIES_SOAPACTION,
				 ConnectionParameters.UPLOAD_URL_NAMESPACE);
		 conn.init();
		 SoapObject soapObj = conn.getSoapObject();
		 soapObj.addProperty("userName",joinMeSettings.getString(JoinMeBaseActivity.JOIN_ME_PREFERENCES_NICKNAME, "not set"));
		 try{
			 soapObj = conn.sendSoapObject();
			 int count=0;
			 AdItem newItem = null;
			 StringTokenizer stringTokenizer;
			 while(count < soapObj.getPropertyCount()){
				 SoapObject data = (SoapObject)soapObj.getProperty("return");
				 data = (SoapObject)soapObj.getProperty(count);
				 newItem = new AdItem(data.getProperty("replyFrom").toString().trim(),
						 data.getProperty("reply").toString().trim(),
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
}
