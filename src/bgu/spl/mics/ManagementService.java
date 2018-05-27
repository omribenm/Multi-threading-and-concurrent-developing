package bgu.spl.mics;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import bgu.spl.mics.impl.MessageBusImpl;

public class ManagementService extends MicroService{
	

	private static final Logger LOGGER=Logger.getLogger(ManagementService.class.getName());
	private ConcurrentHashMap<ManufacturingOrderRequest,ArrayList<RestockRequest>> factoryRequestMap;
	private ConcurrentHashMap<String,ArrayList<ManufacturingOrderRequest>> ShoeMap;
	private ConcurrentHashMap<Integer,ArrayList<NewDiscountBroadcast>> ListByTick;
	private ArrayList<DiscountSchedule> list;
	private int currTime;
	private CountDownLatch latch;
	private int size;
	
	/** managment service, in charge of sending  discount broadcast and handling
	  restock requests.
	   factoryRequestMap- ManufacturingOrderRequest and the restocks requers attached to them.
	 *  ShoeMap- the shoe type ataached with all her ManufacturingOrderRequests.
	 * @param ListByTick saving the future discounts broadcast with the time to send.
	 *  factoryRequestMap- ManufacturingOrderRequest and the restocks requers attached to them.
	 *  ShoeMap- the shoe type ataached with all her ManufacturingOrderRequests.
	 */ 
	
	public ManagementService(ArrayList<DiscountSchedule> list,CountDownLatch latch) {
		super("manager");
		this.list= list;
		this.latch=latch;
		this.factoryRequestMap= new ConcurrentHashMap<ManufacturingOrderRequest,ArrayList<RestockRequest>>();
		this.ShoeMap= new ConcurrentHashMap<String,ArrayList<ManufacturingOrderRequest>>();
		this.ListByTick = new ConcurrentHashMap<Integer, ArrayList<NewDiscountBroadcast>>();
		 LOGGER.info("Manager cteated");
	}
	
	@Override
	
	protected void initialize() {
		
		subscribeBroadcast(TerminateBrodcast.class, terminate->{
			this.terminate();
			
		});
		
		Iterator<DiscountSchedule>  iter = list.iterator();
		while(iter.hasNext()){
			DiscountSchedule a=iter.next();
			NewDiscountBroadcast req= new NewDiscountBroadcast(a.getShoeType(), a.getAmount());
			ListByTick.put((a).getTick(),new ArrayList<NewDiscountBroadcast>());
			ListByTick.get((a).getTick()).add(req);
		}
		this.subscribeBroadcast(TickBroadcast.class, tick->{
			
			currTime=tick.getTick();
			if(ListByTick.containsKey(currTime)){
				ArrayList<NewDiscountBroadcast> a= ListByTick.get(currTime);
				Iterator<NewDiscountBroadcast> it= a.iterator();
				while(it.hasNext()){
					NewDiscountBroadcast dis=it.next();
					LOGGER.info(this.getName()+" sending NewDiscountBroadcast "+(((NewDiscountBroadcast)dis).getshoeType()));
					if(Store.getInstance().contain(dis.getshoeType())){
					Store.getInstance().addDiscount(dis.getshoeType(),dis.getAmount());
					this.sendBroadcast(dis);
				}
					else
						Store.getInstance().add(dis.getshoeType(),0);
				}
			}
			}); 
			//subscribed to reqstock requset
			this.subscribeRequest(RestockRequest.class, restock->{
				LOGGER.info(this.getName()+" got restock request of: "+restock.getshoeType());
				if ((ShoeMap.containsKey((restock.getshoeType())))){//was ordered before
			size = ShoeMap.get(restock.getshoeType()).size();
			ManufacturingOrderRequest a= ShoeMap.get(restock.getshoeType()).get(size-1);// the ManufacturingOrderRequest in the last index
			if(factoryRequestMap.get(a).size()== a.getAmount()){// the restock arraylist is full
					
					 ManufacturingOrderRequest req= new ManufacturingOrderRequest(restock.getshoeType(),(currTime%5)+1,currTime);
					 ShoeMap.get(req.getshoeType()).add(req);
					 factoryRequestMap.putIfAbsent(req, new ArrayList<RestockRequest>(req.getAmount()));
					 factoryRequestMap.get(req).add(restock);
					 LOGGER.info(this.getName()+" sending manufactring order request of: "+req.getshoeType());
					 this.sendRequest(req, comp->{
						if (comp==null){
							for (int i=0;i<factoryRequestMap.get(req).size();i++){// updating all the restock request in the array list of this spesific manufacture wuth the complte result,
							this.complete(factoryRequestMap.get(req).get(i), false);
							}
						}
						else{// comp is not null
							int amount=comp.getAmountSold()- factoryRequestMap.get(req).size();
							LOGGER.info(this.getName()+" adding to the store shoe: "+req.getshoeType()+"amount: "+amount);
							Store.getInstance().add(comp.getShoeType(),amount);// update the store storage : "comp.getShoeType(),comp.getAmountSold()" - giving back the number of the shoes the  factory made"factoryRequestMap.get(req).size()" giving back the number of restock reqeust of the same shoe)
							for (int i=0;i<factoryRequestMap.get(req).size();i++){//complete all the restock requests that belongs to req
							this.complete(factoryRequestMap.get(req).get(i),true);}
						}	
				 });
				}
				 else{// was ordered before and the order not full
					 
					 factoryRequestMap.get(a).add(restock);
					
				 }
				 
				}
	
						
			
			
	     else {// the shoe was never ordered before
								int amount=(currTime%5)+1;
								ManufacturingOrderRequest b= new ManufacturingOrderRequest(restock.getshoeType(),amount,currTime);
								ShoeMap.put(b.getshoeType(), new ArrayList<ManufacturingOrderRequest>());
								ShoeMap.get(b.getshoeType()).add(b);
								factoryRequestMap.put(b, new ArrayList<RestockRequest>(b.getAmount()));
								factoryRequestMap.get(b).add(restock);
								LOGGER.info(this.getName()+" sending manufactring order request of: "+b.getshoeType()+",amount: "+b.getAmount());
								this.sendRequest(b,reciept->{
									if (reciept==null){
										for (int i=0;i<factoryRequestMap.get(b).size();i++){
											this.complete(factoryRequestMap.get(b).get(i), false);
											}
									}
					
									else{
										
										Store.getInstance().add(reciept.getShoeType(),reciept.getAmountSold()- factoryRequestMap.get(b).size());// update the store storage : "comp.getShoeType(),comp.getAmountSold()" - giving back the number of the shoes the  factory made"factoryRequestMap.get(req).size()" giving back the number of restock reqeust of the same shoe)
										for (int i=0;i<factoryRequestMap.get(b).size();i++){//complete all the restock requests that belongs to req
											this.complete(factoryRequestMap.get(b).get(i),true);}
										
									}
					
			});
						
				}
					
					
			
				
			});
	
				
				
				
	      latch.countDown();
				
	}		
			
		}