package com.xml.parsing.demo.activities;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

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
public class DomParsingActivity extends BaseActivity {

	private ListView listView;
	private TextView companyNameTxt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_domparsing_layout);
		setTitle(getString(R.string.dom_parsing));
		
		//Initializing list view
		listView = (ListView)findViewById(R.id.list);
		companyNameTxt = (TextView) findViewById(R.id.text_view);
		
		if(NetworkUtils.isNetworkAvailable(getApplicationContext())){
			new ParseXMLTask().execute();	
		}else {
			showNetworkError();
		}
	}

	class ParseXMLTask extends AsyncTask<String, String, Company> {
		
		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {
			dialog = getProgressDialog(DomParsingActivity.this);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected Company doInBackground(String... params) {
			//Initializing company class
			Company company = new Company();

            //Will store employee value in list
            ArrayList<Employee> employeeList = new ArrayList<Employee>();
            
			//Getting xml 
			String xml = getXMLFromURL();

			if(!StringUtils.isEmpty(xml)){
				
				XMLDOMParser parser = new XMLDOMParser();
		        InputStream is;
		        try {
		            is = new ByteArrayInputStream(xml.getBytes());
		            Document doc = parser.getDocument(is);
		 
		            //Getting company element 
		            NodeList companyNode = doc.getElementsByTagName(COMPANY);

		            //Iterating node to get Attribute value             
		            for (int i = 0; i < companyNode.getLength(); i++) {
		            	Element e = (Element) companyNode.item(i);
						company.setCompanyName(e.getAttribute(NAME));
					}

		            //Getting employee element 
		            NodeList employeesListNode = doc.getElementsByTagName(EMPLOYEE);
		            
		            for (int i = 0; i < employeesListNode.getLength(); i++) {
		                Element e = (Element) employeesListNode.item(i);
		                
		                Employee employee = new Employee();
		                employee.setFirstName(parser.getValue(e, FIRSTNAME));
		                employee.setLastName(parser.getValue(e, LASTNAME));
		                employee.setEmployeeEmail(parser.getValue(e, EMAIL));
		                employee.setDesignation(parser.getValue(e, DESIGNATION));
		                
		                Address address = new Address();
		                address.setStreet(parser.getValue(e, STREET));
		                address.setCity(parser.getValue(e, CITY));
		                address.setState(parser.getValue(e, STATE));
		                address.setZipcode(parser.getValue(e, ZIPCODE));
		                
		                employee.setAddress(address);
		                
		                //Adding employee to employee list
		                employeeList.add(employee);
		            }
		            company.setEmployeeList(employeeList);
		        } catch (Exception e) {
		            e.printStackTrace();
		        }	
			}
			
			return company;
		}

		@Override
		protected void onPostExecute(Company company) {
			if (null != dialog) {
				dialog.cancel();
			}
			
			//Populating views
			if(company != null){
				companyNameTxt.setText(company.getCompanyName());
				listView.setAdapter(new CustomBaseAdapter(DomParsingActivity.this, company.getEmployeeList()));
			}
			super.onPostExecute(company);
		}

	}

	/**
	 * Parser class for dom element
	 *
	 */
	class XMLDOMParser {
		
		/**
		 * @param inputStream
		 * @return Document 
		 */
		public Document getDocument(InputStream inputStream) {
			Document document = null;
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			try {
				DocumentBuilder db = factory.newDocumentBuilder();
				InputSource inputSource = new InputSource(inputStream);
				document = db.parse(inputSource);
			} catch (Exception e) {
				return null;
			} 
			return document;
		}

		/**
		 * @param item
		 * @param name
		 * @return value of given name from element
		 */
		public String getValue(Element item, String name) {
			NodeList nodes = item.getElementsByTagName(name);
			return this.getTextNodeValue(nodes.item(0));
		}

		private final String getTextNodeValue(Node node) {
			Node child;
			if (node != null) {
				if (node.hasChildNodes()) {
					child = node.getFirstChild();
					while (child != null) {
						if (child.getNodeType() == Node.TEXT_NODE) {
							return child.getNodeValue();
						}
						child = child.getNextSibling();
					}
				}
			}
			return "";
		}
	}
}
