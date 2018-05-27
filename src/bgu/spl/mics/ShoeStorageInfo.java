package bgu.spl.mics;


public class ShoeStorageInfo {
	
	private String shoeType;
	private int amountOnStorage ;
	private int discountAmount;
	 //the number of shoes in discount
	

	//Creates a new AtomicInteger with initial value 0.
	
	public ShoeStorageInfo(String shoeType,int amountOnStorage, int discountAmount) {
		this.shoeType= shoeType;
		this.amountOnStorage= amountOnStorage;
		this.discountAmount=discountAmount;
		
	}
	/**
	 * add amount to the store
	 * @param amount
	 */
	public void addShoe(int amount){
		amountOnStorage=amount+amountOnStorage;
	}
	/**
	 * remove one shoe from the store
	 */
	public void removeShoe(){// 
		amountOnStorage=amountOnStorage-1;
	}
	/**
	 * decreesing discount amount by 1.
	 */
	public void updateDiscountAmount(){
		
		discountAmount=discountAmount-1;
	}
	/**
	 * 
	 * @return amount this shoe from the store
	 */
	public int getAmountOnStorage(){
		
		return amountOnStorage;
		
	}
		/**
		 * 
		 * @return amount of discount
		 */
	public int getDiscountedAmount(){
		
		return discountAmount;
		
	}
	/**
	 * 
	 * @return shoe type
	 */
	public String getShoeType(){
		
		return shoeType;
		
	}
	/**
	 * set discount amount 
	 * @param amount to add
	 */
	public void setDiscountedAmount(int amount){
		
		discountAmount=discountAmount+amount;
		
	}

}

