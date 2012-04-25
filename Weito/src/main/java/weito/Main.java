package weito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;

import debug.Debug;
import debug.Debug.DebugMode;
import drools.AlgorithmContents;
import drools.AlgorithmContentsFactory;


public class Main {
	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		Debug.setMode(EnumSet.of(DebugMode.FEATURE));
		
		List<String> paperLocs = new ArrayList<String>();
		paperLocs.add("C:/rootfiles/agilestudygeneral/agileconference05/agilestyle-ex2.pdf");
		//FileAccess fa = new FileAccess("pdf", true);
		//paperLocs.addAll( fa.getFilesFromDir("C:/rootfiles/agilestudygeneral/agileconference05") );
		//System.out.println("No of files: "+result.size());
		
		List<AlgorithmContents> drlLocs = new ArrayList<AlgorithmContents>();
		AlgorithmContentsFactory f = new AlgorithmContentsFactory();
		drlLocs.add( f.forDRLfile("basicformatalgorithm.drl") );
		drlLocs.add( f.forRFfile( "formatalgorithm.rf" ) );
		drlLocs.add( f.forDRLfile("conferencestyle.drl") );
		
		drlLocs.add( f.forDRLfile("basiccatalgorithm.drl") );
		drlLocs.add( f.forRFfile( "categoryalgorithm.rf" ) );
		
		if( Debug.getMode().contains(DebugMode.DROOLSSTAGEENTER) ) drlLocs.add( f.forDRLfile("debug.drl") );
		
		ArrayList<String> keywords = new ArrayList<String>(Arrays.asList( new String[]{"agile","extreme programming","xp","scrum",
				"dsdm","fdd","software","feature","driven","development","lean","empirical","study","result"} ));
		
		try {
		Backend.runPapers(new RunPapersParameter(paperLocs, drlLocs, keywords));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
