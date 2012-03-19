package weito;

public class OutputPaper {
	String papertitle;
	String paperfilelocation;
	PaperTypes category;
	
	/**
	 * @param papertitle
	 * @param paperfilelocation
	 * @param category
	 */
	public OutputPaper(String paperTitle, String paperFileLocation,
			PaperTypes category) {
		this.papertitle = paperTitle;
		this.paperfilelocation = paperFileLocation;
		this.category = category;
	}


	/**
	 * @return the papertitle
	 */
	public String getPapertitle() {
		return papertitle;
	}


	/**
	 * @return the paperfilelocation
	 */
	public String getPaperfilelocation() {
		return paperfilelocation;
	}


	/**
	 * @return the category
	 */
	public PaperTypes getCategory() {
		return category;
	}
}
