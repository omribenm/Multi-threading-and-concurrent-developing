package bgu.spl.mics;
import com.google.gson.annotations.SerializedName;
/**
 * reading the jason file
 * @author Xps
 *
 */
public class readFromJson {
	
	@SerializedName("initialStorage")
	public shoeStorageInput [] initialStorage;
	
	public class shoeStorageInput{
		public String shoeType;
		public int amount;
	}
	
	@SerializedName("services")
	public inputServices services;
	
	public class inputServices {
		@SerializedName("time")
	public time time;
		@SerializedName("manager")
	public manager manager;
		@SerializedName("factories")
	public int factory;
		@SerializedName("sellers")
	public int seller;
		@SerializedName("customers")
public customers [] customers;
	
	
	
	
	public class time {
		public int speed;
		public int duration;
	}

	public class manager{
		@SerializedName("discountSchedule")
		public discountSchedule [] DiscountSchedule;
	
	}
	
	public class discountSchedule{
		public String shoeType;
		public int amount;
		public int tick;
		
	}
	//public class factory{
//		public int factories;
	//}

//	public class seller{
	//	public int seller;
	//}

public class customers{
		public String name;
		@SerializedName("purchaseSchedule")
		public PurchaseSchedule [] PurchaseSchedule;
		public String [] wishList;
}

public class PurchaseSchedule{
	public String shoeType;
	public int tick;
}

}
}
	
		/*manager: {
		discountSchedule: [
		{shoeType: "red-boots", amount: 1, tick: 3},
		{shoeType: "green-flip-flops", amount: 3, tick:10}
		]
		},
		factories: 3,
		sellers: 2,
		customers: [
		{
		name: "Bruria",
		wishList: ["green-flip-flops"],
		purchaseSchedule: [
		{shoeType: "red-boots", tick: 3}
		]
		},
		{
		name: "Shraga",
		wishList: [],
		purchaseSchedule: [
		{shoeType: "green-flip-flops", tick: 12}
		]
		}
		]
}*/
