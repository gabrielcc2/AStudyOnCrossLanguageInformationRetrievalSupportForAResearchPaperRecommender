package clir.control.query;


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
}
