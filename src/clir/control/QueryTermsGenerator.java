package clir.control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.docear.pdf.PdfDataExtractor;

import clir.model.LanguageFolder;
import clir.model.QueryTerms;

/* In charge of generating an object of type QueryTerms, that encapsulates all the query terms
 * found for a specific language.
 * 
 * */

public class QueryTermsGenerator {

	private Boolean VERBOSE=true;
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
	
	private String DEFAULT_QUERY_FOLDER="tmp/default_query_folder";

	private List<LanguageFolder> queryFolders;
	
	
	public QueryTermsGenerator(){ //We only initialize the number of results and set query folders to default.
	
		//Here we set up the default folders.
		queryFolders= new ArrayList<LanguageFolder>();
		List<String> support = new ArrayList<String>();
		support.addAll(LanguagesManager.getInstance().getLanguagesSupported());
		for (int i=0; i<support.size(); i++){
			String name=support.get(i).toUpperCase();
			LanguageFolder newFolder = new LanguageFolder(DEFAULT_QUERY_FOLDER+"/"+name, name);
			queryFolders.add(newFolder);
		}
	}
	
	public void setLanguageFolder(LanguageFolder folder){
		if (!LanguagesManager.getInstance().isSupported(folder.getLang())){
			return;
		}
		int removePosition=-1;
		for (int i=0; i<queryFolders.size(); i++){
			if (queryFolders.get(i).getLang().equals(folder.getLang())){
				removePosition=i;
				i=queryFolders.size(); //break
			}
		}
		if (removePosition!=-1){
			queryFolders.remove(removePosition);	
		}
		queryFolders.add(folder);
	}
	
	public void setLanguageFolder(String folder, String lang){
		if (!LanguagesManager.getInstance().isSupported(lang.toUpperCase())){
			return;
		}
		int removePosition=-1;
		for (int i=0; i<queryFolders.size(); i++){
			if (queryFolders.get(i).getLang().equals(lang)){
				removePosition=i;
				i=queryFolders.size(); //break
			}
		}
		if (removePosition!=-1){
			queryFolders.remove(removePosition);	
		}
		LanguageFolder newLangFolder = new LanguageFolder(folder, lang);
		queryFolders.add(newLangFolder);
	}
	
	public QueryTerms generateQueryTerms(List<String> queryLanguages){
		QueryTerms result = new QueryTerms();
		
		
		for (int i=0; i<queryLanguages.size(); i++){
			String lang=queryLanguages.get(i);
			int index=-1;
			for (int j=0; j<queryFolders.size(); j++){
				if (queryFolders.get(j).getLang().equals(lang)){
					index=j;
					j=queryFolders.size();//break
				}
			}
			if (index>=0){//The language is supported.
				File queryFolder= new File(queryFolders.get(index).getFolder());
				File[] files = queryFolder.listFiles(); //An array with all the files and directories within the current folder.

				for (int j = 0; j < files.length; j++) {
					if (files[j].isFile() && files[j].getName().endsWith(".pdf")) { //For pdf files
						
							/*We start by extracting the relevant information...*/
							PdfDataExtractor extractor = new PdfDataExtractor(files[j]);
							String title=null;
							try {
								title = extractor.extractTitle();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
							if (title.length()>2){
								result.addQueryTerm(title.toLowerCase(), lang);
							}
					}
				}
			}
		}
		if (VERBOSE){
			System.out.println("Query terms generated: ");
			for (int i=0; i<result.getLangs().size(); i++){
				String lang=result.getLangs().get(i);
				System.out.println("Terms for "+lang+" : "+result.getTermsOfLang(lang));
			}
		}
		return result;	
	}
}
