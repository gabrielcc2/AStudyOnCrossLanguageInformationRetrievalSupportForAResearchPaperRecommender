package clir.control;
import java.util.ArrayList;
import java.util.List;

import clir.model.PaperHit;

public class RecommendationsHandler {

	/**Singleton instance of type recommendationsHandler */
	private static RecommendationsHandler instance= null;

	private String queryFolder;
	private String DEFAULT_QUERY_FOLDER="default_query_folder";
	private List<String> queryLanguages;
	private Boolean usesLSI;	
	
	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected RecommendationsHandler(){
		 // Exists only to defeat instantiation.
		queryLanguages= new ArrayList<String>();
		usesLSI=false;	
		queryFolder=DEFAULT_QUERY_FOLDER;
	}
	
	/**getInstance function, for singleton use*/
	public static RecommendationsHandler getInstance() {
	      if(instance == null) {
	    	  instance = new RecommendationsHandler();
	      }
	      return instance;
	}
	
	String getQueryFolder(){
		return queryFolder;
	}
	
	void setQueryFolder(String queryFolder){
		this.queryFolder=queryFolder;
	}
	
	void resetQueryFolder(){
		this.queryFolder=DEFAULT_QUERY_FOLDER;
	}

	List<String> getQueryLanguages(){
		return queryLanguages;
	}
	
	void resetQueryLanguages(){
		queryLanguages.clear();
	}
	
	void addQueryLanguage(String queryLang){
		queryLanguages.add(queryLang);
	}
	
	Boolean getUsesLSI(){
		return usesLSI;
	}
	
	void setUsesLSI(Boolean usesLSI){
		this.usesLSI=usesLSI;
	}
	
	List<PaperHit> getRecommendations(){
		List<PaperHit> results = new ArrayList<PaperHit>();

		boolean englishUsed=queryLanguages.contains("EN");
		boolean germanUsed=queryLanguages.contains("DE");
		boolean spanishUsed=queryLanguages.contains("ES");
		
		if (!englishUsed&&!germanUsed&&!spanishUsed){
			return results; //No language configured for the query
		}
		
		
		/**Here we will assume that there exist indexes of the given languages and that the query
		 * parameters are configured**/

		
		
		/**The recommender begins by opening the queryFolder and navigating towards each subfolder 
		 * of the specified languages. 
		 * 
		 * There it extracts data and adds them to a string of each type of language.**/
		
		/**Now we have a query term per language.
		 *
		 *  If using language model: 
		 	* 	We need to expand it by translating from other languages.
		 	* 	First we define cross language vectors and call the language handler
		 	* 	to deal with the translations, it returns the translations, we add them to the query terms.
		 	* 
		 	* Then we clean the query terms somehow.
		 	* Then we perform a query in each language...
		 	* And then we merge the results of the queries
		 *  
		 * If using LSI:
		 	* We turn all query terms to whatever LSI representation we have...
		 	* We sum up the partial LSI in folders, and then we search over this...
		 	* And we return the results assigning a relevance score...**/
		return results;
	}

}
