package bgu.spl.mics;
/**
 * the manager will send a restock request to the factory if there isnt any shoes in the stock
 * @author Xps
 *
 */
/**
 * constractur 
 * @author 
 *
 */
public class RestockRequest implements Request<Boolean> {
	private String shoeType;

	/**
	 * 
	 * @param ShoeType return 
	 */
	public RestockRequest(String ShoeType){
	this.shoeType=ShoeType;
	}

	public String getshoeType(){
		return this.shoeType;
	}
	
}
