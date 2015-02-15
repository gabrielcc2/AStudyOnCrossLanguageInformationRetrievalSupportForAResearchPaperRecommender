package clir.control;

import java.util.ArrayList;
import java.util.List;

import clir.control.mgmt.LanguagesManager;
import clir.control.mgmt.RecommendationsHandler;
import clir.model.ResultsList;
import clir.view.CLIRTipsUI;


// TODO: Auto-generated Javadoc
/**
 * 
 *          This is the main class of the project. The control flow of the
 *          program starts and ends in the main method of this class. Creating
 *          other methods here is not advised.
 *          <p>
 *          As shipped it only starts the GUI. But by accessing the source code it can be manually changed
 *          to allow console runs. This could be useful for debugging and understanding the library. If expecting messages on this
 *          scenario, DEBUG_MODE and VERBOSE can be set to true in the classes which are used.
 *          <p>
 *          
 * @author Gabriel
 */

public class Main {
	
	/**
	 * Main function. No arguments used as input.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		
		boolean useGUI=true;
		boolean useLSI=false;
				
		if (useGUI){

			java.awt.EventQueue.invokeLater(new Runnable() {
	            public void run() {
	                new CLIRTipsUI().setVisible(true);
	            }});

		}
		else{
			
			/**Defining the general language support*/
			LanguagesManager.getInstance().addLanguage("EN");
			LanguagesManager.getInstance().addLanguage("ES");
		//	LanguagesManager.getInstance().addLanguage("DE");
			
						
			/**Where we'll store the resulting recommendations...*/
			ResultsList recommendations = new ResultsList();
			String folder="DEFAULT";
			
			if (useLSI){
					/**The index is explicitly created for LSA*/
					LanguagesManager.getInstance().createLSAIndex(LanguagesManager.getInstance().getLanguagesSupported(), 3);
				
					List<String> expectedLanguages= new ArrayList<String>();
					expectedLanguages.add("EN");
					
					List<String> queryLanguages= new ArrayList<String>();
					queryLanguages.add("ES");
			//		optionalQuery.add("ES");
					/**We ask for the recommendations*/
					
					recommendations.assign(
							RecommendationsHandler.getInstance().getRecommendationsLSA(
									queryLanguages,
									expectedLanguages, 
									folder,
									10));
			}
			else{
					/**The per-language indexes are explicitly created for LSA. 
					 * If this is not done, we can still use default indexes.
					 * Here we could also change the indexLocation*/
				
					LanguagesManager.getInstance().getSpecificManager("EN").createIndex(); 
					LanguagesManager.getInstance().getSpecificManager("ES").createIndex(); 
				
					//	LanguagesManager.getInstance().getSpecificManager("DE").createIndex(); 
					
					List<String> expectedLanguages= new ArrayList<String>();
					expectedLanguages.add("EN");
					
					List<String> queryLanguages= new ArrayList<String>();
					queryLanguages.add("ES");
					

					/**We ask for the recommendations*/
					recommendations.assign(
							RecommendationsHandler.getInstance().getRecommendations(
									queryLanguages,
									expectedLanguages,
									folder,
									10, true, "google", true, true));
			}
			
			/**Printing the recommendations*/
			System.out.println("\n*****************************************************************");
			if (recommendations.isEmpty()){
				System.out.println("No recommendations found.");
				System.out.println("*****************************************************************");
			}
			else {
				if (useLSI){
					System.out.println("Results produced using LSA:");
				}
				else{
					System.out.println("Results produced using Automatic Query Translation and Optimizations:");
				}
				System.out.println("Number of hits: "+recommendations.getPaperHits().get(0).getNumOfResults());
				for (int i=0; i<recommendations.getPaperHits().size(); i++){
					System.out.println("*****************************************************************");
					String paperHit="Rank: "+recommendations.getPaperHits().get(i).getRank().toString();
					paperHit+=" Title: "+recommendations.getPaperHits().get(i).getTitle()+
					" Url: "+recommendations.getPaperHits().get(i).getUrl()+
					" Relevance Score: "+recommendations.getPaperHits().get(i).getRelevanceScore();
					System.out.println(paperHit);
				}
				System.out.println("*****************************************************************");
			}
		}
	}
}
