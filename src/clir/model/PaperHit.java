package clir.model;


// TODO: Auto-generated Javadoc
/**
 * 
 * A POJO to represent a paper hit (i.e. a paper returned as a recommendation given a query).
 * It encapsulates the data that must be printed...
 * 
 * Additionally, each element contains the number of results of the given query.
 * 
 * @author Gabriel
 * 
 */

public class PaperHit implements Comparable<PaperHit> {

	/** The rank. */
	private Integer rank;
	
	/** The title. */
	private String title;
	
	/** The url. */
	private String url;
	
	/** The lang. */
	private String lang;
		
	/** The relevance score. */
	private Float relevanceScore;
	
	/** The num of results. */
	private Integer numOfResults;
	
	/**
	 * Instantiates a new paper hit.
	 *
	 * @param rank the rank
	 * @param title the title
	 * @param url the url
	 * @param lang the lang
	 * @param relevanceScore the relevance score
	 * @param numOfResults the num of results
	 */
	public PaperHit(Integer rank, String title, String url, String lang, Float relevanceScore, Integer numOfResults){
			this.rank=rank;
			this.title=title;
			this.url=url;
			this.relevanceScore=relevanceScore;
			this.numOfResults=numOfResults;
			this.lang=lang;
	}
	
	/**
	 * Gets the rank.
	 *
	 * @return the rank
	 */
	public Integer getRank() {
		return rank;
	}

	/**
	 * Sets the rank.
	 *
	 * @param rank the new rank
	 */
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	
	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Sets the lang.
	 *
	 * @param lang the new lang
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	/**
	 * Gets the relevance score.
	 *
	 * @return the relevance score
	 */
	public Float getRelevanceScore() {
		return relevanceScore;
	}

	/**
	 * Sets the relevance score.
	 *
	 * @param relevanceScore the new relevance score
	 */
	public void setRelevanceScore(float relevanceScore) {
		this.relevanceScore = relevanceScore;
	}
	
	/**
	 * Gets the num of results.
	 *
	 * @return the num of results
	 */
	public Integer getNumOfResults() {
		return numOfResults;
	}

	/**
	 * Sets the num of results.
	 *
	 * @param numOfResults the new num of results
	 */
	public void setNumOfResults(Integer numOfResults) {
		this.numOfResults = numOfResults;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PaperHit arg0) {
		return (arg0).getRelevanceScore().compareTo(this.getRelevanceScore());
	}


}

