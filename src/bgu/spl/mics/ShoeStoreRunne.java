package bgu.spl.mics;
import java.io.BufferedReader;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import javax.swing.SingleSelectionModel;

import com.google.gson.Gson;

import bgu.spl.mics.readFromJson.inputServices.manager;
import bgu.spl.mics.impl.MessageBusImpl;


public class ShoeStoreRunne{
/**
 * main program
 * reading from the json file the data and creating the services and Threads.
 * printing the recipets in he end.
 */
	public static void main(String[]args) {
		BufferedReader br;
		
		try {
		br=new BufferedReader (new FileReader(args[0]));
		
			Gson gson=new Gson();
			readFromJson readJson=gson.fromJson(br, readFromJson .class);
			ShoeStorageInfo [] storage=new ShoeStorageInfo[readJson.initialStorage.length];
			for(int i=0;i<readJson.initialStorage.length;i++){
		ShoeStorageInfo shoe= new ShoeStorageInfo(readJson.initialStorage[i].shoeType,readJson.initialStorage[i].amount,0);
		storage[i]=shoe;}
			
		Store.getInstance().load(storage);
		
		int NumOfThreds=readJson.services.factory+readJson.services.seller+readJson.services.customers.length+1;
		CountDownLatch latchObject = new CountDownLatch (NumOfThreds);
		TimeService timer=new TimeService(readJson.services.time.speed,readJson.services.time.duration);
		Thread Timer=new Thread(timer);
		
		
		ArrayList<DiscountSchedule> discounts = new ArrayList<DiscountSchedule>();

		for(int i=0;i<readJson.services.manager.DiscountSchedule.length;i++){
	
	   DiscountSchedule discount=new DiscountSchedule(readJson.services.manager.DiscountSchedule[i].shoeType,readJson.services.manager.DiscountSchedule[i].tick,readJson.services.manager.DiscountSchedule[i].amount);
	   discounts.add(discount);
	  
		}
		ManagementService manager=new ManagementService(discounts,latchObject);
		Thread Manager=new Thread(manager);
		
		Thread [] factoryThreads = new Thread [readJson.services.factory];
	for(int i=0;i<readJson.services.factory;i++){
		ShoeFactoryService factory=new ShoeFactoryService("factory "+i,latchObject);
		factoryThreads[i]=new Thread(factory);
	}
	
	Thread [] SellingServiceThreads = new Thread [readJson.services.seller];
	for(int i=0;i<readJson.services.seller;i++){
		SellingService seller=new SellingService("seller "+i,latchObject);
		SellingServiceThreads[i]=new Thread(seller);
	}
	

	Thread [] clientsThread = new Thread [readJson.services.customers.length];
	for(int i=0;i<readJson.services.customers.length;i++){
		List<PurchaseSchedule> arrpurch=new ArrayList<PurchaseSchedule>();
		Set<String> wishList=new  HashSet<String>();
		for(int k=0;k<readJson.services.customers[i].wishList.length;k++){
		wishList.add(readJson.services.customers[i].wishList[k]);
		}
		
		for(int j=0;j<readJson.services.customers[i].PurchaseSchedule.length;j++){
			PurchaseSchedule purch= new PurchaseSchedule(readJson.services.customers[i].PurchaseSchedule[j].shoeType,readJson.services.customers[i].PurchaseSchedule[j].tick);
			arrpurch.add(purch);
		}
		WebsiteClientService client=new WebsiteClientService(readJson.services.customers[i].name,arrpurch,wishList,latchObject);
		clientsThread[i] = new Thread(client);
			}
		
		Manager.start();
		for(int i=0;i<factoryThreads.length;i++){
			factoryThreads[i].start();
			
		}
		for(int i=0;i< SellingServiceThreads.length;i++){
			 SellingServiceThreads[i].start();
				
		}
		for(int i=0;i< clientsThread.length;i++){
			clientsThread[i].start();
	
		}
		
		try {
			
			latchObject.await();

	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			
		Timer.start();
		try {
			Manager.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<factoryThreads.length;i++){
			try {
				factoryThreads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=0;i<SellingServiceThreads.length;i++){
			try {
				SellingServiceThreads[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=0;i<clientsThread.length;i++){
			try {
				clientsThread[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
		
		Store.getInstance().print();
		
		 {
			
		}
	}
	}

	 

