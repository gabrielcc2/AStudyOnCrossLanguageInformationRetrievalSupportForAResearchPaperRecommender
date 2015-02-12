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
		boolean useLSI=true;
				
		if (useGUI){
		}
		else{
			LanguagesManager.getInstance().addLanguage("EN");
			LanguagesManager.getInstance().addLanguage("ES");
		//	LanguagesManager.getInstance().addLanguage("DE");
			//LanguagesManager.getInstance().getSpecificManager("EN").createIndex();
			//LanguagesManager.getInstance().getSpecificManager("ES").createIndex();
		//	LanguagesManager.getInstance().getSpecificManager("DE").createIndex();
			
			/**Defining the configuration required for recommending...*/

						
			/**Ask for the recommendations...*/
			ResultsList recommendations = new ResultsList();
	
			if (useLSI){
				LanguagesManager.getInstance().createLSAIndex(LanguagesManager.getInstance().getLanguagesSupported(), 3);
				recommendations.assign(RecommendationsHandler.getInstance().getRecommendationsLSA(
						LanguagesManager.getInstance().getLanguagesSupported(), 
						10));
			}
			else{
			recommendations.assign(RecommendationsHandler.getInstance().getRecommendations(
					LanguagesManager.getInstance().getLanguagesSupported(), 
					10, false, "google", false, false));
			}
			
			/**Printing the recommendations*/
			if (recommendations.isEmptyPaperHits()){
				System.out.println("No recommendations found.");
			}
			else {
				if (useLSI){
					System.out.println("Results produced using LSI:");
				}
				else{
					System.out.println("Results produced using Automatic Query Translation and Optimizations:");
				}
				System.out.println("Number of hits: "+recommendations.getPaperHits().get(0).getNumOfResults());
				for (int i=0; i<recommendations.getPaperHits().size(); i++){
					System.out.println("***************************************************");
					String paperHit="Rank: "+recommendations.getPaperHits().get(i).getRank().toString();
					paperHit+=" Title: "+recommendations.getPaperHits().get(i).getTitle()+
					" Url: "+recommendations.getPaperHits().get(i).getUrl()+
					" Relevance Score: "+recommendations.getPaperHits().get(i).getRelevanceScore();
					System.out.println(paperHit);
				}
				System.out.println("***************************************************");
			}
		}
	}
}
