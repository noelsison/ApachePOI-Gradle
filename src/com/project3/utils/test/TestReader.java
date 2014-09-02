package com.project3.utils.test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TestReader {
	private JSONParser parser = new JSONParser();
	private List<JSONObject> jsonQuestionList = new ArrayList<JSONObject>();
	private List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>();
	
	public TestReader (String fileName) {
		try {
			System.out.println("Opening JSON file: " + fileName);
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(fileName));
			System.out.println("Opened Test: " + jsonObject.get("name"));
			readJSONQuestions(jsonObject);
			makeTestQuestions(jsonObject);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<TestQuestion> getTestQuestionList() {
		return testQuestionList;
	}
	
	private void readJSONQuestions(JSONObject jsonObject) {
		JSONArray questions = (JSONArray) jsonObject.get("questions");
		Iterator<JSONObject> iter = questions.iterator();
		int questionsRead = 0;
		while (iter.hasNext()) {
			jsonQuestionList.add(iter.next());
			questionsRead++;
		}
		System.out.println("Detected: " + questionsRead + " question(s).");
	}
	
	private void makeTestQuestions(JSONObject jsonObject) {
		JSONArray questions = (JSONArray) jsonObject.get("questions"), 
				  jsonStrings, 
				  jsonProperties;
		Iterator<JSONObject> jsonQuestionsIterator = questions.iterator(),
							 jsonPropertiesIterator;
		JSONObject jsonQuestion, 
				   jsonProperty;
		TestQuestion tempTestQuestion;
		ArrayList<String> tempStrings;
		HashMap<String, String> tempProperties;
		int testQuestionsGenerated = 0;
		
		while (jsonQuestionsIterator.hasNext()) {
			jsonQuestion = jsonQuestionsIterator.next();
			tempTestQuestion = new TestQuestion(jsonQuestion.get("name").toString(), jsonQuestion.get("type").toString(), jsonQuestion.get("mustPass").toString());
			// Strings
			jsonStrings = (JSONArray) jsonQuestion.get("strings");
			tempStrings = new ArrayList<String>();
			for (Object o : jsonStrings) {
				tempStrings.add(o.toString());
			}
			tempTestQuestion.setStrings(tempStrings);
			// Properties
			jsonProperties = (JSONArray) jsonQuestion.get("properties");
			tempProperties = new HashMap<String, String>();
			jsonPropertiesIterator = jsonProperties.iterator();
			while (jsonPropertiesIterator.hasNext()) {
				jsonProperty = jsonPropertiesIterator.next();
				tempProperties.put(jsonProperty.get("name").toString(), jsonProperty.get("value").toString());
			}
			tempTestQuestion.setProperties(tempProperties);
			
			testQuestionList.add(tempTestQuestion);
			testQuestionsGenerated++;
		}
		System.out.println("Generated: " + testQuestionsGenerated + " testQuestionObject(s).");
	}
}
