package bgu.spl.mics;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import java.util.*;

public class WebsiteClientService extends MicroService {// only one client!!
	/**
	 * client that will buy shoes from the store by sending purchaseOrderRequest to the selling service
	 * the client have  purchase Order schedule and a wish list for shoes to buy only in discount.
	 */
	private static final Logger LOGGER=Logger.getLogger(ManagementService.class.getName());
	private ConcurrentHashMap<Integer,ArrayList<purchaseOrderRequest>> purchaseListByTick;
	private List<PurchaseSchedule> list;
	private Set<String> wishList ;
	private int currTime;
	private CountDownLatch latch;
	public WebsiteClientService(String name,List<PurchaseSchedule> list ,Set<String> wishList,CountDownLatch latch){
		super(name);
		this.list=list;
		this.wishList=wishList;	
		purchaseListByTick= new ConcurrentHashMap<Integer,ArrayList<purchaseOrderRequest>>();		
		this.latch=latch;
		}
	
	 
	@Override
	
	protected void initialize() {
		
			Iterator<PurchaseSchedule>  iter = list.listIterator();
		 	while(iter.hasNext()){
		 		PurchaseSchedule b= iter.next();
		 		
			purchaseOrderRequest req=new purchaseOrderRequest(b.getShoeType(),this,false, (b).getTick());
			purchaseListByTick.putIfAbsent(req.getTick(),new ArrayList<purchaseOrderRequest>());
			purchaseListByTick.get(req.getTick()).add(req);
			
		 	}
			Iterator<String>  iterset = wishList.iterator();
			while(iterset.hasNext()){
				this.wishList.add(iterset.next());	
			}
			
		
			this.subscribeBroadcast(TickBroadcast.class, tick->{
				currTime=tick.getTick();
				
				ArrayList<purchaseOrderRequest> a= purchaseListByTick.get(currTime);
				if(a!=null){
				Iterator <purchaseOrderRequest> iter1 = a.listIterator();
				while(iter1.hasNext()){
					purchaseOrderRequest purch=	iter1.next();
					LOGGER.info(this.getName()+"sending order requset for: "+purch.getshoeType());
					this.sendRequest(purch, onComplete->{
					//	System.out.println(purchaseListByTick.get(currTime).size());
					purchaseListByTick.get(purch.getTick() ).remove(purch);
					//System.out.println(purchaseListByTick.get(currTime).size());
					if(purchaseListByTick.get(purch.getTick()).isEmpty()){
						purchaseListByTick.remove(purch.getTick());
					}
				
					});
					
					if(wishList.contains(purch)){
						wishList.remove(purch);
					}
					
				}
				}
				if(wishList.isEmpty()&& purchaseListByTick.isEmpty()){
					this.terminate();
			}
				});
			this.subscribeBroadcast(NewDiscountBroadcast.class,broad->{
				String shoe=broad.getshoeType();
			
					if (wishList.contains(shoe)){
						purchaseOrderRequest disReq=new purchaseOrderRequest(broad.getshoeType(),this,true, currTime);
						LOGGER.info(this.getName()+"sending discount order requset for: "+broad.getshoeType());
						wishList.remove(broad.getshoeType());
						this.sendRequest(disReq,res->{
						
						});
					}
			});
			
			subscribeBroadcast(TerminateBrodcast.class, terminate->{
				this.terminate();
			});
			latch.countDown();
}
	}

	