/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project3.poi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlink;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageMar;

import com.project3.test.models.TestResultProperty;
import com.project3.test.models.TestResultItem;

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
    /**
     * Returns true if XWPFRun has the correct value for the given property
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
                    return "NIL";
            }
        }
        catch (NullPointerException e) {
            return "NIL";
        }
    }
    
    public static Boolean checkIfRunHasProperty(XWPFRun r, String property, String value) {
        try {
            switch (property) {
                case "HYPERLINK":
                    XWPFHyperlink link = ((XWPFHyperlinkRun) r).getHyperlink(r.getDocument());
                    return link.getURL().toString().contains(value);
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
    /**
     * Check all XWPFRun in a single paragraph using checkIfRunHasProperty.
     */
    public static Map<String, TestResultItem> checkPropertiesofParagraphRuns(XWPFParagraph p, ArrayList<String> sl, Map<String, String> properties) {
        List<XWPFRun> rl = p.getRuns();
        Map<String, TestResultItem> results = new HashMap<String, TestResultItem>();
        
        //Initialize results
        for (String s: sl) {
//        	System.out.println("### For string " + s);
        	results.put(s, new TestResultItem(s));
        	for(String property: properties.keySet()) {
//        		System.out.println("\t\tAdd test question property "+property);
        		results.get(s).getProperties().add(new TestResultProperty(property));
        	}
        }
        //Check first if elements in sl are in p
        for (String s: sl) {
//        	System.out.println("\t\tExists in paragraph");
        	results.get(s).setExists(p.getParagraphText().contains(s));
        }

        //For each existing string, 
        for (XWPFRun r : rl) {
            //Skip run if empty string
            if (r.toString().isEmpty()) {
                continue;
            }
            
//            System.out.println("\t\tRun text is "+ r.getText(0));
            
            for (String s : sl) {
                //Skip string if it does't exist
                if (results.get(s).isExists()) {
//                	System.out.println("\t\tChecking run properties");
                    //For each property, check if it applies to the run
                    for (String property : properties.keySet()) {
                    	System.out.println("\t\t\t"+property+"="+properties.get(property)+" vs "+getRunProperty(r,property));
                        if (checkIfRunHasProperty(r, property, properties.get(property))) {
                        	results.get(s).getProperty(property).setValue(getRunProperty(r,property));
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
    /**
     * Uses checkPropertiesofParagraphRuns to check XWPFRun properties of a list of paragraphs.
     */
    public static Map<String, TestResultItem> checkRunPropertiesOfParagraphs(List<XWPFParagraph> pl, ArrayList<String> sl, Map<String, String> properties) {
        Map<String, TestResultItem> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        ArrayList<String> tempList;
        String removeString = "";
        
        //Initialize results
        for (String s: sl) {
        	results.put(s, new TestResultItem(s));
        	for(String property: properties.keySet()) {
        		results.get(s).getProperties().add(new TestResultProperty(property, properties.get(property)));
        	}
        }
        
        for (XWPFParagraph p : pl) {
            for (String s : sl) {
                tempMap = null;
                //Will fail on typos, but pass on extra elements before or after string of interest
                //Need to change for typo toleration and exactness?
                if (p.getParagraphText().contains(s))
                {
                    tempList = new ArrayList<String>();
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
    /**
     * Returns true if XWPFParagraph has the correct value for the given property
     */
    public static Boolean checkIfParagraphHasProperty(XWPFParagraph p, String property, String value) {
        try {
            switch (property) {
                case "LINE SPACING":
                    TestXWPFParagraph pc;
                    pc = new TestXWPFParagraph(p.getCTP(), p.getBody());
                    return pc.getCTSpacing(false).getLine().floatValue()/240 == Float.parseFloat(value);
                case "NUMBERING FORMAT":
                    return p.getNumFmt().equalsIgnoreCase(value);
                case "ALIGN":
                    return p.getAlignment().toString().equalsIgnoreCase(value);
                case "BORDER BOTTOM":
                    return p.getBorderBottom().toString().equalsIgnoreCase(value);
                default:
                    System.out.println("Property " + property +  " does not exist!");
                    return false;
            }
        }
        catch (NullPointerException e) {
            return false;
        }
    }
    /**
     * Checks the Paragraph properties of a single XWPFParagraph.
     */
    public static Map<String, TestResultItem> checkPropertiesofParagraph(XWPFParagraph p, String s, Map<String, String> properties) {
        Map<String, TestResultItem> results = new HashMap<String, TestResultItem>();
        
        results.put(s, new TestResultItem(s));
        
        for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestResultProperty(property, properties.get(property)));
    	}
        
        results.get(s).setExists(p.getParagraphText().contains(s));
        
        //Initialize counts to 0

        for (String property : properties.keySet()) {
            results.get(s).setExists(false);
        	results.get(s).getProperty(property).setCorrect(0);
        	results.get(s).getProperty(property).setTotal(0);
        }

        //For each existing string, 
        for (String property : properties.keySet()) {
            if (checkIfParagraphHasProperty(p, property, properties.get(property)))
            {
                results.get(s).setExists(true);
            	results.get(s).getProperty(property).setCorrect(1);
            	results.get(s).getProperty(property).setTotal(1);
            }
        }
        
        return results;
    }
    /**
     * Uses checkPropertiesofParagraph to check a list of paragraphs.
     */
    public static Map<String, TestResultItem> checkPropertiesOfParagraphs(List<XWPFParagraph> pl, ArrayList<String> sl, Map<String, String> properties) {
    	Map<String, TestResultItem> results = new HashMap<>(), 
                             			tempMap = new HashMap<>();
        String removeString = "";
        
      //Initialize results
        for (String s: sl) {
        	results.put(s, new TestResultItem(s));
        	for(String property: properties.keySet()) {
        		results.get(s).getProperties().add(new TestResultProperty(property, properties.get(property)));
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
    /**
     * Checks the Paragraph properties of a list of XWPFParagraph.
     */
    public static Map<String, TestResultItem> checkPropertiesOfAllParagraphs(List<XWPFParagraph> pl, Map<String, String> properties) {
    	Map<String, TestResultItem> results = new HashMap<>();
        int paragraph_count = 0;
        
        // Initialize results, properties which were not found in the document are left as 0
        String s = "ALL PARAGRAPHS";
    	results.put(s, new TestResultItem(s));
    	
    	for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestResultProperty(property, properties.get(property)));
    		results.get(s).getProperty(property).setCorrect(0);
    	}
        
        for (XWPFParagraph p : pl) {
            if (p.getParagraphText().isEmpty()) { continue; }
            paragraph_count++;
            for (String property : properties.keySet()) {
                if(checkIfParagraphHasProperty(p, property, properties.get(property))) {
                    results.get(s).setExists(true);
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
    /**
     * Checks properties of XWPFDocument. 
     */
    public static Map<String, TestResultItem> checkPropertiesOfDocument(XWPFDocument docx, Map<String, String> properties) {
    	Map<String, TestResultItem> results = new HashMap<>();
        // Initialize results, properties which were not found in the document are left as 0
        String s = "DOCUMENT";
    	results.put(s, new TestResultItem(s));
    	
    	for(String property: properties.keySet()) {
    		results.get(s).getProperties().add(new TestResultProperty(property, properties.get(property)));
    		results.get(s).setExists(false);
    		results.get(s).getProperty(property).setCorrect(0);
    		results.get(s).getProperty(property).setTotal(0);
    	}
        
        for (String property : properties.keySet()) {
            if(checkIfDocumentHasProperty(docx, property, properties.get(property))) {
                results.get(s).setExists(true);
            	results.get(s).getProperty(property).setCorrect(1);
            	results.get(s).getProperty(property).setTotal(1);
            }
        }
        return results;
    }
    /**
     * Check if XWPFParagraph text contains the given string 
     */
    public static Map<String, TestResultItem> checkIfStringExistsInParagraphs(List<XWPFParagraph> pl,  ArrayList<String> sl) {
    	Map<String, TestResultItem> results = new HashMap<>();
        List<String> removeStrings = new ArrayList<String>();
        // Initialize results, properties which were not found in the document are left as 0
        for (String s: sl) {
        	results.put(s, new TestResultItem(s));
        }
        
        for (XWPFParagraph p : pl) {
            if (p.getParagraphText().isEmpty()) { continue; }
            if (!removeStrings.isEmpty()) { removeStrings = new ArrayList<String>(); }
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
    public static Map<String, TestResultItem> checkPropertiesOfPictures(List<XWPFPictureData> pictureList, ArrayList<String> sl, Map<String, String> properties) {
        Map<String, TestResultItem> results = new HashMap<>();
        String removeString = "";
        // Initialize results, properties which were not found in the document are left as 0
        for (String s: sl) {
            results.put(s, new TestResultItem(s));
            for(String property: properties.keySet()) {
              results.get(s).getProperties().add(new TestResultProperty(property, properties.get(property)));
              results.get(s).setExists(false);
              results.get(s).getProperty(property).setCorrect(0);
              results.get(s).getProperty(property).setTotal(0);
            }
        }
        for (XWPFPictureData picture: pictureList) {
            for (String s: sl) {
               if(picture.getChecksum() == Long.parseLong(s)) {
                 results.get(s).setExists(true);
                   for (String property : properties.keySet()) {
                         if(checkIfPictureHasProperty(picture, property, properties.get(property))) {
                             results.get(s).getProperty(property).setCorrect(1);
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
        
        return results;
    }
    public static Map<String, TestResultItem> checkContentsOfTable(XWPFTable t, ArrayList<String> sl) {
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
        return results;
    }
}