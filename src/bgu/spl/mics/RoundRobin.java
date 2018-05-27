package bgu.spl.mics;

import java.util.concurrent.CopyOnWriteArrayList;
import bgu.spl.mics.impl.*;
/**
 * 
 * creating list that works in a round robin manner.
 *
 * @param <T>
 */
public class RoundRobin<T> {
	private CopyOnWriteArrayList<MicroService> list;
	private int index;
	/**
	 * constractur 
	 * creating new array list
	 */
	public RoundRobin(){
		index = 0;
		list = new CopyOnWriteArrayList<MicroService> ();
	}
/**
 * checking if the microservice is in the list
 * @param ms
 * @return
 */
	public boolean exists(MicroService ms){
		return list.contains(ms);
	}
	/**
	 * deleting the microservice from the list
	 * @param ms
	 */
	public synchronized void delete(MicroService ms) {
		if(list.size()!=0 && list.indexOf(ms)!=(-1)){
			int x= list.indexOf(ms);
			list.remove(ms);
			if(index>x) index--;
		}
	}
	/**
	 * adding microservice to the list
	 * @param microservice 
	 */
	public synchronized void add(MicroService ms){
		list.add(ms);
	}
	
/**
 * 
 * @returning the next microservice in the list in round robin manner.
 */
	public synchronized MicroService nextMicroService(){
		MicroService ans = null;
		if(list.size() > 0){
			ans = list.get(index);
			if(list.size()!=0)
				index = (index + 1) % list.size();
		}
		return ans;
	}
}