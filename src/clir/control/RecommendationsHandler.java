package clir.control;

import java.util.ArrayList;
import java.util.List;

import clir.model.PaperHit;
import clir.model.QueryTerms;

/**
 * Guarantees that all languages are in upper case.
 * 
 * */
public class RecommendationsHandler {
	@SuppressWarnings("unused")
	private Boolean VERBOSE=true;
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
		
	private QueryTermsGenerator queryTermsGenerator;
	/**Singleton instance of type recommendationsHandler */
	private static RecommendationsHandler instance= null;
	
	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected RecommendationsHandler(){
		 // Exists only to defeat instantiation.
		queryTermsGenerator=new QueryTermsGenerator();
	}
	
	/**getInstance function, for singleton use*/
	public static RecommendationsHandler getInstance() {
	      if(instance == null) {
	    	  instance = new RecommendationsHandler();
	      }
	      return instance;
	}
	
	void setLanguageQueryFolder (String folder, String lang){
		queryTermsGenerator.setLanguageFolder(folder, lang);
	}
	
	List<PaperHit> getRecommendations(List<String> queryLanguages, int numExpectedResults, Boolean preProcessingOption, 
			String translationOption, Boolean postProcessingOption, Boolean combiningOption){

		List<PaperHit> results = new ArrayList<PaperHit>();
		QueryTerms terms = queryTermsGenerator.generateQueryTerms(queryLanguages);
		if (queryLanguages.size()==1){
			String chosenLanguage= queryLanguages.get(0);
			PerLanguageQueryHandler plqh = new PerLanguageQueryHandler(chosenLanguage, numExpectedResults);
			results.addAll(plqh.runQuery(terms.getTermsOfLang(chosenLanguage)));
			return results;
		}
		results.addAll(CrossLanguageQueryHandler.getInstance().runQuery(terms, numExpectedResults, preProcessingOption,translationOption, postProcessingOption, combiningOption));
		return results;
	}
	
	List<PaperHit> refineQuery(QueryTerms postProcessedQueries, int numExpectedResults, Boolean combiningOption){
		return CrossLanguageQueryHandler.getInstance().refineQuery(postProcessedQueries, numExpectedResults, combiningOption);
	}
}
