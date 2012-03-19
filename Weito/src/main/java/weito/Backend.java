package weito;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pdfbox.Extraction;
import pdfbox.Format;
import debug.Debug;
import debug.Debug.DebugMode;
import debug.Printer;
import drools.AlgorithmContents;
import drools.Analysis;

public class Backend {

	public static List<OutputPaper> runPapers(List<String> inputPaperFileLocs, List<AlgorithmContents> drlLocs, List<String> keywords ) throws Exception {
		Analysis algorithm = new Analysis(drlLocs, keywords);
		
		List<OutputPaper> output = new ArrayList<OutputPaper>();
		
		for(String inputLocation : inputPaperFileLocs) {
				OutputPaper paper = runPaper(algorithm, inputLocation);
				output.add(paper);
		}
		
		return output;
	}

	/**
	 * @param algorithm
	 * @param inputLocation
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	private static OutputPaper runPaper(Analysis algorithm, String inputLocation) throws Exception {
		OutputPaper paper = null;
		Printer.getInstance().startNewPaper();
		List<Format> results = Extraction.extract( inputLocation );
		
		if(Debug.getMode().contains(DebugMode.FORMAT)) Printer.getInstance().printFormatResults(results, inputLocation);
		
		paper = algorithm.analyse( results, inputLocation );
		return paper;
	} 

}
