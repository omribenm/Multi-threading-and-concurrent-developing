package bgu.spl.mics;



import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import bgu.spl.mics.Store.BuyResult;
/**
 * 
 * @param constracture
 *  selling service handling purchaseOrderRequest :selling shoes for clients.
 * if the shoe dose not exits sending order requests to the store manager. 
 *
 */
public class SellingService extends MicroService{
	private static final Logger LOGGER=Logger.getLogger(ManagementService.class.getName());
	private Store store= Store.getInstance();
	private int currTime;
	private CountDownLatch latch;
	
	public SellingService(String name,CountDownLatch latch){
		super(name);
		this.latch=latch;
		}
	@Override
	protected void initialize() {
		
		this.subscribeBroadcast(TickBroadcast.class, tick->{
			currTime=tick.getTick();
		});
		
		this.subscribeRequest(purchaseOrderRequest.class, shoe->{
			
			BuyResult res= store.take(shoe.getshoeType(), shoe.isDiscount());
			int requestTick = shoe.getTick();
			if(res== BuyResult.REGULAR_PRICE){
				Receipt receipt = new Receipt(this.getName(),shoe.getClient().getName(),shoe.getshoeType(),false,currTime, requestTick,1);
				this.complete(shoe, receipt);
				store.file(receipt);
				LOGGER.info(this.getName()+" sold to "+shoe.getClient().getName()+": "+shoe.getshoeType());
				
			}
			
			else if (res == BuyResult.DISCOUNTED_PRICE){
				
				Receipt receipt = new Receipt(this.getName(),shoe.getClient().getName(),shoe.getshoeType(),true,currTime, requestTick,1);
				LOGGER.info(this.getName()+" sold in discount "+shoe.getClient().getName()+" "+shoe.getshoeType());
				this.complete(shoe, receipt);
				store.file(receipt);
				
			}
		
			else if(res == BuyResult.NOT_IN_STOCK){
				
				if (shoe.isDiscount()==false || !store.getInstance().contain(shoe.getshoeType())){
					
				RestockRequest restock= new RestockRequest(shoe.getshoeType());
				
				this.sendRequest(restock, stock->{
					
					if( stock.booleanValue()==true){
						Receipt receipt = new Receipt(this.getName(),shoe.getClient().getName(),shoe.getshoeType(),true,currTime, requestTick,1);
						LOGGER.info(this.getName()+" sold to "+shoe.getClient().getName()+": "+shoe.getshoeType());
						this.complete(shoe, receipt);
						store.file(receipt);
					}
						else {
							
							this.complete(shoe, null);
						}
					
						
				});
		}
				else
				this.complete(shoe, null);
				
			}
			
			else if(res == BuyResult.NOT_ON_DISCOUNT){
				
				this.complete(shoe, null);
			}
			subscribeBroadcast(TerminateBrodcast.class, terminate->{
			
				
				this.terminate();
			});
					
				});
		latch.countDown();
	}

}

	
