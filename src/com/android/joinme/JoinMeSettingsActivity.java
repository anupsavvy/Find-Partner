package com.android.joinme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Pattern;

import com.android.joinme.R;
import android.provider.MediaStore.Images.Media;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

import org.kobjects.base64.Base64;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import com.android.soapconnection.SOAPConnection;
import com.android.soapconnection.ConnectionParameters;

public class JoinMeSettingsActivity extends JoinMeBaseActivity {
	
	SharedPreferences joinMeSettings;
	static final int PASSWORD_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	static final int AVATAR_CAMERA_ID=2;
	static final int AVATAR_GALLERY_ID=3;
	static final String[] COUNTRIES = new String[] {
		  "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra",
		  "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina",
		  "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan",
		  "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
		  "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia",
		  "Bosnia and Herzegovina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory",
		  "British Virgin Islands", "Brunei", "Bulgaria", "Burkina Faso", "Burundi",
		  "Cote d'Ivoire", "Cambodia", "Cameroon", "Canada", "Cape Verde",
		  "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
		  "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
		  "Cook Islands", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic",
		  "Democratic Republic of the Congo", "Denmark", "Djibouti", "Dominica", "Dominican Republic",
		  "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
		  "Estonia", "Ethiopia", "Faeroe Islands", "Falkland Islands", "Fiji", "Finland",
		  "Former Yugoslav Republic of Macedonia", "France", "French Guiana", "French Polynesia",
		  "French Southern Territories", "Gabon", "Georgia", "Germany", "Ghana", "Gibraltar",
		  "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau",
		  "Guyana", "Haiti", "Heard Island and McDonald Islands", "Honduras", "Hong Kong", "Hungary",
		  "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
		  "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
		  "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg",
		  "Macau", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands",
		  "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia", "Moldova",
		  "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia",
		  "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand",
		  "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea", "Northern Marianas",
		  "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru",
		  "Philippines", "Pitcairn Islands", "Poland", "Portugal", "Puerto Rico", "Qatar",
		  "Reunion", "Romania", "Russia", "Rwanda", "Sqo Tome and Principe", "Saint Helena",
		  "Saint Kitts and Nevis", "Saint Lucia", "Saint Pierre and Miquelon",
		  "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Saudi Arabia", "Senegal",
		  "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands",
		  "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "South Korea",
		  "Spain", "Sri Lanka", "Sudan", "Suriname", "Svalbard and Jan Mayen", "Swaziland", "Sweden",
		  "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "The Bahamas",
		  "The Gambia", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey",
		  "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Virgin Islands", "Uganda",
		  "Ukraine", "United Arab Emirates", "United Kingdom",
		  "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
		  "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Wallis and Futuna", "Western Sahara",
		  "Yemen", "Yugoslavia", "Zambia", "Zimbabwe"
		};
	private static final Pattern emailPattern = Pattern.compile( 
	        "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$" 
	); 

	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        joinMeSettings = getSharedPreferences(JOIN_ME_PREFERENCES, Context.MODE_PRIVATE);
        initAvatarEntry();
        initNickNameEntry();
        initFullNameEntry();
        initEmailEntry();
        initCityEntry();
        initCountryEntry();
        initPasswordEntry();
        //initDOBEntry();
        //initGenderSpinner();
        initSubmit();
    }
	
	 @Override
	    protected void onDestroy(){
	    	Log.d(DEBUG_TAG,"SHARED PREFERENCES");
	    	Log.d(DEBUG_TAG, "Nickname is :" + joinMeSettings.getString(JOIN_ME_PREFERENCES_NICKNAME, "not set"));
	    	Log.d(DEBUG_TAG, "Email is :" + joinMeSettings.getString(JOIN_ME_PREFERENCES_EMAIL, "not set"));
	    	Log.d(DEBUG_TAG, "Password is :" + joinMeSettings.getString(JOIN_ME_PREFERENCES_PASSWORD, "not set"));
	    	Log.d(DEBUG_TAG, "DOB is :" + joinMeSettings.getLong(JOIN_ME_PREFERENCES_DOB, 0));
	    	Log.d(DEBUG_TAG, "Gender is :" + joinMeSettings.getInt(JOIN_ME_PREFERENCES_GENDER, 0));
	    	super.onDestroy();
	    }
	 private void initFullNameEntry(){
		 final EditText fullName  = (EditText) findViewById(R.id.Full_Name);
		 
		 if(joinMeSettings.contains(JOIN_ME_PREFERENCES_FULLNAME)){
	    		fullName.setText(joinMeSettings.getString(JOIN_ME_PREFERENCES_FULLNAME, "not set"));
	    	}
	    	
	    	fullName.setOnKeyListener(new View.OnKeyListener() {
				
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
						String fullNameString = fullName.getText().toString();
						Editor edit = joinMeSettings.edit();
						edit.putString(JOIN_ME_PREFERENCES_FULLNAME, fullNameString);
						edit.commit();
						return true;
					}
					return false;
				}
			}); 
	 }
	 private void initCityEntry(){
		 final EditText city  = (EditText) findViewById(R.id.EditText_City);
		 
		 if(joinMeSettings.contains(JOIN_ME_PREFERENCES_CITY)){
	    		city.setText(joinMeSettings.getString(JOIN_ME_PREFERENCES_CITY, "Ithaca"));
	    	}
	    	
	    	city.setOnKeyListener(new View.OnKeyListener() {
				
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
						String cityString = city.getText().toString();
						Editor edit = joinMeSettings.edit();
						edit.putString(JOIN_ME_PREFERENCES_CITY, cityString);
						edit.commit();
						return true;
					}
					return false;
				}
			}); 
	 }
	 
	 private void initCountryEntry(){
			
		    final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView)
		    findViewById(R.id.autocomplete_country);
		    
		    if(joinMeSettings.contains(JOIN_ME_PREFERENCES_COUNTRY)){
	    		autoCompleteTextView.setText(joinMeSettings.getString(JOIN_ME_PREFERENCES_COUNTRY, "United States"));
	    	}
		    
		    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(JoinMeSettingsActivity.this,
		    		R.layout.country_list_item, COUNTRIES);
		    
		    autoCompleteTextView.setAdapter(arrayAdapter);
		    autoCompleteTextView.setOnItemClickListener(new OnItemClickListener(){
		    	public void onItemClick(AdapterView<?> parent, View view, int position,
						long id) {
		    		String country = ((TextView)view).getText().toString();
					Editor edit = joinMeSettings.edit();
					edit.putString(JOIN_ME_PREFERENCES_COUNTRY, country);
					edit.commit();
				}  
		    });
		} 
	 
	 private void initSubmit(){
		 final Button submitButton = (Button) findViewById(R.id.Submit_Button);
		 submitButton.setOnClickListener(new View.OnClickListener() {

			 public void onClick(View v) {
				 if(validateForm()){
					 SOAPConnection conn = new SOAPConnection(ConnectionParameters.UPLOAD_SETTINGS,
							 ConnectionParameters.UPLOAD_URL,
							 ConnectionParameters.UPLOAD_SETTINGS_SOAPACTION,
							 ConnectionParameters.UPLOAD_URL_NAMESPACE);
					 conn.init();
					 SoapObject soapObj = conn.getSoapObject();
					 soapObj.addProperty("nickName",joinMeSettings.getString(
							 JOIN_ME_PREFERENCES_NICKNAME, "not set"));
					 soapObj.addProperty("email",joinMeSettings.getString(
							 JOIN_ME_PREFERENCES_EMAIL, "not set"));
					 soapObj.addProperty("password",joinMeSettings.getString(
							 JOIN_ME_PREFERENCES_PASSWORD, "not set"));

					 /*SimpleDateFormat currFormat = new SimpleDateFormat("MMMM dd,yyyy");
				 try{
					 java.util.Date dateObj = currFormat.parse((String)DateFormat.format("MMMM dd,yyyy",joinMeSettings.getLong(
							 JOIN_ME_PREFERENCES_DOB, 0)));
					 soapObj.addProperty("date",dateObj);
				 }catch(ParseException e){
					 Log.e(DEBUG_TAG, "ATTENTION: Parser exception");
					 Log.e(DEBUG_TAG,e.getMessage());
				 }catch(ClassCastException e){
					 Log.e(DEBUG_TAG,"ATTENTION: Class cast exception");
					 Log.e(DEBUG_TAG,e.getMessage());
				 }*/
					// soapObj.addProperty("date",(String)DateFormat.format("MMMM dd,yyyy",joinMeSettings.getLong(
						//	 JOIN_ME_PREFERENCES_DOB, 0)));
					 //soapObj.addProperty("gender",joinMeSettings.getInt(
						//	 JOIN_ME_PREFERENCES_GENDER, 0));
					 soapObj.addProperty("city",joinMeSettings.getString(JOIN_ME_PREFERENCES_CITY, "not set"));
					 soapObj.addProperty("country",joinMeSettings.getString(JOIN_ME_PREFERENCES_COUNTRY, "not set"));
					 soapObj.addProperty("fullName",joinMeSettings.getString(JOIN_ME_PREFERENCES_FULLNAME, "not set"));
					 String imageString = null;
					 try {
						 File imageFile = new File(joinMeSettings.getString(JOIN_ME_PREFERENCES_AVATAR,
						 "android.resource://com.android.joinme/drawable/joinme"));
						 FileInputStream is = new FileInputStream(imageFile);
						 byte[] imageBytes = new byte[(int)imageFile.length()];
						 is.read(imageBytes);
						 imageString = new String(Base64.encode(imageBytes));
						 
					 } catch(IOException e){
						 Log.e(DEBUG_TAG,"Error while reading image: " + e.getLocalizedMessage());
					 }
					 soapObj.addProperty("image",imageString);
					 try{
						 conn.sendSoapObject();
					 }
					 catch(Exception e){
						 Log.e(DEBUG_TAG,e.getMessage());
					 }
					 startActivity(new Intent(JoinMeSettingsActivity.this,JoinMeCategoriesActivity.class));
					 JoinMeSettingsActivity.this.finish();
				 }
			 }
		 });
	 }
	 
	private void initAvatarEntry(){
		final ImageButton avatar = (ImageButton) findViewById(R.id.ImageButton_Avatar);
		avatar.setOnLongClickListener(new View.OnLongClickListener() {
			
			public boolean onLongClick(View v) {
				 String strAvatarPrompt = "Choose a picture to use as your avatar!";
	             Intent pickPhoto = new Intent(Intent.ACTION_PICK);
	             pickPhoto.setType("image/*");
	             startActivityForResult(Intent.createChooser(pickPhoto, strAvatarPrompt), AVATAR_GALLERY_ID);
	             return true;
			}
		});
		
		avatar.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				  String strAvatarPrompt = "Take your picture to store as your avatar!";
	              Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	              startActivityForResult(Intent.createChooser(pictureIntent, strAvatarPrompt), AVATAR_CAMERA_ID);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode){
		case AVATAR_CAMERA_ID:
			if(resultCode == Activity.RESULT_CANCELED){
				//Camera mode canceled
			}
			else if(resultCode == Activity.RESULT_OK){
				Bitmap cameraPicture = (Bitmap) data.getExtras().get("data");
				if(cameraPicture!=null){
					saveAvatar(cameraPicture);
				}
				else{
					Log.e(DEBUG_TAG, "Some error while saving the image");
				}
			}
			break;
		case AVATAR_GALLERY_ID:
			if(resultCode == Activity.RESULT_CANCELED){
				//Gallery mode canceled
			}
			else if(resultCode == Activity.RESULT_OK){
				  // Get image picked
                Uri photoUri = data.getData();
                if (photoUri != null) {
                    try {
                        int maxLength = 75;
                        // Full size image likely will be large. Let's scale the graphic to a more appropriate size for an avatar
                        Bitmap galleryPic = Media.getBitmap(getContentResolver(), photoUri);
                        Bitmap scaledGalleryPic = createScaledBitmapKeepingAspectRatio(galleryPic, maxLength);
                        saveAvatar(scaledGalleryPic);
                    } catch (Exception e) {
                        Log.e(DEBUG_TAG, "saveAvatar() with gallery picker failed.", e);
                    }
                }

			}
			break;
		}
	}
	
	 private Bitmap createScaledBitmapKeepingAspectRatio(Bitmap bitmap, int maxSide) {
	        int orgHeight = bitmap.getHeight();
	        int orgWidth = bitmap.getWidth();

	        // scale to no longer any either side than 75px
	        int scaledWidth = (orgWidth >= orgHeight) ? maxSide : (int) ((float) maxSide * ((float) orgWidth / (float) orgHeight));
	        int scaledHeight = (orgHeight >= orgWidth) ? maxSide : (int) ((float) maxSide * ((float) orgHeight / (float) orgWidth));

	        // create the scaled bitmap
	        Bitmap scaledGalleryPic = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
	        return scaledGalleryPic;
	    }

	private void saveAvatar(Bitmap picture){
		 String strAvatarFilename = "avatar.jpg";
	        try {
	            picture.compress(CompressFormat.JPEG, 100, openFileOutput(strAvatarFilename, MODE_PRIVATE));
	        } catch (Exception e) {
	            Log.e(DEBUG_TAG, "Avatar compression and save failed.", e);
	        }

	        Uri imageUriToSaveCameraImageTo = Uri.fromFile(new File(JoinMeSettingsActivity.this.getFilesDir(), strAvatarFilename));

	        Editor editor = joinMeSettings.edit();
	        editor.putString(JOIN_ME_PREFERENCES_AVATAR, imageUriToSaveCameraImageTo.getPath());
	        editor.commit();

	        // Update the settings screen
	        ImageButton avatarButton = (ImageButton) findViewById(R.id.ImageButton_Avatar);
	        String strAvatarUri = joinMeSettings.getString(JOIN_ME_PREFERENCES_AVATAR, "android.resource://com.android.joinme/drawable/avatar");
	        Uri imageUri = Uri.parse(strAvatarUri);
	        avatarButton.setImageURI(null); // Workaround for refreshing an ImageButton, which tries to cache the previous image Uri. Passing null effectively resets it.
	        avatarButton.setImageURI(imageUri);

	}
	
	private void initNickNameEntry(){
    	final EditText nickName = (EditText) findViewById(R.id.EditText_Nickname);
    	
    	if(joinMeSettings.contains(JOIN_ME_PREFERENCES_NICKNAME)){
    		nickName.setText(joinMeSettings.getString(JOIN_ME_PREFERENCES_NICKNAME, "Anup"));
    	}
    	
    	nickName.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
					String nickNameString = nickName.getText().toString();
					Editor edit = joinMeSettings.edit();
					edit.putString(JOIN_ME_PREFERENCES_NICKNAME, nickNameString);
					edit.commit();
					return true;
				}
				return false;
			}
		});
    	
    }
	
private void initEmailEntry(){
    	
    	final EditText email = (EditText) findViewById(R.id.EditText_Email);
    	
    	if(joinMeSettings.contains(JOIN_ME_PREFERENCES_EMAIL)){
    		email.setText(joinMeSettings.getString(JOIN_ME_PREFERENCES_EMAIL, "example@something.com"));
    	}
    	
    	email.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
					String emailString = email.getText().toString();
					Editor edit = joinMeSettings.edit();
					edit.putString(JOIN_ME_PREFERENCES_EMAIL, emailString);
					edit.commit();
					return true;
				}
				return false;
			}
		});
    }

private void initPasswordEntry(){
	final Button password = (Button) findViewById(R.id.Button_Password);
	final TextView passwordSet = (TextView) findViewById(R.id.Set_Password);
	
	if(joinMeSettings.contains(JOIN_ME_PREFERENCES_PASSWORD)){
		passwordSet.setText("Password set");
		passwordSet.setTextColor(Color.BLUE);
	}
	else{
		passwordSet.setText("Password not set");
		password.setTextColor(Color.RED);
	}
	
	password.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			JoinMeSettingsActivity.this.showDialog(PASSWORD_DIALOG_ID);
			
		}
	});
}

private void initDOBEntry(){
	/*final Button dob = (Button) findViewById(R.id.Button_DOB);
	final TextView dobSet = (TextView) findViewById(R.id.DOB_Set);
	
	if(joinMeSettings.contains(JOIN_ME_PREFERENCES_DOB)){
		dobSet.setText(DateFormat.format("MMMM dd yyyy", joinMeSettings.getLong(JOIN_ME_PREFERENCES_DOB, 0)));
	}
	else{
		dobSet.setText("Date not set");
		dobSet.setTextColor(Color.RED);
	}
	
	dob.setOnClickListener(new View.OnClickListener() {
		
		public void onClick(View v) {
			JoinMeSettingsActivity.this.showDialog(DATE_DIALOG_ID);	
		}
	});*/
}

private void initGenderSpinner(){
	
	/*final Spinner gender = (Spinner) findViewById(R.id.Spinner_Gender);
	
	ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(this,R.array.genders,android.R.layout.simple_spinner_item);
	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	gender.setAdapter(adapter);
	
	if(joinMeSettings.contains(JOIN_ME_PREFERENCES_GENDER)){
		gender.setSelection(joinMeSettings.getInt(JOIN_ME_PREFERENCES_GENDER, 0));
	}
	
	gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

		public void onItemSelected(AdapterView<?> parent, View itemSelected,
				int selectedItemPosition, long selectedId) {
			Editor edit = joinMeSettings.edit();
			edit.putInt(JOIN_ME_PREFERENCES_GENDER, selectedItemPosition);
			edit.commit();
		}

		public void onNothingSelected(AdapterView<?> arg0) {
			
			
		}
	});*/
}

@Override
protected Dialog onCreateDialog(int id){
	switch(id){
	case DATE_DIALOG_ID:
		/*final TextView dob = (TextView) findViewById(R.id.DOB_Set);
		DatePickerDialog pickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Log.i(DEBUG_TAG, "Day : " + dayOfMonth + "Month : " + monthOfYear
						+ "Year : " + year);
				Time dateOfBirth = new Time();
				dateOfBirth.set(dayOfMonth, monthOfYear, year);
				long dtBirth = dateOfBirth.toMillis(true);
				dob.setText(DateFormat.format("MMMM dd,yyyy", dtBirth));
				Editor editor = joinMeSettings.edit();
				editor.putLong(JOIN_ME_PREFERENCES_DOB, dtBirth);
				editor.commit();
			}
		}, 0, 0, 0);
		return pickerDialog;*/
	case PASSWORD_DIALOG_ID:
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View layout = inflater.inflate(R.layout.signup_password_dialog,(ViewGroup) findViewById(R.id.signup_password_root));
		final EditText newPassword = (EditText) layout.findViewById(R.id.Signup_NewPassword_EditText);
		final EditText confirmPassword = (EditText) layout.findViewById(R.id.Signup_ConfirmPassword_EditText);
		final TextView error = (TextView) layout.findViewById(R.id.Signup_PasswordProb_TextView);
		
		confirmPassword.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String newPasswordString = newPassword.getText().toString();
				String confirmPasswordString = confirmPassword.getText().toString();
				if(newPasswordString.equals(confirmPasswordString)){
					error.setText("Password equal");
					error.setTextColor(Color.GRAY);
				}
				else{
					error.setText("Password not equal");
					error.setTextColor(Color.RED);
				}
				
			}
		});
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(layout);
		builder.setTitle(R.string.password);
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int whichButton) {
				// TODO Auto-generated method stub
				JoinMeSettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
				
			}
		});
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				TextView passwordSetStatus = (TextView) findViewById(R.id.Set_Password);
				String newPasswordString = newPassword.getText().toString();
				String confirmPasswordString = confirmPassword.getText().toString();
				if(newPasswordString.equals(confirmPasswordString)){
					Editor editor = joinMeSettings.edit();
					editor.putString(JOIN_ME_PREFERENCES_PASSWORD, newPasswordString);
					editor.commit();
					passwordSetStatus.setText("Password Set");
					passwordSetStatus.setTextColor(Color.GRAY);
				}
				else{
					passwordSetStatus.setText("Passwords do not match. Not saving. Keeping old password (if set).");
					passwordSetStatus.setTextColor(Color.RED);
				}
				
				JoinMeSettingsActivity.this.removeDialog(PASSWORD_DIALOG_ID);
			}
			
		});
		AlertDialog signUpDialog = builder.create();
		return signUpDialog;
	}
	return null;
}


@Override
protected void onPrepareDialog(int dialogID, Dialog dialog){
	super.onPrepareDialog(dialogID, dialog);
	switch(dialogID){
	case PASSWORD_DIALOG_ID:
		return;
	case DATE_DIALOG_ID:
		/*DatePickerDialog pickerDialog = (DatePickerDialog) dialog;
		int day =0;
		int month=0;
		int year = 0;
		if(joinMeSettings.contains(JOIN_ME_PREFERENCES_DOB)){
			long msBirthDate = joinMeSettings.getLong(JOIN_ME_PREFERENCES_DOB,0);
            Time dateOfBirth = new Time();
            dateOfBirth.set(msBirthDate);
            day = dateOfBirth.monthDay;
            month = dateOfBirth.month;
            year = dateOfBirth.year;
		}
		else{
			Calendar cal = Calendar.getInstance();
			day = cal.get(Calendar.DAY_OF_MONTH);
			month = cal.get(Calendar.MONTH);
			year = cal.get(Calendar.YEAR);
		}
		pickerDialog.updateDate(year, month, day);
		return;*/
	}
}

private boolean validateForm(){
	boolean result=false;
	if(((EditText)findViewById(R.id.Full_Name)).getText().toString().equals("")){
		validateFormMessage("The full name field is required");
		return result;
	}else if(((EditText)findViewById(R.id.EditText_Nickname)).getText().toString().equals("")){
		validateFormMessage("The nickname field is required");
		return result;
	}else if(!emailPattern.matcher(((EditText)findViewById(R.id.EditText_Email)).getText().toString()).matches()){
		validateFormMessage("Is not a valid email address");
		return result;
	}else if(((EditText)findViewById(R.id.EditText_City)).getText().toString().equals("")){
		validateFormMessage("The city field is required");
		return result;
	}else if(((EditText)findViewById(R.id.autocomplete_country)).getText().toString().equals("")){
		validateFormMessage("The country field is required");
		return result;
	}else
		result=true;	

	return result;
}
private void validateFormMessage(String message){
	Toast.makeText(JoinMeSettingsActivity.this, message, Toast.LENGTH_SHORT).show();
}
}
