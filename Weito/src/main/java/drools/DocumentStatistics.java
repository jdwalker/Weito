package drools;

import java.io.Serializable;

public class DocumentStatistics implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3198405170168727035L;
	
	double averagefontsize = 0;
	String fulltext = "";


	public DocumentStatistics(double averageFontSize, String fullText) {
		super();
		this.averagefontsize = averageFontSize;
		this.fulltext = fullText;
	}

	/**
	 * @return the averageFontSize
	 */
	public double getAveragefontsize() {
		return averagefontsize;
	}

	/**
	 * @param averageFontSize the averageFontSize to set
	 */
	public void setAveragefontsize(double averageFontSize) {
		this.averagefontsize = averageFontSize;
	}

	/**
	 * @return the full text
	 */
	public String getFulltext() {
		return fulltext;
	}
	
	/**
	 * @param charIndex - specified char index of full text
	 * @return the line index that the char index is in
	 */
	public int getLineIndex(int charIndex) {
		int lineNo = 0;
		int i = 0;
		for(char c : fulltext.toCharArray()) {
			if(i == charIndex) break;
			if(c == '\n') lineNo++;
			i++;
		}
		return lineNo;
	}
	
}
