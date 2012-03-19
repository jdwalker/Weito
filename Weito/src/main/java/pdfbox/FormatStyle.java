package pdfbox;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.fontbox.afm.FontMetric;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptorAFM;


public class FormatStyle implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4330874334928575842L;

	private static List<FormatStyle> styles = new ArrayList<FormatStyle>();
	private PDFontDescriptor fontDescriptor = null;
	private float fontSizePt = 0;

	private FormatStyle (PDFont font, float fontSizePt) {
		if(font != null && font.getFontDescriptor() != null) {
			this.fontDescriptor = font.getFontDescriptor();
		} else {
			this.fontDescriptor = new PDFontDescriptorAFM(new FontMetric());
		}
		this.fontSizePt = fontSizePt;
	}
	
	public static FormatStyle getPhraseStyle(PDFont font, float fontSizePt) {
		FormatStyle s = new FormatStyle(font, fontSizePt);
		int lastIndex = styles.lastIndexOf(s);
		if(lastIndex == -1) {
			styles.add(s);
			return s;
		} else {
			return styles.get(lastIndex);
		}
	}

	/**
	 * @return the font descriptor
	 */
	private PDFontDescriptor getFontDescriptor() {
		return fontDescriptor;
	}
	

	
	

	/**
	 * @return the fontSizePt
	 */
	public float getfontSizePt() {
		return fontSizePt;
	}

	public boolean equals(Object obj) {		
		return (this.fontDescriptor == ((FormatStyle) obj).getFontDescriptor()) && (this.fontSizePt == ((FormatStyle) obj).getfontSizePt());
	}

	/**
	 * @return
	 * @see org.apache.pdfbox.pdmodel.font.PDFontDescriptor#isItalic()
	 */
	public boolean isItalic() {
		return fontDescriptor.isItalic() || isNameMatch(".*(?i:italic).*");
	}

	/**
	 * @return
	 * @see org.apache.pdfbox.pdmodel.font.PDFontDescriptor#isAllCap()
	 */
	public boolean isAllCap() {
		return fontDescriptor.isAllCap();
	}

	/**
	 * @return
	 * @see org.apache.pdfbox.pdmodel.font.PDFontDescriptor#isSmallCap()
	 */
	public boolean isSmallCap() {
		return fontDescriptor.isSmallCap();
	}

	/**
	 * @return
	 * @see org.apache.pdfbox.pdmodel.font.PDFontDescriptor#isForceBold()
	 */
	public boolean isBold() {
		return fontDescriptor.isForceBold() || isNameMatch(".*(?i:bold).*");
	}
	
	private boolean isNameMatch(String regex) {
		try {
		return !TextMatch.match(regex, fontDescriptor.getFontName()).getResults().isEmpty();
		} catch (Exception e) {
		return false;
		}
	}

	public String getStyleInfo() {
		String info = "INFO[";
		//info += this.getFontDescriptor().getFontName();
		if( this.isAllCap() ) info += "C";
		if( this.isSmallCap() ) info += "c";
		if( this.isBold() ) info += "B";
		if( this.isItalic() ) info += "I";
		
		info += fontSizePt;
		info += "]";
		return info;
	}
}
