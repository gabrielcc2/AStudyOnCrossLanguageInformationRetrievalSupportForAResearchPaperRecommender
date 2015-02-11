package clir.model;
/**A POJO that represents an indexed doc for LSA.
 */

public class IndexedDocLSA {

	private String title;
	private String url;
	private String lang;
	private double weights[];//Weights from the LSA indexing w.r.t. the terms list.

	public IndexedDocLSA(){
		weights=null;
	}
	
	public IndexedDocLSA(int size){
		weights= new double[size];
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
	
	public double[] getWeights(){
		return weights;
	}
	
	public int getSize(){
		if (weights==null){
			return 0;
		}
		return weights.length;
	}
	
	public double get(int i){
		if (weights==null){
			return 0;
		}
		return weights[i];
	}
	
	public void set(int i, double val){
		if (weights!=null){
			weights[i]=val;
		}
	}
}
