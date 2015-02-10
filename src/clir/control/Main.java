package clir.control;
import java.util.ArrayList;
import java.util.List;

import clir.model.PaperHit;

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

		
		if (useGUI){
		}
		else{
			LanguagesManager.getInstance().addLanguage("EN");
			LanguagesManager.getInstance().addLanguage("ES");
			//LanguagesManager.getInstance().addLanguage("DE");
			LanguagesManager.getInstance().getSpecificManager("EN").createIndex();
			LanguagesManager.getInstance().getSpecificManager("ES").createIndex();
			//LanguagesManager.getInstance().getSpecificManager("DE").createIndex();
			
			/**Defining the configuration required for recommending...*/

						
			/**Ask for the recommendations...*/
			List<PaperHit> recommendations = new ArrayList<PaperHit>();
			recommendations.addAll(RecommendationsHandler.getInstance().getRecommendations(
					LanguagesManager.getInstance().getLanguagesSupported(), 
					10, false, "google", false, false));

			/**Printing the recommendations*/
			if (recommendations.isEmpty()){
				System.out.println("No recommendations found.");
			}
			else {
				System.out.println("Number of hits: "+recommendations.get(0).getNumOfResults());
				for (int i=0; i<recommendations.size(); i++){
					System.out.println("***************************************************");
					String paperHit="Rank: "+recommendations.get(i).getRank().toString();
					paperHit+=" Title: "+recommendations.get(i).getTitle()+
					" Url: "+recommendations.get(i).getUrl()+
					" Relevance Score: "+recommendations.get(i).getRelevanceScore();
					System.out.println(paperHit);
				}
				System.out.println("***************************************************");
			}
		}
	}
}
