package clir.control.mgmt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clir.control.index.LSAIndexer;



// TODO: Auto-generated Javadoc
/**
 * The Class LanguagesManager. It keeps track of the general language configuration and state. Implemented as a singleton.
 * 
 * @author Gabriel
 */
public class LanguagesManager {
	
	/** The verbose. */
	@SuppressWarnings("unused")
	private Boolean VERBOSE=true;
	
	/** The debug. */
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
		
	/** Singleton instance of type LanguageManager. */
	private static LanguagesManager manager=null;
	
	/** The specific lang managers. */
	private List<SpecificLanguageManager> specificLangManagers= new ArrayList<SpecificLanguageManager>();
	
	/** The languages supported. */
	private List<String> languagesSupported= new ArrayList<String>();
	
	/** The index folder lsa. */
	private String indexFolderLSA;
	
	/** The training data lsa folder. */
	private String trainingDataLSAFolder;

	/**
	 * Functions.
	 */
	
	/**Protected constructor function, to defeat instantiation. */
	protected LanguagesManager(){
		 // Exists only to defeat instantiation.
		trainingDataLSAFolder="tmp/default_LSI_training_data";
		indexFolderLSA="tmp/default_LSI_index_location";
	}

	/**
	 * getInstance function, for singleton use.
	 *
	 * @return single instance of LanguagesManager
	 */
	public static LanguagesManager getInstance() {
		  if (manager==null){
			  manager= new LanguagesManager();
		  }
	      return manager;
	}

	/**
	 * getInstance function, for singleton use.
	 *
	 * @param lang the lang
	 * @return the specific manager
	 */
	public SpecificLanguageManager getSpecificManager(String lang) {
		for (int i=0; i<languagesSupported.size(); i++){
			if (lang.toUpperCase().equals(languagesSupported.get(i))){
				return specificLangManagers.get(i).getInstance(lang.toUpperCase());
			}
		}
		return null;
	}
	
	/**
	 * Starts support for a language.
	 *
	 * @param lang the lang
	 */
	public void addLanguage(String lang) {
		for (int i=0; i<languagesSupported.size(); i++){
			if (lang.toUpperCase().equals(languagesSupported.get(i))){
				return;
			}
		}
		SpecificLanguageManager newLang = new SpecificLanguageManager(lang.toUpperCase());
		languagesSupported.add(lang.toUpperCase());
		specificLangManagers.add(newLang);
	}
	
	/**
	 * Gets the languages supported.
	 *
	 * @return the languages supported
	 */
	public List<String> getLanguagesSupported() {
		return languagesSupported;
	}
	
	/**
	 * Checks if is supported.
	 *
	 * @param lang the lang
	 * @return the boolean
	 */
	public Boolean isSupported(String lang){
		return languagesSupported.contains(lang.toUpperCase());
	}
	
	/**
	 * Gets the index folder lsa.
	 *
	 * @param queryLanguages the query languages
	 * @return the index folder lsa
	 */
	public String getIndexFolderLSA(List<String> queryLanguages){
		String LSAfolder=indexFolderLSA;
		List<String> queryLanguagesCopy= new ArrayList<String>();
		queryLanguagesCopy.addAll(queryLanguages);
		Collections.sort(queryLanguagesCopy);
		String subFolder="/";
		for (int i=0; i<queryLanguagesCopy.size(); i++){
			if (i!=0){
				subFolder+="_";
			}
			subFolder+=queryLanguagesCopy.get(i);
		}
		LSAfolder+=subFolder;
		
		return LSAfolder;
	}
	
	/**
	 * Gets the training data lsa folder.
	 *
	 * @param queryLanguages the query languages
	 * @return the training data lsa folder
	 */
	public String getTrainingDataLSAFolder(List<String> queryLanguages){
		String LSAtrainingData=trainingDataLSAFolder;
		List<String> queryLanguagesCopy= new ArrayList<String>();
		queryLanguagesCopy.addAll(queryLanguages);
		Collections.sort(queryLanguagesCopy);
		String subFolder="/";
		for (int i=0; i<queryLanguagesCopy.size(); i++){
			if (i!=0){
				subFolder+="_";
			}
			subFolder+=queryLanguagesCopy.get(i);
		}
		LSAtrainingData+=subFolder;
		return LSAtrainingData;
	}
	
	/**
	 * Creates the lsa index.
	 *
	 * @param queryLanguages the query languages
	 * @param numSemanticDimensions the num semantic dimensions
	 */
	public void createLSAIndex(List<String> queryLanguages, int numSemanticDimensions){
		String LSAfolder=this.getIndexFolderLSA(queryLanguages);
		String LSAtrainingData=this.getTrainingDataLSAFolder(queryLanguages);
		LSAIndexer.getInstance().createIndex(queryLanguages, LSAtrainingData, LSAfolder, numSemanticDimensions);		
	}

}
