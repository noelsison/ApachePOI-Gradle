/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project3.poi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.project3.test.models.TestQuestion;
import com.project3.test.models.TestResultItem;
import com.project3.test.models.TestResultProperty;

/**
 * Make this class read from XML files that contain the formatted "question"
 * @author Noel
 */
public class DocumentPropertyChecker {
    
//    public static Map<String, HashMap> checkIfStringsExistInParagraph(XWPFParagraph p, List<String> sl) {
//        Map<String, HashMap> results = new HashMap();
//        for (String s: sl) {
//            results.put(s, new HashMap());
//            results.get(s).put("EXISTS", p.getParagraphText().contains(s));
//        }
//        return results;
//    }
    
	static void print(String s) {
		System.out.println(s);
	}
    
    public static List<TestResultItem> checkParagraphs(List<XWPFParagraph> paragraphs, TestQuestion question) {
    	print("$ Checking paragraphs for question " + question.getQuestionId());
    	
    	List<TestResultItem> results = new ArrayList<TestResultItem>();
    	Iterator<String> strings = question.getStrings().iterator();
    	Map<String, String> properties = question.getProperties();
    	
    	for (XWPFParagraph paragraph : paragraphs) {
    		print("\t---START paragraph");
    		boolean goToNext = false;
    		
    		while (strings.hasNext() && !goToNext) {
    			String string = strings.next();
    			print("\t---Checking paragraph for string "+ string);
        		if (paragraph.getParagraphText().contains(string)) {
        			print("\t\tParagraph contains string");
	    			TestResultItem resultItem = checkRunProperties(paragraph.getRuns(), string, properties);
	    			results.add(resultItem);
	    			goToNext = true;
    			}
    		}
    		print("\t---END paragraph");
    	}
    	
    	return results;
    }
    
    // Checking the runs, count if all instances contain the said formating
    public static List<TestResultItem> checkParagraph(XWPFParagraph paragraph, String string, Map<String, String> questionProperties) {
//    	print("--- Checking paragraph for question " + question.getQuestionId());
//    	TestResultItem result = new TestResultItem(string);
    	List<TestResultItem> results = new ArrayList<TestResultItem>();
//    	List<XWPFRun> runs = paragraph.getRuns();
//    	
////    	List<String> sl = question.getStrings();
////    	Map<String, String> questionProperties = question.getProperties();
//    	
//    	for (String string : sl ) {
//    		print("### For string " +string);
//    		
////    		results.add(resultItem);
//    	}
//    	
    	// TODO where to add to total
        
//        //Count only runs which are not empty for scoring
//        int total_runs = 0;
//        for (XWPFRun r : rl) {
//            if (!r.toString().isEmpty()) {
//                total_runs++;
//            }
//        }
//        //Transform results to score
//        for (String s : sl) {
//            for (String property : properties.keySet()) {
//            	results.get(s).getProperty(property).setTotal(total_runs);
//            }
//        }
        return results;
    }
    
    /**
     * Returns run properties of paragraph as TestResultItem object. Returns empty TestResultItem if string is not found in paragraph
     * @param string the string to check
     * @param paragraph which may or may not contain the string to be checked
     * @param questionProperties set of formatting properties to check in the string
     * @return TestResultItem
     */
    public static TestResultItem checkRunProperties(List<XWPFRun> runs, String string, Map<String, String> questionProperties) {
    	print("\t\tBegin checking runs");
    	TestResultItem resultItem = new TestResultItem(string);
    	
    	for (XWPFRun run : runs) {
			print("\t\tChecking run \"" + run.getText(0) + "\"");
			// skip spaces or empty strings
			if (run.toString().trim().isEmpty())
				continue;

			print("\t\tChecking run properties of " + run.getText(0));
			// get run properties and compare with question properties
			for (Map.Entry<String, String> correctProperty : questionProperties
					.entrySet()) {
				String propertyName = correctProperty.getKey();
				String correctValue = correctProperty.getValue();

				String runValue = getRunProperty(run, propertyName);
				TestResultProperty runProperty = new TestResultProperty(
						propertyName, runValue);
				runProperty.addTotal(1);
				print("\t\tRun property is " + propertyName + "=" + runValue
						+ "\t" + "Correct property is " + propertyName + "="
						+ correctValue);

				if (runValue.equalsIgnoreCase(correctValue)) {
					print("\t\tRun property is correct");
					runProperty.addNumCorrect(1);
				}

				resultItem.setProperty(runProperty);
				print("\t\tSave result");
			}
		}
    	
    	return resultItem;
    }
    
    
    
    /*
    // check for strings that span whole paragraphs
    public static Map<String, TestResultItem> checkRunPropertiesOfParagraphs(List<XWPFParagraph> pl, ArrayList<String> sl, Map<String, String> properties) {
        Map<String, TestResultItem> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        ArrayList<String> tempList;
        String removeString = "";
        
        //Initialize results
        for (String s: sl) {
        	results.put(s, new TestResultItem(s));
        	for(String property: properties.keySet()) {
        		results.get(s).getProperties().add(new TestResultProperty(property));
        	}
        }
        
        for (XWPFParagraph p : pl) {
            for (String s : sl) {
                tempMap = null;
                //Will fail on typos, but pass on extra elements before or after string of interest
                //Need to change for typo toleration and exactness?
                if (p.getParagraphText().contains(s))
                {
                    tempList = new ArrayList();
                    tempList.add(s);
                    tempMap = checkPropertiesofParagraphRuns(p, tempList, properties);
                    results.put(s, tempMap.get(s));
                    removeString = s;
                    break;
                }
            }
            //Remove string if it has been evaluated
            if (tempMap != null) {
                sl.remove(removeString);
            }
        }
        return results;
    }
    public static Boolean checkIfParagraphHasProperty(XWPFParagraph p, String property, String value) {
        try {
            switch (property) {
                case "LINE SPACING":
                    XWPFParagraphClone pc;
                    pc = new XWPFParagraphClone(p.getCTP(), p.getBody());
                    return pc.getCTSpacing(false).getLine().floatValue()/240 == Float.parseFloat(value);
                case "NUMBERING FORMAT":
                    return p.getNumFmt().equalsIgnoreCase(value);
                case "ALIGN":
                    return p.getAlignment().toString().equalsIgnoreCase(value);
                default:
                    System.out.println("Property " + property +  " does not exist!");
                    return false;
            }
        }
        catch (NullPointerException e) {
            return false;
        }
    }
    //single paragraph
    public static Map<String, TestResultItem> checkPropertiesofParagraph(XWPFParagraph p, String s, Map<String, String> properties) {
        List<XWPFRun> rl = p.getRuns();
        Map<String, TestResultItem> results = new HashMap();
        
        results.put(s, new TestResultItem(s));
        
        for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestResultProperty(property));
    	}
        
        results.get(s).setExists(p.getParagraphText().contains(s));
        
        //Initialize counts to 0

        for (String property : properties.keySet()) {
        	results.get(s).getProperty(property).setNumCorrect(0);
        	results.get(s).getProperty(property).setTotal(0);
        }

        //For each existing string, 
        for (String property : properties.keySet()) {
            if (checkIfParagraphHasProperty(p, property, properties.get(property)))
            {
            	results.get(s).getProperty(property).setNumCorrect(1);
            }
        }
        
        return results;
    }
    
    //Paragraph properties, ignore runs
    public static Map<String, TestResultItem> checkPropertiesOfParagraphs(List<XWPFParagraph> pl, ArrayList<String> sl, Map<String, String> properties) {
    	Map<String, TestResultItem> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        ArrayList tempList;
        String removeString = "";
        
      //Initialize results
        for (String s: sl) {
        	results.put(s, new TestResultItem(s));
        	for(String property: properties.keySet()) {
        		results.get(s).getProperties().add(new TestResultProperty(property));
        	}
        }
        //Check first if elements in sl are in p
        for (String s: sl) {
        	results.get(s).setExists(false);
        }
        
        for (XWPFParagraph p : pl) {
            for (String s : sl) {
                tempMap = null;
                //Will fail on typos, but pass on extra elements before or after string of interest
                //Need to change for typo toleration and exactness?
                if (p.getParagraphText().contains(s))
                {   
                	results.get(s).setExists(p.getParagraphText().contains(s));
                    tempMap = checkPropertiesofParagraph(p, s, properties);
                    results.put(s, tempMap.get(s));
                    removeString = s;
                    break;
                }
            }
            //Remove string if it has been evaluated
            if (tempMap != null) {
                sl.remove(removeString);
            }
        }
        return results;
    }
    //Check all paragraphs
    public static Map<String, TestResultItem> checkPropertiesOfAllParagraphs(List<XWPFParagraph> pl, Map<String, String> properties) {
    	Map<String, TestResultItem> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        ArrayList tempList;
        String removeString = "";
        
        int paragraph_count = 0;
        
        // Initialize results, properties which were not found in the document are left as 0
        String s = "ALL PARAGRAPHS";
    	results.put(s, new TestResultItem(s));
    	
    	for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestResultProperty(property));
    		results.get(s).getProperty(property).setNumCorrect(0);
    	}
        
        for (XWPFParagraph p : pl) {
            if (p.getParagraphText().isEmpty()) { continue; }
            paragraph_count++;
            for (String property : properties.keySet()) {
                if(checkIfParagraphHasProperty(p, property, properties.get(property))) {
                	results.get(s).getProperty(property).setNumCorrect(results.get(s).getProperty(property).getNumCorrect() + 1);
                }
            }
        }
        for (String property : properties.keySet()) {
        	results.get(s).getProperty(property).setTotal(paragraph_count);
        }
        return results;
    }
    public static boolean checkIfDocumentHasProperty(XWPFDocument docx, String property, String value) {
        CTPageMar margin = docx.getDocument().getBody().getSectPr().getPgMar();
        switch (property) {
            case "MARGIN TOP":
                return String.valueOf(margin.getTop().longValue()/1440).equals(value);
            case "MARGIN LEFT":
                return String.valueOf(margin.getLeft().longValue()/1440).equals(value);
            case "MARGIN BOTTOM":
                return String.valueOf(margin.getBottom().longValue()/1440).equals(value);
            case "MARGIN RIGHT":
                return String.valueOf(margin.getRight().longValue()/1440).equals(value);
            default:
                return false;
        }
    }
    public static Map<String, TestResultItem> checkPropertiesOfDocument(XWPFDocument docx, Map<String, String> properties) {
    	Map<String, TestResultItem> results = new HashMap<>();
        CTPageMar margin = docx.getDocument().getBody().getSectPr().getPgMar();
        
        // Initialize results, properties which were not found in the document are left as 0
        String s = "DOCUMENT";
    	results.put(s, new TestResultItem(s));
    	
    	for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestResultProperty(property));
    		results.get(s).getProperty(property).setNumCorrect(0);
    		results.get(s).getProperty(property).setTotal(0);
    	}
        
        for (String property : properties.keySet()) {
            if(checkIfDocumentHasProperty(docx, property, properties.get(property))) {
            	results.get(s).getProperty(property).setNumCorrect(1);
            }
        }
        return results;
    }
    public static Map<String, TestResultItem> checkIfStringExistsInParagraphs(List<XWPFParagraph> pl,  ArrayList<String> sl) {
    	Map<String, TestResultItem> results = new HashMap<>();
        List<String> removeStrings = new ArrayList();
        // Initialize results, properties which were not found in the document are left as 0
        for (String s: sl) {
        	results.put(s, new TestResultItem(s));
        }
        
        for (XWPFParagraph p : pl) {
            if (p.getParagraphText().isEmpty()) { continue; }
            if (removeStrings.isEmpty()) { removeStrings = new ArrayList(); }
            for (String s : sl) {
                if (p.getParagraphText().contains(s)) {
                    results.get(s).setExists(true);;
                    removeStrings.add(s);
                }
            }
            for (String s : removeStrings) {
                sl.remove(s);
            }
        }
        return results;
    }
    */
    
    public static String getRunProperty(XWPFRun r, String property) {
        try {
            switch (property) {
                case "COLOR":
                    return r.getColor();
                case "FONT FAMILY":
                    return r.getFontFamily();
                case "FONT SIZE":
                    return r.getFontSize() + "";
                case "BOLD":
                    return r.isBold() +"";
                case "ITALIC":
                    return r.isItalic()+"";
                case "STRIKETHROUGH":
                    return r.isStrike()+"";
                default:
                    System.out.println("Property " + property +  " does not exist!");
                    return "";
            }
        }
        catch (NullPointerException e) {
            return "";
        }
    }
    
    
    public static Boolean checkIfRunHasProperty(XWPFRun r, String property, String value) {
        try {
            switch (property) {
                case "COLOR":
                    return r.getColor().equals(value);
                case "FONT FAMILY":
                    return r.getFontFamily().equalsIgnoreCase(value);
                case "FONT SIZE":
                    return r.getFontSize() == Integer.parseInt(value);
                case "BOLD":
                    return r.isBold() == Boolean.valueOf(value);
                case "ITALIC":
                    return r.isItalic() == Boolean.valueOf(value);
                case "STRIKETHROUGH":
                    return r.isStrike() == Boolean.valueOf(value);
                default:
                    System.out.println("Property " + property +  " does not exist!");
                    return false;
            }
        }
        catch (NullPointerException e) {
            return false;
        }
    }

}
