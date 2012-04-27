package weito;

import java.util.ArrayList;
import java.util.List;

import drools.AlgorithmContents;

public class RunPapersParameter {
	private static RunPapersParameter instance = new RunPapersParameter();
	private List<String> inputPaperFileLocs;
	private List<AlgorithmContents> drlLocs;
	private ArrayList<Keyword> keywords;

	public RunPapersParameter(List<String> inputPaperFileLocs,
			List<AlgorithmContents> drlLocs, ArrayList<Keyword> keywords) {
		this.inputPaperFileLocs = inputPaperFileLocs;
		this.drlLocs = drlLocs;
		this.keywords = keywords;
	}

	protected RunPapersParameter() {
		this(new ArrayList<String>(), new ArrayList<AlgorithmContents>(), new ArrayList<Keyword>());
	}



	public List<String> getInputPaperFileLocs() {
		return inputPaperFileLocs;
	}

	public List<AlgorithmContents> getDrlLocs() {
		return drlLocs;
	}

	public ArrayList<Keyword> getKeywords() {
		return keywords;
	}

	public static RunPapersParameter getInstance() {
		return instance;
	}
}