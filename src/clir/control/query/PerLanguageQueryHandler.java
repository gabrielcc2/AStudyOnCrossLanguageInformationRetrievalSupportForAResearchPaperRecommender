package clir.control.query;

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

import clir.control.mgmt.LanguagesManager;
import clir.model.PaperHit;

public class PerLanguageQueryHandler extends QueryHandler{
	
	private String lang;
	
	public PerLanguageQueryHandler(String lang){
		this.lang=lang;
		numberOfResults=DEFAULT_NUMBER_OF_RESULTS;
	}
	
	public PerLanguageQueryHandler(String lang, int numExpectedResults){
		this.lang=lang;
		this.numberOfResults=numExpectedResults;
	}

	@SuppressWarnings("deprecation")
	public List<PaperHit> runQuery(String query){
		List<PaperHit> results= new ArrayList<PaperHit>();
		if (LanguagesManager.getInstance().isSupported(lang) && query.length()>2){
			Directory indexFolder=null;
			String indexLocation = LanguagesManager.getInstance().getSpecificManager(lang).getIndexFolder();
			try {
				indexFolder=FSDirectory.open(new File(indexLocation));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Error: Query not performed: Invalid directory.");
			}
			if (lang.equals("EN")){
				Analyzer sa = new EnglishAnalyzer(Version.LUCENE_4_10_0);
				MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "title", "abstract"}, sa);
				Query q=null;
				try {
					if (VERBOSE){
						System.out.println("Query String EN: "+query);
					}
					q = multiFieldQP.parse(MultiFieldQueryParser.escape(query));
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
						results.add(new PaperHit(rank, title, url, "EN", relevanceScore, resultingDocs.totalHits));
					}
				}
			}
			else if (lang.equals("DE")){
				Analyzer sa = new GermanAnalyzer(Version.LUCENE_4_10_0);
				MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "title", "abstract"}, sa);
				Query q=null;
				try {
					if (VERBOSE){
						System.out.println("Query String DE: "+query);
					}
					q = multiFieldQP.parse(MultiFieldQueryParser.escape(query));
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
				
						results.add(new PaperHit(rank, title, url, "DE", relevanceScore, resultingDocs.totalHits));
					}
				}
			}
			else{ //By default spanish
				
				Analyzer sa = new SpanishAnalyzer(Version.LUCENE_4_10_0);
				MultiFieldQueryParser multiFieldQP = new MultiFieldQueryParser(new String[] { "title", "abstract"}, sa);
				Query q=null;
				try {
					if (VERBOSE){
						System.out.println("Query String ES: "+query);
					}
					q = multiFieldQP.parse(MultiFieldQueryParser.escape(query));
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
				
						results.add(new PaperHit(rank, title, url, "ES", relevanceScore, resultingDocs.totalHits));
					}
				}	
			}
		}
		return results;//If list is empty: language not supported, or empty results.
	}

}
