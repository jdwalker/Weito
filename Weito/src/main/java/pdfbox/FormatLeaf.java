package pdfbox;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.util.TextPosition;


public class FormatLeaf extends Format {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2157709196198093402L;

	private String text = "";
	private FormatStyle style;

	public FormatLeaf(String chars, FormatStyle style) {
		this.text = chars;
		this.style = style;
	}
	
	public FormatLeaf(TextPosition textPos) {
		this( textPos.getCharacter(), FormatStyle.getPhraseStyle( textPos.getFont(), textPos.getFontSizeInPt() ) );
	}
	
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the style
	 */
	public List<FormatStyle> getStyles() {
		ArrayList<FormatStyle> styles = new ArrayList<FormatStyle>();
		styles.add(style);
		return styles;
	}

	/* (non-Javadoc)
	 * @see Phrase#getText()
	 */
	@Override
	public String getText() {
		return text;
	}

	/* (non-Javadoc)
	 * @see Phrase#appendText(java.lang.String)
	 */
	@Override
	public void appendText(String moretext) {
		text = text.concat(moretext);
	}
}
