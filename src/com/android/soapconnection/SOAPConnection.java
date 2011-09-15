package com.android.soapconnection;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.transport.AndroidHttpTransport;
import org.ksoap2.serialization.SoapSerializationEnvelope;

import android.util.Log;

import com.android.joinme.JoinMeBaseActivity;
public class SOAPConnection {
	
	private final String methodName;
	private final String url;
	private final String soapAction;
	private final String namespace;
	private SoapObject request;
	private AndroidHttpTransport androidHttpTransport;
	private SoapSerializationEnvelope envelope;
	
	public SOAPConnection(String methodName, String url, String soapAction,
			String namespace){
		this.methodName = methodName;
		this.url = url;
		this.soapAction = soapAction;
		this.namespace = namespace;
	}
	
	public SoapObject getSoapObject(){
		return this.request;
	}
	
	public void init(){
		request = new SoapObject(namespace, methodName);
		envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(request);
		envelope.dotNet=true;
		androidHttpTransport = new AndroidHttpTransport(url);
	}
	
	public SoapObject sendSoapObject() throws Exception{
		SoapObject resultObject = null;

		androidHttpTransport.call(soapAction, envelope);
		resultObject = (SoapObject) envelope.bodyIn;

		return resultObject;
	}

}
