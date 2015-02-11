package clir.control.query;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.linear.*;

import clir.control.mgmt.LanguagesManager;
import clir.model.IndexedDocLSA;
import clir.model.PaperHit;

public class LSAQueryHandler extends QueryHandler {
	/**Singleton instance of type LSAQueryHandler */
	private static LSAQueryHandler handler = null;
	
	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected LSAQueryHandler(){
		 // Exists only to defeat instantiation.
	}
	
	/**getInstance function, for singleton use*/
	public static LSAQueryHandler getInstance(){
		if (handler==null){
			handler= new LSAQueryHandler();
		}
		return handler;
	}
	
	public List<PaperHit> runQuery(List<String> queryLanguages, String query, int numExpectedResults){
		List<PaperHit> results = new ArrayList<PaperHit>();
		List<String> termsArray = new ArrayList<String>();
		int numSemanticDimensions=0;
		String indexFolder=LanguagesManager.getInstance().getIndexFolderLSA(queryLanguages);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(indexFolder+"/terms.txt"));
			String line = null;
			while ((line = br.readLine()) != null) {
				termsArray.add(line);
			}
			br.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] words= query.split(" ");
		String[] termsFound = new String[words.length];
		int[] termsFreq = new int[words.length];
		int numTerms=0;
		for (int k=0; k<words.length; k++){
			if (!words[k].isEmpty()&&words[k].length()>1&&termsArray.contains(words[k].toLowerCase())){//We only perform queries for recognized terms from the training data. Perhaps this could be improved.
				boolean termFound=false;
				for (int l=0; l<numTerms; l++){
					if(termsFound[l].equals(words[k].toLowerCase())){
						termsFreq[l]++;
						termFound=true;
					}
				}
				if (!termFound){
					termsFound[numTerms]=words[k].toLowerCase();
					termsFreq[numTerms]=1;
					numTerms++;
				}
			}
		}
		if (numTerms>0){
			
			double[][] freq= new double [termsArray.size()][1];
			for (int m=0; m<termsArray.size(); m++){
				freq[m][0]=0;
			}
			
			for (int m=0; m<numTerms; m++){
				freq[termsArray.indexOf(termsFound[m])][0]=termsFreq[m];
			}

			BufferedReader br2;
			try {
				br2 = new BufferedReader(new FileReader(indexFolder+"/config.txt"));
				String line = null;
				if ((line = br2.readLine()) != null) {
					numSemanticDimensions = Integer.valueOf(line.split(":")[1].trim());
				}
				br2.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			double[][] uMatrix= new double[termsArray.size()][numSemanticDimensions];
			BufferedReader br3;
			try {
				br3 = new BufferedReader(new FileReader(indexFolder+"/u.txt"));
				String line = null;
				int currentRow=0;
				while ((line = br3.readLine()) != null) {
					String [] splitted= line.split(" ");
					for (int k=0; k<numSemanticDimensions; k++){
						uMatrix[currentRow][k]=Double.valueOf(splitted[k]);
					}
					currentRow++;
				}
				br3.close();
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			double[][] inverseSMatrix= new double[numSemanticDimensions][numSemanticDimensions];
			BufferedReader br4;
			try {
				br4 = new BufferedReader(new FileReader(indexFolder+"/inverseS.txt"));
				String line = null;
				int currentRow=0;
				while ((line = br4.readLine()) != null) {
					String [] splitted= line.split(" ");
					for (int k=0; k<numSemanticDimensions; k++){
						inverseSMatrix[currentRow][k]=Double.valueOf(splitted[k]);
					}
					currentRow++;
				}
				br4.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			RealMatrix u= MatrixUtils.createRealMatrix(uMatrix);
			RealMatrix inverseS= MatrixUtils.createRealMatrix(inverseSMatrix);
			RealMatrix qt= MatrixUtils.createRealMatrix(freq).transpose();
			RealMatrix qnew= qt.multiply(u).multiply(inverseS);//A row of numSemanticDimensions columns.

			double[][] dMatrix=null;
			BufferedReader br5;
			int numDocs=0;
			List<IndexedDocLSA> indexedDocs = new ArrayList<IndexedDocLSA>();
			try {
				br5 = new BufferedReader(new FileReader(indexFolder+"/d.txt"));
				String line = null;
				int currentRow=0;
				if((line = br5.readLine()) != null) {
					numDocs= Integer.valueOf(line.split(":")[1].trim());
				}
				dMatrix= new double[numDocs][numSemanticDimensions];//Each row is a document
				while ((line = br5.readLine()) != null) {
					String [] splitted= line.split("§");
					IndexedDocLSA doc = new IndexedDocLSA(numSemanticDimensions);
					doc.setTitle(splitted[0]);
					doc.setUrl(splitted[1]);
					doc.setLang(splitted[2]);
					String [] splittedValues = splitted[3].split(" ");
					for (int k=0; k<numSemanticDimensions; k++){
						dMatrix[currentRow][k]=Double.valueOf(splittedValues[k]);
					}
					currentRow++;
					indexedDocs.add(doc);
				}
				br5.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int numResults;
			if (numDocs>numExpectedResults){
				numResults=numExpectedResults;
			}
			else{
				numResults=numDocs;
			}
			RealMatrix d= MatrixUtils.createRealMatrix(dMatrix);
			double [] relevanceScores= new double[numDocs];
			int [] indexesToRelevanceScores = new int[numDocs];
			for (int k=0; k<numDocs; k++){
				indexesToRelevanceScores[k]=k;
				RealVector v1= MatrixUtils.createRealVector(qnew.getRow(0));
				RealVector v2= MatrixUtils.createRealVector(d.getRow(k));
				relevanceScores[k]=(v1.dotProduct(v2) / (v1.getNorm() * v2.getNorm()));
			}
			
			//Bubble sort:			
			 int j;
		     boolean flag = true;   // set flag to true to begin first pass
		     double temp;   //holding variable
		     int tempPos;
		     
		     while ( flag )
		     {
		            flag= false;    //set flag to false awaiting a possible swap
		            for( j=0;  j < numDocs -1;  j++ )
		            {
		                   if ( relevanceScores[ j ] < relevanceScores[j+1] )   // change to > for ascending sort
		                   {
		                	   	   tempPos=j;
		                           temp = relevanceScores[ j ];                //swap elements
		                           
		                           indexesToRelevanceScores[j]=indexesToRelevanceScores[j+1];
		                           relevanceScores[ j ] = relevanceScores[ j+1 ];
		                           
		                           indexesToRelevanceScores[j+1]=tempPos;
		                           relevanceScores[ j+1 ] = temp;
		                           
		                          flag = true;              //shows a swap occurred  
		                  } 
		            } 
		      } 
		     for (int i=0; i<numResults; i++){
		    	 int indexOfDoc=indexesToRelevanceScores[i];
		    	 PaperHit hit= new PaperHit(i+1, indexedDocs.get(indexOfDoc).getTitle() ,
		    			 indexedDocs.get(indexOfDoc).getUrl(),
		    			 indexedDocs.get(indexOfDoc).getLang(),
		    			 (float)relevanceScores[i], numDocs);
		    	 results.add(hit);
		     }

		}
		return results;
		
	}
}
