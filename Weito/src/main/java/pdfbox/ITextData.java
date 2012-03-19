package pdfbox;

import java.io.Serializable;

public interface ITextData extends Serializable {

	public abstract int getStartpos();

	public abstract int getEndpos();

	public abstract String getText();

}