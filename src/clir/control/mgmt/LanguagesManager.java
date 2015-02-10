package clir.control.mgmt;
import java.util.ArrayList;
import java.util.List;



public class LanguagesManager {
	@SuppressWarnings("unused")
	private Boolean VERBOSE=true;
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
		
	/**Singleton instance of type LanguageManager */
	private static LanguagesManager manager=null;
	
	private List<SpecificLanguageManager> specificLangManagers= new ArrayList<SpecificLanguageManager>();
	private List<String> languagesSupported= new ArrayList<String>();
	

	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected LanguagesManager(){
		 // Exists only to defeat instantiation.
		
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

}
