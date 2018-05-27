package bgu.spl.mics;

public class purchaseOrderRequest implements Request<Receipt> {
	
	private String shoeType;
	private MicroService client;
	//private int amount;
	private boolean isDiscount;
	private int reqTick;
	//private Receipt result;
	/**
	 * purchaseOrderRequest the will send by a customer.
	 * @author Xps
	 *
	 */
	public purchaseOrderRequest( String shoeType, MicroService client,boolean isDiscount, int reqTick){
		this.shoeType=shoeType;
		this.client= client;
		this.isDiscount= isDiscount;
		this.reqTick = reqTick;
		
		
	}
	/**
	 * 
	 * @return the shoe type of the order
	 */
	public String getshoeType(){
		return shoeType;
	}
	/**
	 * 
	 * @return the client of the requst
	 */
	public MicroService getClient(){
		return client;
	}
	/**
	 * 
	 * @return if the coustomer want the shoe only in discount
	 */
	
	public boolean isDiscount(){
		return isDiscount;
	}
	/**
	 * 
	 * @return the tick of the request.
	 */
	public int getTick(){
		return reqTick;
	}
	
}
