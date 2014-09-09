package com.project3.poi.test.controllers;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.project3.poi.DocumentPropertyChecker;
import com.project3.test.models.TestQuestion;
import com.project3.test.models.TestResultItem;

public class TestChecker {
	
	private DocumentPropertyChecker documentChecker;
	private XWPFDocument document;
	
	public TestChecker(XWPFDocument document) {
		this.document = document;
		documentChecker = new DocumentPropertyChecker(document);
	}
	
	public void setDocument(XWPFDocument document) {
		this.document = document;
	}
	
	public XWPFDocument getDocument() {
		return document;
	}

	public void checkAllQuestions(List<TestQuestion> testQuestionList) {
		System.out.println("----------------- RESULTS -----------------");
		System.out.println("Text\tExists\tProperties\tCorrect\tTotal");
		for (TestQuestion question: testQuestionList) {
			System.out.println("QUESTION " + question.getQuestionId());
			List<TestResultItem> results = checkQuestion(question);
			System.out.println(resultsToString(results));
		}
	}
	
	public List<TestResultItem> checkQuestion(TestQuestion question) {
		List<TestResultItem> resultMap = new ArrayList<TestResultItem>();
		
		switch (question.getType()) {
		case RUN:
			resultMap = documentChecker.checkRunQuestion(question);
			break;
		case PARAGRAPH:
			resultMap = documentChecker.checkParagraphQuestion(question);
			break;
		case ALL_PARAGRAPHS:
			resultMap = documentChecker.checkAllParagraphsQuestion(question);
			break;
		case MATCH:
			resultMap = documentChecker.checkIfStringsExist(question.getStrings());
			break;
		case DOCUMENT:
			resultMap = documentChecker.checkDocumentQuestion(question);
			break;
		case PICTURE:
            resultMap = documentChecker.checkPropertiesOfPictures(question);
            break;
        case TABLE_CONTENT:
            resultMap = documentChecker.checkContentsOfTable(question.getStrings());
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
