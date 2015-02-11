package clir.control.mgmt;

import clir.control.index.PerLanguageIndexer;

public class SpecificLanguageManager {

	@SuppressWarnings("unused")
	private Boolean VERBOSE=true;
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
	
	private SpecificLanguageManager manager= null;
	String name;
	String repositoryFolder;
	String indexFolder;
	
	public SpecificLanguageManager(String name){ //Not entirely Singletons...
		this.name=name;
		repositoryFolder="tmp/default_"+name+"_repository";
		indexFolder="tmp/default_"+name+"_index_location";
	}
	
	public SpecificLanguageManager getInstance(String name){
		if (manager==null){
			manager= new SpecificLanguageManager(name);
		}
		return manager;
	}
	
	public String getName(){
		return name;
	}
	
	public String getRepository(){
		return repositoryFolder;
	}
	
	public void setRepository(String repo){
		this.repositoryFolder=repo;
	}
	
	public String getIndexFolder(){
		return indexFolder;
	}
	
	public void setIndexFolder(String index){
		this.indexFolder=index;
	}
	
	public void createIndex(){
		PerLanguageIndexer.getInstance().createIndex(name, repositoryFolder, indexFolder);
	}
}
