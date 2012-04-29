package drools;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.decisiontable.ExternalSpreadsheetCompiler;
import org.drools.io.ResourceFactory;
import org.drools.io.impl.ByteArrayResource;

public class AlgorithmContentsFactory {
	DecisionTableConfiguration conf;
	final ExternalSpreadsheetCompiler converter;

   public AlgorithmContentsFactory() {
	   conf = KnowledgeBuilderFactory.newDecisionTableConfiguration();
	   conf.setInputType(DecisionTableInputType.XLS);
	   converter = new ExternalSpreadsheetCompiler();
	   }
	
	public AlgorithmContents forDRLfile(String drlfileloc) {
		return new AlgorithmContents(  ResourceFactory.newFileResource(new File(drlfileloc)), ResourceType.DRL);
	}
	
	public AlgorithmContents forRFfile(String rffileloc) {
		return new AlgorithmContents(  ResourceFactory.newClassPathResource(rffileloc), ResourceType.DRF);
	}
	
	public AlgorithmContents forDRTfile(String drtfileloc, String xlsfilesloc) {
		try {
			return forDRTfileError(drtfileloc, xlsfilesloc);
		} catch (IOException e) {
			throw new IllegalArgumentException("Could not read spreadsheet or rules stream." ,e );
		}
	}

	private AlgorithmContents forDRTfileError(String drtfileloc,String xlsfilesloc) throws IOException {
		InputStream drtStream = ResourceFactory.newFileResource(new File( drtfileloc) ).getInputStream();
		InputStream xlsStream = ResourceFactory.newFileResource(new File( xlsfilesloc) ).getInputStream(); 
		String drl = converter.compile(xlsStream, drtStream, 2, 2); //Data always starts at B2;
		return new AlgorithmContents( new ByteArrayResource( drl.getBytes() ) , ResourceType.DRL);
	}

	public AlgorithmContents forDRLlocalfile(String string) {
		return new AlgorithmContents(ResourceFactory.newClassPathResource(string), ResourceType.DRL);
	}	
}
