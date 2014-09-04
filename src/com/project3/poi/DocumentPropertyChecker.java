/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project3.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;

import com.project3.test.models.TestQuestion;
import com.project3.test.models.TestResultItem;
import com.project3.test.models.TestResultProperty;

/**
 * Make this class read from XML files that contain the formatted "question"
 * 
 * @author Noel
 */
public class DocumentPropertyChecker {

	static void print(String s) {
		System.out.println(s);
	}

	/**
	 * Returns results for MATCH-type questions (if string exists in any of the
	 * paragraphs)
	 * 
	 * @param paragraphs
	 *            paragraphs to be checked
	 * @param question
	 *            MATCH-type question
	 * @return
	 */
	public static List<TestResultItem> checkStringsInParagraphs(
			List<XWPFParagraph> paragraphs, TestQuestion question) {
		List<TestResultItem> results = new ArrayList<TestResultItem>();
		List<String> strings = question.getStrings();

		// Look for each string in each paragraph
		for (String string : strings) {
			// Find which paragraph contains the string
			XWPFParagraph paragraph = findParagraphWithString(paragraphs,
					string);

			// Create result and set exists to true if string was found in one
			// of the paragraphs
			TestResultItem resultItem = new TestResultItem(string);
			resultItem.setExists(paragraph != null);
			results.add(resultItem);
		}

		return results;
	}

	// Check the next paragraphs while string is not found
	private static XWPFParagraph findParagraphWithString(List<XWPFParagraph> paragraphs, String string) {
		XWPFParagraph paragraph = null;
		Iterator<XWPFParagraph> iterator = paragraphs.iterator();
		boolean found = false;

		while (iterator.hasNext() && !found) {
			paragraph = iterator.next();
			found = paragraph.getText().contains(string);
		}

		paragraph = found ? paragraph : null;
		return paragraph;
	}

	/**
	 * Returns results for RUN-type questions (if strings that may be split into
	 * runs have specified properties)
	 * 
	 * @param paragraphs
	 *            paragraphs to be checked
	 * @param question
	 *            RUN-type question
	 * @return
	 */
	public static List<TestResultItem> checkRunQuestion(List<XWPFParagraph> paragraphs, TestQuestion question) {
		List<TestResultItem> results = new ArrayList<TestResultItem>();

		Map<String, String> questionProperties = question.getProperties();
		List<String> strings = question.getStrings();

		// Look for each string in each paragraph
		for (String string : strings) {
			// Find which paragraph contains the string
			XWPFParagraph paragraph = findParagraphWithString(paragraphs,
					string);

			// Paragraph is null if string was not found
			TestResultItem resultItem = new TestResultItem(string);
			resultItem.setExists(paragraph != null);

			// If paragraph contains the string, check properties of the runs in
			// this paragraph
			if (resultItem.exists()) {
				Map<String, TestResultProperty> properties = checkRunProperties(
						paragraph.getRuns(), string, questionProperties);
				resultItem.setProperties(properties);
			}

			results.add(resultItem);
		}

		return results;
	}

	/**
	 * Returns run properties of paragraph as TestResultItem object. Returns
	 * empty TestResultItem if string is not found in paragraph
	 * 
	 * @param string
	 *            the string to check
	 * @param paragraph
	 *            which may or may not contain the string to be checked
	 * @param questionProperties
	 *            set of formatting properties to check in the string
	 * @return TestResultItem
	 */
	private static Map<String, TestResultProperty> checkRunProperties(
			List<XWPFRun> runs, String string,
			Map<String, String> questionProperties) {
		Map<String, TestResultProperty> resultProperties = new HashMap<String, TestResultProperty>();

		for (XWPFRun run : runs) {
			// Skip spaces or empty strings
			if (run == null || run.toString().trim().isEmpty())
				continue;

			// Get run properties and compare with question properties
			for (Map.Entry<String, String> correctProperty : questionProperties
					.entrySet()) {
				String propertyName = correctProperty.getKey();
				String runValue = getRunProperty(run, propertyName);

				// Save run property in map and update total
				TestResultProperty runProperty = resultProperties.containsKey(propertyName) ? resultProperties
						.get(propertyName) : new TestResultProperty(propertyName, runValue);

				runProperty.addTotal(1);

				// Add score if run property matches question property
				if (runValue.equalsIgnoreCase(correctProperty.getValue())) {
					runProperty.addScore(1);
				}

				// Add property to result
				resultProperties.put(runProperty.getName(), runProperty);
			}
		}

		return resultProperties;
	}

	/**
	 * Returns result property of paragraph
	 * @param paragraph the paragraph to be checked
	 * @param propertyName
	 * @return
	 */
	private static TestResultProperty getParagraphProperty(XWPFParagraph paragraph, String propertyName) {
		String resultValue = "";

		switch (propertyName) {
		case "LINE SPACING":
			XWPFParagraphClone pc = new XWPFParagraphClone(paragraph.getCTP(), paragraph.getBody());
			resultValue = (pc.getCTSpacing(false).getLine().floatValue() / 240) + "";
			break;
		case "NUMBERING FORMAT":
			resultValue = paragraph.getNumFmt();
			break;
		case "ALIGN":
			resultValue = paragraph.getAlignment().toString();
			break;
		default:
			System.out.println("Property " + propertyName + " does not exist!");
			resultValue = "";
			break;
		}

		TestResultProperty resultProperty = new TestResultProperty(propertyName, resultValue);
		return resultProperty;
	}

	
	/**
	 * Returns result properties of paragraph based on question properties
	 * @param paragraph the paragraph to be checked
	 * @param question TestQuestion object
	 * @return
	 */
	private static Map<String, TestResultProperty> checkParagraphProperties(XWPFParagraph paragraph, TestQuestion question) {
		Map<String, TestResultProperty> results = new HashMap<String, TestResultProperty>();
		Map<String, String> questionProperties = question.getProperties();

		// Check properties of this paragraph that are included in question properties 
		for (Map.Entry<String, String> correctProperty : questionProperties.entrySet()) {
			// Get property of this paragraph
			TestResultProperty resultProperty = getParagraphProperty(paragraph, correctProperty.getKey());
			
			// Add score if it matches the current question property
			if (resultProperty.getValue().equalsIgnoreCase(correctProperty.getValue())) {
				resultProperty.addScore(1);
			}
			
			// Update total and add to result properties
			resultProperty.addTotal(1);
			results.put(resultProperty.getName(), resultProperty);
		}
		
		return results;
	}

	/**
	 * Returns results for PARAGRAPH-type questions (if certain paragraphs have the specified properties) 
	 * @param paragraphs 
	 * @param question
	 * @return
	 */
	public static List<TestResultItem> checkParagraphQuestion(List<XWPFParagraph> pl, TestQuestion question) {
		List<TestResultItem> results = new ArrayList<TestResultItem>();
		List<String> strings = question.getStrings();
		
		for (String string : strings) {
			XWPFParagraph paragraph = findParagraphWithString(pl, string);
			TestResultItem resultItem = new TestResultItem(string);
			resultItem.setExists(paragraph != null);
			
			if (resultItem.exists()) {
				resultItem.setProperties(checkParagraphProperties(paragraph, question));
			}
			
			results.add(resultItem);
		}

		return results;
	}

	/**
	 * Returns result of ALL PARAGRAPHS-type questions (if all paragraphs in the document have the specified properties)
	 * @param paragraphs
	 * @param question
	 * @return
	 */
	public static List<TestResultItem> checkAllParagraphsQuestion(List<XWPFParagraph> paragraphs, TestQuestion question) {
		List<TestResultItem> results = new ArrayList<TestResultItem>();
		Map<String, TestResultProperty> resultProperties = new HashMap<String, TestResultProperty>();
		
		for (XWPFParagraph paragraph : paragraphs) {
			TestResultProperty resultProperty;
			
			if (paragraph.getText().isEmpty()) {
				continue;
			}

			for (Map.Entry<String, String> entry : question.getProperties().entrySet()) {
				String name = entry.getKey();
				
				if (resultProperties.containsKey(name)) {
					resultProperty = resultProperties.get(name);
				} else {
					resultProperty = new TestResultProperty(name, entry.getValue());
				}
		
				if (checkIfParagraphHasProperty(paragraph, name, entry.getValue())) {
					resultProperty.addScore(1);
				}
				
				resultProperty.addTotal(1);
				resultProperties.put(name, resultProperty);
			}
		}
		
		TestResultItem resultItem = new TestResultItem("ALL PARAGRAPHS");
		resultItem.setExists(true);
		resultItem.setProperties(resultProperties);
		results.add(resultItem);
		return results;
	}

	/**
	 * Returns <code>true</code> if paragraph has the correct property
	 * @param paragraph
	 * @param propertyName
	 * @param correctValue
	 * @return
	 */
	private static boolean checkIfParagraphHasProperty(XWPFParagraph paragraph, String propertyName, String correctValue) {
		boolean hasProperty = false;
		
		TestResultProperty resultProperty = getParagraphProperty(paragraph, propertyName);
		hasProperty = resultProperty.getValue().equalsIgnoreCase(correctValue);

		return hasProperty;
	}

	/**
	 * Returns <code>true</code> if document has the correct property
	 * @param docx
	 * @param property
	 * @param value
	 * @return
	 */
	public static boolean checkIfDocumentHasProperty(XWPFDocument docx, String property, String value) {
		CTPageMar margin = docx.getDocument().getBody().getSectPr().getPgMar();
		switch (property) {
			case "MARGIN TOP":
				return String.valueOf(margin.getTop().longValue() / 1440).equals(
						value);
			case "MARGIN LEFT":
				return String.valueOf(margin.getLeft().longValue() / 1440).equals(
						value);
			case "MARGIN BOTTOM":
				return String.valueOf(margin.getBottom().longValue() / 1440)
						.equals(value);
			case "MARGIN RIGHT":
				return String.valueOf(margin.getRight().longValue() / 1440).equals(
						value);
			default:
				return false;
		}
	}

	/**
	 * Returns results of DOCUMENT-type questions (if document has the specified properties)
	 * @param docx
	 * @param question
	 * @return
	 */
	public static List<TestResultItem> checkDocumentQuestion(XWPFDocument docx, TestQuestion question) {
		List<TestResultItem> results = new ArrayList<TestResultItem>();
		Map<String, TestResultProperty> resultProperties = new HashMap<String, TestResultProperty>();
		
		Map<String, String> properties = question.getProperties();
		
		for (Map.Entry<String, String> correctProperty : properties.entrySet()) {
			String propertyName = correctProperty.getKey();
			String value = getDocumentProperty(docx, propertyName);
		
			TestResultProperty resultProperty = new TestResultProperty(propertyName, value);
			if (resultProperty.getValue().equalsIgnoreCase(correctProperty.getValue())) {
				resultProperty.addScore(1);
			}
			
			resultProperty.addTotal(1);
			resultProperties.put(propertyName, resultProperty);
		}

		TestResultItem resultItem = new TestResultItem("DOCUMENT");
		resultItem.setExists(true);
		resultItem.setProperties(resultProperties);
		results.add(resultItem);
		
		return results;
	}
	
	/**
	 * Returns the value of the document property
	 * @param docx
	 * @param property name of the property
	 * @return
	 */
	private static String getDocumentProperty(XWPFDocument docx, String property) {
		String value = "";
		CTPageMar margin = docx.getDocument().getBody().getSectPr().getPgMar();

		switch (property) {
		case "MARGIN TOP":
			value = String.valueOf(margin.getTop().longValue() / 1440);
			break;
		case "MARGIN LEFT":
			value = String.valueOf(margin.getLeft().longValue() / 1440);
			break;
		case "MARGIN BOTTOM":
			value = String.valueOf(margin.getBottom().longValue() / 1440);
			break;
		case "MARGIN RIGHT":
			value = String.valueOf(margin.getRight().longValue() / 1440);
			break;
		default:
			value = "";
			break;
		}
		
		return value;
	}	

	/**
	 * Returns the value of the run property
	 * @param run
	 * @param property
	 * @return
	 */
	public static String getRunProperty(XWPFRun run, String property) {
		String runProperty = "";
		
		switch (property) {
			case "COLOR":
				runProperty = run.getColor();
				break;
			case "FONT FAMILY":
				runProperty = run.getFontFamily();
				break;
			case "FONT SIZE":
				runProperty = run.getFontSize() + "";
				break;
			case "BOLD":
				runProperty = run.isBold() + "";
				break;
			case "ITALIC":
				runProperty = run.isItalic() + "";
				break;
			case "STRIKETHROUGH":
				runProperty = run.isStrike() + "";
				break;
			default:
				System.out.println("Property " + property + " does not exist!");
				runProperty = "";
				break;
			}
		
		runProperty = runProperty == null ? "" : runProperty;
		return runProperty;
	}
	
}
