package clir.control.mgmt;

import java.util.List;

import clir.control.query.CrossLanguageQueryHandler;
import clir.control.query.PerLanguageQueryHandler;
import clir.control.querytermgen.QueryTermsGenerator;
import clir.model.QueryTerms;
import clir.model.ResultsList;

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
	
	public void setLanguageQueryFolder (String folder, String lang){
		queryTermsGenerator.setLanguageFolder(folder, lang);
	}
	
	public ResultsList getRecommendations(List<String> queryLanguages, int numExpectedResults, Boolean preProcessingOption, 
			String translationOption, Boolean postProcessingOption, Boolean combiningOption){

		ResultsList results = new ResultsList();
		QueryTerms terms = queryTermsGenerator.generateQueryTerms(queryLanguages);
		results.setQueryTerms(terms);
		if (queryLanguages.size()==1){
			String chosenLanguage= queryLanguages.get(0);
			PerLanguageQueryHandler plqh = new PerLanguageQueryHandler(chosenLanguage, numExpectedResults);
			results.setPaperHits((plqh.runQuery(terms.getTermsOfLang(chosenLanguage))).getPaperHits());
			return results;
		}
		results.assign(CrossLanguageQueryHandler.getInstance().runQuery(terms, numExpectedResults, preProcessingOption,translationOption, postProcessingOption, combiningOption));
		return results;
	}
	
	public ResultsList refineQuery(QueryTerms postProcessedQueries, int numExpectedResults, Boolean combiningOption){
		return CrossLanguageQueryHandler.getInstance().refineQuery(postProcessedQueries, numExpectedResults, combiningOption);
	}
}
