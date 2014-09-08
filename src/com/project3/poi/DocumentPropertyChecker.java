/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project3.poi;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
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
	public static List<TestResultItem> checkIfStringExists(
			List<XWPFParagraph> paragraphs, TestQuestion question) {
		List<TestResultItem> results = new ArrayList<TestResultItem>();
		List<String> strings = question.getStrings();

		// Check each string
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

	/**
	 * Returns the paragraph that contains the string argument
	 * @param paragraphs
	 * @param string
	 * @return
	 */
	private static XWPFParagraph findParagraphWithString(List<XWPFParagraph> paragraphs, String string) {
		XWPFParagraph paragraph = null;
		Iterator<XWPFParagraph> iterator = paragraphs.iterator();
		boolean found = false;

		// Check the next paragraphs while string is not found
		while (iterator.hasNext() && !found) {
			paragraph = iterator.next();
			found = paragraph.getText().contains(string);
		}

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

		// Check each string
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

			// Add to results
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

		// Check each run
		for (XWPFRun run : runs) {
			// Skip spaces or empty strings
			if (run == null || run.toString().trim().isEmpty())
				continue;

			// Get properties of this run and compare with question properties
			for (Map.Entry<String, String> correctProperty : questionProperties
					.entrySet()) {
				String propertyName = correctProperty.getKey();
				String runValue = getRunProperty(run, propertyName);

				// Create new or update existing result property for string (update occurs when string has 2 or more runs)
				TestResultProperty stringProperty = resultProperties.containsKey(propertyName) ? resultProperties
						.get(propertyName) : new TestResultProperty(propertyName, runValue);

				// Add score if run property matches question property
				if (runValue.equalsIgnoreCase(correctProperty.getValue())) {
					stringProperty.addScore(1);
				}

				// Add property to result
				stringProperty.addTotal(1);
				resultProperties.put(stringProperty.getName(), stringProperty);
			}
		}

		return resultProperties;
	}

	/**
	 * Returns the value of the run property
	 * @param run
	 * @param property
	 * @return
	 */
	public static String getRunProperty(XWPFRun run, String property) {
		String runProperty;
		
		// For each type of property, check if value is null before converting to string or manipulating its value to avoid NullPointerException except for native types
		switch (property) {
			case "COLOR":
				String color = run.getColor();
				runProperty = color == null ? "" : color;
				break;
			case "FONT FAMILY":
				String fontFamily = run.getFontFamily();
				runProperty = fontFamily == null ? "" : fontFamily;
				break;
			case "FONT SIZE":
//				int fontSize = run.getFontSize();
				runProperty = String.valueOf(run.getFontSize());
				break;
			case "BOLD":
				runProperty = String.valueOf(run.isBold());
				break;
			case "ITALIC":
				runProperty = String.valueOf(run.isItalic());
				break;
			case "STRIKETHROUGH":
				runProperty = String.valueOf(run.isStrike());
				break;
			default:
				System.out.println("Property " + property + " does not exist!");
				runProperty = "";
				break;
			}
		
		return runProperty;
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
		
		// Check each paragraph identified by the string
		for (String string : strings) {
			// Get paragraph that contains the string and set exists if found
			XWPFParagraph paragraph = findParagraphWithString(pl, string);
			TestResultItem resultItem = new TestResultItem(string);
			resultItem.setExists(paragraph != null);
			
			// Get paragraph properties
			if (resultItem.exists()) {
				resultItem.setProperties(checkParagraphProperties(paragraph, question));
			}
			
			// Add to results
			results.add(resultItem);
		}

		return results;
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

		// Check if this paragraph has the properties in the question 
		for (Map.Entry<String, String> correctProperty : questionProperties.entrySet()) {
			// Get property of this paragraph
			TestResultProperty resultProperty = getParagraphProperty(paragraph, correctProperty.getKey());
			
			// Add score if it matches the current question property
			if (resultProperty.getValue().equalsIgnoreCase(correctProperty.getValue())) {
				resultProperty.addScore(1);
			}
			
			// Update total and add to results
			resultProperty.addTotal(1);
			results.put(resultProperty.getName(), resultProperty);
		}
		
		return results;
	}

	/**
	 * Returns result property of paragraph
	 * @param paragraph the paragraph to be checked
	 * @param propertyName
	 * @return
	 */
	private static TestResultProperty getParagraphProperty(XWPFParagraph paragraph, String propertyName) {
		String resultValue;
		// int someValue = 240;

		// For each type of property, check if value is null before converting to string or manipulating its value to avoid NullPointerException except for native types
		switch (propertyName) {
		case "LINE SPACING":
			XWPFParagraphClone pc = new XWPFParagraphClone(paragraph.getCTP(), paragraph.getBody());
			resultValue = pc == null ? "" : String.valueOf(pc.getCTSpacing(false).getLine().floatValue() / 240);
			break;
		case "NUMBERING FORMAT":
			String numFormat = paragraph.getNumFmt();
			resultValue = numFormat == null ? "" : numFormat;
			break;
		case "ALIGN":
			ParagraphAlignment alignment = paragraph.getAlignment();
			resultValue = alignment == null ? "" : alignment.toString();
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
	 * Returns result of ALL PARAGRAPHS-type questions (if all paragraphs in the document have the specified properties)
	 * @param paragraphs
	 * @param question
	 * @return
	 */
	public static List<TestResultItem> checkAllParagraphsQuestion(List<XWPFParagraph> paragraphs, TestQuestion question) {
		List<TestResultItem> results = new ArrayList<TestResultItem>();
		Map<String, TestResultProperty> resultProperties = new HashMap<String, TestResultProperty>();
		
		// Check properties of all paragraphs
		for (XWPFParagraph paragraph : paragraphs) {
			TestResultProperty resultProperty;
			
			// Skip spaces or empty paragraphs
			if (paragraph.getText().trim().isEmpty()) {
				continue;
			}

			// Check if this paragraph has all the correct properties
			for (Map.Entry<String, String> entry : question.getProperties().entrySet()) {
				String name = entry.getKey();
				
				// Create new or update existing result property (since paragraphs have common properties)
				if (resultProperties.containsKey(name)) {
					resultProperty = resultProperties.get(name);
				} else {
					resultProperty = new TestResultProperty(name, entry.getValue());
				}
				
				// Compare properties of this paragraph with question properties and update result if they match
				TestResultProperty tempProperty = getParagraphProperty(paragraph, name);
				if (tempProperty.getValue().equalsIgnoreCase(entry.getValue())) {
					resultProperty.addScore(1);
				}
				
				// Update total and add to results
				resultProperty.addTotal(1);
				resultProperties.put(name, resultProperty);
			}
		}
		
		// Create result item with the evaluated properties 
		TestResultItem resultItem = new TestResultItem("ALL PARAGRAPHS");
		resultItem.setExists(true);
		resultItem.setProperties(resultProperties);
		results.add(resultItem);
		return results;
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
		
		// Check each question property
		for (Map.Entry<String, String> correctProperty : properties.entrySet()) {
			// Get document property
			String propertyName = correctProperty.getKey();
			String value = getDocumentProperty(docx, propertyName);
		
			// Create result object for this property and update score if it is correct 
			TestResultProperty resultProperty = new TestResultProperty(propertyName, value);
			if (resultProperty.getValue().equalsIgnoreCase(correctProperty.getValue())) {
				resultProperty.addScore(1);
			}
			
			// Update total and addd to results
			resultProperty.addTotal(1);
			resultProperties.put(propertyName, resultProperty);
		}

		// Create result item with the evaluated properties 
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
		BigInteger targetMargin;
		String value = "";
		// int someNumber = 1440;
		
		CTPageMar marginObject = docx.getDocument().getBody().getSectPr().getPgMar();

		switch (property) {
			case "MARGIN TOP":
				targetMargin = marginObject.getTop();
				break;
			case "MARGIN LEFT":
				targetMargin = marginObject.getLeft();
				break;
			case "MARGIN BOTTOM":
				targetMargin = marginObject.getBottom();
				break;
			case "MARGIN RIGHT":
				targetMargin = marginObject.getRight();
				break;
			default:
				targetMargin = new BigInteger("0");
				value = "";
				break;
		}
		
		value = String.valueOf(targetMargin.longValue() / 1440);
		return value;
	}
	
	//////// REFACTOR THIS TO FOLLOW ABOVE STYLE
	public static boolean checkIfPictureHasProperty(XWPFPictureData picture, String property, String value) {
      switch (property) {
        case "EXTENSION":
          return picture.suggestFileExtension().equalsIgnoreCase(value);
        default: 
          System.out.print("Picture property unsupported: " + property);
          return false;
      }
    }
    /**
     * For now, sl contains checksums of pictures to identify them
     */
    public static List<TestResultItem> checkPropertiesOfPictures(List<XWPFPictureData> pictureList, ArrayList<String> sl, Map<String, String> properties) {
        Map<String, TestResultItem> results = new HashMap<>();
        String removeString = "";
        // Initialize results, properties which were not found in the document are left as 0
        for (String s: sl) {
            results.put(s, new TestResultItem(s));
            for(String property: properties.keySet()) {
              results.get(s).setProperty(property, properties.get(property));
              results.get(s).setExists(false);
              results.get(s).getProperty(property).setScore(0);
              results.get(s).getProperty(property).setTotal(0);
            }
        }
        for (XWPFPictureData picture: pictureList) {
            for (String s: sl) {
               if(picture.getChecksum() == Long.parseLong(s)) {
                 results.get(s).setExists(true);
                   for (String property : properties.keySet()) {
                         if(checkIfPictureHasProperty(picture, property, properties.get(property))) {
                             results.get(s).getProperty(property).setScore(1);
                             results.get(s).getProperty(property).setTotal(1);
                         }
                   }
                 removeString = s;
                 break;
               }
            }
            if (! removeString.isEmpty()) {
              sl.remove(removeString);
            }
        }
        List<TestResultItem> result_list = new ArrayList();
        for (String key: results.keySet()) {
          result_list.add(results.get(key));
        }
        return result_list;
    }
    public static List<TestResultItem> checkContentsOfTable(XWPFTable t, ArrayList<String> sl) {
        Map<String, TestResultItem> results = new HashMap<>();
        XWPFTableRow r;
        XWPFTableCell c;
        String removeString = "";
        for (String s: sl) {
            results.put(s, new TestResultItem(s));
            results.get(s).setExists(false);
        }
        for (int row=0; row<t.getRows().size(); row++) {
            r = t.getRow(row);
            for (int col=0; col<r.getTableCells().size(); col++) {
                c = r.getCell(col);
                for (String s: sl) {
                    if (s.contains(c.getText())) {
                      results.get(s).setExists(true);
                      removeString = s;
                    }
                }
                sl.remove(removeString);
            }
        }
        List<TestResultItem> result_list = new ArrayList();
        for (String key: results.keySet()) {
            result_list.add(results.get(key));
        }
        return result_list;
    }
    ///////// END REFACTOR THIS TO FOLLOW ABOVE STYLE
}
