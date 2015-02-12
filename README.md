CLIR Tips
===============

In this repository are some files from a study on how to support cross-language recommendations for an existing research paper recommender system. 

We focused on German, English and Spanish.

This was a project carried out in the University of Magdeburg Oct 2014-Feb 2015

Apart from surveying some available alternatives for implementing cross-language information retrieval, the aim of this project was to allow the comparison of different approaches to the cross-language support:

- Latent Semantic Analysis (based on user provided cross-language training data)
- Traditional indexing (with Lucene) and automated query translation (using one of several systems: Apertium, Google Translate API and Moses), further improved by:
	- User mediated query refinement
	- Translation post-processing: query expansion, by adding synonyms found in ontologies (after tagging with Stanfords PoS tagger) was tested. In the end only MultiWordNet support for English was included, but its Spanish section and GermaNet could be potentially used as well for those languages (after reaching agreements with author's institutions).
	- Other pre-processing improvements.
	- Other improvements at the point when results from different indexes are merged (i.e. boosting scores of hits in under-represented languages).

Additional notes:
Sense disambiguation for the query expansion was not supported (most common sense is chosen so far), but it could be added, also some recognition for searching phrases as opposed to words could also be an useful improvement over our approach.

Code for PoS tagging German and Spanish is included.

Code structure:
===============

Overall the software consists of the following packages:

Control package: 
Classes that encapsuate the Business logic:
- Main class, that starts the GUI or allows a console run.

Mgmt sub-package:
- LanguagesManager: Singleton in charge of the overall information on languages. 
- SpecificLanguageManager: Singleton looks after index and repo of a given language.
- RecommendationsHandler: Singleton that generates the recommendations.

Querytermgen sub-package: 
- Query term generator: Called by the RecommendationsHandler.

Index sub-package: 
- LSA indexer: LSA indexing.
- Per-Language indexer: Lucene indexing.
- Indexer: dummy super-class from which the 2 former inherit. 

Query sub-package: 
- Query handler: dummy super-class 
- Per-language query handler
- LSA query handler: Querying with LSA. 
- CL query handler: Cross-language query handler. Implements optimizations.

Utils sub-package: 
- TranslationHandler: Singleton allowing to use translation services of Apertium, Google Translate and/or Moses.

Model package: 
Data classes:
- LanguageFolder: Matches a language to its repository and index.
- PaperHit: Resulting item from a query.
- ResultsList: List of resulting items from a query.
- QueryTerms: Encapsulates a query string over each language, or one string that combines all languages. 
- IndexedDocLSA: Data for indexing a document in LSA.

View package: 
GUI related classes.

List of some open-sourced resources used:
===============

For title and abstract extraction from pdf files:
PDF-Inspector: https://github.com/Docear/PDF-Inspector
 
For matrix operations during LSA:
Apache Commons Math: https://commons.apache.org/proper/commons-math/

For automated translation:
Apertium Java Api: https://github.com/rmtheis/apertium-translator-java-api
Apertium Service: http://api.apertium.org/

Google Translate Api: https://code.google.com/p/google-api-translate-java/
Google Service: https://cloud.google.com/translate/docs

MOSES statistical machine translation system: http://www.statmt.org/moses/
Aditionally Giza++ statistical translation models toolkit was required for alignment: https://code.google.com/p/giza-pp/
Corpora used (still to be defined).

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

Upcoming work:
===============
Check and upload code for GUI.
Check and upload Moses model support.
Create proper documentation (JavaDocs), including explanations on all the server configurations.
Evaluation.

Future work:
===============
- Facilitate the server configurations so as to make the system more portable. 
- Adapting to an existing domain and testing infrastructure might be fruitful, specially since it would provide a clearer way for evaluating benefits and shortcomings of the methods used. 
- Optionally other configurations could be studied. The use of an inter-lingual net for supporting translation, while coupled with good word-sense predictors, might be of interest, since it can lead to better translations and more accurate post-processing. Additional improvements with semantics and NLP, as well as extensions to the current LSA model, would be of interest as well. 
- Finally, with a basis on the large amount of technologies available, aditional services could be further built for users, such as cross-languge query suggestions,  among others.
