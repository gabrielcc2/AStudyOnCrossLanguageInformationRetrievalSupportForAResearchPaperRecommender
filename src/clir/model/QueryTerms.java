package clir.model;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * 
 * A POJO to represent the query terms (i.e. a string of terms next to its language).
 * It can be transformed to any 2 representations: A LSA query representation or
 * a per language representation.
 * 
 * 
 * @author Gabriel
 * 
 */

public class QueryTerms {

	/** The terms. */
	List<String> terms = new ArrayList<String>();
	
	/** The langs. */
	List<String> langs = new ArrayList<String>();
	
	/** The delimiter. */
	String DELIMITER=" ";
	
	/**
	 * Instantiates a new query terms.
	 */
	public QueryTerms (){	
	}
	
	/**
	 * Instantiates a new query terms.
	 *
	 * @param terms the terms
	 * @param langs the langs
	 */
	public QueryTerms (List<String> terms, List<String> langs){
		this.terms.addAll(terms);
		this.langs.addAll(langs);
	}
	
	/**
	 * Gets the langs.
	 *
	 * @return the langs
	 */
	public List<String> getLangs(){
		return langs;
	}
	
	/**
	 * Gets the terms.
	 *
	 * @return the terms
	 */
	public List<String> getTerms(){
		return terms;
	}
	
	/**
	 * Gets the terms of lang.
	 *
	 * @param lang the lang
	 * @return the terms of lang
	 */
	public String getTermsOfLang(String lang){
		String result=" ";
		for (int i=0; i<langs.size(); i++){
			if (langs.get(i).equals(lang)){
				result=terms.get(i);
				return result;
			}
		}
		return result;
	}
	
	/**
	 * Gets the all terms in one string.
	 *
	 * @return the all terms in one string
	 */
	public String getAllTermsInOneString(){
		String result="";
		for (int i=0; i<terms.size(); i++){
				result+=terms.get(i)+DELIMITER;
		}
		return result;
	}
	
	/**
	 * Adds the query term.
	 *
	 * @param term the term
	 * @param lang the lang
	 */
	public void addQueryTerm(String term, String lang){
		if (langs.contains(lang)){
			int removePos=-1;
			String originalTerms="";
			for (int i=0; i<langs.size(); i++){
				if (langs.get(i).equals(lang)){
					removePos=i;
					originalTerms=terms.get(i);
					i=langs.size(); //break
				}
			}
			langs.remove(removePos);
			terms.remove(removePos);
			langs.add(lang);
			terms.add(originalTerms+DELIMITER+term);
		}
		else{
			terms.add(term);
			langs.add(lang);
		}
	}
	
	/**
	 * Sets the query term.
	 *
	 * @param term the term
	 * @param lang the lang
	 */
	public void setQueryTerm(String term, String lang){
		if (langs.contains(lang)){
			int removePos=-1;
			for (int i=0; i<langs.size(); i++){
				if (langs.get(i).equals(lang)){
					removePos=i;
					i=langs.size(); //break
				}
			}
			langs.remove(removePos);
			terms.remove(removePos);
			langs.add(lang);
			terms.add(term);
		}
		else{
			terms.add(term);
			langs.add(lang);
		}
	}
	
	/**
	 * Initialize.
	 *
	 * @param terms the terms
	 * @param langs the langs
	 */
	public void initialize (List<String> terms, List<String> langs){
		this.reset();
		this.terms.addAll(terms);
		this.langs.addAll(langs);
	}
	
	/**
	 * Reset.
	 */
	public void reset(){
		if (!this.terms.isEmpty()){
			this.terms.clear();
		}
		if (!this.langs.isEmpty()){
			this.langs.clear();
		}
	}
}
