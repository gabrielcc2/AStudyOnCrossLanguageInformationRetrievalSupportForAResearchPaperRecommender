package clir.model;
// TODO: Auto-generated Javadoc
/**A POJO that represents an indexed doc for LSA.
 * 
 * @author Gabriel
 */

public class IndexedDocLSA {

	/** The title. */
	private String title;
	
	/** The url. */
	private String url;
	
	/** The lang. */
	private String lang;
	
	/** The weights. */
	private double weights[];//Weights from the LSA indexing w.r.t. the terms list.

	/**
	 * Instantiates a new indexed doc lsa.
	 */
	public IndexedDocLSA(){
		weights=null;
	}
	
	/**
	 * Instantiates a new indexed doc lsa.
	 *
	 * @param size the size
	 */
	public IndexedDocLSA(int size){
		weights= new double[size];
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	
	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url the new url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the lang.
	 *
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * Sets the lang.
	 *
	 * @param lang the new lang
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	
	/**
	 * Gets the weights.
	 *
	 * @return the weights
	 */
	public double[] getWeights(){
		return weights;
	}
	
	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize(){
		if (weights==null){
			return 0;
		}
		return weights.length;
	}
	
	/**
	 * Gets the.
	 *
	 * @param i the i
	 * @return the double
	 */
	public double get(int i){
		if (weights==null){
			return 0;
		}
		return weights[i];
	}
	
	/**
	 * Sets the.
	 *
	 * @param i the i
	 * @param val the val
	 */
	public void set(int i, double val){
		if (weights!=null){
			weights[i]=val;
		}
	}
}
