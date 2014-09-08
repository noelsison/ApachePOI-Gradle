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

	private static final String RUN = "RUN";
	private static final String PARAGRAPH = "PARAGRAPH";
	private static final String MATCH = "MATCH";
	private static final String ALL_PARAGRAPHS = "ALL PARAGRAPHS";
	private static final String DOCUMENT = "DOCUMENT";
	private static final String PICTURE = "PICTURE";
    private static final String TABLE_CONTENT = "TABLE CONTENT";
	
	public static List<TestQuestion> parseJSONQuestions(String filename) throws FileNotFoundException, IOException, ParseException {
		System.out.println("Opening JSON file: " + filename);
		List<TestQuestion> questions = new ArrayList<TestQuestion>();
		
		JSONParser parser = new JSONParser(); 
		JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(
				filename));
		
		System.out.println("Opened Test: " + jsonObject.get("name"));
		Iterator<JSONObject> jsonQuestions = ((JSONArray) jsonObject.get("questions")).iterator();
		
		// Create TestQuestion from JSON question object then add to list
		while (jsonQuestions.hasNext()) {
			TestQuestion question = createQuestion(jsonQuestions.next());
			questions.add(question);
		}
		
		System.out.println("Generated: " + questions.size() + " testQuestionObject(s).");
		return questions;
	}

	private static TestQuestion createQuestion(JSONObject jsonQuestion) {
		// Get json question properties
		String questionId = jsonQuestion.get("name").toString();
		QuestionType type = getQuestionType(jsonQuestion.get("type").toString());
		JSONArray jsonStrings = (JSONArray) jsonQuestion.get("strings");
		JSONArray jsonProperties = (JSONArray) jsonQuestion.get("properties");
		
		// Create test question object
		TestQuestion question = new TestQuestion(questionId, type);
		
		// Add strings to question
		for (Object o : jsonStrings) {
			question.addString(o.toString());
		}
		
		// Add properties to question 
		Iterator<JSONObject> jsonPropertiesIterator = jsonProperties.iterator();
		while (jsonPropertiesIterator.hasNext()) {
			JSONObject jsonProperty = jsonPropertiesIterator.next();
			question.setProperty(jsonProperty.get("name").toString(), jsonProperty.get("value").toString());
		}
			
		return question;
	}

	// Convert string constants from file to enum constants for TestQuestion class
	public static QuestionType getQuestionType(String type) {
		switch (type) {
		case RUN:
			return QuestionType.RUN;
		case PARAGRAPH:
			return QuestionType.PARAGRAPH;
		case DOCUMENT:
			return QuestionType.DOCUMENT;
		case MATCH:
			return QuestionType.MATCH;
		case ALL_PARAGRAPHS:
			return QuestionType.ALL_PARAGRAPHS;
		case PICTURE:
            return QuestionType.PICTURE;
        case TABLE_CONTENT:
              return QuestionType.TABLE_CONTENT;
		}
		
		return QuestionType.RUN;
	}
}
