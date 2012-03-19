package pdfbox;

public class TextData implements ITextData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	int endpos;
	int startpos;
	String text;
	
	
	/**
	 * @param startpos
	 * @param endpos
	 * @param text
	 */
	public TextData(int startpos, int endpos, String text) {
		this.startpos = startpos;
		this.endpos = endpos;
		this.text = text;
	}
	public int getEndpos() {
		return endpos;
	}
	public int getStartpos() {
		return startpos;
	}
	public String getText() {
		return text;
	}

	

}
