package bgu.spl.mics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.mics.readFromJson.shoeStorageInput;

import java.lang.Enum;
import java.nio.channels.ShutdownChannelGroupException;

public class Store {
	
	private ArrayList<Receipt> reciptlist= new ArrayList<Receipt> (); 
	private ConcurrentHashMap<String,ShoeStorageInfo> storeMap;
	
	private int i,j=1;
	
	
	public enum BuyResult {
		NOT_IN_STOCK, NOT_ON_DISCOUNT , REGULAR_PRICE, DISCOUNTED_PRICE
	}
	
	private static class SingeltonHolder{
		private static Store instance = new Store();
	
	}
	/**
	 * constractur
	 * @store
	 * singelton, saving the store shoes and updating the restock amount and discount amount 
	 * @param storeMap saving the shoe with her info
	 */
	private Store(){
		storeMap= new ConcurrentHashMap<String ,ShoeStorageInfo>();
		
	}
	/**
	 * 
	 * @param storage load the storage with ShoeStorageInfo
	 */
	public void load ( ShoeStorageInfo [ ]  storage){
		for(int i=0; i<storage.length; i++){
			storeMap.put(storage[i].getShoeType(), storage[i]);
		}
	}
		/**
		 * 
		 * @param shoeType to take
		 * @param onlyDiscount if the client want only with discount
		 * @return
		 */
	public synchronized BuyResult take (String shoeType, boolean onlyDiscount){
		
		if(storeMap.containsKey(shoeType)){
		ShoeStorageInfo	shoe=storeMap.get(shoeType);
		if (shoe.getAmountOnStorage()==0){
			return BuyResult.NOT_IN_STOCK;}
		else if(onlyDiscount && shoe.getDiscountedAmount()==0)	
			return BuyResult.NOT_ON_DISCOUNT;
		
		else if(shoe.getAmountOnStorage()>0){
			if(shoe.getDiscountedAmount()>0){
			shoe.removeShoe();
			shoe.updateDiscountAmount();
			return BuyResult.DISCOUNTED_PRICE;}
		else {
			shoe.removeShoe();
			return BuyResult.REGULAR_PRICE;
		}
		}
		}
		else{
			return BuyResult.NOT_IN_STOCK;
			
		}
		 return null;
	}
/**
 * 
 * @param shoeType
 * @return if the shoe type in the store
 */
		public boolean contain(String shoeType){
			return storeMap.containsKey(shoeType);
		}
		/**
		 * add shoe type to the store
		 * @param shoeType to add 
		 * @param amount to add
		 */
		public void add(String shoeType, int amount){
			ShoeStorageInfo shoe=new ShoeStorageInfo(shoeType,amount,0);
			storeMap.putIfAbsent(shoeType, shoe);
			ShoeStorageInfo shoe1 =  storeMap.get(shoeType);
			shoe1.addShoe(amount);
		}
	/**
	 * add discount to the shoe type info
	 * @param shoeType
	 * @param amount
	 */
		public void addDiscount (String  shoeType ,int amount){
			ShoeStorageInfo shoe =  storeMap.get(shoeType);
			shoe.setDiscountedAmount(amount);
		}
		/**
		 * add recipt to the store
		 * @param receipt
		 */
		public void file ( Receipt receipt){
			reciptlist.add(receipt);
		}
		/**
		 * prints all the recipts
		 */
		public void print(){
			
			int i=1;
			//Iterator<Receipt> iter=reciptlist.iterator();
			
			for(Receipt R : reciptlist){
			
				System.out.println( "--------------------------------------");
				System.out.println( "Reciet number: " +i );
					
					System.out.println( "ShoeType: " + R.getShoeType());
					System.out.println( "   Amount: " + R.getAmountSold());
					System.out.println( "   Discounted: " + R.getDiscount());
					System.out.println( "   issuedTick : " + R.getIssuedTick());
					System.out.println( "   requestd tick : " + R.getRequestTick());
					System.out.println( "   customer : " + R.getCustomer());
					System.out.println( "   seller : " + R.getSeller());
					
					i++;
				}
	
			}
		
		
			
	public static Store getInstance(){
		return SingeltonHolder.instance;
}
}