package pdfbox;

import java.io.File;
import java.util.List;

import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.PDDocument;

public class Extraction {

	public static List<Format> extract(String fileLoc) throws Exception {
		PDDocument document = null;
		 try {
            document = PDDocument.load(new File(fileLoc));
            if( document.isEncrypted() )
            {
                try
                {
                    document.decrypt( "" );
                }
                catch( InvalidPasswordException e )
                {
                    throw new Exception( "Error: Document is encrypted with a password." );
                } 
            }
           PDFTextWithFormatExtractor ext = new PDFTextWithFormatExtractor();
           ext.getText(document);
           return ext.getDocumentFormatChunks();
        }
        finally
        {
            if( document != null )
            {
                document.close();
            }
        }
		
	}
}
