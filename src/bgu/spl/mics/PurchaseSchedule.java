package bgu.spl.mics;

public class PurchaseSchedule {

	private String shoeType;
	private int tick;
	 /**
	  * 
	  * @param shoeType 
	  * @param tick
	  */
	 public PurchaseSchedule(String shoeType,int tick){
		 this.shoeType=shoeType;
		 this.tick= tick;
	 }
	 
	 public String getShoeType(){
		 return shoeType;
	 }
	 
	 public int getTick(){
		 return tick;
	 }

	
}
