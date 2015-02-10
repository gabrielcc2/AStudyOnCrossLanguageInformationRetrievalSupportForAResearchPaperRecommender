package clir.control;


public class LSAIndexer extends Indexer {
	
		/**Singleton instance of type LSAIndexer */
		private static LSAIndexer indexer = null;
		
		/**Functions */
		
		/**Protected constructor function, to defeat instantiation. */
		protected LSAIndexer(){
			 // Exists only to defeat instantiation.
		}
		
		/**getInstance function, for singleton use*/
		public static LSAIndexer getInstance(){
			if (indexer==null){
				indexer= new LSAIndexer();
			}
			return indexer;
		}
		
		/**Function that creates a LSA index over all language repositories.*/
		public void createIndex(){
		}

}
