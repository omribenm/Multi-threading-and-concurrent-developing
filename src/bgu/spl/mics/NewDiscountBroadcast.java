package bgu.spl.mics;
/**
 * the manager will send this message when a new discount has apply.
 * @author Xps
 *
 */
public class NewDiscountBroadcast implements Broadcast  {
	private String shoeType;
	private int Amount;

	/**
	 * 
	 * @param ShoeType the shot type of the discount
	 * @param Amount the aount of the discount
	 */
	public NewDiscountBroadcast(String ShoeType,int Amount){
		this.shoeType= ShoeType;
		this.Amount=Amount;
		
	}
	/**
	 * 
	 * @return the shoe type
	 */
	public String getshoeType(){
		return this.shoeType;
	}
	/**
	 * 
	 * @return the amount
	 */
	public int getAmount(){
		return Amount;
	}

}

