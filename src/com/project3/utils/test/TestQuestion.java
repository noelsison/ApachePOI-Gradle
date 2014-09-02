package com.project3.utils.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestQuestion {
	private String name, type, mustPass;
	private ArrayList<String> strings;
	private Map<String, String> properties;
	
	public TestQuestion (String name, String type, String mustPass) {
		this.name = name;
		this.type = type;
		this.mustPass = mustPass;
	}
	
	public ArrayList<String> getStrings() {
		return strings;
	}

	public void setStrings(ArrayList<String> strings) {
		this.strings = strings;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(HashMap<String, String> properties) {
		this.properties = properties;
	}
	
	public String toString() {
		return "Name: " + this.name + ", Type: " + this.type + ", Must Pass: " + this.mustPass + ", Strings: " + this.strings + ", Properties: " + this.properties;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getMustPass() {
		return mustPass;
	}
}
