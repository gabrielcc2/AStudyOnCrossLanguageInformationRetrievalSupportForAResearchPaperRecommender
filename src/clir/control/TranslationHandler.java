package clir.control;

import apertium.Translator;

import com.google.api.GoogleAPI;
import com.google.api.GoogleAPIException;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

/*Singleton class that handles translation. 
 * It uses different services: Apertium, Google Translate Api and a Moses Language Model. All of them 
 * are provided by running servers. Only Moses is configured locally.
 * The Google Translate Api is a paid service, therefore the keys are not included.
 * The Apertium key was found in an example code online, and does not belong to us (it is set in the Jar). But since 
 * the use of the service is free, this shouldnt cause any problem.
 * */

public class TranslationHandler {
	private static String REFERRER="https://github.com/gabrielcc2";
	private static String GOOGLE_KEY="_";
	
	private Boolean VERBOSE=true;
	@SuppressWarnings("unused")
	private Boolean DEBUG=true;
	
	/**Singleton instance of type TranslationHandler */
	private static TranslationHandler handler = null;
	
	/**Functions */
	
	/**Protected constructor function, to defeat instantiation. */
	protected TranslationHandler(){
		 // Exists only to defeat instantiation.
	}
	
	/**getInstance function, for singleton use*/
	public static TranslationHandler getInstance(){
		if (handler==null){
			handler= new TranslationHandler();
		}
		return handler;
	}

	public String translate(String config, String queryStr, String Lang1, String Lang2){
		String translatedText="";
		if (config.equals("apertium")){
			if (!Lang1.toLowerCase().equals("de")&&!Lang2.toLowerCase().equals("de")){ //German not supported
				Translator tr= new Translator();
				translatedText=tr.translate(queryStr, Lang1.toLowerCase(), Lang2.toLowerCase());   
				if (VERBOSE){
					System.out.println(queryStr+" translated with Apertium as: "+translatedText);
				}
				return translatedText;
			}
		}
		else if (config.equals("google")){
		 	GoogleAPI.setHttpReferrer(REFERRER);
		 	GoogleAPI.setKey(GOOGLE_KEY);
		 	
		 	Language language1;
		 	if (Lang1.toLowerCase().equals("de")){
		 		language1= Language.GERMAN;
		 	}
		 	else if (Lang1.toLowerCase().equals("en")){
		 		language1= Language.ENGLISH;
		 	}
		 	else{
		 		language1= Language.SPANISH;
		 	}
		 	
		 	Language language2; 
		 	if (Lang2.toLowerCase().equals("de")){
		 		language2= Language.GERMAN;
		 	}
		 	else if (Lang2.toLowerCase().equals("en")){
		 		language2= Language.ENGLISH;
		 	}
		 	else{
		 		language2= Language.SPANISH;
		 	}
		    
			try {
				translatedText = Translate.DEFAULT.execute(queryStr, language1, language2);
			} catch (GoogleAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		   if (VERBOSE){
			   System.out.println(queryStr+" translated with Google Translate API as: "+translatedText);
		   }

		   return translatedText;
		}
		//Moses by default
		if (VERBOSE){
			 System.out.println(queryStr+" translated with Moses model as: "+translatedText);
		}
		return translatedText;
	}

}
