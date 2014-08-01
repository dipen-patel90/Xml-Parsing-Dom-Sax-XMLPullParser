package com.xml.parsing.demo.models;

import java.util.ArrayList;

/**
 * @author dipenp
 *
 */
public class Company {

	private String companyName;
	private ArrayList<Employee> employeeList;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public ArrayList<Employee> getEmployeeList() {
		return employeeList;
	}

	public void setEmployeeList(ArrayList<Employee> employeeList) {
		this.employeeList = employeeList;
	}

}
