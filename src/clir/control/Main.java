package clir.control;



import clir.control.mgmt.LanguagesManager;
import clir.control.mgmt.RecommendationsHandler;
import clir.model.ResultsList;


/**
 * 
 *          This is the main class of the project. The control flow of the
 *          program starts and ends in the main method of this class. Creating
 *          other methods here is not advised.
 *          <p>
 *          As shipped it only starts the GUI. But by accessing the source code it can be manually changed
 *          to allow console runs. This could be useful for debugging. If expecting messages on this
 *          scenario, DEBUG_MODE and VERBOSE can be set to true in the LanguageHandler and RecommendationsHandler classes.
 *          <p>
 *          
 * @author Gabriel
 */

public class Main {
	
	/**
	 * Main function. No arguments used as input.
	 * 
	 * */
	public static void main(String[] args) {
		
		boolean useGUI=false;
		boolean useLSI=false;
				
		if (useGUI){
		}
		else{
			
			/**Defining the general language support*/
			LanguagesManager.getInstance().addLanguage("EN");
			LanguagesManager.getInstance().addLanguage("ES");
		//	LanguagesManager.getInstance().addLanguage("DE");
			
						
			/**Where we'll store the resulting recommendations...*/
			ResultsList recommendations = new ResultsList();
	
			if (useLSI){
					/**The index is explicitly created for LSA*/
					LanguagesManager.getInstance().createLSAIndex(LanguagesManager.getInstance().getLanguagesSupported(), 3);
				
					/**We ask for the recommendations*/
					recommendations.assign(
							RecommendationsHandler.getInstance().getRecommendationsLSA(
									LanguagesManager.getInstance().getLanguagesSupported(), 
									10));
			}
			else{
					/**The per-language indexes are explicitly created for LSA. 
					 * If this is not done, we can still use default indexes.
					 * Here we could also change the indexLocation*/
				
					LanguagesManager.getInstance().getSpecificManager("EN").createIndex(); 
					LanguagesManager.getInstance().getSpecificManager("ES").createIndex(); 
				
					//	LanguagesManager.getInstance().getSpecificManager("DE").createIndex(); 
					
					/**We ask for the recommendations*/
					recommendations.assign(
							RecommendationsHandler.getInstance().getRecommendations(
									LanguagesManager.getInstance().getLanguagesSupported(), 
									10, true, "google", true, true));
			}
			
			/**Printing the recommendations*/
			System.out.println("\n*****************************************************************");
			if (recommendations.isEmptyPaperHits()){
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
