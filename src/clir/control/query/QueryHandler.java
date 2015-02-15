package clir.control.query;

// TODO: Auto-generated Javadoc
/** Abstract super-class where common attributes and operations of an Per-language,
 * Cross-Language and Latent Semantic Query Handlers, can be placed.
 * 
 * @author Gabriel
 */
public class QueryHandler {
	
	/** The verbose. */
	protected Boolean VERBOSE=true;
	
	/** The debug. */
	protected Boolean DEBUG=true;
	
	/** The default number of results. */
	protected int DEFAULT_NUMBER_OF_RESULTS=10;
	
	/** The number of results. */
	protected int numberOfResults;
	
	/**
	 * Gets the number of expected results.
	 *
	 * @return the number of expected results
	 */
	public int getNumberOfExpectedResults(){
		return numberOfResults;
	}
	
	/**
	 * Sets the number of expected results.
	 *
	 * @param num the new number of expected results
	 */
	public void setNumberOfExpectedResults(int num){
		if (num>0){
			numberOfResults=num;
		}
	}
}
