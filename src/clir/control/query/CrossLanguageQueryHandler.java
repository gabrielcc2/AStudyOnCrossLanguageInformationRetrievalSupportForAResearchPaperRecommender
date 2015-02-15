package clir.control.query;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.itc.mwn.DictionaryDatabase;
import org.itc.mwn.IndexWord;
import org.itc.mwn.MysqlDictionary;
import org.itc.mwn.POS;
import org.itc.mwn.Synset;
import org.itc.mwn.Word;

import clir.control.utils.TranslationHandler;
import clir.model.PaperHit;
import clir.model.QueryTerms;
import clir.model.ResultsList;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

// TODO: Auto-generated Javadoc
/**
 * The Class CrossLanguageQueryHandler: Implements querying with automated query translation. Implemented as a singleton.
 * 
 * <p>  This processing is completed in 5 steps:
 * <p>  First step: Pre-processing for each language
 * <p>  Second step: Translation for each language pair
 * <p>  Third step: Post-processing... 
 * <p>  Forth step: Matching or searching in every index
 * <p> Fifth step: Combining the results
 * 
 * Stanfords PoS taggers used here. Connection to MultiWordNet also established here. Translation with Apis from Apertium
 * and Google are initiated here but implemented in clir.control.utils.TranslationHandler. Support for Moses translation
 * belongs there as well.
 * 
 * @author Gabriel
 * */

public class CrossLanguageQueryHandler extends QueryHandler{
	
	/** Singleton instance of type CrossLanguageQueryHandler. */
	private static CrossLanguageQueryHandler handler = null;
//	private static String DE_tagger="german-fast";
	/** The E n_tagger. */
private static String EN_tagger="wsj-0-18-bidirectional-distsim";
//	private static String ES_tagger="spanish-distsim";
	/**
 * Functions.
 */
	
	/**Protected constructor function, to defeat instantiation. */
	protected CrossLanguageQueryHandler(){
		 // Exists only to defeat instantiation.
		numberOfResults=DEFAULT_NUMBER_OF_RESULTS;
	}
	
	/**
	 * getInstance function, for singleton use.
	 *
	 * @return single instance of CrossLanguageQueryHandler
	 */
	public static CrossLanguageQueryHandler getInstance(){
		if (handler==null){
			handler= new CrossLanguageQueryHandler();
		}
		return handler;
	}
	
	/**
	 * Pre process.
	 *
	 * @param query the query
	 * @param preProcessingOption the pre processing option
	 * @return the query terms
	 */
	private QueryTerms preProcess (QueryTerms query, Boolean preProcessingOption){
	/*	List<String> langs= new ArrayList<String>();
		langs.addAll(query.getLangs());
		for (int i=0; i<langs.size(); i++){
			String lang=langs.get(i);
			if (lang.equals("DE")&&preProcessingOption){
				//Pre-processing optimizations... 
				//Perhaps spell-checking for obvious mistakes?... Left for future versions.
			}
			else if (lang.equals("EN")&&preProcessingOption){
				//Pre-processing optimizations
				
			}
			else if (lang.equals("ES")&&preProcessingOption){
				//Pre-processing optimizations
			}
		}
		*/
		QueryTerms improvedTerms = new QueryTerms(query.getTerms(), query.getLangs());
		return improvedTerms;
	}

	/**
	 * Post process.
	 *
	 * @param query the query
	 * @param postProcessingOption the post processing option
	 * @return the query terms
	 */
	private QueryTerms postProcess (QueryTerms query, Boolean postProcessingOption){
		List<String> langs= new ArrayList<String>();
		langs.addAll(query.getLangs());
		QueryTerms improvedTerms = new QueryTerms(query.getTerms(), query.getLangs());
		for (int i=0; i<langs.size(); i++){
			String lang=langs.get(i);
			if (lang.equals("DE")&&postProcessingOption){
				
				//Post-processing refinements
				//We start by tagging for POS the query terms, with Stanfords POS tagger.
		/*		List<String> nounsFound= new ArrayList<String>();
				List<String> verbsFound= new ArrayList<String>();
				List<String> adjFound= new ArrayList<String>();
			*/	
				//MaxentTagger tagger = new MaxentTagger("resources/taggers/"+DE_tagger+".tagger");
				
	/*			InputStream is = new ByteArrayInputStream(query.getTermsOfLang("DE").getBytes());
				 
				// read it with BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
			    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(br);
			    
			    for (List<HasWord> sentence : sentences) {
			      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			      for (int j=0; j<tSentence.size(); j++){
			    	  if (tSentence.get(j).tag().toLowerCase().startsWith("n")){
			    		  	nounsFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			    	  else if (tSentence.get(j).tag().toLowerCase().startsWith("ad")){
			    		  	adjFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			    	  else if (tSentence.get(j).tag().toLowerCase().startsWith("v")){
			    		  	verbsFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			      }
			    }
				Set<String> newWords = new HashSet<String>();
	 			*/
				
				//Semantic knowledge of German can be plugged in here...
				
				//For German we could use GermanNet
				//Sense disambiguation is not handled, but could be. We stick to the most common sense. 
				//Then we find the synsets for each term and add them to a set we have
				//Finally we add this set to the query
			}
			else if (lang.equals("EN")&&postProcessingOption){
				//Post-processing refinements

				//Perhaps a useful approach would be to detect concepts, so they get queried for synonyms together. 

				//We start by tagging for POS the query terms, with Stanfords POS tagger.
				List<String> nounsFound= new ArrayList<String>();
				List<String> verbsFound= new ArrayList<String>();
				List<String> adjFound= new ArrayList<String>();
				
				MaxentTagger tagger = new MaxentTagger("resources/taggers/"+EN_tagger+".tagger");
				
				InputStream is = new ByteArrayInputStream(query.getTermsOfLang(lang).getBytes());
				 
				// read it with BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
			    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(br);
			    
			    for (List<HasWord> sentence : sentences) {
			      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			      for (int j=0; j<tSentence.size(); j++){
			    	  if (tSentence.get(j).tag().toLowerCase().startsWith("n")){
			    		  	nounsFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			    	  else if (tSentence.get(j).tag().toLowerCase().startsWith("j")){
			    		  	adjFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			    	  else if (tSentence.get(j).tag().toLowerCase().startsWith("v")){
			    		  	verbsFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			      }
			    }
				Set<String> newWords = new HashSet<String>();
	            DictionaryDatabase dictionary = new MysqlDictionary();
				
	            for (int k=0; k<nounsFound.size(); k++){
	                IndexWord word = dictionary.lookupIndexWord(POS.NOUN, nounsFound.get(k), "english");
	                if (word!=null){
	                	Synset[] senses = word.getSenses();
	                	if (senses!=null && senses.length>=1){
	                		Synset sense= senses[0];//Most common sense chosen, perhaps this could be improved.
	            
	                		Word[] synonyms = sense.getWords();
	                		for (int j=0; j<synonyms.length; j++){
	                			if (!synonyms[j].getLemma().equals(nounsFound.get(k))&&synonyms[j].getLemma().length()>1){
	                				newWords.add(synonyms[j].getLemma());     
	                			}
	                		}
	                	}
	                }
				}
				for (int k=0; k<adjFound.size(); k++){
				    IndexWord word = dictionary.lookupIndexWord(POS.ADJ, adjFound.get(k), "english");
	                if (word!=null){
	                	Synset[] senses = word.getSenses();
	                	if (senses!=null && senses.length>=1){
	                		Synset sense= senses[0];//Most common sense chosen, perhaps this could be improved.
	            
	                		Word[] synonyms = sense.getWords();
	                		for (int j=0; j<synonyms.length; j++){
	                			if (!synonyms[j].getLemma().equals(adjFound.get(k))&&synonyms[j].getLemma().length()>1){
	                				newWords.add(synonyms[j].getLemma());     
	                			}
	                		}
	                	}
	                }
				}
				for (int k=0; k<verbsFound.size(); k++){
				    IndexWord word = dictionary.lookupIndexWord(POS.VERB, verbsFound.get(k), "english");
	                if (word!=null){
	                	Synset[] senses = word.getSenses();
	                	if (senses!=null && senses.length>=1){
	                		Synset sense= senses[0];//Most common sense chosen, perhaps this could be improved.
	            
	                		Word[] synonyms = sense.getWords();
	                		for (int j=0; j<synonyms.length; j++){
	                			if (!synonyms[j].getLemma().equals(verbsFound.get(k))&&synonyms[j].getLemma().length()>1){
	                				newWords.add(synonyms[j].getLemma());    
	                			}
	                		}
	                	}
	                }	
				}
				Object[] newWordsArray=newWords.toArray();
				for (int j=0; j<newWordsArray.length; j++){
					improvedTerms.addQueryTerm(newWordsArray[j].toString(), "EN");
				}
			}
			else if (lang.equals("ES")&&postProcessingOption){
				//Post-processing refinements
				//We start by tagging for POS the query terms, with Stanfords POS tagger.
	/*			List<String> nounsFound= new ArrayList<String>();
				List<String> verbsFound= new ArrayList<String>();
				List<String> adjFound= new ArrayList<String>();
*/
	//			MaxentTagger tagger = new MaxentTagger("resources/taggers/"+ES_tagger+".tagger");
				
		/*		InputStream is = new ByteArrayInputStream(query.getTermsOfLang("ES").getBytes());
				 
				// read it with BufferedReader
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				
			    List<List<HasWord>> sentences = MaxentTagger.tokenizeText(br);
			    
			    for (List<HasWord> sentence : sentences) {
			      List<TaggedWord> tSentence = tagger.tagSentence(sentence);
			      for (int j=0; j<tSentence.size(); j++){
			    	  if (tSentence.get(j).tag().toLowerCase().startsWith("n")){
			    		  	nounsFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			    	  else if (tSentence.get(j).tag().toLowerCase().startsWith("a")){
			    		  	adjFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			    	  else if (tSentence.get(j).tag().toLowerCase().startsWith("v")){
			    		  	verbsFound.add(tSentence.get(j).word().toLowerCase());
			    	  }
			      }
			    }
				Set<String> newWords = new HashSet<String>();
			*/	
				//Semantic knowledge of Spanish can be plugged in here...

				//For Spanish we could use MultiWordNet...
				//Sense disambiguation is not handled, but could be. We stick to the most common sense. 
				//Then we find the synsets for each term and add them to a set we have
				//Finally we add this set to the query
			}
		}
		return improvedTerms;
	}
	
	/**
	 * Gets the translations.
	 *
	 * @param expectedLanguages the expected languages
	 * @param query the query
	 * @param translationOption the translation option
	 * @return the translations
	 */
	private QueryTerms getTranslations (List<String> expectedLanguages, QueryTerms query, String translationOption){
				
		List<String> translationBasis= new ArrayList<String>();
		for (int i=0; i<expectedLanguages.size(); i++){
			if (query.getLangs().contains(expectedLanguages.get(i))){
				translationBasis.add(query.getTermsOfLang(expectedLanguages.get(i)));
			}
			else{
				translationBasis.add("");
			}
		}
		QueryTerms translatedTerms = new QueryTerms(translationBasis, expectedLanguages);
		
		
		for (int i=0; i<expectedLanguages.size(); i++){
			for (int j=0; j<query.getLangs().size(); j++){//We are translating j to the language i
				if (!query.getLangs().get(j).equals(expectedLanguages.get(i))){
					String sourceText=query.getTerms().get(j);
					if (sourceText.length()>1){
						String sourceLanguage=query.getLangs().get(j);
						String targetLanguage=expectedLanguages.get(i);
						String translation=TranslationHandler.getInstance().translate(translationOption, sourceText, sourceLanguage, targetLanguage);
						translatedTerms.addQueryTerm(translation, expectedLanguages.get(i));
					}
				}
			}
		}
		if (VERBOSE){
			for (int i=0; i<translatedTerms.getLangs().size(); i++){
				System.out.println("* Query produced after being expanded with translations "+i+"- Lang: "+
						translatedTerms.getLangs().get(i)+" Query: "+
						translatedTerms.getTerms().get(i));
			}
		}
		return translatedTerms;
	}
	
	/**
	 * Combine results.
	 *
	 * @param partialHitsInput the partial hits input
	 * @param size the size
	 * @param combiningOption the combining option
	 * @return the list
	 */
	public List<PaperHit> combineResults(ResultsList[] partialHitsInput, int size, Boolean combiningOption){
		List<PaperHit> combinedResults= new ArrayList<PaperHit>();
		ResultsList[] partialHits= partialHitsInput;
		int numberOfHits= 0;
		if (combiningOption){
			for (int i=0; i<partialHits.length; i++){
				List<PaperHit> hits= partialHits[i].getPaperHits();
				if (!hits.isEmpty()){
					if(!hits.get(0).getLang().equals("EN")){
						for (int k=0; k<hits.size(); k++){
							float formerValue=hits.get(k).getRelevanceScore();
							partialHits[i].getPaperHits().get(k).setRelevanceScore(1.5f*formerValue);
							if (VERBOSE){
								System.out.println("* Merging improvement used- Relevance score of: "+partialHits[i].getPaperHits().get(k).getTitle()+" boosted by 1.5, reaching: "+partialHits[i].getPaperHits().get(k).getRelevanceScore()+" from: "+formerValue);
							}
						}
					}
				}
			}
		}
		for (int i=0; i<size; i++){
			if (!partialHits[i].isEmpty()){
				combinedResults.addAll(partialHits[i].getPaperHits());
				numberOfHits+=partialHits[i].getPaperHits().get(0).getNumOfResults();
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
	
	/**
	 * Run query.
	 *
	 * @param expectedLanguages the expected languages
	 * @param query the query
	 * @param numExpectedResults the num expected results
	 * @param preProcessingOption the pre processing option
	 * @param translationOption the translation option
	 * @param postProcessingOption the post processing option
	 * @param combiningOption the combining option
	 * @return the results list
	 */
	public ResultsList runQuery(List<String> expectedLanguages, QueryTerms query, int numExpectedResults, Boolean preProcessingOption, String translationOption, Boolean postProcessingOption, Boolean combiningOption){
		ResultsList resultingList= new ResultsList();
		QueryTerms postProcessedTerms = new QueryTerms();
		List<PaperHit> results= new ArrayList<PaperHit>();
		
		
		/**This processing is completed in 5 steps:*/
		
		/**First step: Pre-processing for each language*/
		QueryTerms preProcessedQuery=preProcess(query, preProcessingOption);
		
		/**Second step: Translation for each language pair*/
		QueryTerms translatedQueries=getTranslations(expectedLanguages, preProcessedQuery, translationOption);
		
		/**Third step: Post-processing... */
		QueryTerms postProcessedQueries=postProcess(translatedQueries, postProcessingOption);
		postProcessedTerms.initialize(postProcessedQueries.getTerms(), postProcessedQueries.getLangs());
		resultingList.setQueryTerms(postProcessedTerms);
		
		//User query refinement re-runs the processing from this stage...
		/**Forth step: Matching or searching in every index*/
		int numLangs=postProcessedQueries.getLangs().size();
		ResultsList [] partialHits= new ResultsList[numLangs];
		for (int i=0; i<numLangs; i++){
			String currentLanguage=postProcessedQueries.getLangs().get(i);
			PerLanguageQueryHandler agent= new PerLanguageQueryHandler(currentLanguage, numExpectedResults);
			String q= postProcessedQueries.getTerms().get(i);
			partialHits[i]= new ResultsList();
			if (q.length()>1){
					partialHits[i].setPaperHits((agent.runQuery(q)).getPaperHits());
					if (DEBUG){
						System.out.println("* Hits for: "+currentLanguage);
						if(partialHits[i].getPaperHits().size()>=1){
							for (int k=0; k<partialHits[i].getPaperHits().size(); k++){
								System.out.println(k+": "+partialHits[i].getPaperHits().get(k).getUrl());
							}
						}
					}
			}
		}

		/**Fifth step: Combining the results*/
		results.addAll(combineResults(partialHits, numLangs, combiningOption));
		resultingList.setPaperHits(results);
		return resultingList;
	}

	/**
	 * Refine query.
	 *
	 * @param postProcessedQueries the post processed queries
	 * @param numExpectedResults the num expected results
	 * @param combiningOption the combining option
	 * @return the results list
	 */
	public ResultsList refineQuery(QueryTerms postProcessedQueries, int numExpectedResults, boolean combiningOption){
		List<PaperHit> results= new ArrayList<PaperHit>();
		
		ResultsList resultingList= new ResultsList();
		resultingList.setQueryTerms(postProcessedQueries);

		int numLangs=postProcessedQueries.getLangs().size();
		ResultsList [] partialHits= new ResultsList[numLangs];
		for (int i=0; i<numLangs; i++){
			String currentLanguage=postProcessedQueries.getLangs().get(i);
			PerLanguageQueryHandler agent= new PerLanguageQueryHandler(currentLanguage, numExpectedResults);
			String q= postProcessedQueries.getTerms().get(i);
			partialHits[i]= new ResultsList();
			if (q.length()>1){
					partialHits[i].setPaperHits((agent.runQuery(q)).getPaperHits());
					if (DEBUG){
						System.out.println("* Hits for: "+currentLanguage);
						if(partialHits[i].getPaperHits().size()>=1){
							for (int k=0; k<partialHits[i].getPaperHits().size(); k++){
								System.out.println(k+": "+partialHits[i].getPaperHits().get(k).getUrl());
							}
						}
					}
			}
		}

		/**Fifth step: Combining the results*/
		results.addAll(combineResults(partialHits, numLangs, combiningOption));
		resultingList.setPaperHits(results);
		return resultingList;
	}
}
