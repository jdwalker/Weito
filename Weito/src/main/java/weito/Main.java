package weito;

import java.util.ArrayList;
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
		paperLocs.add("C:/rootfiles/agilestudygeneral/agileconference05/agilestyle.pdf");
		paperLocs.add("C:/rootfiles/agilestudygeneral/agileconference05/agilestyle-ex2.pdf");
		paperLocs.add("C:/rootfiles/agilestudygeneral/agileconference05/agilestyle-ex3.pdf");
		paperLocs.add("C:/rootfiles/agilestudygeneral/agileconference05/agilestyle-ex4.pdf");
		//FileAccess fa = new FileAccess("pdf", true);
		//paperLocs.addAll( fa.getFilesFromDir("C:/rootfiles/agilestudygeneral/agileconference05") );
		//System.out.println("No of files: "+result.size());
		
		List<AlgorithmContents> drlLocs = new ArrayList<AlgorithmContents>();
		AlgorithmContentsFactory f = new AlgorithmContentsFactory();
		drlLocs.add( f.forDRLlocalfile("basicformatalgorithm.drl") );
		drlLocs.add( f.forRFfile( "formatalgorithm.rf" ) );
		drlLocs.add( f.forDRLlocalfile("conferencestyle.drl") );
		
		drlLocs.add( f.forDRLlocalfile("basiccatalgorithm.drl") );
		drlLocs.add( f.forRFfile( "categoryalgorithm.rf" ) );
		
		if( Debug.getMode().contains(DebugMode.DROOLSSTAGEENTER) ) drlLocs.add( f.forDRLfile("debug.drl") );
		
		ArrayList<Keyword> keywords = new ArrayList<Keyword>();
		keywords.add(new Keyword("test"));
		
		try {
		Backend.runPapers(new RunPapersParameter(paperLocs, drlLocs, keywords));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
