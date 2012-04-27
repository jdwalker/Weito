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

	/**
	 * @deprecated Use {@link #runPapers(RunPapersParameter)} instead
	 */
	public static List<OutputPaper> runPapers(List<String> inputPaperFileLocs, List<AlgorithmContents> drlLocs, ArrayList<Keyword> keywords ) throws Exception {
		return runPapers(new RunPapersParameter(inputPaperFileLocs, drlLocs,
				keywords));
	}

	public static List<OutputPaper> runPapers(RunPapersParameter parameterObject ) throws Exception {
		Analysis algorithm = new Analysis(parameterObject.getDrlLocs(), parameterObject.getKeywords());
		
		List<OutputPaper> output = new ArrayList<OutputPaper>();
		
		for(String inputLocation : parameterObject.getInputPaperFileLocs()) {
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
