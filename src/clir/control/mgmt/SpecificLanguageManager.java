package clir.control.mgmt;

import clir.control.index.PerLanguageIndexer;

// TODO: Auto-generated Javadoc
/**
 * The Class SpecificLanguageManager: Keeps track of index and repository folders for a specific language. Implemented as a singleton.
 * 
 *  * @author Gabriel
 */
public class SpecificLanguageManager {

	/** The verbose. */
	@SuppressWarnings("unused")
	private Boolean VERBOSE=true;
	
	/** The debug. */
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
	
	/** The manager. */
	private SpecificLanguageManager manager= null;
	
	/** The name. */
	String name;
	
	/** The repository folder. */
	String repositoryFolder;
	
	/** The index folder. */
	String indexFolder;
	
	/**
	 * Instantiates a new specific language manager.
	 *
	 * @param name the name
	 */
	public SpecificLanguageManager(String name){ //Not entirely Singletons...
		this.name=name;
		repositoryFolder="tmp/default_"+name+"_repository";
		indexFolder="tmp/default_"+name+"_index_location";
	}
	
	/**
	 * Gets the single instance of SpecificLanguageManager.
	 *
	 * @param name the name
	 * @return single instance of SpecificLanguageManager
	 */
	public SpecificLanguageManager getInstance(String name){
		if (manager==null){
			manager= new SpecificLanguageManager(name);
		}
		return manager;
	}
	
	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Gets the repository.
	 *
	 * @return the repository
	 */
	public String getRepository(){
		return repositoryFolder;
	}
	
	/**
	 * Sets the repository.
	 *
	 * @param repo the new repository
	 */
	public void setRepository(String repo){
		this.repositoryFolder=repo;
	}
	
	/**
	 * Gets the index folder.
	 *
	 * @return the index folder
	 */
	public String getIndexFolder(){
		return indexFolder;
	}
	
	/**
	 * Sets the index folder.
	 *
	 * @param index the new index folder
	 */
	public void setIndexFolder(String index){
		this.indexFolder=index;
	}
	
	/**
	 * Creates the index.
	 */
	public void createIndex(){
		PerLanguageIndexer.getInstance().createIndex(name, repositoryFolder, indexFolder);
	}
}
