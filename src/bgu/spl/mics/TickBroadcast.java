package bgu.spl.mics;


public class TickBroadcast implements Broadcast {

	private int tick;
	/**
	 * the broadcast notify all other services the current tick.
	 * @param tick the current time.
	 */
	public TickBroadcast (int tick){
		
		this.tick= tick;	
	}
	/**
	 * 
	 * @return the tick of the broadcast.
	 */
	public int getTick(){
		return tick;
	}

}
