package clir.control.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clir.control.utils.TranslationHandler;
import clir.model.PaperHit;
import clir.model.QueryTerms;
import clir.model.ResultsList;

public class CrossLanguageQueryHandler extends QueryHandler{
	
	/**Singleton instance of type CrossLanguageQueryHandler */
	private static CrossLanguageQueryHandler handler = null;
	
	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected CrossLanguageQueryHandler(){
		 // Exists only to defeat instantiation.
		numberOfResults=DEFAULT_NUMBER_OF_RESULTS;
	}
	
	/**getInstance function, for singleton use*/
	public static CrossLanguageQueryHandler getInstance(){
		if (handler==null){
			handler= new CrossLanguageQueryHandler();
		}
		return handler;
	}
	
	private QueryTerms preProcess (QueryTerms query, Boolean preProcessingOption){
		List<String> langs= new ArrayList<String>();
		langs.addAll(query.getLangs());
		for (int i=0; i<langs.size(); i++){
			String lang=langs.get(i);
			if (lang.equals("DE")){
				//Pre-process
			}
			else if (lang.equals("EN")){
				//Pre-process
			}
			else if (lang.equals("ES")){
				//Pre-process
			}
		}
		QueryTerms improvedTerms = new QueryTerms(query.getTerms(), query.getLangs());
		return improvedTerms;
	}

	private QueryTerms postProcess (QueryTerms query, Boolean preProcessingOption){
		List<String> langs= new ArrayList<String>();
		langs.addAll(query.getLangs());
		for (int i=0; i<langs.size(); i++){
			String lang=langs.get(i);
			if (lang.equals("DE")){
				//Pre-process
			}
			else if (lang.equals("EN")){
				//Pre-process
			}
			else if (lang.equals("ES")){
				//Pre-process
			}
		}
		QueryTerms improvedTerms = new QueryTerms(query.getTerms(), query.getLangs());
		return improvedTerms;
	}
	private QueryTerms getTranslations (QueryTerms query, String translationOption){
		QueryTerms translatedTerms = new QueryTerms(query.getTerms(), query.getLangs());
		for (int i=0; i<query.getLangs().size(); i++){
			for (int j=0; j<query.getLangs().size(); j++){//We are translating j to the language i
				if (i!=j){
					String sourceText=query.getTerms().get(j);
					if (sourceText.length()>1){
						String sourceLanguage=query.getLangs().get(j);
						String targetLanguage=query.getLangs().get(i);
						String translation=TranslationHandler.getInstance().translate(translationOption, sourceText, sourceLanguage, targetLanguage);
						translatedTerms.addQueryTerm(translation, query.getLangs().get(i));
					}
				}
			}
		}
		if (VERBOSE){
			for (int i=0; i<translatedTerms.getLangs().size(); i++){
				System.out.println("Translated query "+i+"- Lang: "+
						translatedTerms.getLangs().get(i)+" Query: "+
						translatedTerms.getTerms().get(i));
			}
		}
		return translatedTerms;
	}
	
	public List<PaperHit> combineResults(ResultsList[] partialHits, int size, Boolean combiningOption){
		List<PaperHit> combinedResults= new ArrayList<PaperHit>();
		int numberOfHits= 0;
		for (int i=0; i<size; i++){
			if (!partialHits[i].hits.isEmpty()){
				combinedResults.addAll(partialHits[i].hits);
				numberOfHits+=partialHits[i].hits.get(0).getNumOfResults();
			}
		}
		for (int i=0; i<combinedResults.size(); i++){
			combinedResults.get(i).setNumOfResults(numberOfHits);
		}
		Collections.sort(combinedResults);
		for (int i=0; i<combinedResults.size(); i++){
			combinedResults.get(i).setRank(i+1);
		}
		if (combinedResults.size()<numberOfResults){
			return combinedResults;
		}
		return combinedResults.subList(0, numberOfResults);
	}
	public List<PaperHit> runQuery(QueryTerms query, int numExpectedResults, Boolean preProcessingOption, String translationOption, Boolean postProcessingOption, Boolean combiningOption){
		List<PaperHit> results= new ArrayList<PaperHit>();
		
		/**This processing is completed in 5 steps:*/
		
		/**First step: Pre-processing for each language*/
		QueryTerms preProcessedQuery=preProcess(query, preProcessingOption);
		
		/**Second step: Translation for each language pair*/
		QueryTerms translatedQueries=getTranslations(preProcessedQuery, translationOption);
		
		/**Third step: Post-processing... */
		QueryTerms postProcessedQueries=postProcess(translatedQueries, postProcessingOption);
		
		//Query refinement aims to re-run from this stage...
		/**Forth step: Searching in every index*/
		int numLangs=postProcessedQueries.getLangs().size();
		ResultsList [] partialHits= new ResultsList[numLangs];
		for (int i=0; i<numLangs; i++){
			String currentLanguage=postProcessedQueries.getLangs().get(i);
			PerLanguageQueryHandler agent= new PerLanguageQueryHandler(currentLanguage, numExpectedResults);
			String q= postProcessedQueries.getTerms().get(i);
			partialHits[i]= new ResultsList();
			if (q.length()>1){
					partialHits[i].hits.addAll(agent.runQuery(q));
					if (DEBUG){
						System.out.println("Hits for: "+currentLanguage);
						if(partialHits[i].hits.size()>=1){
							for (int k=0; k<partialHits[i].hits.size(); k++){
								System.out.println(k+": "+partialHits[i].hits.get(k).getUrl());
							}
						}
					}
			}
		}

		/**Fifth step: Combining the results*/
		results.addAll(combineResults(partialHits, numLangs, combiningOption));
		return results;
	}

	public List<PaperHit> refineQuery(QueryTerms postProcessedQueries, int numExpectedResults, boolean combiningOption){
		List<PaperHit> results= new ArrayList<PaperHit>();
		int numLangs=postProcessedQueries.getLangs().size();
		ResultsList [] partialHits= new ResultsList[numLangs];
		for (int i=0; i<numLangs; i++){
			String currentLanguage=postProcessedQueries.getLangs().get(i);
			PerLanguageQueryHandler agent= new PerLanguageQueryHandler(currentLanguage, numExpectedResults);
			partialHits[i].hits.addAll(agent.runQuery(postProcessedQueries.getTermsOfLang(currentLanguage)));
		}

		/**Fifth step: Combining the results*/
		results.addAll(combineResults(partialHits, numLangs, combiningOption));
		return results;
	}
}
