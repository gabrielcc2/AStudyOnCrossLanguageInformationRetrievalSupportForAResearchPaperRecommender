package clir.control.mgmt;

import java.util.List;

import clir.control.query.CrossLanguageQueryHandler;
import clir.control.query.LSAQueryHandler;
import clir.control.query.PerLanguageQueryHandler;
import clir.control.querytermgen.QueryTermsGenerator;
import clir.model.QueryTerms;
import clir.model.ResultsList;

// TODO: Auto-generated Javadoc
/**
 * RecommendationsHandler class: Manager for the recommendations. Implemented as a singleton.
 * 
 *  * @author Gabriel
 *  
 * */
public class RecommendationsHandler {
	
	/** The verbose. */
	@SuppressWarnings("unused")
	private Boolean VERBOSE=true;
	
	/** The debug. */
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
		
	/** The query terms generator. */
	private QueryTermsGenerator queryTermsGenerator;
	
	/** Singleton instance of type recommendationsHandler. */
	private static RecommendationsHandler instance= null;
	
	/**
	 * Functions.
	 */
	
	/**Protected constructor function, to defeat instantiation. */
	protected RecommendationsHandler(){
		 // Exists only to defeat instantiation.
		queryTermsGenerator=new QueryTermsGenerator();
	}
	
	/**
	 * getInstance function, for singleton use.
	 *
	 * @return single instance of RecommendationsHandler
	 */
	public static RecommendationsHandler getInstance() {
	      if(instance == null) {
	    	  instance = new RecommendationsHandler();
	      }
	      return instance;
	}
	
	/**
	 * Sets the language query folder.
	 *
	 * @param folder the folder
	 * @param lang the lang
	 */
	public void setLanguageQueryFolder (String folder, String lang){
		queryTermsGenerator.setLanguageFolder(folder, lang);
	}
	
	/**
	 * Gets the recommendations.
	 *
	 * @param queryLanguages the query languages
	 * @param expectedLanguages the expected languages
	 * @param folder the folder
	 * @param numExpectedResults the num expected results
	 * @param preProcessingOption the pre processing option
	 * @param translationOption the translation option
	 * @param postProcessingOption the post processing option
	 * @param combiningOption the combining option
	 * @return the recommendations
	 */
	public ResultsList getRecommendations(List<String> queryLanguages, List<String> expectedLanguages, String folder, int numExpectedResults, Boolean preProcessingOption, 
			String translationOption, Boolean postProcessingOption, Boolean combiningOption){

		ResultsList results = new ResultsList();
		QueryTerms terms= queryTermsGenerator.generateQueryTerms(queryLanguages, folder);
		
		results.setQueryTerms(terms);
		if (queryLanguages.size()==1 && expectedLanguages.size()==1 && queryLanguages.get(0)==expectedLanguages.get(0)){
			String chosenLanguage= queryLanguages.get(0);
			PerLanguageQueryHandler plqh = new PerLanguageQueryHandler(chosenLanguage, numExpectedResults);
			results.setPaperHits((plqh.runQuery(terms.getTermsOfLang(chosenLanguage))).getPaperHits());
			return results;
		}
		results.assign(CrossLanguageQueryHandler.getInstance().runQuery(expectedLanguages, terms, numExpectedResults, preProcessingOption,translationOption, postProcessingOption, combiningOption));
		return results;
	}
	
	/**
	 * Gets the recommendations lsa.
	 *
	 * @param queryLanguages the query languages
	 * @param expectedLanguages the expected languages
	 * @param folder the folder
	 * @param numExpectedResults the num expected results
	 * @return the recommendations lsa
	 */
	public ResultsList getRecommendationsLSA(List<String> queryLanguages, List<String> expectedLanguages, String folder, int numExpectedResults){
		
		ResultsList results = new ResultsList();
		
		QueryTerms qTerms = queryTermsGenerator.generateQueryTerms(queryLanguages, folder);
		String terms=qTerms.getAllTermsInOneString();
		results.setQueryTerms(qTerms);
		
		//Call LSA Query Handler
		results.setPaperHits(LSAQueryHandler.getInstance().runQuery(expectedLanguages, terms, numExpectedResults));
		results.setUsedLSI(true);
		return results;
	}
	
	/**
	 * Refine query.
	 *
	 * @param postProcessedQueries the post processed queries
	 * @param numExpectedResults the num expected results
	 * @param combiningOption the combining option
	 * @return the results list
	 */
	public ResultsList refineQuery(QueryTerms postProcessedQueries, int numExpectedResults, Boolean combiningOption){
		return CrossLanguageQueryHandler.getInstance().refineQuery(postProcessedQueries, numExpectedResults, combiningOption);
	}
}
