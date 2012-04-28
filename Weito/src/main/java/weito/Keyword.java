package weito;

public class Keyword {
	String keyword;

	/**
	 * @param keyword
	 */
	public Keyword(String keyword) {
		this.keyword = keyword;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return keyword;
	}
}
