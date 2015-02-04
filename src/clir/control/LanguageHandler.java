package clir.control;

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


public class LanguageHandler {
	private Boolean VERBOSE=true;
	private Boolean DEBUG=true;
	
	private String DEFAULT_EN_REPOSITORY="tmp/default_EN_repository";
	private String DEFAULT_DE_REPOSITORY="tmp/default_DE_repository";
	private String DEFAULT_ES_REPOSITORY="tmp/default_ES_repository";

	private String INDEX_EN_LOCATION="tmp/default_EN_index_location";
	private String INDEX_DE_LOCATION="tmp/default_DE_index_location";
	private String INDEX_ES_LOCATION="tmp/default_ES_index_location";
	
	/**Singleton instances of type LanguageHandler */
	private static LanguageHandler english= null;
	private static LanguageHandler german= null;
	private static LanguageHandler spanish= null;
	
	private File repository;
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
		return repository.toString();
	}
	
	/**Takes as an input a boolean indicating if there is an existing index 
	 * in the folder.*/
	public void setRepository(String repository, Boolean useExistingIndex){
		this.repository=new File (repository);
		if (this.equals(english) && this.repository.toString()==DEFAULT_EN_REPOSITORY){
			this.resetRepository(useExistingIndex);
		}
		else if (this.equals(german) && this.repository.toString()==DEFAULT_DE_REPOSITORY){
			this.resetRepository(useExistingIndex);
		}
		else if (this.equals(spanish) && this.repository.toString()==DEFAULT_ES_REPOSITORY){
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
			this.repository=new File(DEFAULT_EN_REPOSITORY);
			if (!this.indexCreated){
				this.createIndex();
			}
		}
		else if (this.equals(german)){
			this.repository=new File(DEFAULT_DE_REPOSITORY);
			if (!this.indexCreated){
				this.createIndex();				
			}
		}
		else if (this.equals(spanish)){
			this.repository=new File(DEFAULT_ES_REPOSITORY);
			if (!this.indexCreated){
				this.createIndex();
			}
		}
	}
	
	
	private void createIndex(){//It creates the standard index and also the LSI index.
	
		if (this.equals(english)){
			
			if (repository.exists()&& repository.isDirectory()) { //It checks if it is a directory (i.e. a folder)

					
					@SuppressWarnings("deprecation")
					Analyzer analyzer = new EnglishAnalyzer(Version.LUCENE_4_10_0);
					@SuppressWarnings("deprecation")
					IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
					iwc.setOpenMode(OpenMode.CREATE);
					Directory indexDir=null;
					IndexWriter writer=null;
					try {
						indexDir = FSDirectory.open(new File(INDEX_EN_LOCATION));
						writer = new IndexWriter(indexDir, iwc);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				
					File[] files = repository.listFiles(); //In this case we create an array with all the files and directories within the current folder.
					//Now it iterates over each element in the array.
					
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile() && files[i].getName().endsWith(".pdf")) { //For pdf files
								
								/*We start by extracting the relevant information...*/
								
								boolean noException=true;
								String result=null;
								try{
									org.docear.pdf.PdfDataExtractor extractor = new PdfDataExtractor(files[i]);
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

									
									Document luceneDoc= new Document();
									
									String title="";
									title+=result.substring(0, result.indexOf('|'));
									
									result=result.toLowerCase();
									result=result.substring(result.indexOf('|'), result.length());
									String abstractString="";
									if (result.indexOf("abstract")>0 && result.indexOf("key")>result.indexOf("abstract")){
										abstractString+=result.substring(result.indexOf("abstract")+8, result.indexOf("key"));
									}
									else if (result.indexOf("by")>0 && result.indexOf("copyright")>result.indexOf("by")){
										abstractString+=result.substring(result.indexOf("by"), result.indexOf("copyright"));
									}
									
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
											System.out.println(i+1+" of "+files.length+": File to index: "+files[i].toString()+" Title: "+title+ " Abstract: "+abstractString);
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
	
			//Pending: Create LSI index
			this.indexCreated=true;	
		}
		else if (this.equals(german)){
			
			if (repository.exists()&& repository.isDirectory()) { //It checks if it is a directory (i.e. a folder)
				
				Locale germanLocale = new Locale("de", "DE");
				Locale.setDefault(germanLocale);
				
				@SuppressWarnings("deprecation")
				Analyzer analyzer = new GermanAnalyzer(Version.LUCENE_4_10_0);
				@SuppressWarnings("deprecation")
				IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
				iwc.setOpenMode(OpenMode.CREATE);
				Directory indexDir=null;
				IndexWriter writer=null;
				try {
					indexDir = FSDirectory.open(new File(INDEX_DE_LOCATION));
					writer = new IndexWriter(indexDir, iwc);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			
				File[] files = repository.listFiles(); //In this case we create an array with all the files and directories within the current folder.
				//Now it iterates over each element in the array.
				
				for (int i = 0; i < files.length; i++) {
					if (files[i].isFile() && files[i].getName().endsWith(".pdf")) { //For pdf files
							
							/*We start by extracting the relevant information...*/
							
							boolean noException=true;
							String result=null;
							try{
								org.docear.pdf.PdfDataExtractor extractor = new PdfDataExtractor(files[i]);
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

								
								Document luceneDoc= new Document();
								
								String title="";
								title+=result.substring(0, result.indexOf('|'));
								
								result=result.toLowerCase();
								result=result.substring(result.indexOf('|'), result.length());
								String abstractString="";
								if (result.indexOf("zusammenfassung")>0 && result.indexOf("schlagw")>result.indexOf("zusammenfassung")){
									abstractString+=result.substring(result.indexOf("zusammenfassung")+15, result.indexOf("schlagw"));
								}
								
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
										System.out.println(i+1+" of "+files.length+": File to index: "+files[i].toString()+" Title: "+title+ " Abstract: "+abstractString);
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

			//Pending: Create LSI index
			this.indexCreated=true;				
		}
		else if (this.equals(spanish)){
			
				Locale spanishLocale = new Locale("es", "ES");
				Locale.setDefault(spanishLocale);
			
				if (repository.exists()&& repository.isDirectory()) { //It checks if it is a directory (i.e. a folder)


					
					
					@SuppressWarnings("deprecation")
					Analyzer analyzer = new SpanishAnalyzer(Version.LUCENE_4_10_0);
					@SuppressWarnings("deprecation")
					IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);
					iwc.setOpenMode(OpenMode.CREATE);
					Directory indexDir=null;
					IndexWriter writer=null;
					try {
						indexDir = FSDirectory.open(new File(INDEX_ES_LOCATION));
						writer = new IndexWriter(indexDir, iwc);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	
				
					File[] files = repository.listFiles(); //In this case we create an array with all the files and directories within the current folder.
					//Now it iterates over each element in the array.
					
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile() && files[i].getName().endsWith(".pdf")) { //For pdf files
								
								/*We start by extracting the relevant information...*/
								
								boolean noException=true;
								String result=null;
								try{
									org.docear.pdf.PdfDataExtractor extractor = new PdfDataExtractor(files[i]);
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

									
									Document luceneDoc= new Document();
									
									String title="";
									title+=result.substring(0, result.indexOf('|'));
									
									result=result.toLowerCase();
									result=result.substring(result.indexOf('|'), result.length());
									String abstractString="";
									if (result.indexOf("summary")>0 && result.indexOf("key")>result.indexOf("summary")){
										abstractString+=result.substring(result.indexOf("summary")+7, result.indexOf("key"));
									}
									else if (result.indexOf("abstract")>0 && result.indexOf("key")>result.indexOf("abstract")){
										abstractString+=result.substring(result.indexOf("abstract")+8, result.indexOf("key"));
									}
									else if (result.indexOf("resum")>0 && result.indexOf("palabras clave")>result.indexOf("resum")){
										abstractString+=result.substring(result.indexOf("resum")+7, result.indexOf("palabras clave"));
									}

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
											System.out.println(i+1+" of "+files.length+": File to index: "+files[i].toString()+" Title: "+title+ " Abstract: "+abstractString);
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
	
				//Pending: Create LSI index
				this.indexCreated=true;
			
		}
		
	}
	
	public String getIndexLocation(){
		if (this.equals(english)){
			return INDEX_EN_LOCATION;
		}
		else if (this.equals(german)){
			return INDEX_DE_LOCATION;
		}
		else if (this.equals(spanish)){
			return INDEX_ES_LOCATION;
		}
		return null;
	}
	
}
