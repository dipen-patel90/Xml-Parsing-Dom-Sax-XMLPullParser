package com.xml.parsing.demo.activities;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.xml.parsing.demo.R;

/**
 * @author dipenp
 *
 */
public class BaseActivity extends Activity {

	//URL for fetching xml 
	public static String URL = "https://dl.dropboxusercontent.com/u/88296957/androiddemo/company.xml";
	//XML Nodes name
	public static String COMPANY = "COMPANY", NAME = "name",
			EMPLOYEE = "EMPLOYEE", FIRSTNAME = "FIRSTNAME",
			LASTNAME = "LASTNAME", EMAIL = "EMAIL",
			DESIGNATION = "DESIGNATION", ADDRESS = "ADDRESS",
			STREET = "STREET", CITY = "CITY", STATE = "STATE", ZIPCODE = "ZIPCODE";
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * @return Fetch xml content from URL and return xml as string
	 */
	public String getXMLFromURL() {
		
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URL);
		
		HttpResponse httpResponse = null;
		String xml = null;
		
		try {
			httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
//			xml = EntityUtils.toString(httpEntity);
			xml = EntityUtils.toString(httpEntity, "UTF-8");
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return xml;
	}
	
	/**
	 * @param activity
	 * @return progress dialog
	 */
	public ProgressDialog getProgressDialog(Activity activity){
		ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.please_wait));
        return dialog;
	}
	
	/**
	 * Show network error
	 */
	void showNetworkError(){
		Toast.makeText(BaseActivity.this, getString(R.string.network_error), Toast.LENGTH_LONG).show();
	};
}
