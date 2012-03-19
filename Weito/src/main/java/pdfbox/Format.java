package pdfbox;

import java.util.List;

public abstract class Format implements IFormatData  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5075454024981242279L;
	
	Format parent = null;
	private int startPos = 0;

	public void add(Format p) {
		throw new UnsupportedOperationException();
	}

	public void remove(Format p) {
		throw new UnsupportedOperationException();
	}

	public void getChild(int i) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<FormatStyle> getStyles() {
		throw new UnsupportedOperationException();
	}

	public String getText() {
		throw new UnsupportedOperationException();
	}

	public void setText(String text) {
		throw new UnsupportedOperationException();
	}

	public Format getLastPhrase() {
		return this;
	}

	@Override
	public FormatStyle getPrimarystyle() {
		return this.getStyles().get(0);
	}

	public void appendText(String moretext) {
		throw new UnsupportedOperationException();
	}

	public boolean formattedAs(Format p) {
		return p.getPrimarystyle() == this.getPrimarystyle();
	}

	/**
	 * @return the parent
	 */
	public Format getParent() {
		return parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Format parent) {
		this.parent = parent;
	}

	/* (non-Javadoc)
	 * @see pdfbox.FormatData#getStartPos()
	 */
	@Override
	public int getStartpos() {
		return startPos;
	}

	/* (non-Javadoc)
	 * @see pdfbox.FormatData#getEndPos()
	 */
	@Override
	public int getEndpos() {
		return startPos + getText().length() -1;
	}

	/**
	 * @param startPos the startPos to set
	 */
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}
}
