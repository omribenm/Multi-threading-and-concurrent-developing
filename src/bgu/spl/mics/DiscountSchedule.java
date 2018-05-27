package bgu.spl.mics;

public class DiscountSchedule {
	
	private String shoeType;
	private int tick;
	private int amount;//num f shoes to put on discount
	 /**
	  * constractur;
	  * @param shoeType - discount shoe type 
	  * @param tick- discount type
	  * @param amount- discount amount to add store
	  */
	 public DiscountSchedule (String shoeType,int tick, int amount){
		 this.shoeType=shoeType;
		 this.tick= tick;
		 this.amount=amount;
	 }

	 /**
	  * 
	  * @return shoe type
	  */
	 public String getShoeType(){
		 return shoeType;
	 }
	 /**
	  * 
	  * @return discount time
	  */
	 public int getTick(){
		 return tick;
	 }
	 /**
	  * 
	  * @return discount amount
	  */
	 public int getAmount(){
		 return amount;
	 }



}
