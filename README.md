CLIR Tips
===============

In this repository are some files from a study on how to support cross-language recommendations for an existing research paper recommender system. 

We focused on German, English and Spanish.

Unpublished paper available as pdf file in home folder of repository.

This was a project carried out in the University of Magdeburg Oct 2014-Feb 2015

Apart from surveying some available alternatives for implementing cross-language information retrieval, the aim of this project was to allow the comparison of different approaches to the cross-language support:

- Latent Semantic Analysis (based on user provided cross-language training data + a controlled vocabulary approach during indexing)
- Traditional per-language indexing (with Lucene) and automated query translation (using one of several systems: Apertium, Google Translate API and Moses), further improved by:
	- User mediated query refinement
	- Translation post-processing: Query expansion, by adding synonyms found in ontologies (after tagging with Stanfords PoS tagger) was tested. In the end only support for using MultiWordNet over its English resources was included. For Spanish, MultiWordNet could be also used, and GermaNet (http://www.sfs.uni-tuebingen.de/GermaNet/) for the German language (this requires an agreement with the author's). 
	- Additional improvements at the point when results from different indexes are merged (i.e. boosting scores of hits in under-represented languages).
	- Other pre-processing improvements.
	

Additional notes:
- At this stage our system represents a simple framework that could be used as a starting-point for comparing cross-language services happening at different processing stages, and their contribution to the end results.
- Sense disambiguation for the query expansion was not supported (most common sense is chosen so far), but it could be added. Some recognition for searching phrases as opposed to words could also be a useful improvement to our approach.
- Code for PoS tagging German and Spanish is included but not used.

Code structure:
===============

Overall the provided library consists of the following packages:

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

More information in the included JavaDocs and UML class-layout.

List of some open-source resources used:
===============

For title and plain-text extraction from pdf files:
- PDF-Inspector: https://github.com/Docear/PDF-Inspector
 
For matrix operations during LSA:
- Apache Commons Math: https://commons.apache.org/proper/commons-math/

For automated translation:
- Apertium Java Api: https://github.com/rmtheis/apertium-translator-java-api
- Apertium Web Service: http://api.apertium.org/

- Google Translate Api: https://code.google.com/p/google-api-translate-java/
- Google Web Service: https://cloud.google.com/translate/docs

- MOSES statistical machine translation system: http://www.statmt.org/moses/
- Aditionally Giza++ statistical translation models toolkit was required for alignment: https://code.google.com/p/giza-pp/
- Corpora used (still to be defined).

For PoS tagging in all languages:
- Stanford's PoS tagger: http://nlp.stanford.edu/software/tagger.shtml
Note that this required Java 1.8+

And specific PoS models: 
English model: 
- wsj-0-18-bidirectional-distsim.tagger

Trained on WSJ sections 0-18 using a bidirectional architecture and
including word shape and distributional similarity features.
Penn Treebank tagset.
Performance:
97.28% correct on WSJ 19-21
(90.46% correct on unknown words)

German model (not used in current version):
- german-fast.tagger

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
- Spanish-distsim.tagger

For adding synonym sets as a form of post-processing query expansion:
- MultiWordNet: http://multiwordnet.fbk.eu/english/home.php (English ontology kindly provided by email).
- Additionally we used a MySQL server running a database with the ontologies provided in .sql format.

For indexing and parsing:
- Lucene: http://lucene.apache.org/

Information on specific versions, authors, etc., can be found in the repository itself.

Known issues:
===============
- Portability needs improvement: Facilitate the server configurations so as to make the system more portable. 
- Field extraction with PDF Inspector must be improved. Buggy behaviour has been observed, such as breaking up words when extracting terms in German and Spanish. This must be corrected so we can consider indexing other fields (citations, keywords, authors, etc.) and their benefits/shortcomings. Perhaps other PDF parsers could be tested, as Lemur.
- Observed issues while using Apertium (one translation request EN->ES or ES->EN stalls from time to time).

Future work:
===============
- Extend LSI to accept new terms during indexing. Configure with realistic training data. Evaluate time for querying and improve if needed. If dictionary grows too much, perhaps feature selection could be done, so as to speed matrix calculations.
- Check and upload Moses model support, add this to documentation. Evaluate possibilities for using extended features.
- Use WordNets from other languages and extend with word-sense disambiguation and NER. This might lead to more accurate terms. Perhaps migrate to EuroWordNet.
- Consider alternatives for adding lexical/semantic support to query refinement.
- Test other boost-on-merge options.
- Consider pre-processing alternatives for each supported language.
- Comprehensive evaluation under a more realistic configuration + testing scheme.
- Additional improvements to the existing approaches with semantics and NLP, as well as extensions to the current LSA model, would be of interest too.

- Optionally other configurations could be studied. 
	- The use of aditional information available to the recommender system, as a way of supporting translation-disambiguation, could be of interest for it represents a stronger embedding of the system with the translation services, and a benefit almost exclusive to cross-language recommender systems (since, by comparison, most cross-language information retrieval systems rely on much more limited information for disambiguation). Several approaches could be tested:
		- A less embedded approach would be to provide the translation services with a larger body of text and then extract only the parts that are required. 
		- Also with Moses a bag-of-words approach could be used for it to return potential, instead of exact, translations. Each of these words can be assigned an associated weight, which serves to signal the most likely translation to the document retriever or the indexer. The weights can be further tuned with a knowledge or corpus-based approach. 
		- Inter-lingual nets coupled with good word-sense predictors, could be considered as well.
- Embedding the cross-language aspects into the inner workings of recommender systems. For this our query definition has to be extended so it includes any additional data that the recommender system might use needing translation. The matching itself can be performed in any way the recommender system does, and finally the merging can be adapted to suit the existing system. Of particular interest would be supporting translation of citation-related information (such as citation context), for this might enable the research paper recommender system to produce better cross-language recommendations, by having a multi-language characterization of how a paper is cited. This could be a fruitful area of research. 

-Considerations of relevance feedback could be added.

-Adding a language recognizer.
-Tests with other indexer systems: Wumpus, Indri (packed with Lemur project, for parsing pdfs)...

- Finally, with a basis on our work and the large amount of technologies available, aditional cross-language services could be further built for users, such as cross-languge query suggestions,  providing multi-lingual explanations for the cross-language recommendations, etc.
