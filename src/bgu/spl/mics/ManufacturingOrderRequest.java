package bgu.spl.mics;
import java.util.logging.Logger;

public class ManufacturingOrderRequest implements Request<Receipt> {
	

private String shoeType;
private int Amount;
private int Time;
/**
 * 
 * @param shoeType 
 * @param Amount
 * @param Time
 */ 
 		public ManufacturingOrderRequest(String shoeType,int Amount,int Time){
			this.shoeType=shoeType;
			this.Amount=Amount;
			this.Time=Time;
		}
 		/**
 		 * 
 		 * @return requset shoe type
 		 */
		public String getshoeType(){
			return shoeType;
		}	
		/**
		 * 
		 * @return amount for discount
		 */
		public int getAmount(){
			 return Amount;
		}
		/**
		 * 
		 * @return 
		 */
		public int getTime(){
			return Time;
		}

	}


	
	

