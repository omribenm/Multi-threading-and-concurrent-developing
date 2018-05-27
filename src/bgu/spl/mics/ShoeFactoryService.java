package bgu.spl.mics;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;
import bgu.spl.mics.MicroService;


public class ShoeFactoryService extends MicroService {
	final Logger LOGGER = Logger.getLogger(ShoeStoreRunne.class.getName());
	private CountDownLatch m_latchObject;
	private int currTick;
	private LinkedBlockingQueue<ManufacturingOrderRequest> ordersAwaiting;
	private ConcurrentHashMap<ManufacturingOrderRequest, Integer> shoesToMake;
	/**constractor
	 *   
	 * shoe factory, handle ManufacturingOrderRequests that was sent from the manager.
	 * send shoes to store,and recipet for every ManufacturingOrderRequest 
	 *manufacturing each shoe in 1 tick.
	 * sending recipts to the store. 
	 *
	 */
	public ShoeFactoryService(String name, CountDownLatch latchObject) {
		super(name);
		this.m_latchObject = latchObject;
		this.shoesToMake = new ConcurrentHashMap<ManufacturingOrderRequest, Integer>();
		this.ordersAwaiting = new LinkedBlockingQueue<ManufacturingOrderRequest>();
	}
	@Override
	protected void initialize() {
		
		LOGGER.info(getName()+" Is Open");
		
		subscribeBroadcast(TickBroadcast.class, tick -> {
			currTick = tick.getTick();
			if (ordersAwaiting.size() > 0) {
				ManufacturingOrderRequest order = ordersAwaiting.peek();
				int count = shoesToMake.get(order);
				if (count == 0) {
					 LOGGER.info("Tick " + currTick+": "  + getName()+" finished making " + order.getAmount() + " "
							+ order.getshoeType());
			    //String seller,String customer,String shoeType,boolean discount,int issuedTick, int requestTick,int amountSold );
					Receipt rec=new Receipt(getName(),"store", order.getshoeType(), false, currTick,order.getTime(), order.getAmount());
				Store.getInstance().file(rec);
					this.complete(order,rec);
					shoesToMake.remove(order);
					ordersAwaiting.remove(order);
					if (!ordersAwaiting.isEmpty()) {
						int current = shoesToMake.get(ordersAwaiting.peek()).intValue();
						shoesToMake.replace(ordersAwaiting.peek(), current - 1);
					}
				}
				else {
					shoesToMake.replace(order, count - 1);
				}
			}
		});
		
	/**
	 * subscribed to manu factor order request  
	 */
		subscribeRequest(ManufacturingOrderRequest.class, req -> {
			LOGGER.info("Tick " + currTick +":" +" " +getName()+" recieved an order for " + req.getAmount() + " "
					+ req.getshoeType());
			ordersAwaiting.add(req);
			shoesToMake.put(req, req.getAmount());
		});
		
		//Receives the broadcast and terminate
		this.subscribeBroadcast(TerminateBrodcast.class, res -> {
			while (!ordersAwaiting.isEmpty()) {
				complete(ordersAwaiting.poll(), null);
			}
			this.terminate();
			
		
		});
		m_latchObject.countDown();
	}
}
