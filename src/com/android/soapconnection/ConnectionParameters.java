package com.android.soapconnection;

public class ConnectionParameters {
	// webservices
	static public final String UPLOAD_URL = "http://192.168.247.1:8080/axis2/services/JoinMeUploadWebService";
	//static public final String UPLOAD_URL = "http://sawant.mannlib.cornell.edu:8080/axis2/services/JoinMeUploadWebService";
	
	//static public final String UPLOAD_URL = "http://sawant.local:8080/axis2/services/JoinMeUploadWebService";
	
	// namespaces
	static public final String UPLOAD_URL_NAMESPACE = "http://upload.webservice.com";
	
	
	// methods
	static public final String UPLOAD_SETTINGS = "uploadSettings";
	static public final String UPLOAD_NEW_ENTRY = "uploadNewEntry";
	static public final String UPLOAD_SIGN_IN = "signIn";
	static public final String UPLOAD_GET_REQUESTS = "getRequests";
	static public final String UPLOAD_CHECK_OBSCENITY = "checkObscenity";
	static public final String UPLOAD_SET_REPLY = "setReply";
	static public final String UPLOAD_GET_REPLIES = "getReplies";
	
    
	// soapactions
	static public final String UPLOAD_SETTINGS_SOAPACTION = "http://upload.webservice.com/uploadSettings";
	static public final String UPLOAD_NEW_ENTRY_SOAPACTION = "http://upload.webservice.com/uploadNewEntry";
	static public final String UPLOAD_SIGN_IN_SOAPACTION = "http://upload.webservice.com/signIn";
	static public final String UPLOAD_GET_REQUESTS_SOAPACTION ="http://upload.webservice.com/getRequests";
	static public final String UPLOAD_CHECK_OBSCENITY_SOAPACTION = "http://upload.webservice.com/checkObscenity";
	static public final String UPLOAD_SET_REPLY_SOAPACTION = "http://upload.webservice.com/setReply";
	static public final String UPLOAD_GET_REPLIES_SOAPACTION = "http://upload.webservice.com/getReplies";
	
	
}
