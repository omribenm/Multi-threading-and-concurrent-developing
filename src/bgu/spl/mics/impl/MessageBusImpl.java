package bgu.spl.mics.impl;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.logging.Logger;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Receipt;
import bgu.spl.mics.Request;
import bgu.spl.mics.RequestCompleted;
import bgu.spl.mics.RoundRobin;
import bgu.spl.mics.Store;



public class MessageBusImpl implements MessageBus{

	
	private ConcurrentHashMap<Class<? extends Request>,RoundRobin<MicroService>> queueByRequest;
	private ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>> queueByMicroService;
	private ConcurrentHashMap<Class<? extends Broadcast>,LinkedBlockingQueue<MicroService>> queueByBroadcast;
	private ConcurrentHashMap<Request<?>,MicroService> mapRequester;
	Object lock=new Object();
	
	private static class SingeltonHolder{
		private static MessageBusImpl instance = new MessageBusImpl();
	}
	
	private MessageBusImpl() {
		queueByRequest=new ConcurrentHashMap<Class<? extends Request>,RoundRobin<MicroService>>();
		queueByMicroService=new ConcurrentHashMap<MicroService,LinkedBlockingQueue<Message>>();
		queueByBroadcast=new ConcurrentHashMap<Class<? extends Broadcast>,LinkedBlockingQueue<MicroService>>();
		mapRequester=new ConcurrentHashMap<Request<?>,MicroService>();
	}
	

	public void subscribeRequest(Class<? extends Request> type, MicroService m){

		queueByRequest.putIfAbsent(type,new RoundRobin<MicroService>());
				queueByRequest.get(type).add(m);
		}


	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m){
		
		queueByBroadcast.putIfAbsent(type,new LinkedBlockingQueue<MicroService>());
		if(!( queueByBroadcast.get(type).contains(m)))
			try {
				
				queueByBroadcast.get(type).put(m);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	}


	public <T>  void complete(Request<T> r, T result){
		RequestCompleted r1 = new RequestCompleted(r,result);
		MicroService m=	mapRequester.get(r);
		if(m!=null && queueByMicroService.containsKey(m))
			(queueByMicroService.get(m)).add(r1);
	
			
		
		}

	public void sendBroadcast(Broadcast b){
		synchronized (lock) {
		Iterator <MicroService> iter= queueByBroadcast.get(b.getClass()).iterator();
		
		while(iter.hasNext()){
				MicroService  a=iter.next();
				queueByMicroService.get(a).add(b);
		}
		}
	}

	public  boolean  sendRequest(Request<?> r, MicroService requester){//round robin?
		synchronized(lock){
		MicroService m;
		m= queueByRequest.get(r.getClass()).nextMicroService();
		mapRequester.put(r, requester);
		if(m!=null &&queueByMicroService.containsKey(m)){
			 (queueByMicroService.get(m)).add(r);
			return true;}
			return false;
		}
	}


	public void register(MicroService m){// add it to the queueByMicroService map
		queueByMicroService.putIfAbsent(m,new LinkedBlockingQueue<Message>());
	
	}

	public synchronized  void unregister(MicroService m){

		if ( queueByMicroService.contains(m)) // 
			queueByMicroService.remove(m);

		for(ConcurrentHashMap.Entry< Class<? extends Broadcast> , LinkedBlockingQueue<MicroService>>entry : queueByBroadcast.entrySet()){
			
			if(entry.getValue().contains(m)){
				entry.getValue().remove(m);
			}
		}
		
		for(ConcurrentHashMap.Entry<Request<?>,MicroService> entry : mapRequester.entrySet()){
			
			if(entry.getValue()==m){
				mapRequester.remove(entry.getKey());
			}
		}
		
		
		
		for(ConcurrentHashMap.Entry<Class<? extends Request>,RoundRobin<MicroService>> entry : queueByRequest.entrySet()){
			
			if(entry.getValue().exists(m)){
				entry.getValue().delete(m);
			}
		}

	}

	public Message awaitMessage(MicroService m) throws InterruptedException{

			return queueByMicroService.get(m).take();		 		 
	}
	
	
	
	public static MessageBusImpl getInstance(){
		return SingeltonHolder.instance;
	}

}
