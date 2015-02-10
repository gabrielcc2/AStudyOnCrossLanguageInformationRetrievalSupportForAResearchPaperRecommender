package clir.model;

import java.util.ArrayList;
import java.util.List;

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

	List<String> terms = new ArrayList<String>();
	List<String> langs = new ArrayList<String>();
	String DELIMITER=" ";
	
	public QueryTerms (){	
	}
	
	public QueryTerms (List<String> terms, List<String> langs){
		this.terms.addAll(terms);
		this.langs.addAll(langs);
	}
	
	public List<String> getLangs(){
		return langs;
	}
	
	public List<String> getTerms(){
		return terms;
	}
	
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
	
	public String getAllTermsInOneString(){
		String result="";
		for (int i=0; i<terms.size(); i++){
				result+=terms.get(i)+DELIMITER;
		}
		return result;
	}
	
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
	
	public void reset(){
		this.terms.clear();
		this.langs.clear();
	}
}
