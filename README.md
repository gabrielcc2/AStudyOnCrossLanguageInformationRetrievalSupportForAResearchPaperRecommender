CLIR Tips
===============

In this repository are some files from a study on how to support cross-language recommendations for an existing research paper recommender system. 

This was a project carried out in the University of Magdeburg Oct 2014-Jan 2015

Overall the software consists of the following packages:

Control: Classes that encapsuate the Business logic:
	Mgmt sub-package: With classes LanguagesManager (Singleton in charge of the overall information on languages), 			 SpecificLanguageManager (allowing to manage the index and repository of a language) and RecommendationsHandler (that generates the recommendations).
	Index sub-package: With classes LSA indexer and Per-Language indexer.
	Query sub-package: With classes: Query handler, per-language query handler and singletons: LSA query handler and CL query 		handler.
	Querytermgen sub-package: Containing only the query term generator.
	Utils sub-package: Containing the Translator handler singleton, allowing the use of Apertium, Google Translate and a Moses 		Language Model Server.
Model: Data classes, such as LanguageFolder (matching a language to its repository and index), PaperHit (items resulting from a query), ResultsList (list of results of a query), Query Terms.  
View: GUI related classes.

Apart from surveying the state-of-the-art on implementing cross-language systems, the aim of this project was to allow the comparison of different approaches to the cross-language support:

- Latent Semantic Analysis
- Traditional indexing and automated query translation (using one of several systems), further improved by:
	- User mediated query refinement
	- Translation post-processing: synonym adding through an ontology.
	- Other pre-processing improvments
	- Other improvements at the point when results from different indexes are merged.
