package pdfbox;

import java.util.List;

public interface IFormatData extends ITextData {

	public abstract List<FormatStyle> getStyles();

	public abstract FormatStyle getPrimarystyle();

}