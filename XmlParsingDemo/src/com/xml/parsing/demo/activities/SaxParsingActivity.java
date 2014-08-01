package com.xml.parsing.demo.activities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

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
public class SaxParsingActivity extends BaseActivity{
	
	private ListView listView;
	private TextView companyNameTxt;
	private Company company;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saxparsing_layout);
		setTitle(getString(R.string.sax_parsing));
		
		company = new Company();
		
		//Initializing list view
		listView = (ListView)findViewById(R.id.list);
		companyNameTxt = (TextView)findViewById(R.id.text_view);
		
		if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
			new ParseXMLTask().execute();	
		}else {
			showNetworkError();
		}
	}

	
	class ParseXMLTask extends AsyncTask<String, String, String>{
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = getProgressDialog(SaxParsingActivity.this);
			dialog.show();
			super.onPreExecute();
		}
		
		@Override
		protected String doInBackground(String... params) {
			
			//Getting xml 
			String xml = getXMLFromURL();
		    
			if(!StringUtils.isEmpty(xml)){
				try {
					InputStream is = new ByteArrayInputStream(xml.getBytes());
					SAXParserFactory spf = SAXParserFactory.newInstance();
					SAXParser sp = spf.newSAXParser();
					XMLReader xr = sp.getXMLReader();

					XMLHandler myXMLHandler = new XMLHandler();
					xr.setContentHandler(myXMLHandler);
					InputSource inStream = new InputSource(is);
					xr.parse(inStream);

				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			
			return xml;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(null != dialog){
				dialog.cancel();
			}
			
			//Populating views
			if(company != null){
				companyNameTxt.setText(company.getCompanyName());
				listView.setAdapter(new CustomBaseAdapter(SaxParsingActivity.this, company.getEmployeeList()));
			}
			
			super.onPostExecute(result);
		}
		
	}
	
	class XMLHandler extends DefaultHandler {

		//Name of node
		private String localName;
		private Address address;
		private Employee employee;
		private ArrayList<Employee> employeeList;
		
		/*Execute when parsing start*/
		@Override
		public void startDocument() throws SAXException {
			employeeList = new ArrayList<Employee>();
			super.startDocument();
		}
		
		/*Execute for every element start*/
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			this.localName = localName;//Name of node
			
			if(localName.equals(COMPANY)){
				//Getting attribute value of company node
				company.setCompanyName(attributes.getValue(NAME));
			}else if (localName.equals(EMPLOYEE)) {
				//Initializing employee class when employee node start
				employee = new Employee();
			}else if (localName.equals(ADDRESS)) {
				//Initializing address class when address node start
				address = new Address();
			}
			super.startElement(uri, localName, qName, attributes);
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			
			//Getting actual value of node 
			String string = new String(ch, start, length);
			 
			if(!StringUtils.isEmpty(string)){
				if(localName.equals(COMPANY)){
					
				}else if(localName.equals(EMPLOYEE)){
					
				}else if (localName.equals(FIRSTNAME)) {
					employee.setFirstName(new String(ch, start, length));
				}else if (localName.equals(LASTNAME)) {
					employee.setLastName(new String(ch, start, length));
				}else if (localName.equals(EMAIL)) {
					employee.setEmployeeEmail(new String(ch, start, length));
				}else if (localName.equals(DESIGNATION)) {
					employee.setDesignation(new String(ch, start, length));
				}else if (localName.equals(STREET)) {
					address.setStreet(new String(ch, start, length));
				}else if (localName.equals(CITY)) {
					address.setCity(new String(ch, start, length));
				}else if (localName.equals(STATE)) {
					address.setState(new String(ch, start, length));
				}else if (localName.equals(ZIPCODE)) {
					address.setZipcode(new String(ch, start, length));
				}	
			}

			super.characters(ch, start, length);
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			
			//Setting object values when element ends
			if(localName.equals(COMPANY)){
				company.setEmployeeList(employeeList);
			}else if(localName.equals(EMPLOYEE)){
				employeeList.add(employee);
			}else if (localName.equals(ADDRESS)) {
				employee.setAddress(address);
			}
			super.endElement(uri, localName, qName);
		}
		
		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}
		
		@Override
		public void startPrefixMapping(String prefix, String uri)
				throws SAXException {
			super.startPrefixMapping(prefix, uri);
		}
		
		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
			super.endPrefixMapping(prefix);
		}
	}
}
