package com.xml.parsing.demo.activities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.xml.parsing.demo.R;
import com.xml.parsing.demo.activities.utils.NetworkUtils;
import com.xml.parsing.demo.activities.utils.StringUtils;
import com.xml.parsing.demo.adapter.CustomBaseAdapter;
import com.xml.parsing.demo.models.Address;
import com.xml.parsing.demo.models.Company;
import com.xml.parsing.demo.models.Employee;

/**
 * @author dipenp
 *
 */
public class XMLPullParsingActivity extends BaseActivity{
	
	private ListView listView;
	private TextView companyNameTxt;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmlpullparsing_layout);
		setTitle(getString(R.string.xml_pull_parsing));
		
		listView = (ListView)findViewById(R.id.list);
		companyNameTxt = (TextView)findViewById(R.id.text_view);
		
		if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
			new ParseXMLTask().execute();	
		}else {
			showNetworkError();
		}
	}

	class ParseXMLTask extends AsyncTask<String, String, Company>{
		
		ProgressDialog dialog;
		private String nodeValue;
		
		@Override
		protected void onPreExecute() {
			dialog = getProgressDialog(XMLPullParsingActivity.this);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected Company doInBackground(String... params) {
			//Initializing company class
			Company company = new Company();
			
			//Getting xml 
			String xml = getXMLFromURL();
			
			if(!StringUtils.isEmpty(xml)){

				//Will store employee value in list
				ArrayList<Employee> employeeList = new ArrayList<Employee>();
				Employee employee = null;
				Address address = null;
				
				InputStream is;
				XmlPullParserFactory factory;
				XmlPullParser parser;
				
				try {
					is = new ByteArrayInputStream(xml.getBytes());
					factory = XmlPullParserFactory.newInstance();
		            factory.setNamespaceAware(true);
		            parser = factory.newPullParser();
		 
		            parser.setInput(is, null);
		            
		            //Getting event type
		            int eventType = parser.getEventType();
		            while (eventType != XmlPullParser.END_DOCUMENT) {
		            	String name = parser.getName();
		            	
		            	switch (eventType) {
						
		            	case XmlPullParser.START_DOCUMENT:
							
							break;
						
		            	case XmlPullParser.END_DOCUMENT:
							
							break;
						
		            	case XmlPullParser.START_TAG:
		                	
		                	if(name.equals(COMPANY)){
		                		
		                	}else if(name.equals(EMPLOYEE)){
		                		//Initializing employee class when employee tag start
		                		employee = new Employee();		
		                	}else if(name.equals(ADDRESS)){
		                		//Initializing address class when address tag start
		                		address = new Address();
		                	}
							break;
						
		            	case XmlPullParser.END_TAG:
							
		            		//Setting object values when element ends
		                	if(name.equals(COMPANY)){
		                		company.setEmployeeList(employeeList);
		                	}else if(name.equals(EMPLOYEE)){
		                		employeeList.add(employee);
		                	}else if(name.equals(ADDRESS)){
		                		employee.setAddress(address);
		                	}
		                	
		                	else if(name.equals(FIRSTNAME)){
		                		employee.setFirstName(nodeValue);
		                	}else if(name.equals(LASTNAME)){
		                		employee.setLastName(nodeValue);
		                	}else if(name.equals(EMAIL)){
		                		employee.setEmployeeEmail(nodeValue);
		                	}else if(name.equals(DESIGNATION)){
		                		employee.setDesignation(nodeValue);
		                	}else if(name.equals(STREET)){
		                		address.setStreet(nodeValue);
		                	}else if(name.equals(CITY)){
		                		address.setCity(nodeValue);
		                	}else if(name.equals(STATE)){
		                		address.setState(nodeValue);
		                	}else if(name.equals(ZIPCODE)){
		                		address.setZipcode(nodeValue);
		                	}
							break;
						
		            	case XmlPullParser.TEXT:
							
		            		//Actual text of node
		                	nodeValue = parser.getText();
							break;
						}
		            	
		            	//Iterating attribute values
		            	if(parser.getAttributeCount()>0){
		            		for (int i = 0; i < parser.getAttributeCount(); i++) {
		            			if(parser.getAttributeName(i).equals(NAME)){
		            				company.setCompanyName(parser.getAttributeValue(i));	
		            			}
							}	
		            	}
		                eventType = parser.next();
		               }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			return company;
		}
		
		@Override
		protected void onPostExecute(Company company) {
			if(null != dialog){
				dialog.cancel();
			}
			
			//Populating views
			if(company != null){
				companyNameTxt.setText(company.getCompanyName());
				listView.setAdapter(new CustomBaseAdapter(XMLPullParsingActivity.this, company.getEmployeeList()));
			}
			super.onPostExecute(company);
		}
		
	}
}
