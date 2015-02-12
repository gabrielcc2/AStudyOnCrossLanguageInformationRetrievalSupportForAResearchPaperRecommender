CLIR Tips
===============

In this repository are some files from a study on how to support cross-language recommendations for an existing research paper recommender system. 

We focus on German, English and Spanish.

This was a project carried out in the University of Magdeburg Oct 2014-Jan 2015

Overall the software consists of the following packages:

Control: Classes that encapsuate the Business logic:
	Mgmt sub-package: With classes LanguagesManager (Singleton in charge of the overall information on languages), 			 
                                                         SpecificLanguageManager (allowing to manage the index and repository of a language) and 
                                                         RecommendationsHandler (that generates the recommendations).
	Index sub-package: With classes LSA indexer and Per-Language indexer.
	Query sub-package: With classes: Query handler, per-language query handler and singletons: LSA query handler and CL query handler.
	Querytermgen sub-package: Containing only the query term generator.
	Utils sub-package: Containing the Translator handler singleton, allowing the use of Apertium, Google Translate and a Moses Language Model Server.

        Model: Data classes, such as LanguageFolder (matching a language to its repository and index), PaperHit (items resulting from a query), 
                                     ResultsList (list of results of a query), Query Terms and IndexedDocLSA.

        View: GUI related classes.

Apart from surveying some available alternatives for implementing cross-language information retrieval, the aim of this project was to allow the comparison of different approaches to the cross-language support:

- Latent Semantic Analysis (with user provided cross-language training data)
- Traditional indexing (with Lucene) and automated query translation (using one of several systems: Apertium, Google Translate API and Moses), further improved by:
	- User mediated query refinement
	- Translation post-processing: synonym adding through ontologies after tagging with Stanfords PoS tagger. Only MultiWordNet for english was used, but its spanish section and GermaNet could be potentially used as well (after agreements with author's institutions). Sense disambiguation for this task not supported (most common sense is chosen so far), but it could be added, also some recognition for searching phrases as opposed to words can also be of use.
	Note: This service is also hard to port since it relies on a MySQL server running a DB with the ontology.
	PoS tagging for German and Spanish is included.
	- Other pre-processing improvements.
	- Other improvements at the point when results from different indexes are merged (i.e. boosting scores of hits in under-represented languages).

List of some open-sourced resources used:

For title and abstract extraction from pdf files:
PDF-Inspector: https://github.com/Docear/PDF-Inspector
 
For matrix operations during LSA:
Apache Commons Math: https://commons.apache.org/proper/commons-math/

For automated translation:
Apertium Java Api: https://github.com/rmtheis/apertium-translator-java-api
Apertium Service: http://api.apertium.org/register.jsp

Google Translate Api: https://code.google.com/p/google-api-translate-java/
Google Service: https://cloud.google.com/translate/docs

MOSES statistical machine translation system,: http://www.statmt.org/moses/
Aditionally Giza++ statistical translation models toolkit was required for alignment: https://code.google.com/p/giza-pp/
Corpora used (still to be defined):

For PoS tagging in all languages:
Stanford's PoS tagger: http://nlp.stanford.edu/software/tagger.shtml
And specific PoS models: 
English model: 
wsj-0-18-bidirectional-distsim.tagger
Trained on WSJ sections 0-18 using a bidirectional architecture and
including word shape and distributional similarity features.
Penn Treebank tagset.
Performance:
97.28% correct on WSJ 19-21
(90.46% correct on unknown words)

German model (not used in current version):
german-fast.tagger
Lacks distributional similarity features.
Performance:
96.61% overall / 86.72% unknown.
Based on: 
german-hgc.tagger
Trained on the first 80% of the Negra corpus, which uses the STTS tagset.
The Stuttgart-Tübingen Tagset (STTS) is a set of 54 tags for annotating
German text corpora with part-of-speech labels, which was jointly
developed by the Institut für maschinelle Sprachverarbeitung of the
University of Stuttgart and the Seminar für Sprachwissenschaft of the
University of Tübingen. See: 
http://www.ims.uni-stuttgart.de/projekte/CQPDemos/Bundestag/help-tagset.html
This model uses features from the distributional similarity clusters
built over the HGC.
Performance:
96.90% on the first half of the remaining 20% of the Negra corpus (dev set)
(90.33% on unknown words)

Spanish model (not used in current version):
Spanish-distsim.tagger

For adding synonym sets as a form of post-processing query expansion:
MultiWordNet: http://multiwordnet.fbk.eu/english/home.php (English ontology kindly provided by email). 
Additionally we used a MySQL server running a database with the ontologies provided in .sql format.

For indexing and parsing:
Lucene: http://lucene.apache.org/

Information on specific versions, etc, can be checked in the code itself.

Pending work:
Check and upload code for GUI.
Check and upload Moses model support.
Create proper documentation (JavaDocs), including explanations on all the server configurations.
