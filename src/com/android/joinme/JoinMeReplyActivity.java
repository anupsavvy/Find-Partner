package com.android.joinme;

import org.ksoap2.serialization.SoapObject;

import com.android.soapconnection.ConnectionParameters;
import com.android.soapconnection.SOAPConnection;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class JoinMeReplyActivity extends JoinMeBaseActivity {
	SharedPreferences joinMeSettings;
	private String username = null;
	private String request = null;
	private String level = null;
	private String date = null;
	private Button replyButton = null;
	private Button cancelButton = null;
	private TextView replyTextView = null;
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply);
        joinMeSettings = getSharedPreferences(JOIN_ME_PREFERENCES, Context.MODE_PRIVATE);  
        init();
        initReplyClickAction();
        initCancelClickAction();
    }
	
	private void init(){
		Intent intent = getIntent();
        username = intent.getExtras().getString("username");
        request = intent.getExtras().getString("request");
        level = intent.getExtras().getString("level");
        date = intent.getExtras().getString("date");
        TextView usernameTextView = (TextView) findViewById(R.id.Reply_NickName);
        TextView requestTextView = (TextView) findViewById(R.id.Reply_Request);
        TextView levelTextView = (TextView) findViewById(R.id.Reply_Level);
        TextView dateTextView = (TextView) findViewById(R.id.reply_date);
        replyTextView = (TextView)findViewById(R.id.reply_text);
        usernameTextView.setText(username);
        requestTextView.setText(request);
        levelTextView.setText(level);
        dateTextView.setText(date);
        replyTextView.setText("@" + username + ": ");
        replyButton = (Button)findViewById(R.id.reply_button);
        replyButton.getBackground().setColorFilter(0x880000FF, Mode.MULTIPLY);
        cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.getBackground().setColorFilter(0x880000FF, Mode.MULTIPLY);
	}
	
	private void initReplyClickAction(){
		replyButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(validateForm()){
					SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_SET_REPLY,
							 ConnectionParameters.UPLOAD_URL,
							 ConnectionParameters.UPLOAD_SET_REPLY_SOAPACTION,
							 ConnectionParameters.UPLOAD_URL_NAMESPACE);
					 conn.init();
					 SoapObject soapObj = conn.getSoapObject();
					 soapObj.addProperty("replyTo",username);
					 soapObj.addProperty("dated",date);
					 soapObj.addProperty("leveled",level);
					 soapObj.addProperty("forRequest",request);
					 soapObj.addProperty("replyFrom",joinMeSettings.getString(JOIN_ME_PREFERENCES_NICKNAME, "not set"));
					 soapObj.addProperty("reply",replyTextView.getText().toString().trim());
					 try{
						 conn.sendSoapObject();
						// Toast.makeText(JoinMeReplyActivity.this, "Reply confirmed", Toast.LENGTH_SHORT).show();
						 finish();
					 }
					 catch(Exception e){
						 Log.e(DEBUG_TAG,e.getMessage());
					 }
				}
			}
		});
		
	}
	
	private void initCancelClickAction(){
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				replyTextView.setText("");
				replyTextView.setText("@" + username + ": ");
				finish();
			}
		});
	}
	
	private boolean validateForm(){
		String replyText = replyTextView.getText().toString().trim();
		if(replyText.equals("")){
			validateFormMessage("Reply text cannot be empty");
			return false;
		}else if(validateObscenity(replyText)){
			validateFormMessage("Obscene words found. Help to keep this space clean." +
					"Lets not offend anyone.");
			return false;
		}else{
			return true;
		}
	}
	
	private void validateFormMessage(String message){
		Toast.makeText(this,message, Toast.LENGTH_LONG).show();
	}
	
	private boolean validateObscenity(String replyText){
		SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_CHECK_OBSCENITY,
				 ConnectionParameters.UPLOAD_URL,
				 ConnectionParameters.UPLOAD_CHECK_OBSCENITY_SOAPACTION,
				 ConnectionParameters.UPLOAD_URL_NAMESPACE);
		 conn.init();
		 SoapObject soapObj = conn.getSoapObject();
		 soapObj.addProperty("replyText",replyText);
		 try{
			 soapObj = conn.sendSoapObject();
			 if(soapObj.getProperty(0).toString().equals("true"))
				 return true;
		 }
		 catch(Exception e){
			 Log.e(DEBUG_TAG,e.getMessage());
		 }
		return false;
	}
	
}
