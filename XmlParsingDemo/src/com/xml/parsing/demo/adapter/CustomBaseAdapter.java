package com.xml.parsing.demo.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xml.parsing.demo.R;
import com.xml.parsing.demo.models.Employee;

/**
 * @author dipenp
 *
 */
public class CustomBaseAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Employee> employee;

	public CustomBaseAdapter(Context context, ArrayList<Employee> employee) {
		this.context = context;
		this.employee = employee;
	}

	private class ViewHolder {
		ImageView imageView;
		TextView fullNameTxt;
		TextView emailTxt;
		TextView designationTxt;
		TextView addressTxt;
	}

	@Override
	public int getCount() {
		
		return (employee != null ? employee.size() : 0);
	}

	@Override
	public Object getItem(int position) {
		return employee.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.employee_list_layout, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView.findViewById(R.id.employee_image);
			holder.fullNameTxt = (TextView) convertView.findViewById(R.id.fullname_txt);
			holder.emailTxt = (TextView) convertView.findViewById(R.id.email_txt);
			holder.designationTxt = (TextView) convertView.findViewById(R.id.designation_txt);
			holder.addressTxt = (TextView) convertView.findViewById(R.id.address_txt);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Employee e = (Employee)getItem(position);
		
		holder.fullNameTxt.setText   ("Employee Name : "+e.getFirstName()+" "+e.getLastName());
		holder.emailTxt.setText      ("Employee Email: "+e.getEmployeeEmail());
		holder.designationTxt.setText("Designation   : "+e.getDesignation());
		holder.addressTxt.setText    ("Address       : "+e.getAddress().getStreet()+", "+e.getAddress().getCity()+", "+e.getAddress().getState()+", "+e.getAddress().getZipcode());
		
		return convertView;
	}

}
