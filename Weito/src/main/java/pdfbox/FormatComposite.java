package pdfbox;
import java.util.ArrayList;
import java.util.List;

public class FormatComposite extends Format {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2878877089444433343L;
	
	private List<Format> phraseComponents;
	
	public FormatComposite(List<Format> phrases) {
		phraseComponents = phrases;
		makeParent();
	}
	
	private void makeParent() {
		for(Format p: phraseComponents) {
			p.setParent(this);
		}
	}

	public FormatComposite(Format phrase) {
		phraseComponents = new ArrayList<Format>();
		phraseComponents.add(phrase);
		makeParent();
	}

	
	/* (non-Javadoc)
	 * @see Phrase#add(Phrase)
	 */
	@Override
	public void add(Format p) {
		phraseComponents.add(p);
		p.setParent(p);
		}

	/* (non-Javadoc)
	 * @see Phrase#remove(Phrase)
	 */
	@Override
	public void remove(Format p) {
		phraseComponents.remove(p);
	}

	/* (non-Javadoc)
	 * @see Phrase#getChild(int)
	 */
	@Override
	public void getChild(int i) {
		phraseComponents.get(i);
	}

	/* (non-Javadoc)
	 * @see Phrase#getText()
	 */
	@Override
	public String getText() {
		String text = "";
		for(Format p: phraseComponents) {
			text = text.concat(p.getText());
		}
		return text;
	}

	/* (non-Javadoc)
	 * @see Phrase#appendText(java.lang.String)
	 */
	@Override
	public void appendText(String moretext) {
		int lastIndex = phraseComponents.size();
		Format s = phraseComponents.get(lastIndex);
		s.appendText(moretext);
	}

	/* (non-Javadoc)
	 * @see Phrase#formattedAs(Phrase)
	 */
	@Override
	public boolean formattedAs(Format p) {
		return p.getStyles() == this.getStyles();
	}

	/* (non-Javadoc)
	 * @see Phrase#getStyles()
	 */
	@Override
	public List<FormatStyle> getStyles() {
		ArrayList<FormatStyle> styles = new ArrayList<FormatStyle>();
		for(Format p: phraseComponents) {
			styles.addAll(p.getStyles());
		}
		return styles;
	}


	/* (non-Javadoc)
	 * @see Phrase#getLastPhrase()
	 */
	@Override
	public Format getLastPhrase() {
		return phraseComponents.get(phraseComponents.size() - 1).getLastPhrase();
	}

}
