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

public class ApiHandler {
	
	/**
	 * Main function. No arguments used as input.
	 * 
	 * */
	public static void main(String[] args) {
		
		boolean useGUI=false;

		if (useGUI){
		}
		else{
			
			/**Defining the configuration required for recommending...*/
			RecommendationsHandler.getInstance().resetQueryFolder(); //Use default query folder
			
			RecommendationsHandler.getInstance().resetQueryLanguages(); //Specifying the languages to be recommended
			RecommendationsHandler.getInstance().addQueryLanguage("EN");
			
			RecommendationsHandler.getInstance().setUsesLSI(false); //Use LSI or not...

			/**Here we define the index. 
			 * On the first use we must define if we want to the default repository, by calling the resetRepository.
			 * Furthermore this takes as an input a boolean, saying if there is an index there or not...
			 * If not using the default repository, we can call setRepository passing as input
			 * the address of the new repository, and if there is an index there or not...*/
			LanguageHandler.getInstance("EN").resetRepository(false);
			
			/**Ask for the recommendations...*/
			List<PaperHit> recommendations = new ArrayList<PaperHit>();
			recommendations=RecommendationsHandler.getInstance().getRecommendations();

			/**Printing the recommendations*/
			if (recommendations.isEmpty()){
				System.out.println("No recommendations found.");
			}
			else {
				System.out.println("Number of hits: "+recommendations.get(0).getNumOfResults());
				for (int i=0; i<recommendations.size(); i++){
					System.out.println("***************************************************");
					String paper="Rank: "+recommendations.get(i).getRank().toString();
					paper+=" Title: "+recommendations.get(i).getTitle()+
					" Year: "+recommendations.get(i).getYear().toString()+
					" Authors: "+recommendations.get(i).getAuthors()+
					" Url: "+recommendations.get(i).getUrl()+
					" Relevance Score: "+recommendations.get(i).getRelevanceScore()+
					" Summary: "+recommendations.get(i).getSummary();
					System.out.println(paper);
				}
				System.out.println("***************************************************");
			}
		}
	}
}
