package clir.control;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.docear.pdf.PdfDataExtractor;

import clir.model.PaperHit;

public class RecommendationsHandler {
	private Boolean VERBOSE=true;
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
	
	/**Singleton instance of type recommendationsHandler */
	private static RecommendationsHandler instance= null;

	private File queryFolderEN;
	private File queryFolderDE;
	private File queryFolderES;
	private String DEFAULT_QUERY_FOLDER="tmp/default_query_folder";
	private List<String> queryLanguages;
	private Boolean usesLSI;	
	
	private int numberOfResults=10;
	
	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected RecommendationsHandler(){
		 // Exists only to defeat instantiation.
		queryLanguages= new ArrayList<String>();
		usesLSI=false;	
		queryFolderEN=new File (DEFAULT_QUERY_FOLDER+"/EN");
		queryFolderDE=new File (DEFAULT_QUERY_FOLDER+"/DE");
		queryFolderES=new File (DEFAULT_QUERY_FOLDER+"/ES");
	}
	
	/**getInstance function, for singleton use*/
	public static RecommendationsHandler getInstance() {
	      if(instance == null) {
	    	  instance = new RecommendationsHandler();
	      }
	      return instance;
	}
	
	String getQueryFolderEN(){
		return queryFolderEN.toString();
	}
	
	String getQueryFolderDE(){
		return queryFolderDE.toString();
	}
	
	String getQueryFolderES(){
		return queryFolderES.toString();
	}
	
	void setQueryFolderEN(String queryFolder){
		this.queryFolderEN=new File(queryFolder);
	}
	
	void setQueryFolderDE(String queryFolder){
		this.queryFolderDE=new File(queryFolder);
	}
	
	void setQueryFolderES(String queryFolder){
		this.queryFolderES=new File(queryFolder);
	}
	
	void resetQueryFolders(){
		queryFolderEN=new File (DEFAULT_QUERY_FOLDER+"/EN");
		queryFolderDE=new File (DEFAULT_QUERY_FOLDER+"/DE");
		queryFolderES=new File (DEFAULT_QUERY_FOLDER+"/ES");
	}

	List<String> getQueryLanguages(){
		return queryLanguages;
	}
	
	void resetQueryLanguages(){
		queryLanguages.clear();
	}
	
	void addQueryLanguage(String queryLang){
		queryLanguages.add(queryLang);
	}
	
	Boolean getUsesLSI(){
		return usesLSI;
	}
	
	void setUsesLSI(Boolean usesLSI){
		this.usesLSI=usesLSI;
	}
	
	@SuppressWarnings("deprecation")
	List<PaperHit> getRecommendations(){
		List<PaperHit> results = new ArrayList<PaperHit>();

		boolean englishUsed=queryLanguages.contains("EN");
		boolean germanUsed=queryLanguages.contains("DE");
		boolean spanishUsed=queryLanguages.contains("ES");
		
		if (!englishUsed&&!germanUsed&&!spanishUsed){
			return results; //No language configured for the query
		}
		
		
		/**Here we will assume that there exist indexes of the given languages and that the query
		 * parameters are configured**/

		String queryString="";
		
		if (englishUsed && !germanUsed && !spanishUsed){
			File[] files = queryFolderEN.listFiles(); //In this case we create an array with all the files and directories within the current folder.
			//Now it iterates over each element in the array.
			
			
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().endsWith(".pdf")) { //For pdf files
						
						/*We start by extracting the relevant information...*/
							
						org.docear.pdf.PdfDataExtractor extractor = new PdfDataExtractor(files[i]);
						String title=null;
						try {
							title = extractor.extractTitle();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (title.length()>2){
							queryString+=title.toLowerCase()+" ";
						}
				}
			}
			
			if (queryString.length()>2){
				Directory indexFolder = null;
				try {
					indexFolder=FSDirectory.open(new File(LanguageHandler.getInstance("EN").getIndexLocation()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error: Recommender not initialized: Invalid directory.");
				}
				
				Analyzer sa = new EnglishAnalyzer(Version.LUCENE_4_10_0);
				MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "title", "abstract"}, sa);
				Query q=null;
				try {
					if (VERBOSE){
						System.out.println("Query String: "+queryString);
					}
					q = multiFieldQP.parse(MultiFieldQueryParser.escape(queryString));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				IndexReader ir=null;
				IndexSearcher is = null; 
				TopDocs resultingDocs=null;
				try {
					ir = IndexReader.open(indexFolder);
					is=new IndexSearcher(ir);
					if(q!=null){
						resultingDocs= is.search(q, numberOfResults);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				if (resultingDocs!=null){
					ScoreDoc[] hits = resultingDocs.scoreDocs;
					for (int i = 0; i < hits.length; ++i) {	
						Integer rank=(i + 1);
						String title;
						String url;
						float relevanceScore=hits[i].score;
				
						Document doc=null;
						try {
							doc = is.doc(hits[i].doc);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						title = doc.get("title");
						if (title == null) {
							title="No title available for this document";
						}
				
						url = doc.get("url");
						if (url == null) {
							url="No URL available for this document";
						}
				
						results.add(new PaperHit(rank, title, url, relevanceScore, resultingDocs.totalHits));
					}
				}
			}			
			
		}
		else if (germanUsed && !englishUsed && !spanishUsed){
			
			File[] files = queryFolderDE.listFiles(); //In this case we create an array with all the files and directories within the current folder.
			//Now it iterates over each element in the array.
			
			
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().endsWith(".pdf")) { //For pdf files
						
						/*We start by extracting the relevant information...*/
							
						org.docear.pdf.PdfDataExtractor extractor = new PdfDataExtractor(files[i]);
						String title=null;
						try {
							title = extractor.extractTitle();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (title.length()>2){
							queryString+=title.toLowerCase()+" ";
						}
				}
			}
			
			if (queryString.length()>2){
				Directory indexFolder = null;
				try {
					indexFolder=FSDirectory.open(new File(LanguageHandler.getInstance("DE").getIndexLocation()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error: Recommender not initialized: Invalid directory.");
				}
				
				Analyzer sa = new GermanAnalyzer(Version.LUCENE_4_10_0);
				MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "title", "abstract"}, sa);
				Query q=null;
				try {
					if (VERBOSE){
						System.out.println("Query String: "+queryString);
					}
					q = multiFieldQP.parse(MultiFieldQueryParser.escape(queryString));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				IndexReader ir=null;
				IndexSearcher is = null; 
				TopDocs resultingDocs=null;
				try {
					ir = IndexReader.open(indexFolder);
					is=new IndexSearcher(ir);
					if(q!=null){
						resultingDocs= is.search(q, numberOfResults);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				if (resultingDocs!=null){
					ScoreDoc[] hits = resultingDocs.scoreDocs;
					for (int i = 0; i < hits.length; ++i) {	
						Integer rank=(i + 1);
						String title;
						String url;
						float relevanceScore=hits[i].score;
				
						Document doc=null;
						try {
							doc = is.doc(hits[i].doc);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						title = doc.get("title");
						if (title == null) {
							title="Kein Titel für dieses Dokument verfügbar";
						}
				
						url = doc.get("url");
						if (url == null) {
							url="Kein URL für dieses Dokument verfügbar";
						}
				
						results.add(new PaperHit(rank, title, url, relevanceScore, resultingDocs.totalHits));
					}
				}
			}	
			
		}
		else if (spanishUsed && !englishUsed && !germanUsed){
			
			File[] files = queryFolderES.listFiles(); //In this case we create an array with all the files and directories within the current folder.
			//Now it iterates over each element in the array.
			
			
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().endsWith(".pdf")) { //For pdf files
						
						/*We start by extracting the relevant information...*/
							
						org.docear.pdf.PdfDataExtractor extractor = new PdfDataExtractor(files[i]);
						String title=null;
						try {
							title = extractor.extractTitle();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						if (title.length()>2){
							queryString+=title.toLowerCase()+" ";
						}
				}
			}
			
			if (queryString.length()>2){
				Directory indexFolder = null;
				try {
					indexFolder=FSDirectory.open(new File(LanguageHandler.getInstance("ES").getIndexLocation()));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("Error: Recommender not initialized: Invalid directory.");
				}
				
				Analyzer sa = new SpanishAnalyzer(Version.LUCENE_4_10_0);
				MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "title", "abstract"}, sa);
				Query q=null;
				try {
					if (VERBOSE){
						System.out.println("Query String: "+queryString);
					}
					q = multiFieldQP.parse(MultiFieldQueryParser.escape(queryString));
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
				IndexReader ir=null;
				IndexSearcher is = null; 
				TopDocs resultingDocs=null;
				try {
					ir = IndexReader.open(indexFolder);
					is=new IndexSearcher(ir);
					if(q!=null){
						resultingDocs= is.search(q, numberOfResults);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				if (resultingDocs!=null){
					ScoreDoc[] hits = resultingDocs.scoreDocs;
					for (int i = 0; i < hits.length; ++i) {	
						Integer rank=(i + 1);
						String title;
						String url;
						float relevanceScore=hits[i].score;
				
						Document doc=null;
						try {
							doc = is.doc(hits[i].doc);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						title = doc.get("title");
						if (title == null) {
							title="Título no disponible para este documento.";
						}
				
						url = doc.get("url");
						if (url == null) {
							url="URL no disponible para este documento.";
						}
				
						results.add(new PaperHit(rank, title, url, relevanceScore, resultingDocs.totalHits));
					}
				}
			}	
			
			
		}
		else if (englishUsed && germanUsed && !spanishUsed){
			
		}
		else if (englishUsed&& spanishUsed && !germanUsed){
			
		}
		else if (germanUsed&&spanishUsed&&!englishUsed){
			
		}
		else if (englishUsed && germanUsed && spanishUsed){
			
		}
		else{//None
			
		}
	
		
		/**The recommender begins by opening the queryFolder and navigating towards each subfolder 
		 * of the specified languages. 
		 * 
		 * There it extracts data and adds them to a string of each type of language.**/
		
		/**Now we have a query term per language.
		 *
		 *  If using language model: 
		 	* 	We need to expand it by translating from other languages.
		 	* 	First we define cross language vectors and call the language handler
		 	* 	to deal with the translations, it returns the translations, we add them to the query terms.
		 	* 
		 	* Then we clean the query terms somehow.
		 	* Then we perform a query in each language...
		 	* And then we merge the results of the queries
		 *  
		 * If using LSI:
		 	* We turn all query terms to whatever LSI representation we have...
		 	* We sum up the partial LSI in folders, and then we search over this...
		 	* And we return the results assigning a relevance score...**/
		return results;
	}

}
