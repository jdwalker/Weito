package drools;

import java.util.ArrayList;
import java.util.List;

import pdfbox.ITextData;
import pdfbox.TextMatch;

public class DocumentFeatures {
	List<String> titles;
	List<String> headings;
	List<String> authors;
	List<String> publicationnames;
	List<String> publicationdates;
	List<String> abstracts;
	List<String> introductions;
	List<String> conclusions;
	List<String> references;
	String fulltext;
	String fileloc;
		
	/**
	 * @param titles
	 * @param headings
	 * @param authors
	 * @param publicationnames
	 * @param publicationdates
	 * @param abstracts
	 * @param introductions
	 * @param conclusions
	 * @param references
	 * @param fulltext
	 * @param fileloc
	 */
	public DocumentFeatures(List<String> titles, List<String> headings,
			List<String> authors, List<String> publicationnames,
			List<String> publicationdates, List<String> abstracts,
			List<String> introductions, List<String> conclusions,
			List<String> references, String fulltext, String fileloc) {
		this.titles = titles;
		this.headings = headings;
		this.authors = authors;
		this.publicationnames = publicationnames;
		this.publicationdates = publicationdates;
		this.abstracts = abstracts;
		this.introductions = introductions;
		this.conclusions = conclusions;
		this.references = references;
		this.fulltext = fulltext;
		this.fileloc = fileloc;
	}
	/**
	 * @return the titles
	 */
	public List<String> getTitles() {
		return titles;
	}
	/**
	 * @param titles the titles to set
	 */
	public void setTitles(List<String> titles) {
		this.titles = titles;
	}
	/**
	 * @return the headings
	 */
	public List<String> getHeadings() {
		return headings;
	}
	/**
	 * @param headings the headings to set
	 */
	public void setHeadings(List<String> headings) {
		this.headings = headings;
	}
	/**
	 * @return the authors
	 */
	public List<String> getAuthors() {
		return authors;
	}
	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	/**
	 * @return the publicationnames
	 */
	public List<String> getPublicationnames() {
		return publicationnames;
	}
	/**
	 * @param publicationnames the publicationnames to set
	 */
	public void setPublicationnames(List<String> publicationnames) {
		this.publicationnames = publicationnames;
	}
	/**
	 * @return the publicationdates
	 */
	public List<String> getPublicationdates() {
		return publicationdates;
	}
	/**
	 * @param publicationdates the publicationdates to set
	 */
	public void setPublicationdates(List<String> publicationdates) {
		this.publicationdates = publicationdates;
	}
	/**
	 * @return the abstracts
	 */
	public List<String> getAbstracts() {
		return abstracts;
	}
	/**
	 * @param abstracts the abstracts to set
	 */
	public void setAbstracts(List<String> abstracts) {
		this.abstracts = abstracts;
	}
	/**
	 * @return the introductions
	 */
	public List<String> getIntroductions() {
		return introductions;
	}
	/**
	 * @param introductions the introductions to set
	 */
	public void setIntroductions(List<String> introductions) {
		this.introductions = introductions;
	}
	/**
	 * @return the conclusions
	 */
	public List<String> getConclusions() {
		return conclusions;
	}
	/**
	 * @param conclusions the conclusions to set
	 */
	public void setConclusions(List<String> conclusions) {
		this.conclusions = conclusions;
	}
	/**
	 * @return the references
	 */
	public List<String> getReferences() {
		return references;
	}
	/**
	 * @param references the references to set
	 */
	public void setReferences(List<String> references) {
		this.references = references;
	}
	/**
	 * @return the fulltext
	 */
	public String getFulltext() {
		return fulltext;
	}
	/**
	 * @param fulltext the fulltext to set
	 */
	public void setFulltext(String fulltext) {
		this.fulltext = fulltext;
	}
	
	public static int existsMatch(List<String> patterns, List<String> texts) {
		int value = 0;
		for(String pattern : patterns) {
			for(String text : texts) {
				List<ITextData> matches = TextMatch.match(pattern, text).getResults();
					value += matches.size();
			}
		}
		return value;
	}
	
	public static int existsMatch(List<String> patterns, String text) {
		List<String> texts = new ArrayList<String>();
		texts.add(text);
		return existsMatch(patterns, texts);
	}
	/**
	 * @return the fileloc
	 */
	public String getFileloc() {
		return fileloc;
	}
	/**
	 * @param fileloc the fileLoc to set
	 */
	public void setFileloc(String fileloc) {
		this.fileloc = fileloc;
	}
	
	
}
