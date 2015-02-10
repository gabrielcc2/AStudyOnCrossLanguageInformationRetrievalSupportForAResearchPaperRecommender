package clir.model;

import java.util.ArrayList;
import java.util.List;

public class ResultsList {
	private List<PaperHit> hits;
	private QueryTerms queryTerms;
	
	public ResultsList(){
		hits=new ArrayList<PaperHit>();
		queryTerms= new QueryTerms();
	}
	
	public List<PaperHit> getPaperHits(){
		return hits;
	}
	
	public Boolean isEmptyPaperHits(){
		return hits.isEmpty();
	}
	
	public void setPaperHits(List<PaperHit> newHits){
		hits.clear();
		hits.addAll(newHits);
	}
	
	public QueryTerms getQueryTerms(){
		return queryTerms;
	}
	
	public void setQueryTerms(QueryTerms terms){
		queryTerms.initialize(terms.getTerms(), terms.getLangs());
	}
	
	public void assign (ResultsList copy){
		hits.clear();
		hits.addAll(copy.getPaperHits());
		queryTerms.initialize(copy.getQueryTerms().getTerms(), copy.getQueryTerms().getLangs());
	}
	
}
