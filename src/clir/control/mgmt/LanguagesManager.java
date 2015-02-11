package clir.control.mgmt;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import clir.control.index.LSAIndexer;



public class LanguagesManager {
	@SuppressWarnings("unused")
	private Boolean VERBOSE=true;
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
		
	/**Singleton instance of type LanguageManager */
	private static LanguagesManager manager=null;
	
	private List<SpecificLanguageManager> specificLangManagers= new ArrayList<SpecificLanguageManager>();
	private List<String> languagesSupported= new ArrayList<String>();
	
	private String indexFolderLSA;
	private String trainingDataLSAFolder;

	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected LanguagesManager(){
		 // Exists only to defeat instantiation.
		trainingDataLSAFolder="tmp/default_LSI_training_data";
		indexFolderLSA="tmp/default_LSI_index_location";
	}

	/**getInstance function, for singleton use*/
	public static LanguagesManager getInstance() {
		  if (manager==null){
			  manager= new LanguagesManager();
		  }
	      return manager;
	}

	/**getInstance function, for singleton use*/
	public SpecificLanguageManager getSpecificManager(String lang) {
		for (int i=0; i<languagesSupported.size(); i++){
			if (lang.toUpperCase().equals(languagesSupported.get(i))){
				return specificLangManagers.get(i).getInstance(lang.toUpperCase());
			}
		}
		return null;
	}
	
	/**Starts support for a language*/
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
	
	public List<String> getLanguagesSupported() {
		return languagesSupported;
	}
	
	public Boolean isSupported(String lang){
		return languagesSupported.contains(lang.toUpperCase());
	}
	
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
	
	public void createLSAIndex(List<String> queryLanguages, int numSemanticDimensions){
		String LSAfolder=this.getIndexFolderLSA(queryLanguages);
		String LSAtrainingData=this.getTrainingDataLSAFolder(queryLanguages);
		LSAIndexer.getInstance().createIndex(queryLanguages, LSAtrainingData, LSAfolder, numSemanticDimensions);		
	}

}
