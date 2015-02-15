package clir.control.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.linear.LUDecomposition;
import org.docear.pdf.PdfDataExtractor;

import clir.control.mgmt.LanguagesManager;
import clir.model.IndexedDocLSA;

// TODO: Auto-generated Javadoc
/**
 * The Class LSAIndexer. A singleton. It implements the functionality of Latent Semantic indexing, based on matix decomposition 
 * over cross-language training data, and folding-in the queries and new documents to this vector-space.
 *
 * The class is composed fundamentally by the createIndex function.
 * 
 * The currrent approach used controlled vocabulary (no new terms are added). This could be improved in future works.
 * 
 * The generated index consists of 5 files:
 * Terms.txt: a terms list (also postings list), 
 * Config.txt: Recording number of semantic dimensions and training data used.
 * d.txt: A representantion of the documents in the semantic space, next to meta-data allowing their identification (title, url, language). 
 * It goes from documents to semantic dimensions.
 * u.txt: The u matrix, going from terms to semantic dimensions.
 * inverseS.txt: The dimxdim inverse S matrix, completing the definition of the decomposition.
 * 
 * Apache commons math used here.
 * 
 *  Extraction from PDFs is carried out here.
 * 
 *  @author Gabriel
 */
public class LSAIndexer extends Indexer {
	
		/** Singleton instance of type LSAIndexer. */
		private static LSAIndexer indexer = null;
	
		/**
		 * Functions.
		 */
		
		/**Protected constructor function, to defeat instantiation. */
		protected LSAIndexer(){
			 // Exists only to defeat instantiation.
		}
		
		/**
		 * getInstance function, for singleton use.
		 *
		 * @return single instance of LSAIndexer
		 */
		public static LSAIndexer getInstance(){
			if (indexer==null){
				indexer= new LSAIndexer();
			}
			return indexer;
		}
		
		/**
		 * Function that creates a LSA index over all language repositories.
		 * 
		 * In our approach the Vector-Space for the LSA is defined by an user-provided
		 * cross-language training data, consisting of a data.txt file, with text in each line
		 * corresponding to more than one of the languages defined.
		 * Each line is mapped as a document.
		 * 
		 * After the initialization of the Vector-Space: A= U x S x Vt, we perform a reduction
		 * of semantic dimensions.
		 * 
		 * Then we iterate through all repositories. The way of considering new documents is called 
		 * folding-in. And basically the representation of the new document would be based 
		 * on their frequencies of terms (d), following this: dnew= dT x U x inverseOf(A).
		 * 
		 * Since its been observed that adding new documents or terms to this vector-space doesn't change
		 * it, we decided not to accept new terms after the training (THIS SHOULD BE REVISED) and
		 * to store the indexed documents separately, so the basic dummy documents don't interfere with the querying.
		 *  
		 * 
		 * Notes: 
		 * - Assumes the numDimensions passed is correct for matrix decomposition (lesser than the number od documents).
		 * - So far must be called only once during the execution of the program.
		 *
		 * @param queryLanguages the query languages
		 * @param trainingData the cross-language training data (a folder to the data in a file of named data.txt)
		 * @param indexFolder the index folder
		 * @param numDimensions the number of semantic dimensions
		 */
		public void createIndex(List<String> queryLanguages, String trainingData, String indexFolder, int numDimensions){
		
			if (VERBOSE){
				System.out.println("\n*****************************************************************");
				System.out.println("Indexing with LSA:\n*****************************************************************");
			}
			
			/**First we read the training data only to establish the term index*/
			File td = new File(trainingData+"/data.txt");
			List<String> terms= new ArrayList<String>();
			FileInputStream fis;
			try {
				fis = new FileInputStream(td);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			//Construct BufferedReader from InputStreamReader
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
		 
			String line = null;
			int numDocs=0;
			try {
				while ((line = br.readLine()) != null) {
					numDocs++;
					String[] potentialTerms= line.split(" ");
					for (int i=0; i<potentialTerms.length; i++){
						if (!potentialTerms[i].isEmpty()&&potentialTerms[i].length()>1){
							if (!terms.contains(potentialTerms[i].toLowerCase())){
								terms.add(potentialTerms[i].toLowerCase());
							}
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		 
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (numDocs<=0){
				return;//No training possible
			}
			
			
			File td2 = new File(trainingData+"/data.txt");
			FileInputStream fis2;
			try {
				fis2 = new FileInputStream(td2);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			
			/**On the second time we read the training data to construct the vector representations of documents*/
			
			//Construct BufferedReader from InputStreamReader
			BufferedReader br2 = new BufferedReader(new InputStreamReader(fis2));
		 
			line = null;
			double[][] matrixLSA= new double[terms.size()][numDocs];
			Collections.sort(terms);
			for (int i=0; i<terms.size(); i++){
				for (int j=0; j<numDocs; j++){
					matrixLSA[i][j]=0;
				}
			}
			int currDoc=0;
			try {
				while ((line = br2.readLine()) != null) {
					String[] potentialTerms= line.split(" ");
					for (int i=0; i<potentialTerms.length; i++){
						if (!potentialTerms[i].isEmpty()&&potentialTerms[i].length()>1){
							matrixLSA[terms.lastIndexOf(potentialTerms[i].toLowerCase())][currDoc]++;
						}
					}
					currDoc++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		 
			try {
				br2.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			/*Note the commented ifs, we are assuming that the dimensions selected fit the criteria.
			 * (Most likely for our testing scenario)*/
			
		//	if(terms.size()>numDimensions){//We can do dimensionality reduction over the matrix...

			RealMatrix matrixLSAReal= MatrixUtils.createRealMatrix(matrixLSA);
			SingularValueDecomposition decomposer = new SingularValueDecomposition(matrixLSAReal);
				

			//	if (decomposer.getS().getColumnDimension()>numDimensions){//We can definitively do dimensionality reduction.

			int[] reduceArray = new int[numDimensions];
			for (int i=0; i<numDimensions; i++){
				reduceArray[i]=i;
			}
			
			int uNumRows= decomposer.getU().getRowDimension();
			int[] keepArray_URows = new int[uNumRows];
			for (int i=0; i<uNumRows; i++){
				keepArray_URows[i]=i;
			}
					
			int vtNumCols= decomposer.getVT().getColumnDimension();
			int[] keepArray_VtCols = new int[vtNumCols];
			for (int i=0; i<vtNumCols; i++){
				keepArray_VtCols[i]=i;
			}
					
			//We reduce U	
			RealMatrix u=decomposer.getU().getSubMatrix(keepArray_URows, reduceArray);
					
			//We reduce S
			RealMatrix s=decomposer.getS().getSubMatrix(reduceArray, reduceArray);
					
			//We reduce Vt (Not used, since we are not accepting terms)
	//		RealMatrix vt=decomposer.getVT().getSubMatrix(reduceArray, keepArray_VtCols);
					
			//We calculate the A matrix for the reduced space of the cross-language, user provided, training data
			RealMatrix inverseS= new LUDecomposition(s).getSolver().getInverse();
							
			//Now we can add the mono-lingual documents, based on the existing A matrix...
			List<IndexedDocLSA> indexArray = new ArrayList<IndexedDocLSA>();
			
			for (int j=0; j<queryLanguages.size() ;j++){
				
				String repoLocation= LanguagesManager.getInstance().getSpecificManager(queryLanguages.get(j)).getRepository();
				
				File repository = new File(repoLocation);
				
				if (repository.exists()&& repository.isDirectory()) { //It checks if it is a directory (i.e. a folder)
					
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
								if (noException){//We can index the document

										String title="";
										title+=result.substring(0, result.indexOf('|'));
									
										result=result.toLowerCase();
										result=result.substring(result.indexOf('|'), result.length());
										
										String abstractString="";
									
										if (queryLanguages.get(j).equals("DE")){							
											if (result.indexOf("zusammenfassung")>0 && result.indexOf("schlagw")>result.indexOf("zusammenfassung")){
												abstractString+=result.substring(result.indexOf("zusammenfassung")+15, result.indexOf("schlagw"));
											}
										}
										else if (queryLanguages.get(j).equals("ES")){
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
										
										
										String featuresToIndex=title+" "+abstractString;
										
										/**This house-keeping is needed since we don't use Lucene's Analyzer for cleaning */
										featuresToIndex=featuresToIndex.replace(".", " ");
										featuresToIndex=featuresToIndex.replace(",", " ");
										featuresToIndex=featuresToIndex.replace(":", " ");
										featuresToIndex=featuresToIndex.replace("(", " ");
										featuresToIndex=featuresToIndex.replace(")", " ");
										featuresToIndex=featuresToIndex.replace("©", " ");
										featuresToIndex=featuresToIndex.replace("*", " ");
										featuresToIndex=featuresToIndex.replace("[", " ");
										featuresToIndex=featuresToIndex.replace("]", " ");
										featuresToIndex=featuresToIndex.replace("|", " ");
										featuresToIndex=featuresToIndex.replace(" ", "");
										featuresToIndex=featuresToIndex.replace("", "");
										featuresToIndex=featuresToIndex.replace("_", " ");
										featuresToIndex=featuresToIndex.toLowerCase();
										
										String[] words= featuresToIndex.split(" ");
										
										String[] termsFound = new String[words.length];
										int[] termsFreq = new int[words.length];
										int numTerms=0;
										for (int k=0; k<words.length; k++){
											if (!words[k].isEmpty()&&words[k].length()>1&&terms.contains(words[k].toLowerCase())){//We only index recognized terms from the training data. Perhaps this could be improved.
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
										if (VERBOSE){
											System.out.println("File indexed with LSA: "+title+" Number of terms (from training data) found for LSA indexing:"+numTerms);
										}
										if (numTerms>0){

											IndexedDocLSA doc = new IndexedDocLSA(numDimensions);
											doc.setTitle(title);
											doc.setUrl(files[i].getAbsolutePath().toString());
											doc.setLang(queryLanguages.get(j));

											
											double[][] freq= new double [terms.size()][1];
											for (int m=0; m<terms.size(); m++){
												freq[m][0]=0;
											}
											
											for (int m=0; m<numTerms; m++){
												freq[terms.indexOf(termsFound[m])][0]=termsFreq[m];
											}
										
											
											RealMatrix dt= MatrixUtils.createRealMatrix(freq).transpose();
											RealMatrix dnew= dt.multiply(u).multiply(inverseS);


											double[] weights= dnew.getRow(0);
											for (int m=0; m<weights.length; m++){
												doc.set(m, weights[m]);
											}
											indexArray.add(doc);
										}
								}//Closes no exception when accessing the document
						}//Closes if pdf
					}//Closes loop over files in folder
				}//Closes folder checking
			}//Closes iteration over query languages.
			
			File oldTermsFile = new File(indexFolder+"/terms.txt");
			Boolean justCreated= false;
			if(!oldTermsFile.exists()) {
			    try {
					oldTermsFile.createNewFile();
					justCreated=true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else{
				oldTermsFile.delete();
			}
			File newTermsFile=null;
			if (justCreated){
				newTermsFile=oldTermsFile;
			}
			else{
				newTermsFile=new File(indexFolder+"/terms.txt");
				if(!newTermsFile.exists()) {
				    try {
						newTermsFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
			
			try {
				FileWriter oTermsFile = new FileWriter(newTermsFile, false);
				for (int n=0; n<terms.size(); n++){
					oTermsFile.write(terms.get(n).toString()+"\n");
				}
				oTermsFile.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			File oldUFile = new File(indexFolder+"/u.txt");
			justCreated= false;
			if(!oldUFile.exists()) {
			    try {
			    	oldUFile.createNewFile();
					justCreated=true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else{
				oldUFile.delete();
			}
			File newUFile=null;
			if (justCreated){
				newUFile=oldUFile;
			}
			else{
				newUFile=new File(indexFolder+"/u.txt");
				if(!newUFile.exists()) {
				    try {
						newUFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
			
			try {
				FileWriter oUFile = new FileWriter(newUFile, false);
				for (int n=0; n<u.getRowDimension(); n++){
					for (int o=0; o<u.getColumnDimension()-1; o++){
						oUFile.write(String.valueOf(u.getEntry(n,o)+" "));
					}
					oUFile.write(String.valueOf(u.getEntry(n,u.getColumnDimension()-1))+"\n");
				}
				oUFile.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			File oldInverseSFile = new File(indexFolder+"/inverseS.txt");
			justCreated= false;
			if(!oldInverseSFile.exists()) {
			    try {
			    	oldInverseSFile.createNewFile();
					justCreated=true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else{
				oldInverseSFile.delete();
			}
			File newInverseSFile=null;
			if (justCreated){
				newInverseSFile=oldInverseSFile;
			}
			else{
				newInverseSFile=new File(indexFolder+"/inverseS.txt");
				if(!newInverseSFile.exists()) {
				    try {
						newInverseSFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
			
			try {
				FileWriter oInverseAFile = new FileWriter(newInverseSFile, false);
				for (int n=0; n<inverseS.getRowDimension(); n++){
					for (int o=0; o<inverseS.getColumnDimension()-1; o++){
						oInverseAFile.write(String.valueOf(inverseS.getEntry(n,o)+" "));
					}
					oInverseAFile.write(String.valueOf(inverseS.getEntry(n,inverseS.getColumnDimension()-1))+"\n");
				}
				oInverseAFile.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			File oldDFile = new File(indexFolder+"/d.txt");
			justCreated= false;
			if(!oldDFile.exists()) {
			    try {
			    	oldDFile.createNewFile();
					justCreated=true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else{
				oldDFile.delete();
			}
			File newDFile=null;
			if (justCreated){
				newDFile=oldDFile;
			}
			else{
				newDFile=new File(indexFolder+"/d.txt");
				if(!newDFile.exists()) {
				    try {
						newDFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
			
			try {
				FileWriter oDFile = new FileWriter(newDFile, false);
				for (int n=0; n<indexArray.size(); n++){
					oDFile.write(indexArray.get(n).getTitle()+"§"+indexArray.get(n).getUrl()+"§"+indexArray.get(n).getLang()+"§");
					double[] weights =indexArray.get(n).getWeights();
					for (int o=0; o<weights.length-1; o++){
						oDFile.write(String.valueOf(weights[o])+" ");
					}
					oDFile.write(String.valueOf(weights[weights.length-1])+"\n");
				}
				oDFile.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 

			File oldConfigFile = new File(indexFolder+"/config.txt");
			justCreated= false;
			if(!oldConfigFile.exists()) {
			    try {
			    	oldConfigFile.createNewFile();
					justCreated=true;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
			else{
				oldConfigFile.delete();
			}
			File newConfigFile=null;
			if (justCreated){
				newConfigFile=oldConfigFile;
			}
			else{
				newConfigFile=new File(indexFolder+"/config.txt");
				if(!newConfigFile.exists()) {
				    try {
						newConfigFile.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} 
			}
			
			try {
				FileWriter oConfigFile = new FileWriter(newConfigFile, false);
				oConfigFile.write("Number of semantic dimensions: "+numDimensions+"\n");
				oConfigFile.write("Training data used: "+trainingData+"/data.txt\n");
				oConfigFile.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}//Closes createIndex function.
			
}


