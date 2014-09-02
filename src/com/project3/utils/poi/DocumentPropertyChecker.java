/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project3.utils.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;

import com.project3.utils.test.TestQuestionProperty;
import com.project3.utils.test.TestQuestionResult;

/**
 * Make this class read from XML files that contain the formatted "question"
 * @author Noel
 */
public class DocumentPropertyChecker {
    
    public static Map<String, HashMap> checkIfStringsExistInParagraph(XWPFParagraph p, List<String> sl) {
        Map<String, HashMap> results = new HashMap();
        for (String s: sl) {
            results.put(s, new HashMap());
            results.get(s).put("EXISTS", p.getParagraphText().contains(s));
        }
        return results;
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
    //Checking the runs, count if all instances contain the said formating
    /*Returns a map of strings with a map of properties with booleans as checks*/
    public static Map<String, TestQuestionResult> checkPropertiesofParagraphRuns(XWPFParagraph p, ArrayList<String> sl, Map<String, String> properties) {
        List<XWPFRun> rl = p.getRuns();
        Map<String, TestQuestionResult> results = new HashMap();
        
        //Initialize results
        for (String s: sl) {
        	results.put(s, new TestQuestionResult(s));
        	for(String property: properties.keySet()) {
        		results.get(s).getProperties().add(new TestQuestionProperty(property));
        	}
        }
        //Check first if elements in sl are in p
        for (String s: sl) {
        	results.get(s).setExists(p.getParagraphText().contains(s));
        }

        //For each existing string, 
        for (XWPFRun r : rl) {
            //Skip run if empty string
            if (r.toString().isEmpty()) {
                continue;
            }
            for (String s : sl) {
                //Skip string if it does't exist
                if (results.get(s).isExists()) {
                    //For each property, check if it applies to the run
                    for (String property : properties.keySet()) {
                        if (checkIfRunHasProperty(r, property, properties.get(property)))
                        {
                        	results.get(s).getProperty(property).setCorrect(results.get(s).getProperty(property).getCorrect() + 1);
                        }
                    }
                }
            }
        }
        //Count only runs which are not empty for scoring
        int total_runs = 0;
        for (XWPFRun r : rl) {
            if (!r.toString().isEmpty()) {
                total_runs++;
            }
        }
        //Transform results to score
        for (String s : sl) {
            for (String property : properties.keySet()) {
            	results.get(s).getProperty(property).setTotal(total_runs);
            }
        }
        return results;
    }
    // check for strings that span whole paragraphs
    public static Map<String, TestQuestionResult> checkRunPropertiesOfParagraphs(List<XWPFParagraph> pl, ArrayList<String> sl, Map<String, String> properties) {
        Map<String, TestQuestionResult> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        ArrayList<String> tempList;
        String removeString = "";
        
        //Initialize results
        for (String s: sl) {
        	results.put(s, new TestQuestionResult(s));
        	for(String property: properties.keySet()) {
        		results.get(s).getProperties().add(new TestQuestionProperty(property));
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
    public static Map<String, TestQuestionResult> checkPropertiesofParagraph(XWPFParagraph p, String s, Map<String, String> properties) {
        List<XWPFRun> rl = p.getRuns();
        Map<String, TestQuestionResult> results = new HashMap();
        
        results.put(s, new TestQuestionResult(s));
        
        for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestQuestionProperty(property));
    	}
        
        results.get(s).setExists(p.getParagraphText().contains(s));
        
        //Initialize counts to 0

        for (String property : properties.keySet()) {
        	results.get(s).getProperty(property).setCorrect(0);
        	results.get(s).getProperty(property).setTotal(0);
        }

        //For each existing string, 
        for (String property : properties.keySet()) {
            if (checkIfParagraphHasProperty(p, property, properties.get(property)))
            {
            	results.get(s).getProperty(property).setCorrect(1);
            }
        }
        
        return results;
    }
    
    //Paragraph properties, ignore runs
    public static Map<String, TestQuestionResult> checkPropertiesOfParagraphs(List<XWPFParagraph> pl, ArrayList<String> sl, Map<String, String> properties) {
    	Map<String, TestQuestionResult> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        ArrayList tempList;
        String removeString = "";
        
      //Initialize results
        for (String s: sl) {
        	results.put(s, new TestQuestionResult(s));
        	for(String property: properties.keySet()) {
        		results.get(s).getProperties().add(new TestQuestionProperty(property));
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
    public static Map<String, TestQuestionResult> checkPropertiesOfAllParagraphs(List<XWPFParagraph> pl, Map<String, String> properties) {
    	Map<String, TestQuestionResult> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        ArrayList tempList;
        String removeString = "";
        
        int paragraph_count = 0;
        
        // Initialize results, properties which were not found in the document are left as 0
        String s = "ALL PARAGRAPHS";
    	results.put(s, new TestQuestionResult(s));
    	
    	for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestQuestionProperty(property));
    		results.get(s).getProperty(property).setCorrect(0);
    	}
        
        for (XWPFParagraph p : pl) {
            if (p.getParagraphText().isEmpty()) { continue; }
            paragraph_count++;
            for (String property : properties.keySet()) {
                if(checkIfParagraphHasProperty(p, property, properties.get(property))) {
                	results.get(s).getProperty(property).setCorrect(results.get(s).getProperty(property).getCorrect() + 1);
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
    public static Map<String, TestQuestionResult> checkPropertiesOfDocument(XWPFDocument docx, Map<String, String> properties) {
    	Map<String, TestQuestionResult> results = new HashMap<>();
        CTPageMar margin = docx.getDocument().getBody().getSectPr().getPgMar();
        
        // Initialize results, properties which were not found in the document are left as 0
        String s = "DOCUMENT";
    	results.put(s, new TestQuestionResult(s));
    	
    	for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestQuestionProperty(property));
    		results.get(s).getProperty(property).setCorrect(0);
    		results.get(s).getProperty(property).setTotal(0);
    	}
        
        for (String property : properties.keySet()) {
            if(checkIfDocumentHasProperty(docx, property, properties.get(property))) {
            	results.get(s).getProperty(property).setCorrect(1);
            }
        }
        return results;
    }
    public static Map<String, TestQuestionResult> checkIfStringExistsInParagraphs(List<XWPFParagraph> pl,  ArrayList<String> sl) {
    	Map<String, TestQuestionResult> results = new HashMap<>();
        List<String> removeStrings = new ArrayList();
        // Initialize results, properties which were not found in the document are left as 0
        for (String s: sl) {
        	results.put(s, new TestQuestionResult(s));
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
}
