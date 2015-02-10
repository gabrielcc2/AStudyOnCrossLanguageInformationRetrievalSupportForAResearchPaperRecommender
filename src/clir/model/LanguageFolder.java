package clir.model;
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
	
	String folder;
	String lang;
	
	public LanguageFolder(String folder, String lang){
		this.folder=folder;
		this.lang=lang;
	}
	
	public String getFolder(){
		return folder;
	}
	
	public void setFolder(String folder){
		this.folder=folder;
	}
		
	public String getLang(){
		return lang;
	}
	
	public void setLang(String lang){
		this.lang=lang;
	}


}
