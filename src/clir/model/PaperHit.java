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

public class PaperHit {

	Integer rank;
	String title;
	String authors;
	String summary;
	String url;
	
	Integer year;
	
	Float relevanceScore;
	Integer numOfResults;
	
	public PaperHit(Integer rank, String title, String authors,
	String summary, String url, Integer year,	Float relevanceScore, Integer numOfResults){
			this.rank=rank;
			this.title=title;
			this.authors=authors;
			this.summary=summary;
			this.url=url;
			this.year=year;
			this.relevanceScore=relevanceScore;
			this.numOfResults=numOfResults;
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

	
	public String getAuthors() {
		return authors;
	}
	
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}
	public float getRelevanceScore() {
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


}

