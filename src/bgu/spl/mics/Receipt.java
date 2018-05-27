package bgu.spl.mics;

public class Receipt {

	private String seller;//the name of the service which issued the receipt
	private String customer;// the name of the service this receipt issued to (the client name or â€œstoreâ€�)
	private String shoeType; // shoeType
	private boolean discount;// indicating if the shoe was sold at a discounted price
	private int issuedTick; //tick in which this receipt was issued (upon completing the corresponding request)
	private int requestTick;// tick in which the customer requested to buy the shoe
	private int amountSold;//the amount of shoes sold
	/**
	 * object that creats after a selling accured ,from the selling services or from the factory.
	 * @param seller the service thats creat the receipt.
	 * @param customer the customer thats orderd the services.
	 * @param shoeType shoe type thats orderd
	 * @param discount if the selling was in discount or not
	 * @param issuedTick when the recipetwas created 
	 * @param requestTick when the invite accurde.
	 * @param amountSold the amount that was deliverd
	 */
	public Receipt(String seller,String customer,String shoeType,boolean discount,int issuedTick, int requestTick,int amountSold ){
		
		this.seller= seller;
		this.customer= customer;
		this.shoeType= shoeType;
		this.discount= discount;
		this.issuedTick= issuedTick;
		this.requestTick= requestTick;
		this.amountSold = amountSold;
		
	}
		
		//getters
		/**
		 * 
		 * @return the seller
		 */
	public String getSeller(){
		
		return seller;
	}
	/**
	 * 
	 * @return the costumer
	 */
	
	public String getCustomer(){
		
		return customer;
	}
	/**
	 * 
	 * @return the shoe type
	 */
	public String getShoeType(){
	
	return shoeType;
	}
/**
 * 
 * @return if the purches was in discount or not
 */
	public boolean getDiscount(){
		
		return discount;
	}
	/**
	 * 
	 * @return the time the requset completed
	 */
	public int getIssuedTick(){
		
		return issuedTick;
	}
	/**
	 * 
	 * @return the requst time
	 */
	public int getRequestTick(){
		
		return requestTick;
	}
	/**
	 * 
	 * @return the amount that was sold
	 */
	public int getAmountSold(){
	
	return amountSold;
	}
	


}
