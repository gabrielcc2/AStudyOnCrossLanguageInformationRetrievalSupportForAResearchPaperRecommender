package clir.model;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class ResultsList consisting of a List of PaperHits next to the queryTerms used for producing them,
 * and a flag indicating if they come from LSI or automatic query translation. Further information could be
 * included here.
 * 
 * @author Gabriel
 */
public class ResultsList {
	
	/** The hits. */
	private List<PaperHit> hits;
	
	/** The query terms. */
	private QueryTerms queryTerms;
	
	/** The used lsi. */
	private Boolean usedLSI;
	
	/**
	 * Instantiates a new results list.
	 */
	public ResultsList(){
		hits=new ArrayList<PaperHit>();
		queryTerms= new QueryTerms();
		usedLSI=false;
	}
	
	/**
	 * Gets the paper hits.
	 *
	 * @return the paper hits
	 */
	public List<PaperHit> getPaperHits(){
		return hits;
	}
	
	
	/**
	 * Sets the paper hits.
	 *
	 * @param newHits the new paper hits
	 */
	public void setPaperHits(List<PaperHit> newHits){
		hits.clear();
		hits.addAll(newHits);
	}
	
	/**
	 * Gets the query terms.
	 *
	 * @return the query terms
	 */
	public QueryTerms getQueryTerms(){
		return queryTerms;
	}
	
	/**
	 * Sets the query terms.
	 *
	 * @param terms the new query terms
	 */
	public void setQueryTerms(QueryTerms terms){
		queryTerms.initialize(terms.getTerms(), terms.getLangs());
	}
	
	/**
	 * Assign.
	 *
	 * @param copy the copy
	 */
	public void assign (ResultsList copy){
		hits.clear();
		hits.addAll(copy.getPaperHits());
		queryTerms.initialize(copy.getQueryTerms().getTerms(), copy.getQueryTerms().getLangs());
		usedLSI=copy.getUsedLSI();
	}
	
	/**
	 * Gets the used lsi.
	 *
	 * @return the used lsi
	 */
	public Boolean getUsedLSI(){
		return usedLSI;
	}
	
	/**
	 * Checks if is empty.
	 *
	 * @return the boolean
	 */
	public Boolean isEmpty(){
		return hits.isEmpty();
	}
	
	/**
	 * Sets the used lsi.
	 *
	 * @param flag the new used lsi
	 */
	public void setUsedLSI(Boolean flag){
		usedLSI=flag;
	}
	
}
