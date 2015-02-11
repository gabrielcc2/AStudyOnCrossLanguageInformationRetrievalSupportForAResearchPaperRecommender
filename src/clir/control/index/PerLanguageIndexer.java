package clir.control.index;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.es.SpanishAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.docear.pdf.PdfDataExtractor;

public class PerLanguageIndexer extends Indexer {

	/**Singleton instance of type PerLanguageIndexer */
	private static PerLanguageIndexer indexer = null;
	
	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected PerLanguageIndexer(){
		 // Exists only to defeat instantiation.
	}
	
	/**getInstance function, for singleton use*/
	public static PerLanguageIndexer getInstance(){
		if (indexer==null){
			indexer= new PerLanguageIndexer();
		}
		return indexer;
	}
	
	public void createIndex(String lang, String repoLocation, String indexLocation){
		
		File repository = new File(repoLocation);

		if (repository.exists()&& repository.isDirectory()) { //It checks if it is a directory (i.e. a folder)
			
			Locale locale;
			Directory indexDir=null;
			IndexWriter writer=null;
			
			if (lang.equals("DE")){
				locale= new Locale("de", "DE");
				Locale.setDefault(locale);
				@SuppressWarnings("deprecation")
				Analyzer analyzer = new GermanAnalyzer(Version.LUCENE_4_10_0);
				@SuppressWarnings("deprecation")
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
				iwc.setOpenMode(OpenMode.CREATE);
				try {
					indexDir = FSDirectory.open(new File(indexLocation));
					writer = new IndexWriter(indexDir, iwc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			else if (lang.equals("ES")){
				locale= new Locale("es", "ES");
				Locale.setDefault(locale);
				@SuppressWarnings("deprecation")
				Analyzer analyzer = new SpanishAnalyzer(Version.LUCENE_4_10_0);
				@SuppressWarnings("deprecation")
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
				iwc.setOpenMode(OpenMode.CREATE);
				try {
					indexDir = FSDirectory.open(new File(indexLocation));
					writer = new IndexWriter(indexDir, iwc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			else {//English by default
				@SuppressWarnings("deprecation")
				Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_4_10_0);
				@SuppressWarnings("deprecation")
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
				iwc.setOpenMode(OpenMode.CREATE);
				try {
					indexDir = FSDirectory.open(new File(indexLocation));
					writer = new IndexWriter(indexDir, iwc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
		
			File[] files = repository.listFiles(); //In this case we create an array with all the files and directories within the current folder.
			//Now it iterates over each element in the array.
			
			for (int i = 0; i < files.length; i++) {
				if (files[i].isFile() && files[i].getName().endsWith(".pdf")) { //For pdf files
						
						/*We start by extracting the relevant information...*/
						
						boolean noException=true;
						String result=null;
						try{
							PdfDataExtractor extractor = new PdfDataExtractor(files[i]);
							result=extractor.extractTitle();
							if (result.length()>2){
								result+="|";
								String plainText=extractor.extractPlainText();
								if (plainText.length()>10000){
									plainText=plainText.substring(0, 10000);
								}
								result+=plainText;
							}
							if (result.length()<2){
								noException=false;
								if (DEBUG){
									System.out.println("Exception, result string empty.");
								}
							}

						} catch (Exception e2){
								e2.printStackTrace();
								noException=false;
						}
						if (noException){
							/**
						 	* A Lucene Document variable is declared.
							*/	

							result=result.replace(".", " ");
							result=result.replace(",", " ");
							result=result.replace(":", " ");
							result=result.replace("(", " ");
							result=result.replace(")", " ");
							result=result.replace("©", " ");
							result=result.replace("*", " ");
							result=result.replace("[", " ");
							result=result.replace("]", " ");
							result=result.replace(" ", "");
							result=result.toLowerCase();
							
							Document luceneDoc= new Document();
							
							String title="";
							title+=result.substring(0, result.indexOf('|'));
							
							title=title.replace("|", " ");
							result=result.substring(result.indexOf('|'), result.length());
							String abstractString="";

							if (lang.equals("DE")){							
								if (result.indexOf("zusammenfassung")>0 && result.indexOf("schlagw")>result.indexOf("zusammenfassung")){
									abstractString+=result.substring(result.indexOf("zusammenfassung")+15, result.indexOf("schlagw"));
								}
							}
							else if (lang.equals("ES")){
								if (result.indexOf("summary")>0 && result.indexOf("key")>result.indexOf("summary")){
									abstractString+=result.substring(result.indexOf("summary")+7, result.indexOf("key"));
								}
								else if (result.indexOf("abstract")>0 && result.indexOf("key")>result.indexOf("abstract")){
									abstractString+=result.substring(result.indexOf("abstract")+8, result.indexOf("key"));
								}
								else if (result.indexOf("resum")>0 && result.indexOf("palabras clave")>result.indexOf("resum")){
									abstractString+=result.substring(result.indexOf("resum")+7, result.indexOf("palabras clave"));
								}
							}
							else{
								if (result.indexOf("abstract")>0 && result.indexOf("key")>result.indexOf("abstract")){
									abstractString+=result.substring(result.indexOf("abstract")+8, result.indexOf("key"));
								}
								else if (result.indexOf("by")>0 && result.indexOf("copyright")>result.indexOf("by")){
									abstractString+=result.substring(result.indexOf("by"), result.indexOf("copyright"));
								}
								
							}
							abstractString=abstractString.replace("|", " ");
							
							TextField field1=new TextField("title", title, Field.Store.YES);
							if (title.length()<2){
								field1.setBoost((float) 0.0);
							}
							else {
								field1.setBoost((float)1.5);
							}
							luceneDoc.add(field1);
							
							
							TextField field2=new TextField("abstract", abstractString, Field.Store.YES);
							if (abstractString.length()<2){
								field2.setBoost((float) 0.0);
							}
							luceneDoc.add(field2);
							
							@SuppressWarnings("deprecation")
							Field field3=new Field ("url", files[i].toString(), Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.NO);
							field3.setBoost((float)0.0);
							luceneDoc.add(field3);
							
							
							try {
								writer.addDocument(luceneDoc);
								if (VERBOSE){
									System.out.println(lang+" "+(i+1)+" of "+files.length+": File to index: "+files[i].toString()+" Title: "+title+ " Abstract: "+abstractString);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						
						
					}
			}
			try {
				writer.commit();
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		Locale englishLocale = new Locale("en", "US");
		Locale.setDefault(englishLocale);	
		
	}
}
