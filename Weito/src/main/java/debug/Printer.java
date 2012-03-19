package debug;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import pdfbox.Format;
import pdfbox.TextMatch;
import drools.DocumentFeatureResult;

public class Printer {
	static int papercount = 0;

	private static Printer instance;

	public static Printer getInstance() {
		if(instance != null) return instance;
		else {
			instance = new Printer();
			return new Printer();
		}
	}

	private Printer() {}

	public void startNewPaper() {
		papercount++;
	}

	public void printFormatResults(List<Format> results, String inputLocation) {
		println("Format Results: "+inputLocation);
		for(Format f : results) {
			println(f.getPrimarystyle().getStyleInfo() + " " + f.getText());
		}
	}

	public void println(String string) {
		System.out.println(string);
	}

	public void printKeywordResults(List<DocumentFeatureResult> results,
			List<String> keywords, String inputLocation) {
		println("Keyword Results: "+inputLocation);
		String rowHeader = "";
		for(DocumentFeatureResult result : results) rowHeader += "," + result.getUniquename();
		println(rowHeader);
		int[] featuretotals = new int[results.size()];
		for(String keyword : keywords) {
			print(keyword+",");
			for(DocumentFeatureResult result : results) {
				int total = 0;
				for(String s : result.getFeatures()) {
					 total += TextMatch.match("(?i).*"+keyword+".*?", s).getResults().size();
					 featuretotals[results.indexOf(result)] += TextMatch.match("(?i).*"+keyword+".*?", s).getResults().size();
				}
				print( total + ",");
			}			
			print("\n");
		}
		String rowTotals = "TOTAL";
		for(int total : featuretotals) rowTotals += "," + total;
		println(rowTotals);

	}

	public void print(String string) {
		System.out.print(string);
	}

	public void printFeatureResults(List<DocumentFeatureResult> results,
			String inputLocation) {
		println("Feature Results: "+inputLocation);
		for(DocumentFeatureResult result : results) {
			if( !result.getUniquename().equals("Full Text") ) {
			printResultFeatures(result);
			}
		}
	}

	private void printResultFeatures(DocumentFeatureResult result) {
			println("["+result.getUniquename()+"]");
			for(String feature : result.getFeatures()) {
				println("* '"+feature+"'");
		}
	}

	public void printFeatureSpecResults(List<DocumentFeatureResult> results,
			String inputLocation, List<String> keywords, List<Format> format_results) {
		List<String> desiredUniqueNames = new ArrayList<String>( Arrays.asList(new String[] {"heading","abstract","conclusion"} ) );
		List<String> actualUniqueNames = new ArrayList<String>();
		@SuppressWarnings("unused")
		String title = "";
		for(DocumentFeatureResult result : results) {
			actualUniqueNames.add(result.getUniquename());
			if(result.getUniquename() == "title") title = result.getFeatures().get(0);
		}
		if(actualUniqueNames.containsAll(desiredUniqueNames)) {
			//println("==Valid file"+papercount+" : "+inputLocation+", Publication title: "+title+"==");
			//printKeywordResults(results, keywords, inputLocation);
		} else {
			println("==Not Valid file"+papercount+" : "+inputLocation+"==");
			printFeatureResults(results, inputLocation);
		}
	}

	public static String join(Collection<String> s, String delimiter) {
		if ( s.isEmpty() ) return "";
		Iterator<String> iter = s.iterator();
		StringBuffer buffer = new StringBuffer(iter.next());
		while (iter.hasNext()) buffer.append(delimiter).append(iter.next());
		return buffer.toString();
	}

}
