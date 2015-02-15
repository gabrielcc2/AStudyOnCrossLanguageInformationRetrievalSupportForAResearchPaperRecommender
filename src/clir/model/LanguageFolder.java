package clir.model;
// TODO: Auto-generated Javadoc
/**
 * 
 * A POJO to keep together a language and a folder. 
 * 
 * Useful for keeping an repository, index and query folders next to the languages. 
 * 
 * @author Gabriel
 * 
 */
public class LanguageFolder {
	
	/** The folder. */
	String folder;
	
	/** The lang. */
	String lang;
	
	/**
	 * Instantiates a new language folder.
	 *
	 * @param folder the folder
	 * @param lang the lang
	 */
	public LanguageFolder(String folder, String lang){
		this.folder=folder;
		this.lang=lang;
	}
	
	/**
	 * Gets the folder.
	 *
	 * @return the folder
	 */
	public String getFolder(){
		return folder;
	}
	
	/**
	 * Sets the folder.
	 *
	 * @param folder the new folder
	 */
	public void setFolder(String folder){
		this.folder=folder;
	}
		
	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public String getLang(){
		return lang;
	}
	
	/**
	 * Sets the lang.
	 *
	 * @param lang the new lang
	 */
	public void setLang(String lang){
		this.lang=lang;
	}


}
