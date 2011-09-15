package com.android.joinme;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import com.android.soapconnection.ConnectionParameters;
import com.android.soapconnection.SOAPConnection;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.format.Time;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.widget.Button;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;

public class JoinMeSplashActivity extends JoinMeBaseActivity {
	
	static final int SIGNIN_DIALOG_ID = 1;
	SharedPreferences joinMeSettings;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        joinMeSettings = getSharedPreferences(JOIN_ME_PREFERENCES, Context.MODE_PRIVATE);
        if(!joinMeSettings.contains(JOIN_ME_PREFERENCES_NICKNAME)){
        	setContentView(R.layout.splash);
        	Button signinButton = (Button)findViewById(R.id.Singin_Button);
            signinButton.getBackground().setColorFilter(0x880000FF, Mode.MULTIPLY);
            TextView signUpText = (TextView)findViewById(R.id.Signup_TextView);
            SpannableString content = new SpannableString("Sign Up");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
            signUpText.setText(content);
            handleSignUp();
            handleSignIn();
        }
        else{
        	Intent intent = new Intent(JoinMeSplashActivity.this,JoinMeHomeActivity.class);
        	startActivity(intent);
        	finish();
        }
        
    }
	
	protected void handleSignUp(){
		TextView signupButton = (TextView) findViewById(R.id.Signup_TextView);
		signupButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				startActivity(new Intent(JoinMeSplashActivity.this, JoinMeSettingsActivity.class));
				JoinMeSplashActivity.this.finish();
			}
		});
	}
	
	protected void handleSignIn(){
		Button signinButton = (Button) findViewById(R.id.Singin_Button);
		final EditText userName = (EditText) findViewById(R.id.Signin_Username_EditText);
		final EditText password = (EditText) findViewById(R.id.Signin_Password_EditText);
		final TextView error = (TextView) findViewById(R.id.Signin_LoginProb_TextView);
		signinButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//JoinMeSplashActivity.this.showDialog(SIGNIN_DIALOG_ID);
				SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_SIGN_IN,
						 ConnectionParameters.UPLOAD_URL,
						 ConnectionParameters.UPLOAD_SIGN_IN_SOAPACTION,
						 ConnectionParameters.UPLOAD_URL_NAMESPACE);
				conn.init();
				SoapObject soapObj = conn.getSoapObject();
				soapObj.addProperty("nickName", userName.getText().toString());
				soapObj.addProperty("password", password.getText().toString());
				 try{
					 soapObj = conn.sendSoapObject();
					
					 if(soapObj.getProperty(0) != null){
						 
						 Editor edit = joinMeSettings.edit();
						 edit.putString(JOIN_ME_PREFERENCES_NICKNAME, userName.getText().toString());
						 
						 edit.putString(JOIN_ME_PREFERENCES_PASSWORD, password.getText().toString());
						 
						 edit.putString(JOIN_ME_PREFERENCES_EMAIL, soapObj.getProperty(0).toString());
						 
						 edit.putString(JOIN_ME_PREFERENCES_CITY, soapObj.getProperty(1).toString());
						 
						 edit.putString(JOIN_ME_PREFERENCES_COUNTRY, soapObj.getProperty(2).toString());
						 
						 edit.putString(JOIN_ME_PREFERENCES_FULLNAME, soapObj.getProperty(3).toString());
						  
						 //conversion of date of birth from string to long
						/* SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd,yyyy");
						 Log.i(DEBUG_TAG,"Date of Birth from table : " + soapObj.getProperty(1).toString().trim());
						 Date dateObj = simpleDateFormat.parse(soapObj.getProperty(1).toString().trim());
						 int dayOfMonth = dateObj.getDay();
						 int monthOfYear = dateObj.getMonth();
						 int year = dateObj.getYear();
						 Log.i(DEBUG_TAG,"Date of Birth : " + dayOfMonth + " " + monthOfYear + " " + year);
						 Time dateOfBirth = new Time();
						 dateOfBirth.set(dayOfMonth, monthOfYear, year);
						 long dOfBirth = dateOfBirth.toMillis(true);
						 
						 edit.putLong(JOIN_ME_PREFERENCES_DOB, dOfBirth);*/
						 edit.commit();
						 
						 Intent intent = new Intent(JoinMeSplashActivity.this,JoinMeHomeActivity.class);
						 startActivity(intent);
						 JoinMeSplashActivity.this.finish();
						 
					 }
					 else if(soapObj.getProperty(0) == null){
						 Toast.makeText(JoinMeSplashActivity.this, "Username/Password incorrect. Try again.",
								 Toast.LENGTH_LONG).show();
					 }
				 }
				 catch(Exception e){
					 Log.e(DEBUG_TAG,"Soap error : " + e.getMessage());
				 }
			}
		});
	}
	
	private void startAnimating(){
		TextView splashTitle = (TextView) findViewById(R.id.Splash_Title);
		Animation title_Animation = AnimationUtils.loadAnimation(this,R.anim.splash_title_anim);
		splashTitle.startAnimation(title_Animation);
	}
	
//	@Override
	/*protected Dialog onCreateDialog(int id){
		switch(id){
		case SIGNIN_DIALOG_ID:
			LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final View layout = inflater.inflate(R.layout.signin_dialog,(ViewGroup) findViewById(R.id.root));
			final EditText userName = (EditText) layout.findViewById(R.id.Signin_Username_EditText);
			final EditText password = (EditText) layout.findViewById(R.id.Signin_Password_EditText);
			final TextView error = (TextView) layout.findViewById(R.id.Signin_LoginProb_TextView);
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setView(layout);
			builder.setTitle(R.string.signin);
			builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int whichButton) {
					// TODO Auto-generated method stub
					JoinMeSplashActivity.this.removeDialog(SIGNIN_DIALOG_ID);
					
				}
			});
			builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_SIGN_IN,
							 ConnectionParameters.UPLOAD_URL,
							 ConnectionParameters.UPLOAD_SIGN_IN_SOAPACTION,
							 ConnectionParameters.UPLOAD_URL_NAMESPACE);
					conn.init();
					SoapObject soapObj = conn.getSoapObject();
					soapObj.addProperty("nickName", userName.getText().toString());
					soapObj.addProperty("password", password.getText().toString());
					 try{
						 soapObj = conn.sendSoapObject();
						
						 if(soapObj.getProperty(0) != null){
							 
							 Editor edit = joinMeSettings.edit();
							 edit.putString(JOIN_ME_PREFERENCES_NICKNAME, userName.getText().toString());
							 
							 edit.putString(JOIN_ME_PREFERENCES_PASSWORD, password.getText().toString());
							 
							 edit.putString(JOIN_ME_PREFERENCES_EMAIL, soapObj.getProperty(0).toString());
							 
							 edit.putInt(JOIN_ME_PREFERENCES_GENDER, Integer.parseInt(soapObj.getProperty(2).toString()));
							 
							 edit.putString(JOIN_ME_PREFERENCES_CITY, soapObj.getProperty(3).toString());
							 
							 edit.putString(JOIN_ME_PREFERENCES_COUNTRY, soapObj.getProperty(4).toString());
							 
							 edit.putString(JOIN_ME_PREFERENCES_FIRSTNAME, soapObj.getProperty(5).toString());
							 
							 edit.putString(JOIN_ME_PREFERENCES_LASTNAME, soapObj.getProperty(6).toString());
							 
							 //conversion of date of birth from string to long
							 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd,yyyy");
							 Log.i(DEBUG_TAG,"Date of Birth from table : " + soapObj.getProperty(1).toString().trim());
							 Date dateObj = simpleDateFormat.parse(soapObj.getProperty(1).toString().trim());
							 int dayOfMonth = dateObj.getDay();
							 int monthOfYear = dateObj.getMonth();
							 int year = dateObj.getYear();
							 Log.i(DEBUG_TAG,"Date of Birth : " + dayOfMonth + " " + monthOfYear + " " + year);
							 Time dateOfBirth = new Time();
							 dateOfBirth.set(dayOfMonth, monthOfYear, year);
							 long dOfBirth = dateOfBirth.toMillis(true);
							 
							 edit.putLong(JOIN_ME_PREFERENCES_DOB, dOfBirth);
							 edit.commit();
							 
							 Intent intent = new Intent(JoinMeSplashActivity.this,JoinMeCategoriesActivity.class);
							 startActivity(intent);
							 JoinMeSplashActivity.this.finish();
							 
						 }
						 else if(soapObj.getProperty(0) == null){
							 Toast.makeText(JoinMeSplashActivity.this, "Username/Password incorrect. Try again.",
									 Toast.LENGTH_LONG).show();
						 }
					 }
					 catch(Exception e){
						 Log.e(DEBUG_TAG,"Soap error : " + e.getMessage());
					 }
				}
				
			});
			AlertDialog signInDialog = builder.create();
			return signInDialog;
		}
		return null;
	}*/
//	@Override
	/*protected void onPrepareDialog(int dialogID, Dialog dialog){
		super.onPrepareDialog(dialogID, dialog);
		switch(dialogID){
		case SIGNIN_DIALOG_ID:
			return;
		}
	}*/
	
}
