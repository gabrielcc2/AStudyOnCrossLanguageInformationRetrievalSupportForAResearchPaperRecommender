package clir.model;


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

	Integer rank;
	String title;
	String url;
	String lang;
		
	Float relevanceScore;
	Integer numOfResults;
	
	public PaperHit(Integer rank, String title, String url, String lang, Float relevanceScore, Integer numOfResults){
			this.rank=rank;
			this.title=title;
			this.url=url;
			this.relevanceScore=relevanceScore;
			this.numOfResults=numOfResults;
			this.lang=lang;
	}
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public Float getRelevanceScore() {
		return relevanceScore;
	}

	public void setRelevanceScore(float relevanceScore) {
		this.relevanceScore = relevanceScore;
	}
	
	public Integer getNumOfResults() {
		return numOfResults;
	}

	public void setNumOfResults(Integer numOfResults) {
		this.numOfResults = numOfResults;
	}

	@Override
	public int compareTo(PaperHit arg0) {
		return (arg0).getRelevanceScore().compareTo(this.getRelevanceScore());
	}


}

