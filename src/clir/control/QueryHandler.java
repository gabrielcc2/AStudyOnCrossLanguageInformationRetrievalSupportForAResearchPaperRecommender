package clir.control;

/** Abstract super-class where common attributes and operations of an Per-language,
 * Cross-Language and Latent Semantic Query Handlers, can be placed.
 */
public class QueryHandler {
	protected Boolean VERBOSE=true;
	protected Boolean DEBUG=true;
	
	protected int DEFAULT_NUMBER_OF_RESULTS=10;
	protected int numberOfResults;
	
	public int getNumberOfExpectedResults(){
		return numberOfResults;
	}
	
	public void setNumberOfExpectedResults(int num){
		if (num>0){
			numberOfResults=num;
		}
	}
}
