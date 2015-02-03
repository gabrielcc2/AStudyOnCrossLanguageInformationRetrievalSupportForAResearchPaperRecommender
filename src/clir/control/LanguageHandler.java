package clir.control;


public class LanguageHandler {
	private String DEFAULT_EN_REPOSITORY="default_EN_repository";
	private String DEFAULT_DE_REPOSITORY="default_DE_repository";
	private String DEFAULT_ES_REPOSITORY="default_ES_repository";

	private String INDEX_EN_LOCATION="default_EN_index_location";
	private String INDEX_DE_LOCATION="default_DE_index_location";
	private String INDEX_ES_LOCATION="default_ES_index_location";
	
	/**Singleton instances of type LanguageHandler */
	private static LanguageHandler english= null;
	private static LanguageHandler german= null;
	private static LanguageHandler spanish= null;
	
	private String repository;
	boolean indexCreated;

	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected LanguageHandler(){
		 // Exists only to defeat instantiation.
	}
	
	/**getInstance function, for singleton use*/
	public static LanguageHandler getInstance(String lang) {
	      if(lang.equals("EN")) {
	         if (english==null){
	        	 	english= new LanguageHandler();
	         }
	         return english;
	      }
	      else if(lang.equals("DE")) {
	         if (german==null){
	        	 	german= new LanguageHandler();
	         }
	         return german;
	      }
	      else if(lang.equals("ES")) {
		         if (spanish==null){
		        	 	spanish= new LanguageHandler();
		         }
		         return spanish;
		  }
	      return null;
	}


	public String getRepository(){
		return repository;
	}
	
	/**Takes as an input a boolean indicating if there is an existing index 
	 * in the folder.*/
	public void setRepository(String repository, Boolean useExistingIndex){
		this.repository=repository;
		if (this.equals(english) && this.repository==DEFAULT_EN_REPOSITORY){
			this.resetRepository(useExistingIndex);
		}
		else if (this.equals(german) && this.repository==DEFAULT_DE_REPOSITORY){
			this.resetRepository(useExistingIndex);
		}
		else if (this.equals(spanish) && this.repository==DEFAULT_ES_REPOSITORY){
			this.resetRepository(useExistingIndex);
		}
		else {
			if (useExistingIndex){
				this.indexCreated=true;
			}
			else{
				this.createIndex();
			}
		}
	}
	
	/**Takes as an input a boolean indicating if there is an existing index 
	 * in the folder.*/
	public void resetRepository(Boolean useExistingIndex){
		this.indexCreated=useExistingIndex;
		if (this.equals(english)){
			this.repository=DEFAULT_EN_REPOSITORY;
			if (!this.indexCreated){
				this.createIndex();
			}
		}
		else if (this.equals(german)){
			this.repository=DEFAULT_DE_REPOSITORY;
			if (!this.indexCreated){
				this.createIndex();				
			}
		}
		else if (this.equals(spanish)){
			this.repository=DEFAULT_ES_REPOSITORY;
			if (!this.indexCreated){
				this.createIndex();
			}
		}
	}
	
	
	private void createIndex(){//It creates the standard index and also the LSI index.
		if (this.equals(english)){
			this.indexCreated=true;
			
		}
		else if (this.equals(german)){
			this.indexCreated=true;			
		}
		else if (this.equals(spanish)){
			this.indexCreated=true;
		}
		
	}
	
}
