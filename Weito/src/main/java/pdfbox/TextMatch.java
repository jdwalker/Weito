package pdfbox;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TextMatch {

	ArrayList<TextData> results;
	String inputstring;

	/**
	 * @param results
	 * @param inputstring
	 */
	private TextMatch(ArrayList<TextData> results, String inputString) {
		this.results = results;
		this.inputstring = inputString;
	}

	public static TextMatch match(String regex, String inputString) {;
		ArrayList<TextData> results = new ArrayList<TextData>();
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(inputString);
		while( matcher.find() ) {
			results.add( new TextData(matcher.start(), matcher.end(), matcher.group() ) );
		}
		return new TextMatch(results, inputString);
	}
	
	public TextMatch orMatch(String regex) {
		Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(inputstring);
		while( matcher.find() ) {
			results.add( new TextData(matcher.start(), matcher.end(), matcher.group() ) );
		}
		return this;
	}

	public ArrayList<TextData> getResults() {
		return results;
	}
}