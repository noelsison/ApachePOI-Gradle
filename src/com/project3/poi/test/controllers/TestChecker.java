package com.project3.poi.test.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.project3.poi.DocumentPropertyChecker;
import com.project3.test.models.TestQuestion;
import com.project3.test.models.TestResultItem;

public class TestChecker {
	
	public static void checkAllQuestions(XWPFDocument docx, List<TestQuestion> testQuestionList) {
		System.out.println("----------------- RESULTS -----------------");
		System.out.println("Text\tExists\tProperties\tCorrect\tTotal");
		for (TestQuestion question: testQuestionList) {
			System.out.println("QUESTION " + question.getQuestionId());
			List<TestResultItem> results = checkQuestion(docx, question);
			System.out.println(resultsToString(results));
		}
	}
	
	public static List<TestResultItem> checkQuestion(XWPFDocument docx, TestQuestion question) {
		List<TestResultItem> resultMap = new ArrayList<TestResultItem>();
		
		switch (question.getType()) {
		case RUN:
			resultMap = DocumentPropertyChecker.checkRunQuestion(docx.getParagraphs(), question);
			break;
		case PARAGRAPH:
			resultMap = DocumentPropertyChecker.checkParagraphQuestion(docx.getParagraphs(), question);
			break;
		case ALL_PARAGRAPHS:
			resultMap = DocumentPropertyChecker.checkAllParagraphsQuestion(docx.getParagraphs(),  question);
			break;
		case MATCH:
			resultMap = DocumentPropertyChecker.checkIfStringExists(docx.getParagraphs(), question);
			break;
		case DOCUMENT:
			resultMap = DocumentPropertyChecker.checkDocumentQuestion(docx, question);
			break;
		case PICTURE:
            resultMap = DocumentPropertyChecker.checkPropertiesOfPictures(docx.getAllPictures(), question.getStrings(), question.getProperties());
            break;
        case TABLE_CONTENT:
            resultMap = DocumentPropertyChecker.checkContentsOfTable(docx.getTables().get(0), question.getStrings());
            break;
		default:
			break;
		}
		return resultMap;
	}
	
	private static String resultsToString(List<TestResultItem> results) {
		StringBuffer resultString = new StringBuffer();
		
		for (TestResultItem resultItem : results) {
			resultString.append(resultItem.toString()).append("\n");
		}
		
		return resultString.toString();
	}
}
